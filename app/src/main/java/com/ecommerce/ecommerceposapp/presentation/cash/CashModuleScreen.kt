package com.ecommerce.ecommerceposapp.presentation.cash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.cash.CashFlowItem
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Brand = Color(0xFFfd0505)
private val GreenIncome = Color(0xFF16A34A)
private val RedExpense = Color(0xFFDC2626)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val Divider = Color(0xFFE5E7EB)

@Composable
fun CashModuleScreen(
    session: CashSession,
    viewModel: CashModuleViewModel,
    onCashClosed: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(session.id) {
        viewModel.load(session)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFFFFFF)),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 90.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Header turno activo ──────────────────────────────────────────
            item {
                CashHeaderCard(
                    session = session,
                    onRequestClose = { viewModel.requestClose() },
                    onRequestMovement = { viewModel.requestMovement() },
                )
            }

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

            val filtered = state.flowItems.filter { item ->
                state.flowSearch.isBlank() ||
                        item.comentario.contains(state.flowSearch, ignoreCase = true) ||
                        item.razonSocial.contains(state.flowSearch, ignoreCase = true) ||
                        item.cajeroNombre.contains(state.flowSearch, ignoreCase = true) ||
                        item.sucursal.contains(state.flowSearch, ignoreCase = true) ||
                        item.tipoPago.contains(state.flowSearch, ignoreCase = true) ||
                        item.origen.contains(state.flowSearch, ignoreCase = true)
            }

            if (!state.flowLoading && state.flowItems.isEmpty() && state.flowError == null) {
                item {
                    Box(Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                        Text("Sin movimientos en este período", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
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
                    Text(err, style = MaterialTheme.typography.bodyMedium, color = Brand, modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            if (!state.flowLoading && filtered.isNotEmpty()) {
                item { CashFlowTable(items = filtered) }
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
            onConfirm = { counted, obs -> viewModel.closeSession(counted, obs, onCashClosed) },
        )
    }

    // ── Dialog movimiento de caja ────────────────────────────────────────────
    if (state.showMovementDialog) {
        CashMovementDialog(
            loading = state.movementLoading,
            error = state.movementError,
            onDismiss = viewModel::dismissMovement,
            onConfirm = { tipo, monto, motivo, obs -> viewModel.registerMovement(tipo, monto, motivo, obs) },
        )
    }
}

// ── Header Card ──────────────────────────────────────────────────────────────

@Composable
private fun CashHeaderCard(
    session: CashSession,
    onRequestClose: () -> Unit,
    onRequestMovement: () -> Unit,
) {
    // Mismo umbral que la tabla de flujo y el resto de módulos (Proveedores, etc.)
    val compact = LocalConfiguration.current.screenWidthDp < 760

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Turno activo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        session.cashRegisterName.ifBlank { "Caja" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                    )
                    val openedStr = if (session.openedAt > 0L)
                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(session.openedAt))
                    else "—"
                    Text("Apertura: $openedStr", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }

                if (!compact) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        MovementButton(onClick = onRequestMovement)
                        CloseButton(onClick = onRequestClose)
                    }
                }
            }

            if (compact) {
                Spacer(Modifier.height(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    MovementButton(onClick = onRequestMovement, fullWidth = true)
                    CloseButton(onClick = onRequestClose, fullWidth = true)
                }
            }
        }
    }
}

