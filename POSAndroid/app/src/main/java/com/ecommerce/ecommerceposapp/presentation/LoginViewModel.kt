package com.ecommerce.ecommerceposapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.data.repository.AuthRepository
import com.ecommerce.ecommerceposapp.data.repository.LoginMode
import com.ecommerce.ecommerceposapp.domain.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LoginUiState(
    val email: String = "admin@gmail.com",
    val password: String = "123456789",
    /** Si es false, solo modo en línea (primera vez o sin catálogo sincronizado). */
    val canChooseOffline: Boolean = false,
    val onlineMode: Boolean = true,
    val busy: Boolean = false,
    val error: String? = null,
    val user: UserSession? = null,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        refreshOfflineAvailability()
    }

    /** Realm 10+ no permite escrituras en el hilo UI: consultar aquí en IO. */
    fun refreshOfflineAvailability() {
        viewModelScope.launch {
            val canOffline = withContext(Dispatchers.IO) { authRepository.canUseOfflineLogin() }
            _uiState.update { s ->
                val wasCan = s.canChooseOffline
                s.copy(
                    canChooseOffline = canOffline,
                    onlineMode = when {
                        !canOffline -> true
                        !wasCan && canOffline -> false
                        else -> s.onlineMode
                    },
                )
            }
        }
    }

    fun setEmail(value: String) = _uiState.update { it.copy(email = value) }
    fun setPassword(value: String) = _uiState.update { it.copy(password = value) }
    fun setOnlineMode(value: Boolean) {
        if (!value && !_uiState.value.canChooseOffline) return
        _uiState.update { it.copy(onlineMode = value, error = null) }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }

    fun login() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(busy = true, error = null) }
            val mode = if (state.onlineMode) LoginMode.OnlineOnly else LoginMode.OfflineOnly
            val result = withContext(Dispatchers.IO) {
                authRepository.login(state.email.trim(), state.password, mode)
            }
            result.fold(
                onSuccess = { user -> _uiState.update { it.copy(user = user, error = null, busy = false) } },
                onFailure = { ex ->
                    _uiState.update {
                        it.copy(error = ex.message ?: "No se pudo iniciar sesión.", busy = false)
                    }
                },
            )
        }
    }
}
