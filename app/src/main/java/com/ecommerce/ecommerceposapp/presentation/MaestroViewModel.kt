package com.ecommerce.ecommerceposapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.data.repository.MaestroRepository
import com.ecommerce.ecommerceposapp.domain.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.ClientRow
import com.ecommerce.ecommerceposapp.domain.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.SupplierRow
import com.ecommerce.ecommerceposapp.domain.UserRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MaestroUiState(
    val users: List<UserRow> = emptyList(),
    val clients: List<ClientRow> = emptyList(),
    val suppliers: List<SupplierRow> = emptyList(),
    val categories: List<CategoryAdminRow> = emptyList(),
    val products: List<ProductAdminRow> = emptyList(),
    val message: String? = null,
    val error: String? = null,
)

class MaestroViewModel(
    private val maestro: MaestroRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MaestroUiState())
    val uiState: StateFlow<MaestroUiState> = _uiState

    fun clearMessages() = _uiState.update { it.copy(message = null, error = null) }

    fun loadAll() {
        viewModelScope.launch {
            val users = withContext(Dispatchers.IO) { maestro.listUsers() }
            val clients = withContext(Dispatchers.IO) { maestro.listClients() }
            val suppliers = withContext(Dispatchers.IO) { maestro.listSuppliers() }
            val categories = withContext(Dispatchers.IO) { maestro.listCategoriesAdmin() }
            val products = withContext(Dispatchers.IO) { maestro.listProductsAdmin() }
            _uiState.update { prev ->
                prev.copy(
                    users = users,
                    clients = clients,
                    suppliers = suppliers,
                    categories = categories,
                    products = products,
                )
            }
        }
    }

    fun saveUser(row: UserRow, password: String?) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.upsertUser(row, password) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Usuario guardado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun removeUser(id: Long, currentUserId: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.deleteUser(id, currentUserId) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Usuario eliminado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun saveClient(row: ClientRow) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.upsertClient(row) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Cliente guardado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun removeClient(id: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.deleteClient(id) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Cliente eliminado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun saveSupplier(row: SupplierRow) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.upsertSupplier(row) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Proveedor guardado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun removeSupplier(id: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.deleteSupplier(id) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Proveedor eliminado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun saveCategory(row: CategoryAdminRow) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.upsertCategory(row) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Categoría guardada.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun removeCategory(id: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.deleteCategory(id) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Categoría desactivada.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun saveProduct(row: ProductAdminRow) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.upsertProduct(row) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Producto guardado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }

    fun removeProduct(id: Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { maestro.deleteProduct(id) }
            result.fold(
                onSuccess = {
                    loadAll()
                    _uiState.update { s -> s.copy(message = "Producto desactivado.", error = null) }
                },
                onFailure = { ex -> _uiState.update { s -> s.copy(error = ex.message ?: "Error") } },
            )
        }
    }
}
