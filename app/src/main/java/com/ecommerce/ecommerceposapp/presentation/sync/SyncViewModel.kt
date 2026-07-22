package com.ecommerce.ecommerceposapp.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository
import com.ecommerce.ecommerceposapp.domain.model.sync.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    // true solo en la primerísima sincronización (modo offline inicial).
    val isInitialSync: Boolean = false,
    // Segundos transcurridos desde que empezó a sincronizar; alimenta el
    // texto "Sincronizando... 12s" en el diálogo para que el usuario vea
    // que el proceso avanza y no está colgado.
    val elapsedSeconds: Int = 0,
)

class SyncViewModel(
    private val syncRepository: SyncRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SyncUiState())
    val uiState: StateFlow<SyncUiState> = _uiState

    private var timerJob: Job? = null

    fun needsSync(userId: Long): Boolean = !syncRepository.hasInitialSync(userId)

    /**
     * [isInitial] debe venir del mismo valor que ya usa la pantalla como
     * `requiresSync` (calculado con [needsSync]). Así evitamos otra
     * consulta y mantenemos una sola fuente de verdad.
     *
     * Regla de selección:
     *  - Primera sincronización -> todos los módulos vienen seleccionados.
     *  - Re-sincronización -> arranca todo deseleccionado; el usuario
     *    decide qué módulo sincronizar cada vez.
     */
    fun loadModules(isInitial: Boolean) {
        viewModelScope.launch {
            val modules = withContext(Dispatchers.IO) { syncRepository.listSyncModuleStatus() }
            _uiState.update { s ->
                val selected = if (isInitial) modules.map { it.key }.toSet() else emptySet()
                s.copy(modules = modules, selectedModules = selected, isInitialSync = isInitial)
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

    fun selectAllModules() {
        _uiState.update { s -> s.copy(selectedModules = s.modules.map { it.key }.toSet()) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedModules = emptySet()) }
    }

    /** Llamar desde `LaunchedEffect` o `scope.launch` — escribe en Realm fuera del hilo UI. */
    suspend fun sync(user: UserSession) {
        val current = _uiState.value
        val selected = current.selectedModules

        // En re-sincronización no dejamos disparar sin nada elegido; en la
        // primera sync sí puede ir vacío porque se interpreta como "todo".
        if (selected.isEmpty() && !current.isInitialSync) {
            _uiState.update { it.copy(message = "Selecciona al menos un módulo para sincronizar.") }
            return
        }

        _uiState.update {
            it.copy(
                syncing = true,
                completed = false,
                elapsedSeconds = 0,
                message = "Sincronizando módulos seleccionados...",
            )
        }

        // Cronómetro visible mientras dura la sincronización real.
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }

        val result = withContext(Dispatchers.IO) {
            if (current.isInitialSync) syncRepository.syncInitialData(user)
            else syncRepository.syncModules(user, selected)
        }

        timerJob?.cancel()

        result.fold(
            onSuccess = {
                val modules = withContext(Dispatchers.IO) { syncRepository.listSyncModuleStatus() }
                _uiState.update {
                    it.copy(
                        syncing = false,
                        completed = true,
                        message = "Sincronización completada.",
                        modules = modules,
                        // La próxima vez que se abra esta pantalla arranca
                        // deseleccionado (ya no es la primera sync).
                        selectedModules = emptySet(),
                        isInitialSync = false,
                    )
                }
            },
            onFailure = { ex ->
                _uiState.update {
                    it.copy(syncing = false, completed = false, message = ex.message ?: "Error al sincronizar.")
                }
            },
        )
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}