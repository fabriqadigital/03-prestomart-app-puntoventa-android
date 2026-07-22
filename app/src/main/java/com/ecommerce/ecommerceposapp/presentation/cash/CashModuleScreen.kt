package com.ecommerce.ecommerceposapp.presentation.cash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.cash.CashFlowItem
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary
import com.ecommerce.ecommerceposapp.ui.theme.AppBackground
import com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.ecommerce.ecommerceposapp.ui.theme.BrandRedLight
import com.ecommerce.ecommerceposapp.ui.theme.GreenSuccess
import com.ecommerce.ecommerceposapp.ui.theme.GreenSuccessLight
import com.ecommerce.ecommerceposapp.ui.theme.PosLinearLoader
import com.ecommerce.ecommerceposapp.ui.theme.Radius
import com.ecommerce.ecommerceposapp.ui.theme.RedDanger
import com.ecommerce.ecommerceposapp.ui.theme.RedDangerLight
import com.ecommerce.ecommerceposapp.ui.theme.Spacing
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceMuted
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceSubtle
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite
import com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
import com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
import com.ecommerce.ecommerceposapp.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ─────────────────────────────────────────────────────────────────────────────
//  MAIN SCREEN
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CashModuleScreen(
    session: CashSession,
    viewModel: CashModuleViewModel,
    onCashClosed: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(session.id) { viewModel.load(session) }

    Box(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start  = Spacing.lg,
                end    = Spacing.lg,
                top    = 90.dp,
                bottom = Spacing.xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        ) {
            item { CashHeaderCard(session = session, onRequestClose = viewModel::requestClose) }
            item {
                CashFlowSection(
                    state          = state,
                    onFromChange   = viewModel::setFilterFrom,
                    onToChange     = viewModel::setFilterTo,
                    onApply        = viewModel::applyDateFilter,
                    onClear        = viewModel::clearDateFilter,
                    onSearchChange = viewModel::setFlowSearch,
                    onRefresh      = viewModel::applyDateFilter,
                )
            }

            val filtered = state.flowItems.filter { item ->
                state.flowSearch.isBlank() ||
                    item.comentario.contains(state.flowSearch, ignoreCase = true) ||
                    item.razonSocial.contains(state.flowSearch, ignoreCase = true) ||
                    item.cajeroNombre.contains(state.flowSearch, ignoreCase = true) ||
                    item.sucursal.contains(state.flowSearch, ignoreCase = true) ||
                    item.tipoPago.contains(state.flowSearch, ignoreCase = true) ||
                    item.origen.contains(state.flowSearch, ignoreCase = true)
            }

            if (state.flowLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(vertical = Spacing.xl), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BrandRed, modifier = Modifier.size(32.dp), strokeWidth = 3.dp)
                    }
                }
            }

            state.flowError?.let { err ->
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(Radius.md))
                            .background(RedDangerLight).padding(Spacing.md),
                    ) {
                        Text(err, color = RedDanger, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (!state.flowLoading && filtered.isEmpty() && state.flowError == null) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(vertical = Spacing.xl),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = SurfaceMuted, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(Spacing.sm))
                            Text("Sin movimientos en este período", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            items(filtered, key = { it.flujoId }) { item -> CashFlowRow(item) }

            item { Spacer(Modifier.height(Spacing.xxl)) }
        }
    }

    if (state.showCloseDialog) {
        CashCloseDialog(
            summary   = state.summary,
            loading   = state.closeLoading,
            error     = state.closeError,
            onDismiss = viewModel::dismissClose,
            onConfirm = { counted, obs -> viewModel.closeSession(counted, obs, onCashClosed) },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  HEADER CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CashHeaderCard(session: CashSession, onRequestClose: () -> Unit) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(Radius.lg),
        color           = SurfaceWhite,
        shadowElevation = 2.dp,
        tonalElevation  = 0.dp,
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = Spacing.lg, vertical = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(Radius.md))
                    .background(BrandRedLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = BrandRed, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(Spacing.md))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "Turno activo",
                    style      = MaterialTheme.typography.labelSmall,
                    color      = TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    session.cashRegisterName.ifBlank { "Caja" },
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                )
                val openedStr = if (session.openedAt > 0L)
                    SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault()).format(Date(session.openedAt))
                else "—"
                Text(
                    "Apertura: $openedStr",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }
            Spacer(Modifier.width(Spacing.md))
            Button(
                onClick        = onRequestClose,
                colors         = ButtonDefaults.buttonColors(containerColor = BrandRed),
                shape          = RoundedCornerShape(Radius.md),
                contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.sm),
            ) {
                Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(Spacing.xs))
                Text("Cerrar caja", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  FLOW SECTION  (filtros + buscador)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CashFlowSection(
    state: CashModuleUiState,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    onApply: () -> Unit,
    onClear: () -> Unit,
    onSearchChange: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(Radius.lg),
        color           = SurfaceWhite,
        shadowElevation = 2.dp,
        tonalElevation  = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(Spacing.lg)) {
            // Título + contador + refresh
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Flujo de caja",
                    style      = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    modifier   = Modifier.weight(1f),
                )
                val total = state.flowItems.size
                if (total > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Radius.pill))
                            .background(SurfaceMuted)
                            .padding(horizontal = Spacing.sm, vertical = 2.dp),
                    ) {
                        Text(
                            "$total ${if (total == 1) "registro" else "registros"}",
                            style  = MaterialTheme.typography.labelSmall,
                            color  = TextSecondary,
                        )
                    }
                    Spacer(Modifier.width(Spacing.sm))
                }
                IconButton(onClick = onRefresh, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Recargar", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(Spacing.md))

            // Filtros de fecha
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value         = state.filterFrom,
                    onValueChange = onFromChange,
                    label         = { Text("Desde", style = MaterialTheme.typography.labelSmall) },
                    placeholder   = { Text("yyyy-MM-dd", style = MaterialTheme.typography.labelSmall, color = TextTertiary) },
                    modifier      = Modifier.weight(1f),
                    singleLine    = true,
                    shape         = RoundedCornerShape(Radius.md),
                    textStyle     = MaterialTheme.typography.bodySmall,
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = BrandRed,
                        unfocusedBorderColor = BorderDefault,
                        cursorColor          = BrandRed,
                        focusedLabelColor    = BrandRed,
                        focusedContainerColor   = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                    ),
                )
                OutlinedTextField(
                    value         = state.filterTo,
                    onValueChange = onToChange,
                    label         = { Text("Hasta", style = MaterialTheme.typography.labelSmall) },
                    placeholder   = { Text("yyyy-MM-dd", style = MaterialTheme.typography.labelSmall, color = TextTertiary) },
                    modifier      = Modifier.weight(1f),
                    singleLine    = true,
                    shape         = RoundedCornerShape(Radius.md),
                    textStyle     = MaterialTheme.typography.bodySmall,
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = BrandRed,
                        unfocusedBorderColor = BorderDefault,
                        cursorColor          = BrandRed,
                        focusedLabelColor    = BrandRed,
                        focusedContainerColor   = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                    ),
                )
                // Botón aplicar filtro
                IconButton(
                    onClick  = onApply,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(Radius.md))
                        .background(BrandRed),
                ) {
                    Icon(Icons.Filled.FilterAlt, contentDescription = "Aplicar filtro", tint = Color.White, modifier = Modifier.size(18.dp))
                }
                // Botón limpiar filtro
                if (state.filterFrom.isNotBlank() || state.filterTo.isNotBlank()) {
                    IconButton(
                        onClick  = onClear,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(Radius.md))
                            .background(SurfaceMuted),
                    ) {
                        Icon(Icons.Filled.FilterAltOff, contentDescription = "Limpiar filtro", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(Modifier.height(Spacing.sm))

            // Buscador inline
            OutlinedTextField(
                value         = state.flowSearch,
                onValueChange = onSearchChange,
                placeholder   = { Text("Buscar en flujo...", style = MaterialTheme.typography.bodySmall, color = TextTertiary) },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                shape         = RoundedCornerShape(Radius.md),
                textStyle     = MaterialTheme.typography.bodySmall,
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = BrandRed,
                    unfocusedBorderColor    = BorderDefault,
                    cursorColor             = BrandRed,
                    focusedContainerColor   = SurfaceWhite,
                    unfocusedContainerColor = SurfaceWhite,
                ),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  FLOW ROW
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CashFlowRow(item: CashFlowItem) {
    val isIngreso    = item.tipoMovimiento.equals("Ingreso", ignoreCase = true)
    val importeColor = if (isIngreso) GreenSuccess else RedDanger
    val importeBg    = if (isIngreso) GreenSuccessLight else RedDangerLight
    val importeSign  = if (isIngreso) "+" else "-"
    val dateStr      = if (item.fecha > 0L)
        SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault()).format(Date(item.fecha))
    else "—"

    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(Radius.lg),
        color           = SurfaceWhite,
        shadowElevation = 1.dp,
        tonalElevation  = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(Spacing.md)) {
            // Fecha + importe
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(Radius.sm))
                            .background(importeBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = if (isIngreso) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                            contentDescription = null,
                            tint     = importeColor,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    Spacer(Modifier.width(Spacing.sm))
                    Text(dateStr, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Text(
                    "$importeSign S/ %.2f".format(Locale.US, kotlin.math.abs(item.importe)),
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color      = importeColor,
                )
            }

            Spacer(Modifier.height(Spacing.sm))

            // Pills: tipo / origen / pago
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                FlowPill(text = item.tipoMovimiento, bg = importeBg, color = importeColor)
                FlowPill(text = item.origen,         bg = SurfaceMuted, color = TextSecondary)
                FlowPill(text = item.tipoPago,       bg = SurfaceMuted, color = TextSecondary)
            }

            Spacer(Modifier.height(Spacing.sm))

            // Razón social
            if (item.razonSocial.isNotBlank()) {
                Text(
                    item.razonSocial,
                    style      = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextPrimary,
                    maxLines   = 1,
                )
                Spacer(Modifier.height(2.dp))
            }

            // Sucursal · Caja · Cajero
            Text(
                "${item.sucursal} · ${item.cajaNombre} · ${item.cajeroNombre}",
                style    = MaterialTheme.typography.labelSmall,
                color    = TextSecondary,
                maxLines = 1,
            )

            // Comentario
            if (item.comentario.isNotBlank()) {
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    item.comentario,
                    style    = MaterialTheme.typography.labelSmall,
                    color    = TextTertiary,
                    maxLines = 2,
                )
            }
        }
    }
}

