package com.ecommerce.ecommerceposapp.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository
import com.ecommerce.ecommerceposapp.domain.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SyncUiState(
    val syncing: Boolean = false,
    val completed: Boolean = false,
    val message: String = "",
    val modules: List<SyncModuleStatus> = emptyList(),
    val selectedModules: Set<String> = emptySet(),
)

class SyncViewModel(
    private val syncRepository: SyncRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SyncUiState())
    val uiState: StateFlow<SyncUiState> = _uiState

    fun needsSync(userId: Long): Boolean = !syncRepository.hasInitialSync(userId)

    fun loadModules() {
        viewModelScope.launch {
            val modules = withContext(Dispatchers.IO) { syncRepository.listSyncModuleStatus() }
            _uiState.update { s ->
                val selected = if (s.selectedModules.isEmpty()) modules.map { it.key }.toSet() else s.selectedModules
                s.copy(modules = modules, selectedModules = selected)
            }
        }
    }

    fun toggleModule(moduleKey: String) {
        _uiState.update { s ->
            val selected = s.selectedModules.toMutableSet()
            if (!selected.add(moduleKey)) selected.remove(moduleKey)
            s.copy(selectedModules = selected)
        }
    }

    /** Llamar desde `LaunchedEffect` o `scope.launch` — escribe en Realm fuera del hilo UI. */
    suspend fun sync(user: UserSession) {
        val selected = _uiState.value.selectedModules
        _uiState.update { it.copy(syncing = true, completed = false, message = "Sincronizando módulos seleccionados...") }
        val result = withContext(Dispatchers.IO) {
            if (selected.isEmpty()) syncRepository.syncInitialData(user)
            else syncRepository.syncModules(user, selected)
        }
        result.fold(
            onSuccess = {
                val modules = withContext(Dispatchers.IO) { syncRepository.listSyncModuleStatus() }
                _uiState.update { it.copy(syncing = false, completed = true, message = "Sincronización completada.", modules = modules) }
            },
            onFailure = { ex ->
                _uiState.update {
                    it.copy(syncing = false, completed = false, message = ex.message ?: "Error al sincronizar.")
                }
            },
        )
    }
}
