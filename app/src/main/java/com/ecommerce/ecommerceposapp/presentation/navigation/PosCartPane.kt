package com.ecommerce.ecommerceposapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.state.ToggleableState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductConversion
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.presentation.pos.PosUiState
import com.ecommerce.ecommerceposapp.presentation.pos.VistaPreviaReciboDialog
import com.ecommerce.ecommerceposapp.ui.theme.AppBackground
import com.ecommerce.ecommerceposapp.ui.theme.BorderDefault
import com.ecommerce.ecommerceposapp.ui.theme.BrandRed
import com.ecommerce.ecommerceposapp.ui.theme.BrandRedDark
import com.ecommerce.ecommerceposapp.ui.theme.BrandYellow
import com.ecommerce.ecommerceposapp.ui.theme.GrayLight
import com.ecommerce.ecommerceposapp.ui.theme.GrayMedium
import com.ecommerce.ecommerceposapp.ui.theme.GreenSuccess
import com.ecommerce.ecommerceposapp.ui.theme.Radius
import com.ecommerce.ecommerceposapp.ui.theme.Spacing
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceMuted
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceSubtle
import com.ecommerce.ecommerceposapp.ui.theme.SurfaceWhite
import com.ecommerce.ecommerceposapp.ui.theme.TextPrimary
import com.ecommerce.ecommerceposapp.ui.theme.TextSecondary
import com.ecommerce.ecommerceposapp.ui.theme.TextTertiary
import java.util.Locale
import kotlin.math.round
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private enum class PosPaymentMethod { Efectivo, Tarjeta, Yape, Plin }

/** Formatea un porcentaje sin decimales innecesarios (20 → "20", 12.5 → "12.50"). */
private fun formatPct(pct: Double): String =
    if (pct == pct.toInt().toDouble()) pct.toInt().toString() else "%.2f".format(Locale.US, pct)

private fun appendMontoRecibido(current: String, key: String): String {
    if (key == "⌫") return if (current.isEmpty()) "" else current.dropLast(1)
    if (key == ".") {
        if (current.contains('.')) return current
        return if (current.isEmpty()) "0." else current + "."
    }
    if (!key.all { it.isDigit() }) return current
    val next = current + key
    val parts = next.split('.')
    if (parts.size > 1 && parts[1].length > 2) return current
    return next
}

private fun mapPosPaymentMethodToTipoPago(m: PosPaymentMethod): String = when (m) {
    PosPaymentMethod.Efectivo -> "EFE"
    PosPaymentMethod.Tarjeta  -> "TAR"
    PosPaymentMethod.Yape     -> "YAP"
    PosPaymentMethod.Plin     -> "PLN"
}