@Composable
private fun FlowPill(text: String, bg: Color, color: Color) {
    if (text.isBlank()) return
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.pill))
            .background(bg)
            .padding(horizontal = Spacing.sm, vertical = 2.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.SemiBold)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CLOSE DIALOG
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CashCloseDialog(
    summary: CashSummary?,
    loading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit,
) {
    var counted      by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!loading) onDismiss() },
        containerColor   = SurfaceWhite,
        shape            = RoundedCornerShape(Radius.xl),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(Radius.md))
                        .background(BrandRedLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = BrandRed, modifier = Modifier.size(22.dp))
                }
                Column {
                    Text("Cerrar caja", fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Confirma el efectivo de tu turno", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                // Resumen rápido
                Surface(color = SurfaceSubtle, shape = RoundedCornerShape(Radius.md)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        SummaryRow("Ventas totales",     summary?.totalSales ?: 0.0)
                        SummaryRow("Efectivo esperado",  summary?.expectedCash ?: 0.0, bold = true)
                    }
                }
                // Efectivo contado
                OutlinedTextField(
                    value         = counted,
                    onValueChange = { counted = it.filter { c -> c.isDigit() || c == '.' } },
                    label         = { Text("Efectivo contado") },
                    prefix        = { Text("S/ ") },
                    modifier      = Modifier.fillMaxWidth(),
                    singleLine    = true,
                    shape         = RoundedCornerShape(Radius.md),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = BrandRed,
                        unfocusedBorderColor = BorderDefault,
                        cursorColor          = BrandRed,
                        focusedLabelColor    = BrandRed,
                        focusedContainerColor   = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                    ),
                )
                // Diferencia en tiempo real
                val diff = (counted.toDoubleOrNull() ?: 0.0) - (summary?.expectedCash ?: 0.0)
                if (counted.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Radius.md))
                            .background(
                                when {
                                    diff > 0  -> GreenSuccessLight
                                    diff < 0  -> RedDangerLight
                                    else      -> SurfaceMuted
                                }
                            )
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
                    ) {
                        Text(
                            "Diferencia: ${if (diff >= 0) "+" else ""}S/ %.2f".format(Locale.US, diff),
                            color      = when {
                                diff > 0 -> GreenSuccess
                                diff < 0 -> RedDanger
                                else     -> TextSecondary
                            },
                            style      = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                // Observaciones
                OutlinedTextField(
                    value         = observations,
                    onValueChange = { observations = it },
                    label         = { Text("Observaciones (opcional)") },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(Radius.md),
                    minLines      = 2,
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = BrandRed,
                        unfocusedBorderColor = BorderDefault,
                        cursorColor          = BrandRed,
                        focusedLabelColor    = BrandRed,
                        focusedContainerColor   = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                    ),
                )
                error?.let {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(Radius.md))
                            .background(RedDangerLight).padding(horizontal = Spacing.md, vertical = Spacing.sm),
                    ) {
                        Text(it, color = RedDanger, style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (loading) PosLinearLoader()
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !loading,
                shape   = RoundedCornerShape(Radius.md),
            ) { Text("Cancelar") }
        },
        confirmButton = {
            Button(
                onClick  = { onConfirm(counted.toDoubleOrNull() ?: 0.0, observations) },
                enabled  = !loading,
                colors   = ButtonDefaults.buttonColors(containerColor = BrandRed),
                shape    = RoundedCornerShape(Radius.md),
            ) { Text("Cerrar caja", fontWeight = FontWeight.SemiBold, color = Color.White) }
        },
    )
}

@Composable
private fun SummaryRow(label: String, value: Double, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        Text(
            "S/ %.2f".format(Locale.US, value),
            color      = TextPrimary,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            style      = MaterialTheme.typography.bodySmall,
        )
    }
}
