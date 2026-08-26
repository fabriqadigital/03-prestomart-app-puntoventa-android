package com.ecommerce.ecommerceposapp.presentation.clients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.ecommerce.ecommerceposapp.ui.theme.GreenSuccess
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceMuted
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite
import com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
import com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
import com.ecommerce.ecommerceposapp.ui.theme.TextTertiary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Ancho de pantalla a partir del cual se considera tablet: panel lateral en vez de pantalla completa. */
private const val TABLET_BREAKPOINT_DP = 600

/**
 * Selector de cliente para el carrito. En celulares se abre como una pantalla nueva (a pantalla
 * completa, deslizando desde la derecha); en tablets se abre como un panel lateral al costado,
 * dejando visible el resto del carrito. Tocar cualquier cliente de la lista lo selecciona y cierra.
 */
@Composable
fun ClientPickerDialog(
    clients: List<ClientRow>,
    selectedClientId: Long?,
    onDismiss: () -> Unit,
    onClientSelected: (ClientRow?) -> Unit,
) {
    val isTablet = LocalConfiguration.current.screenWidthDp >= TABLET_BREAKPOINT_DP
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    fun close() {
        visible = false
        scope.launch { delay(200); onDismiss() }
    }

    var query by remember { mutableStateOf("") }
    val activeClients = remember(clients) { clients.filter { it.active } }
    val filtered = remember(activeClients, query) {
        val q = query.trim()
        if (q.isEmpty()) activeClients
        else activeClients.filter { client ->
            client.name.contains(q, ignoreCase = true) ||
                client.lastName.contains(q, ignoreCase = true) ||
                client.businessName.contains(q, ignoreCase = true) ||
                client.document.contains(q, ignoreCase = true)
        }
    }

    Dialog(
        onDismissRequest = { close() },
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false),
    ) {
        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200)),
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(if (isTablet) Color(0x8A000000) else SurfaceWhite)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                            if (isTablet) close()
                        },
                )
            }
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(animationSpec = tween(260), initialOffsetX = { it }),
                exit = slideOutHorizontally(animationSpec = tween(220), targetOffsetX = { it }),
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                val panelModifier = if (isTablet) {
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.36f)
                        .widthIn(min = 340.dp, max = 440.dp)
                } else {
                    Modifier.fillMaxSize()
                }
                Surface(
                    modifier = panelModifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
                    color = SurfaceWhite,
                    shape = if (isTablet) RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp) else RoundedCornerShape(0.dp),
                    shadowElevation = if (isTablet) 8.dp else 0.dp,
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .let { if (!isTablet) it.statusBarsPadding() else it }
                            .padding(20.dp),
                    ) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            if (!isTablet) {
                                IconButton(onClick = { close() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                                }
                                Spacer(Modifier.width(4.dp))
                            }
                            Text(
                                "Clientes",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f),
                            )
                            if (isTablet) {
                                IconButton(onClick = { close() }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = query,
                            onValueChange = { query = it },
                            placeholder = { Text("Buscar") },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextTertiary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandRed,
                                unfocusedBorderColor = BorderDefault,
                                focusedContainerColor = SurfaceMuted,
                                unfocusedContainerColor = SurfaceMuted,
                            ),
                        )
                        Spacer(Modifier.height(8.dp))
                        LazyColumn(Modifier.weight(1f)) {
                            item {
                                ClientPickerRow(
                                    icon = Icons.Filled.PersonOff,
                                    title = "Cliente genérico",
                                    subtitle = null,
                                    selected = selectedClientId == null,
                                    phone = "",
                                    onClick = { onClientSelected(null); close() },
                                )
                                HorizontalDivider(color = BorderDefault)
                            }
                            items(filtered, key = { it.id }) { client ->
                                val displayName = client.businessName.ifBlank {
                                    "${client.name} ${client.lastName}".trim().ifBlank { "Cliente" }
                                }
                                ClientPickerRow(
                                    icon = if (client.personType == "Juridica") Icons.Filled.Business else Icons.Filled.Person,
                                    title = displayName,
                                    subtitle = if (client.document.isNotBlank()) "${client.documentType} ${client.document}" else null,
                                    selected = client.id == selectedClientId,
                                    phone = client.phone,
                                    onClick = { onClientSelected(client); close() },
                                )
                                HorizontalDivider(color = BorderDefault)
                            }
                            if (filtered.isEmpty() && query.isNotBlank()) {
                                item {
                                    Text(
                                        "No se encontraron clientes.",
                                        color = TextSecondary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 24.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClientPickerRow(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    selected: Boolean,
    phone: String,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (selected) SurfaceMuted else Color.Transparent)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (selected) BrandRed.copy(alpha = 0.12f) else SurfaceMuted),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = if (selected) BrandRed else TextSecondary, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                title,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
        if (phone.isNotBlank()) {
            Icon(Icons.Filled.Call, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(18.dp))
        }
    }
}
