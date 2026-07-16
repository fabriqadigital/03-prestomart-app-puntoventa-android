package com.ecommerce.ecommerceposapp.presentation.suppliers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.SupplierRow
import com.ecommerce.ecommerceposapp.domain.usecase.suppliers.DeleteSupplierUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.suppliers.GetSuppliersUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.suppliers.SaveSupplierUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SuppliersUiState(val suppliers: List<SupplierRow> = emptyList(), val isLoading: Boolean = false, val isSaving: Boolean = false, val message: String? = null, val error: String? = null)

class SuppliersViewModel(private val getSuppliers: GetSuppliersUseCase, private val saveSupplier: SaveSupplierUseCase, private val deleteSupplier: DeleteSupplierUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(SuppliersUiState())
    val uiState: StateFlow<SuppliersUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        runCatching { withContext(Dispatchers.IO) { getSuppliers() } }.onSuccess { rows -> _uiState.update { it.copy(suppliers = rows, isLoading = false) } }.onFailure(::showError)
    }
    fun save(row: SupplierRow) = action("Proveedor guardado.") { saveSupplier(row) }
    fun remove(id: Long) = action("Proveedor eliminado.") { deleteSupplier(id) }
    fun clearMessages() = _uiState.update { it.copy(message = null, error = null) }
    private fun action(message: String, block: suspend () -> Result<Unit>) = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, message = null, error = null) }
        withContext(Dispatchers.IO) { block() }.onSuccess { _uiState.update { it.copy(isSaving = false, message = message) }; load() }.onFailure(::showError)
    }
    private fun showError(error: Throwable) { _uiState.update { it.copy(isLoading = false, isSaving = false, error = error.message ?: "Ocurrio un error") } }
}
