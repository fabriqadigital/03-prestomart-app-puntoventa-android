package com.ecommerce.ecommerceposapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val busy: Boolean = false,
    val error: String? = null,
    val user: UserSession? = null,
    val offlineAvailable: Boolean = false,
    val offlineMode: Boolean = false,
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val rememberedOfflineEmail = authRepository.offlineLoginEmail().orEmpty()
    private val _uiState = MutableStateFlow(
        LoginUiState(
            email = rememberedOfflineEmail,
            offlineAvailable = rememberedOfflineEmail.isNotBlank(),
        ),
    )
    val uiState: StateFlow<LoginUiState> = _uiState

    fun setEmail(value: String) = _uiState.update { it.copy(email = value, offlineAvailable = authRepository.canLoginOffline(value)) }
    fun setPassword(value: String) = _uiState.update { it.copy(password = value) }
    fun setOfflineMode(value: Boolean) = _uiState.update { state ->
        val rememberedEmail = authRepository.offlineLoginEmail().orEmpty()
        val effectiveEmail = state.email.ifBlank { rememberedEmail }
        val available = authRepository.canLoginOffline(effectiveEmail)
        state.copy(
            email = effectiveEmail,
            offlineAvailable = available,
            offlineMode = value && available,
            error = if (value && !available) {
                "Primero inicia sesión online para habilitar el acceso offline."
            } else {
                null
            },
        )
    }
    fun clearError() = _uiState.update { it.copy(error = null) }

    fun login() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(busy = true, error = null) }
            val result = withContext(Dispatchers.IO) {
                if (state.offlineMode) authRepository.loginOffline(state.email.trim(), state.password)
                else authRepository.login(state.email.trim(), state.password)
            }
            result.fold(
                onSuccess = { user -> _uiState.update { it.copy(user = user, error = null, busy = false) } },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message ?: "No se pudo iniciar sesion.", busy = false) }
                },
            )
        }
    }
}
