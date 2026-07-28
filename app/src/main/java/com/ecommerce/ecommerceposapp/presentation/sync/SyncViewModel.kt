package com.ecommerce.ecommerceposapp.presentation.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository
import com.ecommerce.ecommerceposapp.domain.model.sync.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.domain.sync.SyncPlan
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
    val selectionNotice: String = "",
    val progressFraction: Float = 0f,
    val activeModuleLabel: String = "",
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
    suspend fun loadModules(isInitial: Boolean) {
        val modules = withContext(Dispatchers.IO) { syncRepository.listSyncModuleStatus() }
        _uiState.update { s ->
            val selected = if (isInitial) modules.map { it.key }.toSet() else emptySet()
            s.copy(
                modules = modules,
                selectedModules = selected,
                selectionNotice = "",
                isInitialSync = isInitial,
            )
        }
    }

    fun toggleModule(moduleKey: String) {
        _uiState.update { s ->
            val labels = s.modules.associate { it.key to it.label }
            if (moduleKey in s.selectedModules) {
                val selected = SyncPlan.removeWithDependents(s.selectedModules, moduleKey)
                val removedDependents = s.selectedModules - selected - moduleKey
                val notice = if (removedDependents.isEmpty()) {
                    ""
                } else {
                    "También se quitaron: ${removedDependents.joinToString { labels[it] ?: it }}."
                }
                s.copy(selectedModules = selected, selectionNotice = notice)
            } else {
                val selected = SyncPlan.expand(s.selectedModules + moduleKey)
                val addedDependencies = selected - s.selectedModules - moduleKey
                val notice = if (addedDependencies.isEmpty()) {
                    ""
                } else {
                    "Requisitos incluidos automáticamente: ${addedDependencies.joinToString { labels[it] ?: it }}."
                }
                s.copy(selectedModules = selected, selectionNotice = notice)
            }
        }
    }

    fun selectAllModules() {
        _uiState.update { s ->
            s.copy(
                selectedModules = SyncPlan.expand(s.modules.map { it.key }.toSet()),
                selectionNotice = "",
            )
        }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedModules = emptySet(), selectionNotice = "") }
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
            val labels = it.modules.associate { module -> module.key to module.label }
            val firstSelected = SyncPlan.orderedModules.firstOrNull { key -> key in selected }
            it.copy(
                syncing = true,
                completed = false,
                elapsedSeconds = 0,
                progressFraction = 0f,
                activeModuleLabel = firstSelected?.let(labels::get).orEmpty(),
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

        val labels = current.modules.associate { it.key to it.label }
        val onProgress: (com.ecommerce.ecommerceposapp.domain.model.sync.SyncProgress) -> Unit = { progress ->
            _uiState.update {
                it.copy(
                    progressFraction = progress.fraction.coerceIn(0f, 1f),
                    activeModuleLabel = labels[progress.activeModuleKey] ?: progress.activeModuleKey,
                )
            }
        }
        val result = withContext(Dispatchers.IO) {
            if (current.isInitialSync) syncRepository.syncInitialData(user, onProgress)
            else syncRepository.syncModules(user, selected, onProgress)
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
                        selectionNotice = "",
                        progressFraction = 1f,
                        activeModuleLabel = "",
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
