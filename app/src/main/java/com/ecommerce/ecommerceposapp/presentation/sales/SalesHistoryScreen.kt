package com.ecommerce.ecommerceposapp.presentation.sales

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.presentation.pos.VistaPreviaReciboDialog
import com.ecommerce.ecommerceposapp.ui.theme.AppBackground
import com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.ecommerce.ecommerceposapp.ui.theme.BrandRedDark
import com.ecommerce.ecommerceposapp.ui.theme.BrandRedLight
import com.ecommerce.ecommerceposapp.ui.theme.BrandYellow
import com.ecommerce.ecommerceposapp.ui.theme.BrandYellowLight
import com.ecommerce.ecommerceposapp.ui.theme.GrayLight
import com.ecommerce.ecommerceposapp.ui.theme.GrayMedium
import com.ecommerce.ecommerceposapp.ui.theme.GreenSuccess
import com.ecommerce.ecommerceposapp.ui.theme.GreenSuccessLight
import com.ecommerce.ecommerceposapp.ui.theme.PosEmptyState
import com.ecommerce.ecommerceposapp.ui.theme.PosLinearLoader
import com.ecommerce.ecommerceposapp.ui.theme.Radius
import com.ecommerce.ecommerceposapp.ui.theme.RedDanger
import com.ecommerce.ecommerceposapp.ui.theme.RedDangerLight
import com.ecommerce.ecommerceposapp.ui.theme.Spacing
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceMuted
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite
import com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
import com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
import com.ecommerce.ecommerceposapp.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ─────────────────────────────────────────────────────────────────────────────
//  HELPERS
// ─────────────────────────────────────────────────────────────────────────────
private fun formatVentaFecha(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.US).format(Date(millis))

private fun formatVentaFechaShort(millis: Long): String =
    SimpleDateFormat("dd/MM  HH:mm", Locale.US).format(Date(millis))

private fun mapPago(code: String): String = when (code) {
    "EFE" -> "Efectivo"
    "TAR" -> "Tarjeta"
    "YAP" -> "Yape"
    "PLN" -> "Plin"
    else  -> code
}

private fun mapTipoForReissue(tipoComprobante: String): TipoComprobanteEmision = when (tipoComprobante.uppercase()) {
    "01", "FACTURA" -> TipoComprobanteEmision.FACTURA
    "03", "BOLETA"  -> TipoComprobanteEmision.BOLETA
    else            -> TipoComprobanteEmision.SOLO_TICKET
}

// ─────────────────────────────────────────────────────────────────────────────
//  SALE HISTORY CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SaleHistoryCard(
    row: SalesHistoryRow,
    onReprint: () -> Unit,
    onCancel: () -> Unit,
) {
    val isAnulada  = row.estado.equals("Anulada", ignoreCase = true)
    val pagoLabel  = mapPago(row.tipoPago)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(Radius.md), ambientColor = Color(0x0C000000), spotColor = Color(0x0C000000))
            .clip(RoundedCornerShape(Radius.md))
            .background(SurfaceWhite),
    ) {
        // Acento lateral de color
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            // ── Fila 1: comprobante + estado + importe ─────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        row.numeroComprobante.ifBlank { "Sin comprobante" },
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        formatVentaFecha(row.fechaMillis),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                    )
                }
                Spacer(Modifier.width(Spacing.sm))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "S/ ${"%.2f".format(row.total)}",
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color      = if (isAnulada) GrayMedium else BrandRed,
                    )
                    Spacer(Modifier.height(4.dp))
                    // Badge de estado
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Radius.pill))
                            .background(if (isAnulada) RedDangerLight else GreenSuccessLight)
                            .padding(horizontal = Spacing.sm, vertical = 2.dp),
                    ) {
                        Text(
                            if (isAnulada) "Anulada" else "Completada",
                            style      = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color      = if (isAnulada) RedDanger else GreenSuccess,
                        )
                    }
                }
                Spacer(Modifier.width(Spacing.md))
                if (!isAnulada) {
                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(Icons.Filled.Cancel, contentDescription = "Anular venta", tint = RedDanger, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(Spacing.sm))
                }
                IconButton(
                    onClick = onReprint,
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(Icons.Filled.Print, contentDescription = "Reimprimir", tint = BrandRed, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(Spacing.sm))

            // ── Fila 2: cliente + cajero ───────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.lg),
            ) {
                InfoChip(label = "Cliente", value = row.clienteNombre.ifBlank { "General" }, modifier = Modifier.weight(1f))
                InfoChip(label = "Cajero",  value = row.cajeroNombre.ifBlank { "—" },        modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(Spacing.sm))

            // ── Fila 3: pago + acciones ────────────────────────────────────
            if (pagoLabel.isNotBlank()) Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Pill de método de pago
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(SurfaceWhite)
                        .padding(horizontal = Spacing.sm, vertical = 2.dp),
                ) {
                    Text(
                        pagoLabel,
                        style      = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextSecondary,
                    )
                }
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextTertiary, fontWeight = FontWeight.Medium)
        Text(value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SaleStatusBadge(isAnulada: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.pill))
            .background(if (isAnulada) RedDangerLight else GreenSuccessLight)
            .padding(horizontal = 9.dp, vertical = 4.dp),
    ) {
        Text(
            text = if (isAnulada) "Anulada" else "Completada",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isAnulada) RedDanger else GreenSuccess,
        )
    }
}

