package com.ecommerce.ecommerceposapp.presentation.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.usecase.clients.DeleteClientUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.clients.GetClientsUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.clients.SaveClientUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ClientsUiState(
    val clients: List<ClientRow> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val perPage: Int = 20,
    val search: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class ClientsViewModel(private val getClients: GetClientsUseCase, private val saveClient: SaveClientUseCase, private val deleteClient: DeleteClientUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientsUiState())
    val uiState: StateFlow<ClientsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load(page: Int = _uiState.value.page, perPage: Int = _uiState.value.perPage, search: String = _uiState.value.search) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        runCatching { withContext(Dispatchers.IO) { getClients(page, perPage, search) } }
            .onSuccess { result -> _uiState.update { it.copy(clients = result.rows, total = result.total, page = result.page, perPage = result.perPage, search = search, isLoading = false) } }
            .onFailure(::showError)
    }
    fun save(row: ClientRow) = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, message = null, error = null) }
        val result: Result<String> = withContext(Dispatchers.IO) { saveClient(row) }
        result.onSuccess { backendMessage ->
                _uiState.update { it.copy(isSaving = false, message = backendMessage) }
                load()
            }
            .onFailure(::showError)
    }
    fun remove(id: Long) = action("Cliente eliminado.") { deleteClient(id) }
    fun clearMessages() = _uiState.update { it.copy(message = null, error = null) }
    private fun action(message: String, block: suspend () -> Result<Unit>) = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, message = null, error = null) }
        withContext(Dispatchers.IO) { block() }.onSuccess { _uiState.update { it.copy(isSaving = false, message = message) }; load() }.onFailure(::showError)
    }
    private fun showError(error: Throwable) { _uiState.update { it.copy(isLoading = false, isSaving = false, error = error.message ?: "Ocurrio un error") } }
}