@Composable
private fun MovementButton(onClick: () -> Unit, fullWidth: Boolean = false) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Brand),
        border = BorderStroke(1.dp, Brand),
        modifier = if (fullWidth) Modifier.fillMaxWidth() else Modifier,
    ) {
        Icon(Icons.Filled.SwapVert, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text("Movimiento", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun CloseButton(onClick: () -> Unit, fullWidth: Boolean = false) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Brand),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        modifier = if (fullWidth) Modifier.fillMaxWidth() else Modifier,
    ) {
        Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text("Cerrar caja", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    }
}

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
    val compact = LocalConfiguration.current.screenWidthDp < 760
    val hasDateFilter = state.filterFrom.isNotBlank() || state.filterTo.isNotBlank()

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
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                val total = state.flowItems.size
                if (total > 0) {
                    Text(
                        "$total ${if (total == 1) "registro" else "registros"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                IconButton(onClick = onRefresh, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Recargar", tint = TextSecondary, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(Modifier.height(12.dp))

            if (compact) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CashFlowDateField(
                        value = state.filterFrom,
                        onChange = onFromChange,
                        label = "Desde",
                        modifier = Modifier.weight(1f),
                    )
                    CashFlowDateField(
                        value = state.filterTo,
                        onChange = onToChange,
                        label = "Hasta",
                        modifier = Modifier.weight(1f),
                    )
                    FilterApplyButton(onClick = onApply)
                    if (hasDateFilter) FilterClearButton(onClick = onClear)
                }

                Spacer(Modifier.height(10.dp))

                CashFlowSearchField(
                    value = state.flowSearch,
                    onChange = onSearchChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CashFlowSearchField(
                        value = state.flowSearch,
                        onChange = onSearchChange,
                        modifier = Modifier.weight(1.6f),
                    )
                    CashFlowDateField(
                        value = state.filterFrom,
                        onChange = onFromChange,
                        label = "Desde",
                        modifier = Modifier.weight(0.8f),
                    )
                    CashFlowDateField(
                        value = state.filterTo,
                        onChange = onToChange,
                        label = "Hasta",
                        modifier = Modifier.weight(0.8f),
                    )
                    FilterApplyButton(onClick = onApply)
                    if (hasDateFilter) FilterClearButton(onClick = onClear)
                }
            }
        }
    }
}

@Composable
private fun CashFlowDateField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        placeholder = { Text("yyyy-MM-dd", style = MaterialTheme.typography.bodySmall) },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        textStyle = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun CashFlowSearchField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text("Buscar en flujo...", style = MaterialTheme.typography.bodySmall) },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        textStyle = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun FilterApplyButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .background(Brand, RoundedCornerShape(8.dp)),
    ) {
        Icon(Icons.Filled.FilterAlt, contentDescription = "Aplicar filtro", tint = Color.White, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun FilterClearButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(44.dp)) {
        Icon(Icons.Filled.FilterAltOff, contentDescription = "Limpiar filtro", tint = TextSecondary, modifier = Modifier.size(18.dp))
    }
}

// ── Flow Table ────────────────────────────────────────────────────────────────