@Composable
private fun SaleActions(
    isAnulada: Boolean,
    onReprint: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onReprint, modifier = Modifier.size(34.dp)) {
            Icon(Icons.Filled.Print, contentDescription = "Reimprimir", tint = BrandRed, modifier = Modifier.size(17.dp))
        }
        if (!isAnulada) {
            IconButton(onClick = onCancel, modifier = Modifier.size(34.dp)) {
                Icon(Icons.Filled.Cancel, contentDescription = "Anular venta", tint = RedDanger, modifier = Modifier.size(17.dp))
            }
        }
    }
}

@Composable
private fun SalesTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceMuted)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TableLabel("ID COMPROBANTE", 1.65f)
        TableLabel("FECHA / HORA", 1.05f)
        TableLabel("CLIENTE", 1.15f)
        TableLabel("CAJERO", 1f)
        TableLabel("MONTO", .75f)
        TableLabel("ESTADO", .9f)
        TableLabel("ACCIONES", .72f)
    }
}

@Composable
private fun RowScope.TableLabel(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = TextSecondary,
        maxLines = 1,
    )
}

@Composable
private fun SaleHistoryTableRow(
    row: SalesHistoryRow,
    onReprint: () -> Unit,
    onCancel: () -> Unit,
) {
    val isAnulada = row.estado.equals("Anulada", ignoreCase = true)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1.65f)) {
            Text(row.numeroComprobante.ifBlank { "Sin comprobante" }, fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(mapPago(row.tipoPago), color = TextTertiary, style = MaterialTheme.typography.labelSmall)
        }
        Text(formatVentaFecha(row.fechaMillis), Modifier.weight(1.05f), color = TextSecondary, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        Text(row.clienteNombre.ifBlank { "General" }, Modifier.weight(1.15f), color = TextPrimary, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(row.cajeroNombre.ifBlank { "—" }, Modifier.weight(1f), color = TextPrimary, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text("S/ ${"%.2f".format(row.total)}", Modifier.weight(.75f), color = if (isAnulada) GrayMedium else BrandRed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
        Box(Modifier.weight(.9f)) { SaleStatusBadge(isAnulada) }
        Box(Modifier.weight(.72f)) { SaleActions(isAnulada, onReprint, onCancel) }
    }
}

@Composable
private fun SaleHistoryCompactRow(
    row: SalesHistoryRow,
    onReprint: () -> Unit,
    onCancel: () -> Unit,
) {
    val isAnulada = row.estado.equals("Anulada", ignoreCase = true)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.md))
            .background(SurfaceWhite)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1.45f)) {
            Text(row.numeroComprobante.ifBlank { "Sin comprobante" }, fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(formatVentaFechaShort(row.fechaMillis), color = TextTertiary, style = MaterialTheme.typography.labelSmall)
            Text(row.clienteNombre.ifBlank { "General" }, color = TextSecondary, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(Modifier.weight(.8f), horizontalAlignment = Alignment.End) {
            Text("S/ ${"%.2f".format(row.total)}", color = if (isAnulada) GrayMedium else BrandRed, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            SaleStatusBadge(isAnulada)
        }
        Spacer(Modifier.width(4.dp))
        SaleActions(isAnulada, onReprint, onCancel)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CANCEL DIALOG
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CancelSaleDialog(
    row: SalesHistoryRow,
    cancelling: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (comment: String, restoreStock: Boolean) -> Unit,
) {
    var cancelComment by remember { mutableStateOf("") }
    var restoreStock  by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = { if (!cancelling) onDismiss() },
        containerColor   = SurfaceWhite,
        tonalElevation   = 0.dp,
        shape            = RoundedCornerShape(Radius.xl),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Box(
                    modifier = Modifier.size(42.dp).clip(RoundedCornerShape(Radius.md)).background(RedDangerLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.Cancel, contentDescription = null, tint = RedDanger, modifier = Modifier.size(22.dp))
                }
                Column {
                    Text("Anular venta", fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(row.numeroComprobante, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                Text(
                    "Indica el motivo. Esta acción queda registrada en el sistema.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
                OutlinedTextField(
                    value         = cancelComment,
                    onValueChange = { cancelComment = it.take(1000) },
                    label         = { Text("Motivo de anulación") },
                    minLines      = 3,
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(Radius.md),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = BrandRed,
                        unfocusedBorderColor = BorderDefault,
                        cursorColor          = BrandRed,
                        focusedLabelColor    = BrandRed,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        disabledContainerColor = SurfaceWhite,
                    ),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.clip(RoundedCornerShape(Radius.md)).clickable { restoreStock = !restoreStock }.padding(Spacing.xs),
                ) {
                    Checkbox(
                        checked        = restoreStock,
                        onCheckedChange = { restoreStock = it },
                        colors         = CheckboxDefaults.colors(checkedColor = BrandRed, uncheckedColor = BrandRed),
                    )
                    Spacer(Modifier.width(Spacing.xs))
                    Text("Devolver productos al stock", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                }
            }
        },
        confirmButton = {
            Button(
                enabled  = !cancelling && cancelComment.trim().length >= 5,
                onClick  = { onConfirm(cancelComment, restoreStock) },
                colors   = ButtonDefaults.buttonColors(
                    containerColor = RedDanger,
                    disabledContainerColor = BrandRedLight,
                    disabledContentColor = RedDanger,
                ),
                shape    = RoundedCornerShape(Radius.md),
            ) {
                Text(if (cancelling) "Anulando..." else "Anular venta", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(enabled = !cancelling, onClick = onDismiss) {
                Text("Cancelar", color = BrandRed)
            }
        },
    )
}

// ─────────────────────────────────────────────────────────────────────────────
//  SALES HISTORY SCREEN
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SalesHistoryScreen(
    catalog: CatalogRepository,
    clients: List<ClientRow>,
) {
    var rows          by remember { mutableStateOf<List<SalesHistoryRow>>(emptyList()) }
    var loading       by remember { mutableStateOf(false) }
    var search        by remember { mutableStateOf("") }
    var error         by remember { mutableStateOf<String?>(null) }
    var previewReceipt by remember { mutableStateOf<CompletedSaleReceipt?>(null) }
    var previewComp   by remember { mutableStateOf<ComprobanteEmitidoResult?>(null) }
    var saleToCancel  by remember { mutableStateOf<SalesHistoryRow?>(null) }
    var cancelling    by remember { mutableStateOf(false) }
    var pageSize      by remember { mutableStateOf(10) }
    var pageSizeExpanded by remember { mutableStateOf(false) }
    var currentPage   by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    fun reprint(row: SalesHistoryRow) {
        scope.launch {
            loading = true; error = null
            val rec = withContext(Dispatchers.IO) { catalog.getSaleReceipt(row.ventaId) }
            if (rec.isFailure) {
                loading = false
                error = rec.exceptionOrNull()?.message ?: "Venta no encontrada."
                return@launch
            }
            val receipt = rec.getOrThrow()
            val tipo = mapTipoForReissue(row.tipoComprobante)
            val em = withContext(Dispatchers.IO) {
                catalog.emitComprobanteForVenta(
                    row.ventaId,
                    tipo,
                    row.idCliente.takeIf { it > 0L } ?: receipt.idCliente,
                    ReceiptCustomerInfo(
                        id = row.idCliente.takeIf { it > 0L } ?: receipt.idCliente,
                        name = receipt.clienteNombre,
                        document = receipt.clienteDocumento,
                    ),
                )
            }
            loading = false
            if (em.isSuccess) {
                previewReceipt = receipt
                previewComp = em.getOrNull()
            } else {
                error = em.exceptionOrNull()?.message ?: "No se pudo reemitir."
            }
        }
    }

    fun reload() {
        scope.launch {
            loading = true; error = null
            runCatching { withContext(Dispatchers.IO) { catalog.listSalesHistory() } }
                .onSuccess { rows = it }
                .onFailure { error = it.message ?: "No se pudo cargar el historial." }
            loading = false
        }
    }

    LaunchedEffect(Unit) { reload() }

    val filtered = rows.filter {
        search.isBlank() ||
            it.numeroComprobante.contains(search, ignoreCase = true) ||
            it.clienteNombre.contains(search, ignoreCase = true) ||
            it.cajeroNombre.contains(search, ignoreCase = true)
    }
    val totalPages = maxOf(1, (filtered.size + pageSize - 1) / pageSize)
    val pageRows = filtered.drop(currentPage * pageSize).take(pageSize)

    LaunchedEffect(search, rows.size, pageSize) { currentPage = 0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
    ) {
        // ── Header ───────────────────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(Radius.md))
                    .background(BrandRedLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = BrandRed, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(Spacing.md))
            Column {
                Text(
                    "Historial de Ventas",
                    style      = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                )
                Text(
                    "Consulta y vuelve a imprimir comprobantes",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }
        }

        Spacer(Modifier.height(Spacing.lg))

        // ── Barra de búsqueda ────────────────────────────────────────────────
        OutlinedTextField(
            value         = search,
            onValueChange = { search = it },
            placeholder   = { Text("Buscar por comprobante, cliente o cajero…", color = TextTertiary) },
            leadingIcon   = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(Radius.lg),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = BrandRed,
                unfocusedBorderColor    = BorderDefault,
                focusedContainerColor   = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,
                cursorColor             = BrandRed,
            ),
        )

        Spacer(Modifier.height(Spacing.sm))

        // ── Loader / error ───────────────────────────────────────────────────
        if (loading) {
            PosLinearLoader(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(Spacing.sm))
        }
        error?.let { err ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Radius.md))
                    .background(RedDangerLight)
                    .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            ) {
                Text(err, color = RedDanger, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(Spacing.sm))
        }

        // ── Contador de resultados ───────────────────────────────────────────
        if (!loading && filtered.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${filtered.size} ${if (filtered.size == 1) "venta" else "ventas"}",
                    style      = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextSecondary,
                )
                if (search.isNotBlank()) {
                    Spacer(Modifier.width(Spacing.sm))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Radius.pill))
                            .background(BrandRedLight)
                            .padding(horizontal = Spacing.sm, vertical = 2.dp),
                    ) {
                        Text(
                            "Filtrado: \"$search\"",
                            style  = MaterialTheme.typography.labelSmall,
                            color  = BrandRed,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
            Spacer(Modifier.height(Spacing.sm))
        }

        // ── Lista ────────────────────────────────────────────────────────────
        if (!loading && filtered.isEmpty() && error == null) {
            Box(
                modifier         = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                PosEmptyState(
                    icon        = Icons.Filled.Receipt,
                    title       = if (search.isNotBlank()) "Sin resultados" else "Sin ventas registradas",
                    description = if (search.isNotBlank())
                        "No encontramos ventas que coincidan con \"$search\"."
                    else
                        "Aún no hay ventas registradas en este POS.",
                )
            }
        } else {
            BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
                val compact = maxWidth < 720.dp
                LazyColumn(
                    modifier = Modifier.fillMaxSize().then(
                        if (compact) Modifier else Modifier
                            .clip(RoundedCornerShape(Radius.md))
                            .background(SurfaceWhite)
                    ),
                    verticalArrangement = if (compact) Arrangement.spacedBy(Spacing.sm) else Arrangement.Top,
                    contentPadding = if (compact) PaddingValues(vertical = Spacing.xs) else PaddingValues(0.dp),
                ) {
                    if (!compact) {
                        item(key = "sales-table-header") { SalesTableHeader() }
                    }
                    items(pageRows, key = { it.ventaId }) { row ->
                        if (compact) {
                            SaleHistoryCompactRow(
                                row = row,
                                onReprint = { reprint(row) },
                                onCancel = { saleToCancel = row },
                            )
                        } else {
                            SaleHistoryTableRow(
                                row = row,
                                onReprint = { reprint(row) },
                                onCancel = { saleToCancel = row },
                            )
                            Box(Modifier.fillMaxWidth().height(1.dp).background(BorderDefault))
                        }
                    }
                }
            }
        }
        if (!loading && filtered.isNotEmpty()) {
            val from = currentPage * pageSize + 1
            val to = minOf(filtered.size, (currentPage + 1) * pageSize)
            Row(
                modifier = Modifier.fillMaxWidth().background(SurfaceWhite).padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Registros por pagina:", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Box {
                    TextButton(onClick = { pageSizeExpanded = true }) {
                        Text(pageSize.toString(), color = TextPrimary)
                    }
                    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = SurfaceWhite, surfaceTint = Color.Transparent)) {
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
                Text("$from-$to de ${filtered.size}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.weight(1f))
                Text("Pagina ${currentPage + 1} de $totalPages", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                IconButton(onClick = { currentPage-- }, enabled = currentPage > 0) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Pagina anterior")
                }
                IconButton(onClick = { currentPage++ }, enabled = currentPage < totalPages - 1) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Pagina siguiente")
                }
            }
        }
    }

    // ── Preview dialog ───────────────────────────────────────────────────────
    if (previewReceipt != null && previewComp != null) {
        val receipt = previewReceipt!!
        val comp    = previewComp!!
        val cdisp   = if (receipt.clienteNombre.isNotBlank() || receipt.clienteDocumento.isNotBlank()) {
            receipt.clienteNombre to receipt.clienteDocumento
        } else if (receipt.idCliente > 0L) {
            catalog.getClienteDisplay(receipt.idCliente)
        } else null
        VistaPreviaReciboDialog(
            receipt       = receipt,
            emitido       = comp,
            clienteNombre = cdisp?.first,
            clienteDoc    = cdisp?.second,
            whatsappPhone = if (receipt.idCliente > 0L) catalog.getClienteTelefono(receipt.idCliente).orEmpty() else "",
            onDismiss     = { previewReceipt = null; previewComp = null },
        )
    }

    // ── Cancel dialog ────────────────────────────────────────────────────────
    saleToCancel?.let { row ->
        CancelSaleDialog(
            row        = row,
            cancelling = cancelling,
            onDismiss  = { if (!cancelling) saleToCancel = null },
            onConfirm  = { comment, restore ->
                scope.launch {
                    cancelling = true; error = null
                    withContext(Dispatchers.IO) { catalog.cancelSale(row.ventaId, comment, restore) }
                        .onSuccess { saleToCancel = null; reload() }
                        .onFailure { error = it.message ?: "No se pudo anular la venta." }
                    cancelling = false
                }
            },
        )
    }
}
