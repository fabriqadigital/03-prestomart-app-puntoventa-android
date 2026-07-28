package com.ecommerce.ecommerceposapp.presentation.cash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.data.remote.api.CashApiDataSource
import com.ecommerce.ecommerceposapp.domain.model.cash.CashFlowItem
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CashModuleUiState(
    val session: CashSession? = null,
    val summary: CashSummary? = null,
    val flowItems: List<CashFlowItem> = emptyList(),
    val loading: Boolean = false,
    val flowLoading: Boolean = false,
    val error: String? = null,
    val flowError: String? = null,
    val filterFrom: String = "",
    val filterTo: String = "",
    val flowSearch: String = "",
    val showCloseDialog: Boolean = false,
    val closeLoading: Boolean = false,
    val closeError: String? = null,
    val closedSuccess: Boolean = false,
)

class CashModuleViewModel(
    private val catalogRepository: CatalogRepository,
    private val cashApi: CashApiDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CashModuleUiState())
    val uiState: StateFlow<CashModuleUiState> = _uiState

    /** Carga el resumen del turno actual y el flujo de caja. */
    fun load(session: CashSession) {
        _uiState.update { it.copy(session = session, loading = true, error = null) }
        viewModelScope.launch {
            // Resumen del turno
            val summaryResult = withContext(Dispatchers.IO) { catalogRepository.cashSummary(session.id) }
            summaryResult
                .onSuccess { s -> _uiState.update { it.copy(summary = s, loading = false) } }
                .onFailure { e -> _uiState.update { it.copy(loading = false, error = e.message) } }
            // Flujo de caja de la sesión actual
            loadFlow(session.id, null, null)
        }
    }

    fun setFilterFrom(value: String) {
        _uiState.update { it.copy(filterFrom = value) }
    }

    fun setFilterTo(value: String) {
        _uiState.update { it.copy(filterTo = value) }
    }

    fun setFlowSearch(value: String) {
        _uiState.update { it.copy(flowSearch = value) }
    }

    /** Aplica los filtros de fecha actuales para recargar el flujo. */
    fun applyDateFilter() {
        val state = _uiState.value
        val sessionId = state.session?.id
        loadFlow(sessionId, state.filterFrom.ifBlank { null }, state.filterTo.ifBlank { null })
    }

    fun clearDateFilter() {
        _uiState.update { it.copy(filterFrom = "", filterTo = "") }
        val sessionId = _uiState.value.session?.id
        loadFlow(sessionId, null, null)
    }

    private fun loadFlow(sessionId: Long?, fechaInicio: String?, fechaFin: String?) {
        if (sessionId == null || sessionId < 0L) {
            _uiState.update {
                it.copy(
                    flowItems = emptyList(),
                    flowLoading = false,
                    flowError = "Modo offline: el resumen usa las ventas guardadas en este dispositivo.",
                )
            }
            return
        }
        _uiState.update { it.copy(flowLoading = true, flowError = null) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cashApi.cashFlow(
                    sessionId = sessionId,
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin,
                )
            }
                .onSuccess { items -> _uiState.update { it.copy(flowItems = items, flowLoading = false) } }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            flowItems = emptyList(),
                            flowLoading = false,
                            flowError = "Sin conexión. El resumen local continúa disponible.",
                        )
                    }
                }
        }
    }

    fun requestClose() {
        _uiState.update { it.copy(showCloseDialog = true, closeError = null, closedSuccess = false) }
    }

    fun dismissClose() {
        _uiState.update { it.copy(showCloseDialog = false, closeError = null) }
    }

    fun closeSession(countedCash: Double, observations: String, onSuccess: () -> Unit) {
        val sessionId = _uiState.value.session?.id ?: return
        _uiState.update { it.copy(closeLoading = true, closeError = null) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                catalogRepository.closeCashSession(sessionId, countedCash, observations)
            }
                .onSuccess {
                    _uiState.update { it.copy(closeLoading = false, showCloseDialog = false, closedSuccess = true) }
                    onSuccess()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(closeLoading = false, closeError = e.message) }
                }
        }
    }
}
