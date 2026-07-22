package com.ecommerce.ecommerceposapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.presentation.sync.SyncUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Brand = Color(0xFFfd0505)
private val AppBg = Color(0xFFF5F7FA)
private val SurfaceWhite = Color(0xFFFFFFFF)
private val SurfaceAlt = Color(0xFFEEF0F5)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)

@Composable
internal fun SyncScreen(
    user: UserSession,
    state: SyncUiState,
    requiresSync: Boolean,
    onSync: () -> Unit,
    onToggleModule: (String) -> Unit,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
) {
    fun formatLastSync(millis: Long): String {
        if (millis <= 0L) return "Nunca"
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "PE")).format(Date(millis))
    }

    fun formatElapsed(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return if (m > 0) "%d:%02d".format(m, s) else "${s}s"
    }

    // El diálogo aparece apenas empieza a sincronizar y se mantiene abierto
    // en el estado "completado" hasta que el usuario continúe o lo cierre.
    var showSyncDialog by remember { mutableStateOf(false) }
    LaunchedEffect(state.syncing) {
        if (state.syncing) showSyncDialog = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(AppBg, SurfaceAlt))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 620.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                OutlinedButton(onClick = onBack) { Text("← Volver") }
            }
            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brand),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(34.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Hola, ${user.name}",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (requiresSync) "Primera sincronización requerida para modo offline." else "Sincronización inicial ya completada.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
            )
            Spacer(Modifier.height(24.dp))

            if (requiresSync) {
                Button(
                    onClick = onSync,
                    colors = ButtonDefaults.buttonColors(containerColor = Brand),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Sincronizar catálogo", fontWeight = FontWeight.Bold, color = Color.White) }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onSync) { Text("Re-sincronizar") }
                    Button(
                        onClick = onContinue,
                        colors = ButtonDefaults.buttonColors(containerColor = Brand),
                        shape = RoundedCornerShape(12.dp),
                    ) { Text("Entrar al POS", fontWeight = FontWeight.Bold, color = Color.White) }
                }
            }

            Spacer(Modifier.height(24.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 620.dp),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceWhite,
                shadowElevation = 4.dp,
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Módulos de sincronización",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                        )
                        // Solo tiene sentido ofrecer el atajo cuando no es
                        // la primera sync (ahí ya viene todo marcado).
                        if (!requiresSync) {
                            val allSelected = state.modules.isNotEmpty() &&
                                    state.selectedModules.size == state.modules.size
                            TextButton(onClick = if (allSelected) onClearSelection else onSelectAll) {
                                Text(if (allSelected) "Quitar todo" else "Elegir todo", color = Brand)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    state.modules.forEach { m ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(m.label, fontWeight = FontWeight.Medium, color = TextPrimary)
                                Text(
                                    "Última: ${formatLastSync(m.lastSyncAt)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                )
                            }
                            FilterChip(
                                selected = state.selectedModules.contains(m.key),
                                onClick = { onToggleModule(m.key) },
                                label = { Text(if (state.selectedModules.contains(m.key)) "Seleccionado" else "Seleccionar") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Brand,
                                    selectedLabelColor = Color.White,
                                ),
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showSyncDialog) {
        SyncProgressDialog(
            state = state,
            formatElapsed = ::formatElapsed,
            onDismiss = { showSyncDialog = false },
            onContinue = {
                showSyncDialog = false
                onContinue()
            },
        )
    }
}

@Composable
private fun SyncProgressDialog(
    state: SyncUiState,
    formatElapsed: (Int) -> String,
    onDismiss: () -> Unit,
    onContinue: () -> Unit,
) {
    Dialog(onDismissRequest = { if (!state.syncing) onDismiss() }) {
        Surface(
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceWhite,
            shadowElevation = 12.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (state.completed) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(72.dp),
                    )
                } else {
                    Icon(
                        Icons.Filled.Sync,
                        contentDescription = null,
                        tint = Brand,
                        modifier = Modifier.size(64.dp),
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    if (state.completed) "Sincronización completa" else "Sincronizando catálogo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Spacer(Modifier.height(16.dp))

                if (!state.completed) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Brand,
                        trackColor = SurfaceAlt,
                    )
                    Spacer(Modifier.height(8.dp))
                    // Tiempo transcurrido: evita que el usuario piense que
                    // la app se colgó cuando la sync demora.
                    Text(
                        "Tiempo transcurrido: ${formatElapsed(state.elapsedSeconds)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                    )
                    Spacer(Modifier.height(16.dp))
                }

                if (state.message.isNotBlank()) {
                    Text(
                        state.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(20.dp))
                }

                if (state.completed) {
                    Button(
                        onClick = onContinue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Continuar al POS", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text("Cerrar", color = TextSecondary)
                    }
                }
            }
        }
    }
}