// ─────────────────────────────────────────────────────────────────────────────
//  EMPTY STATE DEL CARRITO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CartEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.xl, vertical = Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(Radius.xxl))
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.ShoppingBasket,
                contentDescription = null,
                tint     = Color(0xFF6F7A84),
                modifier = Modifier.size(36.dp),
            )
        }
        Spacer(Modifier.height(Spacing.md))
        Text(
            "Aqu\u00ed ver\u00e1s los productos que elijas\nen tu pr\u00f3xima venta",
            color      = Color(0xFF516E96),
            fontSize   = 22.sp,
            lineHeight  = 32.sp,
            fontWeight = FontWeight.Normal,
            textAlign  = TextAlign.Center,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CART ITEM ROW  — fondo blanco, sin grises
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CartItemRow(
    line: CartLine,
    conversions: List<ProductConversion>,
    onSelectConversion: (ProductConversion?) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    var conversionMenuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(Radius.md), ambientColor = Color(0x0A000000))
            .clip(RoundedCornerShape(Radius.md))
            .background(SurfaceWhite)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (line.conversionName.isBlank()) line.productName else "${line.productName} · ${line.conversionName}",
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary,
                style      = MaterialTheme.typography.bodyMedium,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (conversions.isNotEmpty()) Box {
                IconButton(onClick = { conversionMenuExpanded = true }, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Seleccionar conversión", tint = BrandRed)
                }
                DropdownMenu(
                    expanded = conversionMenuExpanded,
                    onDismissRequest = { conversionMenuExpanded = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 360.dp).heightIn(max = 360.dp),
                ) {
                    DropdownMenuItem(text = { Text("Unidad · precio normal") }, onClick = {
                        onSelectConversion(null); conversionMenuExpanded = false
                    })
                    conversions.forEach { conversion ->
                        DropdownMenuItem(text = {
                            Text(
                                "${conversion.name} · S/ ${"%.2f".format(conversion.finalPrice)}",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }, onClick = { onSelectConversion(conversion); conversionMenuExpanded = false })
                    }
                }
            }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                "S/ ${"%.2f".format(line.unitPrice)} · Total S/ ${"%.2f".format(line.unitPrice * line.quantity)}",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(Modifier.width(Spacing.sm))
        // Controles de cantidad
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            IconButton(
                onClick  = onDecrease,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    Icons.Filled.Remove,
                    contentDescription = "Menos",
                    tint     = BrandRed,
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(
                "${line.quantity}",
                modifier   = Modifier.widthIn(min = 28.dp),
                color      = TextPrimary,
                fontWeight = FontWeight.Bold,
                style      = MaterialTheme.typography.bodyMedium,
                textAlign  = TextAlign.Center,
            )
            IconButton(
                onClick  = onIncrease,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Más",
                    tint     = BrandRed,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  RESUMEN TOTALES
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CartTotalSection(
    subtotal: Double,
    igv: Double,
    descuentoPorcentaje: Double,
    descuentoMonto: Double,
    total: Double,
) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(Radius.lg),
        color           = SurfaceWhite,
        shadowElevation = 2.dp,
        tonalElevation  = 0.dp,
    ) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Subtotal", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Text("S/ ${"%.2f".format(subtotal)}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(Spacing.xs))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("IGV (18%)", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Text("S/ ${"%.2f".format(igv)}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
            if (descuentoMonto > 0.0) {
                Spacer(Modifier.height(Spacing.xs))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Descuento (${formatPct(descuentoPorcentaje)}%)", color = GreenSuccess, style = MaterialTheme.typography.bodySmall)
                    Text("-S/ ${"%.2f".format(descuentoMonto)}", color = GreenSuccess, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(Spacing.sm))
            Box(Modifier.fillMaxWidth().height(1.dp).background(BorderDefault))
            Spacer(Modifier.height(Spacing.sm))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "TOTAL",
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    style      = MaterialTheme.typography.titleSmall,
                )
                Text(
                    "S/ ${"%.2f".format(total)}",
                    fontWeight = FontWeight.Bold,
                    color      = BrandRed,
                    style      = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CART PANE  — panel derecho completo
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun CartPane(
    modifier: Modifier,
    state: PosUiState,
    cashierName: String,
    clients: List<ClientRow>,
    catalog: CatalogRepository,
    onIncrease: (CartLine) -> Unit,
    onDecrease: (CartLine) -> Unit,
    onSelectConversion: (CartLine, ProductConversion?) -> Unit,
    onPay: suspend (SalePaymentInfo, Long, ReceiptCustomerInfo, TipoComprobanteEmision) -> Result<CompletedSaleReceipt>,
    onNewClient: () -> Unit = {},
    onBack: (() -> Unit)? = null,
    onApplyGlobalDiscount: (Double, Set<String>) -> Unit = { _, _ -> },
    onClearGlobalDiscount: () -> Unit = {},
) {
    var message              by remember { mutableStateOf("") }
    var showCobrarVenta      by remember { mutableStateOf(false) }
    var showGlobalDiscount   by remember { mutableStateOf(false) }
    var selectedCliente      by remember { mutableStateOf<ClientRow?>(null) }
    var showClientePicker    by remember { mutableStateOf(false) }
    var pendingReceipt       by remember { mutableStateOf<CompletedSaleReceipt?>(null) }
    var showPreview          by remember { mutableStateOf(false) }
    var comprobanteEmitido   by remember { mutableStateOf<ComprobanteEmitidoResult?>(null) }
    var selectedReceiptType  by remember { mutableStateOf(TipoComprobanteEmision.BOLETA) }
    var receiptCustomerInfo  by remember { mutableStateOf(ReceiptCustomerInfo()) }
    var showGeneratingReceipt by remember { mutableStateOf(false) }
    var showSaleCompleted    by remember { mutableStateOf(false) }
    var receiptPhone         by remember { mutableStateOf("") }
    var postSaleDismissed    by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val compact = LocalConfiguration.current.screenWidthDp < 900
    val cartListState = rememberLazyListState()
    var previousCartQuantities by remember { mutableStateOf<Map<Long, Int>>(emptyMap()) }

    LaunchedEffect(state.cart) {
        val changedIndex = state.cart.indexOfFirst { line ->
            previousCartQuantities[line.productId] != line.quantity
        }
        previousCartQuantities = state.cart.associate { it.productId to it.quantity }
        if (changedIndex >= 0) cartListState.animateScrollToItem(changedIndex)
    }

    Column(
        modifier = modifier
            .background(AppBackground)
            .padding(if (compact) Spacing.sm else Spacing.md),
    ) {
        // ── Header del carrito ───────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a productos", tint = TextPrimary, modifier = Modifier.size(21.dp))
                }
            } else {
                Icon(Icons.Filled.ShoppingBasket, contentDescription = null, tint = BrandRed, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(Spacing.sm))
            Column(Modifier.weight(1f)) {
                Text(
                    "Carrito",
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    style      = MaterialTheme.typography.titleSmall,
                )
                val qty = state.cart.sumOf { it.quantity }
                Text(
                    if (qty == 0) "Vacío" else "$qty ${if (qty == 1) "producto" else "productos"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (qty == 0) TextTertiary else TextSecondary,
                )
            }
            // ── Botón DESCUENTO, alineado con el encabezado del carrito ──────
            OutlinedButton(
                onClick = {
                    if (state.cart.isEmpty()) {
                        message = "No hay productos en el carrito para aplicar el descuento."
                        return@OutlinedButton
                    }
                    message = ""
                    showGlobalDiscount = true
                },
                modifier = Modifier.height(if (compact) 36.dp else 40.dp),
                shape    = RoundedCornerShape(Radius.md),
                colors   = ButtonDefaults.outlinedButtonColors(
                    containerColor = SurfaceWhite,
                    contentColor   = if (state.descuentoPorcentaje > 0.0) GreenSuccess else TextPrimary,
                ),
                border   = androidx.compose.foundation.BorderStroke(1.dp, BorderDefault),
                contentPadding = PaddingValues(horizontal = Spacing.sm),
            ) {
                Icon(
                    Icons.Filled.AttachMoney,
                    contentDescription = null,
                    tint = if (state.descuentoPorcentaje > 0.0) GreenSuccess else BrandRed,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    if (state.descuentoPorcentaje > 0.0)
                        if (compact) "DESC ${formatPct(state.descuentoPorcentaje)}%" else "DESCUENTO (${formatPct(state.descuentoPorcentaje)}%)"
                    else
                        if (compact) "DESC." else "DESCUENTO",
                    fontWeight = FontWeight.Bold,
                    style      = MaterialTheme.typography.labelSmall,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(Modifier.height(Spacing.md))

        // ── Selector de cliente ──────────────────────────────────────────────
        if (!compact) {
            Text("Cliente", style = MaterialTheme.typography.labelMedium, color = TextSecondary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(Spacing.xs))
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Box(Modifier.weight(1f)) {
                OutlinedButton(
                    onClick        = { showClientePicker = true },
                    modifier       = Modifier.fillMaxWidth().height(if (compact) 40.dp else 46.dp),
                    shape          = RoundedCornerShape(Radius.md),
                    colors         = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceWhite, contentColor = TextPrimary),
                    border         = androidx.compose.foundation.BorderStroke(1.dp, BorderDefault),
                    contentPadding = PaddingValues(horizontal = Spacing.md),
                ) {
                    Text(
                        selectedCliente?.name?.takeIf { it.isNotBlank() } ?: "Cliente genérico",
                        modifier  = Modifier.weight(1f),
                        color     = if (selectedCliente != null) TextPrimary else TextSecondary,
                        maxLines  = 1,
                        overflow  = TextOverflow.Ellipsis,
                        style     = MaterialTheme.typography.bodySmall,
                    )
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
                DropdownMenu(
                    expanded        = showClientePicker,
                    onDismissRequest = { showClientePicker = false },
                    modifier        = Modifier.widthIn(min = 250.dp, max = 380.dp).heightIn(max = 360.dp),
                ) {
                    DropdownMenuItem(
                        text    = { Text("Cliente genérico", style = MaterialTheme.typography.bodyMedium) },
                        onClick = { selectedCliente = null; showClientePicker = false },
                    )
                    HorizontalDivider()
                    clients.filter { it.active }.forEach { client ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(client.name.ifBlank { client.businessName.ifBlank { "Cliente" } }, fontWeight = FontWeight.Medium)
                                    if (client.document.isNotBlank()) {
                                        Text(client.document, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    }
                                }
                            },
                            onClick = { selectedCliente = client; showClientePicker = false },
                        )
                    }
                }
            }
            IconButton(
                onClick  = onNewClient,
                modifier = Modifier
                    .size(if (compact) 40.dp else 46.dp)
                    .clip(RoundedCornerShape(Radius.md))
                    .background(BrandRed),
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = "Nuevo cliente", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(Modifier.height(if (compact) Spacing.xs else Spacing.md))

        // ── Lista de items o empty state ─────────────────────────────────────
        if (state.cart.isEmpty()) {
            Box(
                modifier         = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CartEmptyState()
            }
        } else {
            LazyColumn(
                modifier              = Modifier.weight(1f),
                state                 = cartListState,
                verticalArrangement   = Arrangement.spacedBy(Spacing.sm),
                contentPadding        = PaddingValues(vertical = Spacing.xs),
            ) {
                items(state.cart) { line ->
                    CartItemRow(
                        line       = line,
                        conversions = state.products.firstOrNull { it.id == line.productId }?.conversions.orEmpty(),
                        onSelectConversion = { conversion -> onSelectConversion(line, conversion) },
                        onIncrease = { onIncrease(line) },
                        onDecrease = { onDecrease(line) },
                    )
                }
            }
        }

        Spacer(Modifier.height(if (compact) Spacing.xs else Spacing.md))

        // ── Totales ──────────────────────────────────────────────────────────
        CartTotalSection(
            subtotal            = state.subtotal,
            igv                 = state.igv,
            descuentoPorcentaje = state.descuentoPorcentaje,
            descuentoMonto      = state.descuentoMonto,
            total               = state.total,
        )

        Spacer(Modifier.height(if (compact) Spacing.xs else Spacing.md))

        // ── Botón pagar ──────────────────────────────────────────────────────
        Button(
            onClick  = {
                if (state.cart.isEmpty()) {
                    message = "Agregue productos al carrito."
                    return@Button
                }
                message = ""
                showCobrarVenta = true
            },
            modifier = Modifier.fillMaxWidth().height(if (compact) 46.dp else 52.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = BrandRed,
                contentColor           = Color.White,
                disabledContainerColor = GrayLight,
                disabledContentColor   = GrayMedium,
            ),
            shape    = RoundedCornerShape(Radius.md),
        ) {
            Icon(Icons.Filled.Payment, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.sm))
            Text(
                "COBRAR  S/ ${"%.2f".format(state.total)}",
                color      = Color.White,
                fontWeight = FontWeight.Bold,
                style      = MaterialTheme.typography.titleSmall,
            )
        }

        if (message.isNotBlank()) {
            Spacer(Modifier.height(Spacing.sm))
            Text(
                message,
                color = if (message.contains("Agregue")) BrandRed else GreenSuccess,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

    // ── Dialogs ──────────────────────────────────────────────────────────────
    if (showGlobalDiscount) {
        GlobalDiscountDialog(
            cart            = state.cart,
            currentPercent  = state.descuentoPorcentaje,
            currentLineKeys = state.descuentoLineKeys,
            onApply = { pct, lineKeys ->
                onApplyGlobalDiscount(pct, lineKeys)
                showGlobalDiscount = false
            },
            onClear = {
                onClearGlobalDiscount()
                showGlobalDiscount = false
            },
            onDismiss = { showGlobalDiscount = false },
        )
    }

    if (showCobrarVenta) {
        CobrarVentaDialog(
            total        = state.total,
            cashierName  = cashierName,
            clients      = clients,
            initialClient = selectedCliente,
            onDismiss    = { showCobrarVenta = false },
            onCobroExitoso = { receipt, receiptType, customerInfo ->
                message = "Venta registrada."
                pendingReceipt      = receipt
                selectedReceiptType = receiptType
                receiptCustomerInfo = customerInfo
                selectedCliente     = clients.firstOrNull { it.id == customerInfo.id }
                postSaleDismissed   = false
                showGeneratingReceipt = true
                scope.launch {
                    val emitted = withContext(Dispatchers.IO) {
                        catalog.emitComprobanteForVenta(
                            receipt.ventaId,
                            receiptType,
                            customerInfo.id.takeIf { it > 0L } ?: selectedCliente?.id ?: receipt.idCliente,
                            customerInfo,
                            allowOffline = true,
                        )
                    }
                    delay(900)
                    emitted.fold(
                        onSuccess = {
                            if (!postSaleDismissed) {
                                comprobanteEmitido    = it
                                showGeneratingReceipt = false
                                showSaleCompleted     = true
                            }
                        },
                        onFailure = {
                            showGeneratingReceipt = false
                            message = it.message ?: "Error al generar comprobante."
                        },
                    )
                }
            },
            onPay = { payment, customerInfo, receiptType ->
                val customerId = customerInfo.id.takeIf { it > 0L } ?: selectedCliente?.id ?: 0L
                onPay(payment, customerId, customerInfo, receiptType)
            },
        )
    }

    if (showGeneratingReceipt && pendingReceipt != null) {
        GeneratingReceiptDialog(
            type         = selectedReceiptType,
            onPrintTicket = {
                postSaleDismissed     = true
                showGeneratingReceipt = false
                scope.launch {
                    val ticket = withContext(Dispatchers.IO) {
                        catalog.emitComprobanteForVenta(pendingReceipt!!.ventaId, TipoComprobanteEmision.SOLO_TICKET, pendingReceipt!!.idCliente)
                    }
                    ticket.onSuccess {
                        comprobanteEmitido = it
                        showPreview = true
                    }.onFailure { message = it.message ?: "No se pudo preparar el ticket." }
                }
            },
            onContinueSelling = {
                postSaleDismissed     = true
                showGeneratingReceipt = false
                pendingReceipt        = null
                comprobanteEmitido    = null
                selectedCliente       = null
                receiptCustomerInfo   = ReceiptCustomerInfo()
            },
        )
    }

    if (showSaleCompleted && pendingReceipt != null && comprobanteEmitido != null) {
        SaleCompletedDialog(
            receipt       = pendingReceipt!!,
            issued        = comprobanteEmitido!!,
            type          = selectedReceiptType,
            initialPhone  = selectedCliente?.phone.orEmpty(),
            onPhoneChanged = { receiptPhone = it },
            clienteNombre = receiptCustomerInfo.name,
            clienteDoc    = receiptCustomerInfo.document,
            onPrint       = { showPreview = true },
            onNewSale     = {
                showSaleCompleted   = false
                pendingReceipt      = null
                comprobanteEmitido  = null
                selectedCliente     = null
                receiptCustomerInfo = ReceiptCustomerInfo()
                message             = ""
            },
        )
    }

    if (showPreview && pendingReceipt != null && comprobanteEmitido != null) {
        val pr  = pendingReceipt!!
        val em  = comprobanteEmitido!!
        val cid = (selectedCliente?.id ?: pr.idCliente).coerceAtLeast(0L)
        val cdisp = if (receiptCustomerInfo.document.isNotBlank() || receiptCustomerInfo.name.isNotBlank()) {
            receiptCustomerInfo.name to receiptCustomerInfo.document
        } else if (cid > 0L) {
            catalog.getClienteDisplay(cid)
        } else null
        VistaPreviaReciboDialog(
            receipt       = pr,
            emitido       = em,
            clienteNombre = cdisp?.first,
            clienteDoc    = cdisp?.second,
            whatsappPhone = receiptPhone.ifBlank { selectedCliente?.phone.orEmpty() },
            onDismiss     = { showPreview = false },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  COBRAR VENTA DIALOG  — rediseñado
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun LegacyCobrarVentaDialog(
    total: Double,
    onDismiss: () -> Unit,
    onCobroExitoso: (CompletedSaleReceipt) -> Unit,
    onPay: suspend (SalePaymentInfo) -> Result<CompletedSaleReceipt>,
) {
    var method       by remember { mutableStateOf(PosPaymentMethod.Efectivo) }
    var receivedText by remember { mutableStateOf("") }
    var processing   by remember { mutableStateOf(false) }
    var errorText    by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(method, total) {
        receivedText = when (method) {
            PosPaymentMethod.Efectivo -> ""
            else -> String.format(Locale.US, "%.2f", total)
        }
    }

    val received  = receivedText.toDoubleOrNull() ?: 0.0
    val vuelto    = (received - total).coerceAtLeast(0.0)
    val canConfirm = total > 0 && !processing && received + 1e-6 >= total

    Dialog(onDismissRequest = { if (!processing) onDismiss() }) {
        Box(Modifier.fillMaxSize().padding(horizontal = Spacing.lg, vertical = Spacing.xl), contentAlignment = Alignment.Center) {
            Surface(
                modifier        = Modifier.widthIn(max = 440.dp).fillMaxHeight(0.92f),
                shape           = RoundedCornerShape(Radius.xl),
                color           = SurfaceWhite,
                shadowElevation = 12.dp,
            ) {
                Column(Modifier.fillMaxSize()) {
                    // Cabecera
                    Row(
                        modifier = Modifier.fillMaxWidth().background(BrandRedDark).padding(horizontal = Spacing.md, vertical = Spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Payment, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(Spacing.sm))
                            Text("Cobrar venta", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                        IconButton(onClick = { if (!processing) onDismiss() }, enabled = !processing) {
                            Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }
                    Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(Spacing.lg)) {
                        Text("TOTAL A COBRAR", color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(Spacing.xs))
                        Text("S/ ${String.format(Locale.US, "%.2f", total)}", color = BrandRed, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                        Spacer(Modifier.height(Spacing.lg))
                        Text("MÉTODO DE PAGO", color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(Spacing.sm))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                            listOf(
                                Triple(PosPaymentMethod.Efectivo, "Efectivo",  Icons.Filled.AttachMoney),
                                Triple(PosPaymentMethod.Tarjeta,  "Tarjeta",   Icons.Filled.CreditCard),
                                Triple(PosPaymentMethod.Yape,     "Yape",      Icons.Filled.Smartphone),
                                Triple(PosPaymentMethod.Plin,     "Plin",      Icons.Filled.Smartphone),
                            ).forEach { (m, label, icon) ->
                                val sel = method == m
                                Column(
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(Radius.md))
                                        .background(if (sel) BrandRed else SurfaceMuted)
                                        .clickable(enabled = !processing) { method = m; errorText = "" }
                                        .padding(vertical = Spacing.md, horizontal = Spacing.xs),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(icon, contentDescription = null, tint = if (sel) Color.White else TextSecondary, modifier = Modifier.size(22.dp))
                                    Spacer(Modifier.height(Spacing.xs))
                                    Text(label, color = if (sel) Color.White else TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                        Spacer(Modifier.height(Spacing.lg))
                        Text("MONTO RECIBIDO", color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(Spacing.sm))
                        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(Radius.md)).background(SurfaceSubtle).padding(vertical = Spacing.md, horizontal = Spacing.md), contentAlignment = Alignment.CenterEnd) {
                            Text(if (receivedText.isEmpty()) "0.00" else receivedText, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                        }
                        Spacer(Modifier.height(Spacing.md))
                        Text("VUELTO", color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(Spacing.sm))
                        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(Radius.md)).background(SurfaceSubtle).padding(vertical = Spacing.sm, horizontal = Spacing.md), contentAlignment = Alignment.CenterEnd) {
                            Text("S/ ${String.format(Locale.US, "%.2f", vuelto)}", color = GreenSuccess, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                        Spacer(Modifier.height(Spacing.lg))
                        Text("TECLADO NUMÉRICO", color = TextSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(Spacing.sm))
                        listOf(listOf("7","8","9"), listOf("4","5","6"), listOf("1","2","3"), listOf(".","0","⌫")).forEach { row ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                                row.forEach { k ->
                                    Box(
                                        modifier = Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(Radius.md))
                                            .background(SurfaceMuted)
                                            .clickable(enabled = !processing && method == PosPaymentMethod.Efectivo) {
                                                receivedText = appendMontoRecibido(receivedText, k); errorText = ""
                                            },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(k, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 18.sp)
                                    }
                                }
                            }
                            Spacer(Modifier.height(Spacing.sm))
                        }
                        if (errorText.isNotBlank()) {
                            Text(errorText, color = BrandRed, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(Spacing.sm))
                        }
                    }
                    Column(Modifier.fillMaxWidth().padding(Spacing.lg)) {
                        Button(
                            onClick = {
                                if (!canConfirm) return@Button
                                scope.launch {
                                    processing = true; errorText = ""
                                    val r = onPay(SalePaymentInfo(tipoPago = mapPosPaymentMethodToTipoPago(method), montoRecibido = received, vuelto = vuelto))
                                    processing = false
                                    r.fold(onSuccess = { onCobroExitoso(it); onDismiss() }, onFailure = { errorText = it.message ?: "Error al registrar." })
                                }
                            },
                            enabled  = canConfirm,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = BrandRed),
                            shape    = RoundedCornerShape(Radius.md),
                        ) {
                            Text("COBRAR  S/ ${String.format(Locale.US, "%.2f", total)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(Modifier.height(Spacing.sm))
                        TextButton(onClick = { if (!processing) onDismiss() }, modifier = Modifier.fillMaxWidth()) {
                            Text("Cancelar", color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  DESCUENTO DIALOG  — porcentaje + selección de productos del carrito
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun GlobalDiscountDialog(
    cart: List<CartLine>,
    currentPercent: Double,
    currentLineKeys: Set<String>,
    onApply: (Double, Set<String>) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
) {
    var text  by remember { mutableStateOf(if (currentPercent > 0.0) formatPct(currentPercent) else "") }
    var error by remember { mutableStateOf<String?>(null) }
    // Si ya hay un descuento activo se restauran exactamente los productos
    // seleccionados; si no, el modal inicia sin selección y el cajero marca
    // manualmente qué productos desea descontar.
    val cartKeys = remember(cart) { cart.map { it.lineKey }.toSet() }
    var selectedKeys by remember(cart, currentLineKeys) {
        mutableStateOf(
            if (currentLineKeys.isEmpty()) emptySet()
            else currentLineKeys.filter { it in cartKeys }.toSet()
        )
    }
    val parsed = text.replace(',', '.').toDoubleOrNull()
    val pctValid = parsed != null && parsed >= 0.0 && parsed <= 100.0
    val pct = parsed?.coerceIn(0.0, 100.0) ?: 0.0
    val canApply = pctValid && selectedKeys.isNotEmpty()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier        = Modifier.fillMaxWidth().widthIn(max = 420.dp),
            shape           = RoundedCornerShape(Radius.lg),
            color           = SurfaceWhite,
            shadowElevation = 12.dp,
        ) {
            Column(Modifier.padding(Spacing.lg)) {
                Text(
                    "Descuento",
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    style      = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    "Ingresa el porcentaje y selecciona los productos a los que se aplicará.",
                    color   = TextSecondary,
                    style   = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(Spacing.md))
                OutlinedTextField(
                    value         = text,
                    onValueChange = { value ->
                        text = value.filter { it.isDigit() || it == '.' || it == ',' }
                        error = null
                    },
                    label         = { Text("Descuento (%)") },
                    suffix        = { Text("%") },
                    isError       = error != null,
                    singleLine    = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier      = Modifier.fillMaxWidth(),
                )
                if (error != null) {
                    Spacer(Modifier.height(Spacing.xs))
                    Text(error!!, color = BrandRed, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(Spacing.md))

                // ── Seleccionar todo ─────────────────────────────────────────
                // El tri-state se recalcula en cada recomposición a partir de la
                // selección individual: On si están todos, Off si ninguno,
                // Indeterminate si solo algunos (aunque se marquen uno a uno).
                val allSelected = cartKeys.isNotEmpty() &&
                    selectedKeys.size == cartKeys.size &&
                    cartKeys.all { it in selectedKeys }
                val triState = when {
                    allSelected -> ToggleableState.On
                    selectedKeys.isEmpty() -> ToggleableState.Off
                    else -> ToggleableState.Indeterminate
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Radius.md))
                        .clickable {
                            selectedKeys = if (allSelected) emptySet() else cartKeys
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TriStateCheckbox(state = triState, onClick = null)
                    Text(
                        "Seleccionar todo",
                        fontWeight = FontWeight.SemiBold,
                        color      = TextPrimary,
                        style      = MaterialTheme.typography.bodyMedium,
                        modifier   = Modifier.weight(1f),
                    )
                    Text(
                        "${cart.size} ${if (cart.size == 1) "producto" else "productos"}",
                        color   = TextSecondary,
                        style   = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(Modifier.height(Spacing.xs))

                // ── Lista de productos del carrito ───────────────────────────
                if (cart.isEmpty()) {
                    Text(
                        "No hay productos en el carrito.",
                        color = TextTertiary,
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {
                    LazyColumn(
                        modifier            = Modifier.fillMaxWidth().heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        items(cart, key = { it.lineKey }) { line ->
                            val checked = line.lineKey in selectedKeys
                            val discountedUnit = (line.unitPrice * (100.0 - pct) / 100.0)
                                .let { round(it * 100.0) / 100.0 }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(Radius.md))
                                    .clickable {
                                        selectedKeys = if (checked) selectedKeys - line.lineKey else selectedKeys + line.lineKey
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Checkbox(checked = checked, onCheckedChange = null)
                                Text(
                                    line.displayName,
                                    modifier  = Modifier.weight(1f),
                                    color     = TextPrimary,
                                    style     = MaterialTheme.typography.bodySmall,
                                    maxLines  = 2,
                                    overflow  = TextOverflow.Ellipsis,
                                )
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "S/ ${"%.2f".format(line.unitPrice)}",
                                        color          = if (checked) TextTertiary else TextSecondary,
                                        style          = MaterialTheme.typography.bodySmall,
                                        textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                                    )
                                    if (checked && pct > 0.0) {
                                        Text(
                                            "S/ ${"%.2f".format(discountedUnit)}",
                                            color      = GreenSuccess,
                                            fontWeight = FontWeight.SemiBold,
                                            style      = MaterialTheme.typography.bodySmall,
                                        )
                                    } else {
                                        Text("--", color = TextTertiary, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(Spacing.md))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs, Alignment.End),
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    if (currentPercent > 0.0 || currentLineKeys.isNotEmpty()) {
                        TextButton(onClick = onClear) { Text("Quitar descuento", color = BrandRed) }
                    }
                    Button(
                        onClick = {
                            val pctValue = parsed
                            when {
                                pctValue == null || pctValue < 0.0 || pctValue > 100.0 ->
                                    error = "Ingresa un porcentaje entre 0 y 100."
                                selectedKeys.isEmpty() ->
                                    error = "Selecciona al menos un producto."
                                else -> onApply(pctValue, selectedKeys)
                            }
                        },
                        enabled = canApply,
                        colors   = ButtonDefaults.buttonColors(
                            containerColor         = BrandRed,
                            contentColor           = Color.White,
                            disabledContainerColor = GrayLight,
                            disabledContentColor   = GrayMedium,
                        ),
                        shape  = RoundedCornerShape(Radius.md),
                    ) { Text("Aplicar descuento") }
                }
            }
        }
    }
}
