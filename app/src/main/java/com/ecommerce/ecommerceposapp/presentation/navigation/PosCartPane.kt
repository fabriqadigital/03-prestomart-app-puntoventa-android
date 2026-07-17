package com.ecommerce.ecommerceposapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.presentation.pos.PosUiState
import com.ecommerce.ecommerceposapp.presentation.pos.VistaPreviaReciboDialog
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Brand = Color(0xFFfd0505)
private val BrandDark = Color(0xFFa82024)
private val AppBg = Color(0xFFF5F7FA)
private val SurfaceWhite = Color(0xFFFFFFFF)
private val SurfaceAlt = Color(0xFFEEF0F5)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val Divider = Color(0xFFE5E7EB)
private enum class PosPaymentMethod { Efectivo, Tarjeta, Yape, Plin }

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
    PosPaymentMethod.Tarjeta -> "TAR"
    PosPaymentMethod.Yape -> "YAP"
    PosPaymentMethod.Plin -> "PLN"
}

@Composable
private fun LegacyCobrarVentaDialog(
    total: Double,
    onDismiss: () -> Unit,
    onCobroExitoso: (CompletedSaleReceipt) -> Unit,
    onPay: suspend (SalePaymentInfo) -> Result<CompletedSaleReceipt>,
) {
    val headerBg = BrandDark
    val bodyBg = SurfaceWhite
    val keypadBg = SurfaceAlt
    val methodUnselected = Color(0xFFF0F2F5)
    val methodSelected = Brand
    val labelMuted = TextSecondary

    var method by remember { mutableStateOf(PosPaymentMethod.Efectivo) }
    var receivedText by remember { mutableStateOf("") }
    var processing by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(method, total) {
        receivedText = when (method) {
            PosPaymentMethod.Efectivo -> ""
            else -> String.format(Locale.US, "%.2f", total)
        }
    }

    val received = receivedText.toDoubleOrNull() ?: 0.0
    val vuelto = (received - total).coerceAtLeast(0.0)
    val canConfirm = total > 0 && !processing && received + 1e-6 >= total

    Dialog(onDismissRequest = { if (!processing) onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(20.dp),
                color = bodyBg,
                shadowElevation = 12.dp,
            ) {
                Column(Modifier.fillMaxSize()) {
                    // Cabecera roja
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBg)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Payment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Cobrar venta",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                        IconButton(onClick = { if (!processing) onDismiss() }, enabled = !processing) {
                            Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                    ) {
                        Text("TOTAL A COBRAR", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Text(
                                "S/ ${String.format(Locale.US, "%.2f", total)}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("MÉTODO DE PAGO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val methods = listOf(
                                Triple(PosPaymentMethod.Efectivo, "Efectivo", Icons.Filled.AttachMoney),
                                Triple(PosPaymentMethod.Tarjeta, "Tarjeta", Icons.Filled.CreditCard),
                                Triple(PosPaymentMethod.Yape, "Yape", Icons.Filled.Smartphone),
                                Triple(PosPaymentMethod.Plin, "Plin", Icons.Filled.Smartphone),
                            )
                            methods.forEach { (m, label, icon) ->
                                val sel = method == m
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (sel) methodSelected else methodUnselected)
                                        .clickable(enabled = !processing) {
                                            method = m
                                            errorText = ""
                                        }
                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = if (sel) Color.White else TextSecondary,
                                        modifier = Modifier.size(26.dp),
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        label,
                                        color = if (sel) Color.White else TextSecondary,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("MONTO RECIBIDO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceAlt, RoundedCornerShape(10.dp))
                                .padding(vertical = 14.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text(
                                text = if (receivedText.isEmpty()) "0" else receivedText,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("VUELTO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceAlt, RoundedCornerShape(10.dp))
                                .padding(vertical = 12.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Text(
                                "S/ ${String.format(Locale.US, "%.2f", vuelto)}",
                                color = Color(0xFF16A34A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                            )
                        }
                        Spacer(Modifier.height(18.dp))
                        Text("TECLADO NUMÉRICO", color = labelMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        val keys = listOf(
                            listOf("7", "8", "9"),
                            listOf("4", "5", "6"),
                            listOf("1", "2", "3"),
                            listOf(".", "0", "⌫"),
                        )
                        keys.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                row.forEach { k ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(keypadBg)
                                            .clickable(enabled = !processing && method == PosPaymentMethod.Efectivo) {
                                                receivedText = appendMontoRecibido(receivedText, k)
                                                errorText = ""
                                            },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(k, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 18.sp)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        if (errorText.isNotBlank()) {
                            Text(errorText, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                        }
                    }

                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Button(
                            onClick = {
                                if (!canConfirm) return@Button
                                scope.launch {
                                    processing = true
                                    errorText = ""
                                    val payment = SalePaymentInfo(
                                        tipoPago = mapPosPaymentMethodToTipoPago(method),
                                        montoRecibido = received,
                                        vuelto = vuelto,
                                    )
                                    val r = onPay(payment)
                                    processing = false
                                    r.fold(
                                        onSuccess = {
                                            onCobroExitoso(it)
                                            onDismiss()
                                        },
                                        onFailure = { errorText = it.message ?: "No se pudo registrar la venta." },
                                    )
                                }
                            },
                            enabled = canConfirm,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Brand),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(
                                "COBRAR  S/ ${String.format(Locale.US, "%.2f", total)}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        TextButton(
                            onClick = { if (!processing) onDismiss() },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Cancelar", color = labelMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun CartPane(
    modifier: Modifier,
    state: PosUiState,
    cashierName: String,
    clients: List<ClientRow>,
    catalog: CatalogRepository,
    onIncrease: (CartLine) -> Unit,
    onDecrease: (CartLine) -> Unit,
    onPay: suspend (SalePaymentInfo, Long) -> Result<CompletedSaleReceipt>,
    onNewClient: () -> Unit = {},
) {
    var message by remember { mutableStateOf("") }
    var showCobrarVenta by remember { mutableStateOf(false) }
    var selectedCliente by remember { mutableStateOf<ClientRow?>(null) }
    var showClientePicker by remember { mutableStateOf(false) }
    var pendingReceipt by remember { mutableStateOf<CompletedSaleReceipt?>(null) }
    var showPreview by remember { mutableStateOf(false) }
    var comprobanteEmitido by remember { mutableStateOf<ComprobanteEmitidoResult?>(null) }
    var selectedReceiptType by remember { mutableStateOf(TipoComprobanteEmision.BOLETA) }
    var showGeneratingReceipt by remember { mutableStateOf(false) }
    var showSaleCompleted by remember { mutableStateOf(false) }
    var receiptPhone by remember { mutableStateOf("") }
    var postSaleDismissed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.background(SurfaceWhite).padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = Brand,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Carrito (${state.cart.sumOf { it.quantity }})",
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Spacer(Modifier.height(6.dp))
        Text("Cliente", style = MaterialTheme.typography.labelMedium, color = TextSecondary, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { showClientePicker = true },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
                ) {
                    Text(selectedCliente?.name?.takeIf { it.isNotBlank() } ?: "Cliente general", modifier = Modifier.weight(1f), color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Mostrar clientes", tint = TextSecondary)
                }
                DropdownMenu(
                    expanded = showClientePicker,
                    onDismissRequest = { showClientePicker = false },
                    modifier = Modifier.widthIn(min = 250.dp, max = 380.dp).heightIn(max = 360.dp),
                ) {
                    DropdownMenuItem(text = { Text("Cliente general") }, onClick = { selectedCliente = null; showClientePicker = false })
                    clients.filter { it.active }.forEach { client ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(client.name.ifBlank { client.businessName.ifBlank { "Cliente" } }, fontWeight = FontWeight.Medium)
                                    if (client.document.isNotBlank()) Text(client.document, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                            },
                            onClick = { selectedCliente = client; showClientePicker = false },
                        )
                    }
                }
            }
            Button(
                onClick = onNewClient,
                modifier = Modifier.height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand, contentColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(5.dp))
                Text("Nuevo", fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(state.cart) { line ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppBg)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            line.productName,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            "S/ ${"%.2f".format(line.unitPrice)}",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onDecrease(line) },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(Icons.Filled.Remove, contentDescription = "Menos", tint = Brand)
                        }
                        Text(
                            "${line.quantity}",
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        IconButton(
                            onClick = { onIncrease(line) },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Más", tint = Brand)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(AppBg)
                .padding(12.dp),
        ) {
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text("S/ ${"%.2f".format(state.subtotal)}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("IGV (18%)", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text("S/ ${"%.2f".format(state.igv)}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Divider),
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("TOTAL", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "S/ ${"%.2f".format(state.total)}",
                        fontWeight = FontWeight.Bold,
                        color = Brand,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = {
                if (state.cart.isEmpty()) {
                    message = "Agregue productos al carrito."
                    return@Button
                }
                message = ""
                showCobrarVenta = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Brand),
            shape = RoundedCornerShape(12.dp),
        ) {
            Icon(Icons.Filled.Print, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("PAGAR", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
        }
        if (message.isNotBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                message,
                color = if (message.contains("Agregue")) Brand else Color(0xFF16A34A),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

    if (showCobrarVenta) {
        CobrarVentaDialog(
            total = state.total,
            cashierName = cashierName,
            onDismiss = { showCobrarVenta = false },
            onCobroExitoso = { receipt, receiptType ->
                message = "Venta registrada."
                pendingReceipt = receipt
                selectedReceiptType = receiptType
                postSaleDismissed = false
                showGeneratingReceipt = true
                scope.launch {
                    val emitted = withContext(Dispatchers.IO) {
                        catalog.emitComprobanteForVenta(
                            receipt.ventaId,
                            receiptType,
                            selectedCliente?.id ?: receipt.idCliente,
                        )
                    }
                    delay(900)
                    emitted.fold(
                        onSuccess = {
                            if (!postSaleDismissed) {
                                comprobanteEmitido = it
                                showGeneratingReceipt = false
                                showSaleCompleted = true
                            }
                        },
                        onFailure = {
                            showGeneratingReceipt = false
                            message = it.message ?: "Error al generar comprobante."
                        },
                    )
                }
            },
            onPay = { payment -> onPay(payment, selectedCliente?.id ?: 0L) },
        )
    }

    if (showGeneratingReceipt && pendingReceipt != null) {
        val receipt = pendingReceipt!!
        GeneratingReceiptDialog(
            type = selectedReceiptType,
            onPrintTicket = {
                postSaleDismissed = true
                showGeneratingReceipt = false
                scope.launch {
                    val ticket = withContext(Dispatchers.IO) {
                        catalog.emitComprobanteForVenta(receipt.ventaId, TipoComprobanteEmision.SOLO_TICKET, receipt.idCliente)
                    }
                    ticket.onSuccess {
                        comprobanteEmitido = it
                        showPreview = true
                    }.onFailure { message = it.message ?: "No se pudo preparar el ticket." }
                }
            },
            onContinueSelling = {
                postSaleDismissed = true
                showGeneratingReceipt = false
                pendingReceipt = null
                comprobanteEmitido = null
                selectedCliente = null
            },
        )
    }

    if (showSaleCompleted && pendingReceipt != null && comprobanteEmitido != null) {
        val receipt = pendingReceipt!!
        val issued = comprobanteEmitido!!
        SaleCompletedDialog(
            receipt = receipt,
            issued = issued,
            type = selectedReceiptType,
            initialPhone = selectedCliente?.phone.orEmpty(),
            onPhoneChanged = { receiptPhone = it },
            onPrint = { showPreview = true },
            onNewSale = {
                showSaleCompleted = false
                pendingReceipt = null
                comprobanteEmitido = null
                selectedCliente = null
                message = ""
            },
        )
    }

    if (showPreview && pendingReceipt != null && comprobanteEmitido != null) {
        val pr = pendingReceipt!!
        val em = comprobanteEmitido!!
        val cid = (selectedCliente?.id ?: pr.idCliente).coerceAtLeast(0L)
        val cdisp = if (cid > 0L) catalog.getClienteDisplay(cid) else null
        VistaPreviaReciboDialog(
            receipt = pr,
            emitido = em,
            clienteNombre = cdisp?.first,
            clienteDoc = cdisp?.second,
            whatsappPhone = receiptPhone.ifBlank { selectedCliente?.phone.orEmpty() },
            onDismiss = {
                showPreview = false
            },
        )
    }
}
