package com.ecommerce.ecommerceposapp.presentation.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.ClientRow
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

data class ClientsUiState(val clients: List<ClientRow> = emptyList(), val isLoading: Boolean = false, val isSaving: Boolean = false, val message: String? = null, val error: String? = null)

class ClientsViewModel(private val getClients: GetClientsUseCase, private val saveClient: SaveClientUseCase, private val deleteClient: DeleteClientUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientsUiState())
    val uiState: StateFlow<ClientsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        runCatching { withContext(Dispatchers.IO) { getClients() } }.onSuccess { rows -> _uiState.update { it.copy(clients = rows, isLoading = false) } }.onFailure(::showError)
    }
    fun save(row: ClientRow) = action("Cliente guardado.") { saveClient(row) }
    fun remove(id: Long) = action("Cliente eliminado.") { deleteClient(id) }
    fun clearMessages() = _uiState.update { it.copy(message = null, error = null) }
    private fun action(message: String, block: suspend () -> Result<Unit>) = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, message = null, error = null) }
        withContext(Dispatchers.IO) { block() }.onSuccess { _uiState.update { it.copy(isSaving = false, message = message) }; load() }.onFailure(::showError)
    }
    private fun showError(error: Throwable) { _uiState.update { it.copy(isLoading = false, isSaving = false, error = error.message ?: "Ocurrio un error") } }
}
