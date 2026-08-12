package com.ecommerce.ecommerceposapp.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.usecase.users.DeleteUserUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.users.GetUsersPageUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.users.SaveUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UsersUiState(val users: List<UserRow> = emptyList(), val total: Int = 0, val page: Int = 1, val perPage: Int = 20, val search: String = "", val isLoading: Boolean = false, val isSaving: Boolean = false, val message: String? = null, val error: String? = null)

class UsersViewModel(private val getUsers: GetUsersPageUseCase, private val saveUser: SaveUserUseCase, private val deleteUser: DeleteUserUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()
    init { load() }
    fun load(page: Int = _uiState.value.page, perPage: Int = _uiState.value.perPage, search: String = _uiState.value.search) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, page = page, perPage = perPage, search = search) }
        runCatching { withContext(Dispatchers.IO) { getUsers(page, perPage, search) } }.onSuccess { result ->
            _uiState.update { it.copy(users = result.rows, total = result.total, page = result.page, perPage = result.perPage, isLoading = false) }
        }.onFailure(::showError)
    }
    fun save(row: UserRow, password: String?) = action("Usuario guardado.") { saveUser(row, password) }
    fun remove(id: Long, currentUserId: Long) = action("Usuario eliminado.") { deleteUser(id, currentUserId) }
    fun clearMessages() = _uiState.update { it.copy(message = null, error = null) }
    private fun action(message: String, block: suspend () -> Result<Unit>) = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, message = null, error = null) }
        withContext(Dispatchers.IO) { block() }.onSuccess { _uiState.update { it.copy(isSaving = false, message = message) }; load() }.onFailure(::showError)
    }
    private fun showError(error: Throwable) { _uiState.update { it.copy(isLoading = false, isSaving = false, error = error.message ?: "Ocurrio un error") } }
}