@Composable
private fun CashFlowTable(items: List<CashFlowItem>) {
    val compact = LocalConfiguration.current.screenWidthDp < 760
    var pageSize by remember { mutableStateOf(10) }
    var pageSizeExpanded by remember { mutableStateOf(false) }
    val totalPages = maxOf(1, (items.size + pageSize - 1) / pageSize)
    var currentPage by remember(items.size, pageSize) { mutableStateOf(0) }
    val pageItems = items.drop(currentPage * pageSize).take(pageSize)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = TextPrimary,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ── Cabecera ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                TableHeaderCell("Fecha", 1.3f)
                TableHeaderCell("Razón social", if (compact) 2f else 1.8f)
                if (!compact) {
                    TableHeaderCell("Sucursal", 1.1f)
                    TableHeaderCell("Caja", 1f)
                    TableHeaderCell("Empleado", 1.3f)
                }
                TableHeaderCell("Movimiento", 1.2f)
                if (!compact) {
                    TableHeaderCell("Transacción", 1.3f)
                    TableHeaderCell("Origen", 1.1f)
                    TableHeaderCell("Pago", 1f)
                }
                TableHeaderCell("Importe", 1.1f, Alignment.End)
            }
            HorizontalDivider(color = Divider, thickness = 1.dp)

            // ── Filas ─────────────────────────────────────────────────────────
            pageItems.forEachIndexed { index, item ->
                CashFlowTableRow(item = item, compact = compact)
                if (index < pageItems.lastIndex) {
                    HorizontalDivider(color = Divider, thickness = 0.5.dp)
                }
            }

            // ── Paginación (idéntica a Proveedores: sin divisor extra antes) ───
            val paginationInfo: @Composable () -> Unit = {
                val from = if (items.isEmpty()) 0 else currentPage * pageSize + 1
                val to = minOf(items.size, (currentPage + 1) * pageSize)
                Text(if (compact) "Filas:" else "Registros por pagina:", color = TextSecondary)
                Box {
                    TextButton(onClick = { pageSizeExpanded = true }) { Text(pageSize.toString(), color = TextPrimary) }
                    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White, surfaceTint = Color.Transparent)) {
                        DropdownMenu(expanded = pageSizeExpanded, onDismissRequest = { pageSizeExpanded = false }) {
                            listOf(10, 20, 50).forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(size.toString()) },
                                    onClick = { pageSize = size; pageSizeExpanded = false },
                                )
                            }
                        }
                    }
                }
                Text("$from-$to de ${items.size}", color = TextSecondary)
            }
            val paginationButtons: @Composable () -> Unit = {
                IconButton(onClick = { currentPage-- }, enabled = currentPage > 0) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Pagina anterior")
                }
                IconButton(onClick = { currentPage++ }, enabled = currentPage < totalPages - 1) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Pagina siguiente")
                }
            }
            if (compact) {
                Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) { paginationInfo() }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                        Text("Pagina ${currentPage + 1} de $totalPages", color = TextSecondary)
                        paginationButtons()
                    }
                }
            } else {
                Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    paginationInfo()
                    Spacer(Modifier.weight(1f))
                    Text("Pagina ${currentPage + 1} de $totalPages", color = TextSecondary)
                    paginationButtons()
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
    Box(
        modifier = Modifier.weight(weight),
        contentAlignment = when (align) {
            Alignment.End -> Alignment.CenterEnd
            Alignment.CenterHorizontally -> Alignment.Center
            else -> Alignment.CenterStart
        },
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CashFlowTableRow(item: CashFlowItem, compact: Boolean) {
    val isIngreso = item.tipoMovimiento.equals("Ingreso", ignoreCase = true)
    val importeColor = if (isIngreso) GreenIncome else RedExpense

    val dateStr = if (item.fecha > 0L)
        SimpleDateFormat(if (compact) "dd/MM/yy" else "dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(item.fecha))
    else "—"

    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TableCell(text = dateStr, weight = 1.3f)

        Box(modifier = Modifier.weight(if (compact) 2f else 1.8f).padding(horizontal = 4.dp)) {
            Column {
                Text(
                    item.razonSocial.ifBlank { "-" },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = TextPrimary,
                )
                if (compact) {
                    Text(
                        item.cajeroNombre.ifBlank { "-" },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        if (!compact) {
            TableCell(text = item.sucursal, weight = 1.1f)
            TableCell(text = item.cajaNombre, weight = 1f)
            TableCell(text = item.cajeroNombre, weight = 1.3f)
        }

        TableCell(text = item.tipoMovimiento, weight = 1.2f, color = importeColor, bold = true)

        if (!compact) {
            TableCell(text = item.tipoTransaccion, weight = 1.3f)
            TableCell(text = item.origen, weight = 1.1f)
            TableCell(text = item.tipoPago, weight = 1f)
        }

        Box(modifier = Modifier.weight(1.1f).padding(horizontal = 4.dp), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "S/ %.2f".format(item.importe),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = importeColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    weight: Float,
    color: Color = TextPrimary,
    bold: Boolean = false,
) {
    Box(modifier = Modifier.weight(weight).padding(horizontal = 4.dp), contentAlignment = Alignment.CenterStart) {
        Text(
            text = text.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = color,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
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
                Box(Modifier.size(46.dp).background(Color.White, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = Brand)
                }
                Column {
                    Text("Cerrar caja", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Confirma el efectivo de tu turno", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(color = Color.White, shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SummaryDialogRow("Ventas", summary?.totalSales ?: 0.0)
                        SummaryDialogRow("Efectivo esperado", summary?.expectedCash ?: 0.0, bold = true)
                    }
                }
                OutlinedTextField(
                    value = counted,
                    onValueChange = { counted = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Efectivo contado", style = MaterialTheme.typography.bodyMedium) },
                    prefix = { Text("S/ ", style = MaterialTheme.typography.bodyMedium) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = movementFieldColors(),
                )
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
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                OutlinedTextField(
                    value = observations,
                    onValueChange = { observations = it },
                    label = { Text("Observaciones (opcional)", style = MaterialTheme.typography.bodyMedium) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 2,
                    colors = movementFieldColors(),
                )
                error?.let { Text(it, color = Brand, style = MaterialTheme.typography.bodyMedium) }
                if (loading) LinearProgressIndicator(color = Brand, modifier = Modifier.fillMaxWidth())
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, enabled = !loading) { Text("Cancelar", style = MaterialTheme.typography.labelLarge) }
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
            ) { Text("Cerrar caja", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold) }
        },
    )
}

// ── Movement Dialog ───────────────────────────────────────────────────────────

@Composable
private fun CashMovementDialog(
    loading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: (tipo: String, monto: Double, motivo: String, observaciones: String) -> Unit,
) {
    var tipo by remember { mutableStateOf("Ingreso") }
    var monto by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var montoError by remember { mutableStateOf(false) }
    var motivoError by remember { mutableStateOf(false) }

    val trySubmit: () -> Unit = {
        val montoValue = monto.toDoubleOrNull() ?: 0.0
        montoError = montoValue <= 0.0
        motivoError = motivo.trim().isBlank()
        if (!montoError && !motivoError) {
            onConfirm(tipo, montoValue, motivo.trim(), observaciones.trim())
        }
    }

    AlertDialog(
        onDismissRequest = { if (!loading) onDismiss() },
        containerColor = Color.White,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(22.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(46.dp).background(Color.White, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.SwapVert, contentDescription = null, tint = Brand)
                }
                Text("Movimiento de caja", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TipoMovimientoDropdown(value = tipo, onChange = { tipo = it })
                OutlinedTextField(
                    value = monto,
                    onValueChange = { input -> monto = input.filter { it.isDigit() || it == '.' }; montoError = false },
                    label = { Text("Monto *", style = MaterialTheme.typography.bodyMedium) },
                    prefix = { Text("S/ ", style = MaterialTheme.typography.bodyMedium) },
                    isError = montoError,
                    supportingText = { if (montoError) Text("Ingrese un monto válido", color = MaterialTheme.colorScheme.error) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = movementFieldColors(),
                )
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it; motivoError = false },
                    label = { Text("Motivo *", style = MaterialTheme.typography.bodyMedium) },
                    isError = motivoError,
                    supportingText = { if (motivoError) Text("El motivo es obligatorio", color = MaterialTheme.colorScheme.error) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 2,
                    colors = movementFieldColors(),
                )
                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones", style = MaterialTheme.typography.bodyMedium) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 2,
                    colors = movementFieldColors(),
                )
                error?.let { Text(it, color = Brand, style = MaterialTheme.typography.bodyMedium) }
                if (loading) LinearProgressIndicator(color = Brand, modifier = Modifier.fillMaxWidth())
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, enabled = !loading) { Text("Cancelar", style = MaterialTheme.typography.labelLarge) }
        },
        confirmButton = {
            Button(
                onClick = trySubmit,
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(containerColor = Brand),
                shape = RoundedCornerShape(10.dp),
            ) { Text("Guardar", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold) }
        },
    )
}

@Composable
private fun movementFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledContainerColor = Color.White,
    focusedBorderColor = Brand,
    focusedLabelColor = Brand,
    cursorColor = Brand,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipoMovimientoDropdown(value: String, onChange: (String) -> Unit) {
    val opciones = listOf("Ingreso", "Salida")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo", style = MaterialTheme.typography.bodyMedium) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            singleLine = true,
            colors = movementFieldColors(),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opciones.forEach { opcion ->
                DropdownMenuItem(text = { Text(opcion) }, onClick = { onChange(opcion); expanded = false })
            }
        }
    }
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
        Text(label, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
        Text(
            "S/ %.2f".format(Locale.US, value),
            color = color,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}