package com.ecommerce.ecommerceposapp.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.usecase.users.DeleteUserUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.users.GetUsersUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.users.SaveUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UsersUiState(val users: List<UserRow> = emptyList(), val isLoading: Boolean = false, val isSaving: Boolean = false, val message: String? = null, val error: String? = null)

class UsersViewModel(private val getUsers: GetUsersUseCase, private val saveUser: SaveUserUseCase, private val deleteUser: DeleteUserUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        runCatching { withContext(Dispatchers.IO) { getUsers() } }.onSuccess { rows -> _uiState.update { it.copy(users = rows, isLoading = false) } }.onFailure(::showError)
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
