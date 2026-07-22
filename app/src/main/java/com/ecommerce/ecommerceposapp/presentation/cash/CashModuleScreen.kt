package com.ecommerce.ecommerceposapp.presentation.cash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecommerce.ecommerceposapp.domain.model.cash.CashFlowItem
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Brand = Color(0xFFfd0505)
private val BrandLight = Color(0xFFFFFFFF)
private val GreenIncome = Color(0xFF16A34A)
private val GreenIncomeLight = Color(0xFFDCFCE7)
private val RedExpense = Color(0xFFDC2626)
private val RedExpenseLight = Color(0xFFFFE4E6)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val Surface1 = Color(0xFFF8FAFC)
private val Divider = Color(0xFFE5E7EB)

@Composable
fun CashModuleScreen(
    session: CashSession,
    viewModel: CashModuleViewModel,
    onCashClosed: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // Load on first entry
    LaunchedEffect(session.id) {
        viewModel.load(session)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFFFFFF)),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 640.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 90.dp,
                bottom = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Header turno activo ──────────────────────────────────────────
            item {
                CashHeaderCard(session = session, onRequestClose = { viewModel.requestClose() })
            }

            // ── Resumen del turno ────────────────────────────────────────────
            // item {
            //     CashSummaryCard(summary = state.summary, loading = state.loading, error = state.error)
            // }

            // ── Filtro fecha + tabla flujo ───────────────────────────────────
            item {
                CashFlowSection(
                    state = state,
                    onFromChange = viewModel::setFilterFrom,
                    onToChange = viewModel::setFilterTo,
                    onApply = viewModel::applyDateFilter,
                    onClear = viewModel::clearDateFilter,
                    onSearchChange = viewModel::setFlowSearch,
                    onRefresh = { viewModel.applyDateFilter() },
                )
            }

            // Filas del flujo
            val filtered = state.flowItems.filter { item ->
                state.flowSearch.isBlank() ||
                        item.comentario.contains(state.flowSearch, ignoreCase = true) ||
                        item.razonSocial.contains(state.flowSearch, ignoreCase = true) ||
                        item.cajeroNombre.contains(state.flowSearch, ignoreCase = true) ||
                        item.sucursal.contains(state.flowSearch, ignoreCase = true) ||
                        item.tipoPago.contains(state.flowSearch, ignoreCase = true) ||
                        item.origen.contains(state.flowSearch, ignoreCase = true)
            }

            // Placeholder cuando no hay items
            if (!state.flowLoading && state.flowItems.isEmpty() && state.flowError == null) {
                item {
                    Box(
                        Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Sin movimientos en este período", color = TextSecondary)
                    }
                }
            }

            if (state.flowLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Brand)
                    }
                }
            }

            state.flowError?.let { err ->
                item {
                    Text(err, color = Brand, modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            if (!state.flowLoading && filtered.isNotEmpty()) {
                item {
                    CashFlowTable(items = filtered)
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    // ── Dialog cerrar caja ───────────────────────────────────────────────────
    if (state.showCloseDialog) {
        CashCloseDialog(
            summary = state.summary,
            loading = state.closeLoading,
            error = state.closeError,
            onDismiss = viewModel::dismissClose,
            onConfirm = { counted, obs ->
                viewModel.closeSession(counted, obs, onCashClosed)
            },
        )
    }
}

// ── Header Card ──────────────────────────────────────────────────────────────

@Composable
private fun CashHeaderCard(session: CashSession, onRequestClose: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Turno activo",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    session.cashRegisterName.ifBlank { "Caja" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                /*if (session.cashierName.isNotBlank()) {
                    Text(
                        "Cajero: ${session.cashierName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                    )
                }*/
                val openedStr = if (session.openedAt > 0L)
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(session.openedAt))
                else "—"
                Text(
                    "Apertura: $openedStr",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }
            Button(
                onClick = onRequestClose,
                colors = ButtonDefaults.buttonColors(containerColor = Brand),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            ) {
                Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Cerrar caja", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun CashSummaryCard(summary: CashSummary?, loading: Boolean, error: String?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            Text(
                "Resumen del turno",
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(Modifier.height(14.dp))
            if (loading) {
                Box(Modifier.fillMaxWidth().height(72.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Brand, modifier = Modifier.size(28.dp))
                }
            } else if (error != null) {
                Text(error, color = Brand, style = MaterialTheme.typography.bodySmall)
            } else {
                // Fila 1: Fondo inicial, Ventas, Efectivo
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryChip(label = "Fondo inicial", value = summary?.openingAmount ?: 0.0, modifier = Modifier.weight(1f))
                    SummaryChip(label = "Ventas", value = summary?.totalSales ?: 0.0, modifier = Modifier.weight(1f))
                    SummaryChip(label = "Efectivo", value = summary?.cashAmount ?: 0.0, modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
                // Fila 2: Deposito, Esperado, Total flujo
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryChip(label = "Depósito", value = summary?.deposit ?: 0.0, modifier = Modifier.weight(1f))
                    SummaryChip(label = "Esperado", value = summary?.expectedCash ?: 0.0, modifier = Modifier.weight(1f))
                    SummaryChip(label = "Total flujo", value = summary?.totalFlow ?: 0.0, modifier = Modifier.weight(1f), positive = true)
                }
                /*Spacer(Modifier.height(10.dp))
                // Fila 3: Ingresos / Egresos (movimientos manuales)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryChip(label = "Ingresos", value = summary?.income ?: 0.0, modifier = Modifier.weight(1f), positive = true)
                    SummaryChip(label = "Egresos", value = summary?.expenses ?: 0.0, modifier = Modifier.weight(1f), negative = true)
                }*/
            }
        }
    }
}

@Composable
private fun SummaryChip(
    label: String,
    value: Double,
    modifier: Modifier = Modifier,
    positive: Boolean = false,
    negative: Boolean = false,
) {
    val bg = when {
        positive -> GreenIncomeLight
        negative -> RedExpenseLight
        else -> Surface1
    }
    val textColor = when {
        positive -> GreenIncome
        negative -> RedExpense
        else -> TextPrimary
    }
    Column(
        modifier = modifier
            .background(bg, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Spacer(Modifier.height(4.dp))
        Text(
            "S/ %.2f".format(Locale.US, value),
            fontWeight = FontWeight.Bold,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
        )
    }
}

// ── Flow Section ─────────────────────────────────────────────────────────────

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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Flujo de caja",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
                val total = state.flowItems.size
                if (total > 0) {
                    Text(
                        "$total ${if (total == 1) "registro" else "registros"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                IconButton(onClick = onRefresh, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Recargar", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(12.dp))

            // Filtros de fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = state.filterFrom,
                    onValueChange = onFromChange,
                    label = { Text("Desde", style = MaterialTheme.typography.labelSmall) },
                    placeholder = { Text("yyyy-MM-dd", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                )
                OutlinedTextField(
                    value = state.filterTo,
                    onValueChange = onToChange,
                    label = { Text("Hasta", style = MaterialTheme.typography.labelSmall) },
                    placeholder = { Text("yyyy-MM-dd", style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                )
                IconButton(
                    onClick = onApply,
                    modifier = Modifier
                        .size(44.dp)
                        .background(Brand, RoundedCornerShape(8.dp)),
                ) {
                    Icon(Icons.Filled.FilterAlt, contentDescription = "Aplicar filtro", tint = Color.White, modifier = Modifier.size(18.dp))
                }
                if (state.filterFrom.isNotBlank() || state.filterTo.isNotBlank()) {
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(Icons.Filled.FilterAltOff, contentDescription = "Limpiar filtro", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Buscador inline
            OutlinedTextField(
                value = state.flowSearch,
                onValueChange = onSearchChange,
                placeholder = { Text("Buscar en flujo...", style = MaterialTheme.typography.bodySmall) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

// ── Flow Table ────────────────────────────────────────────────────────────────

@Composable
private fun CashFlowTable(items: List<CashFlowItem>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ── Cabecera ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface1)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                TableHeaderCell("Fecha / Tipo", weight = 2.2f)
                TableHeaderCell("Cliente / Ref.", weight = 2f)
                TableHeaderCell("Importe", weight = 1.3f, align = Alignment.End)
            }

            HorizontalDivider(color = Divider, thickness = 1.dp)

            // ── Filas ─────────────────────────────────────────────────────────
            items.forEachIndexed { index, item ->
                CashFlowTableRow(item)
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Divider,
                        thickness = 0.5.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.TableHeaderCell(
    text: String,
    weight: Float,
    align: Alignment.Horizontal = Alignment.Start,
) {
    Box(modifier = Modifier.weight(weight), contentAlignment = when (align) {
        Alignment.End -> Alignment.CenterEnd
        Alignment.CenterHorizontally -> Alignment.Center
        else -> Alignment.CenterStart
    }) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun CashFlowTableRow(item: CashFlowItem) {
    val isIngreso = item.tipoMovimiento.equals("Ingreso", ignoreCase = true)
    val importeColor = if (isIngreso) GreenIncome else RedExpense
    val importeSign = if (isIngreso) "+" else "−"
    val dateStr = if (item.fecha > 0L)
        SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).format(Date(item.fecha))
    else "—"
    val cliente = item.razonSocial.ifBlank { item.cajeroNombre }.ifBlank { "—" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Columna 1: ícono + fecha + tipo
        Row(
            modifier = Modifier.weight(2.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        if (isIngreso) GreenIncomeLight else RedExpenseLight,
                        RoundedCornerShape(7.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isIngreso) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                    contentDescription = null,
                    tint = importeColor,
                    modifier = Modifier.size(15.dp),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1,
                )
                Text(
                    item.tipoMovimiento.ifBlank { item.origen },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = importeColor,
                    maxLines = 1,
                )
                if (item.tipoPago.isNotBlank()) {
                    Text(
                        item.tipoPago,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        maxLines = 1,
                    )
                }
            }
        }

        // Columna 2: cliente + comentario
        Column(
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                cliente,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (item.comentario.isNotBlank()) {
                Text(
                    item.comentario,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (item.sucursal.isNotBlank()) {
                Text(
                    item.sucursal,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        // Columna 3: importe (alineado a la derecha)
        Text(
            "$importeSign S/ %.2f".format(Locale.US, kotlin.math.abs(item.importe)),
            modifier = Modifier.weight(1.3f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = importeColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            maxLines = 1,
        )
    }
}

// ── Close Dialog ─────────────────────────────────────────────────────────────

@Composable
private fun CashCloseDialog(
    summary: CashSummary?,
    loading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit,
) {
    var counted by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!loading) onDismiss() },
        containerColor = Color.White,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(22.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier.size(46.dp).background(Color.White, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = Brand)
                }
                Column {
                    Text("Cerrar caja", fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        "Confirma el efectivo de tu turno",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Resumen rápido
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SummaryDialogRow("Ventas", summary?.totalSales ?: 0.0)
                        SummaryDialogRow("Efectivo esperado", summary?.expectedCash ?: 0.0, bold = true)
                    }
                }
                // Efectivo contado
                OutlinedTextField(
                    value = counted,
                    onValueChange = { counted = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Efectivo contado") },
                    prefix = { Text("S/ ") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Brand,
                        focusedLabelColor = Brand,
                        cursorColor = Brand,
                    ),
                )
                // Diferencia en tiempo real
                val diff = (counted.toDoubleOrNull() ?: 0.0) - (summary?.expectedCash ?: 0.0)
                if (counted.isNotBlank()) {
                    val diffColor = when {
                        diff > 0 -> GreenIncome
                        diff < 0 -> RedExpense
                        else -> TextSecondary
                    }
                    Text(
                        "Diferencia: ${if (diff >= 0) "+" else ""}S/ %.2f".format(Locale.US, diff),
                        color = diffColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                // Observaciones
                OutlinedTextField(
                    value = observations,
                    onValueChange = { observations = it },
                    label = { Text("Observaciones (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Brand,
                        focusedLabelColor = Brand,
                        cursorColor = Brand,
                    ),
                )
                error?.let { Text(it, color = Brand, style = MaterialTheme.typography.bodySmall) }
                if (loading) LinearProgressIndicator(color = Brand, modifier = Modifier.fillMaxWidth())
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, enabled = !loading) {
                Text("Cancelar")
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(counted.toDoubleOrNull() ?: 0.0, observations) },
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Brand,
                    disabledContainerColor = Color(0xFFFFE4E6),
                    disabledContentColor = Brand,
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text("Cerrar caja", fontWeight = FontWeight.SemiBold)
            }
        },
    )
}

@Composable
private fun SummaryDialogRow(
    label: String,
    value: Double,
    bold: Boolean = false,
    positive: Boolean = false,
    negative: Boolean = false,
) {
    val color = when {
        positive -> GreenIncome
        negative -> RedExpense
        else -> TextPrimary
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        Text(
            "S/ %.2f".format(Locale.US, value),
            color = color,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
