package com.ecommerce.ecommerceposapp.presentation.navigation

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import java.util.Locale
import kotlin.math.ceil
import kotlinx.coroutines.launch

private val PaymentBrand = Color(0xFFFD0505)
private val PaymentText = Color(0xFF111827)
private val PaymentMuted = Color(0xFF64748B)
private val PaymentBorder = Color(0xFFD6DCE5)

private enum class PaymentStep { Methods, Details }
private enum class PaymentMethod(val label: String, val code: String, val icon: ImageVector) {
    Cash("Efectivo", "EFE", Icons.Filled.Payments),
    Card("Tarjeta", "TAR", Icons.Filled.CreditCard),
    Yape("Yape", "YAP", Icons.Filled.Smartphone),
    Plin("Plin", "PLN", Icons.Filled.AccountBalanceWallet),
}

private fun quickCashAmounts(total: Double): List<Double> {
    val roundedTen = ceil(total / 10.0) * 10.0
    val denominations = listOf(20.0, 50.0, 100.0, 200.0, 500.0).filter { it > total }
    return (listOf(total, roundedTen) + denominations).distinct().take(3)
}

private fun money(value: Double): String = "S/" + String.format(Locale.US, "%.2f", value)

@Composable
internal fun CobrarVentaDialog(
    total: Double,
    cashierName: String,
    clients: List<ClientRow>,
    initialClient: ClientRow?,
    onDismiss: () -> Unit,
    onCobroExitoso: (CompletedSaleReceipt, TipoComprobanteEmision, ReceiptCustomerInfo) -> Unit,
    onPay: suspend (SalePaymentInfo, ReceiptCustomerInfo, TipoComprobanteEmision) -> Result<CompletedSaleReceipt>,
) {
    val context = LocalContext.current
    val connectivity = remember {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    val isOnline = connectivity.activeNetwork?.let { network ->
        connectivity.getNetworkCapabilities(network)?.let { capabilities ->
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
    } == true
    var step by remember { mutableStateOf(PaymentStep.Methods) }
    var method by remember { mutableStateOf<PaymentMethod?>(null) }
    var receiptType by remember { mutableStateOf(TipoComprobanteEmision.SOLO_TICKET) }
    var selectedClient by remember(initialClient) { mutableStateOf(initialClient) }
    var customerName by remember(initialClient) { mutableStateOf(initialClient?.let { it.businessName.ifBlank { it.name } }.orEmpty()) }
    var customerDoc by remember(initialClient) { mutableStateOf(initialClient?.document.orEmpty()) }
    var receivedText by remember(total) { mutableStateOf("") }
    var processing by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val received = receivedText.replace(',', '.').toDoubleOrNull() ?: 0.0
    val change = (received - total).coerceAtLeast(0.0)
    val customerInfo = if (receiptType == TipoComprobanteEmision.SOLO_TICKET) {
        ReceiptCustomerInfo()
    } else {
        ReceiptCustomerInfo(
            id = selectedClient?.id ?: 0L,
            name = customerName.trim(),
            document = customerDoc.filter(Char::isDigit),
        )
    }
    val hasCustomerData = selectedClient != null || customerInfo.name.isNotBlank() || customerInfo.document.isNotBlank()
    val customerValid = when (receiptType) {
        TipoComprobanteEmision.BOLETA -> !hasCustomerData || (customerInfo.name.isNotBlank() && customerInfo.document.length == 8)
        TipoComprobanteEmision.FACTURA -> !hasCustomerData || (customerInfo.name.isNotBlank() && customerInfo.document.length == 11)
        TipoComprobanteEmision.SOLO_TICKET -> true
    }
    val canContinue = !processing && customerValid && method != null && (method != PaymentMethod.Cash || received >= total)

    fun selectMethod(selected: PaymentMethod) {
        method = selected
        receivedText = if (selected == PaymentMethod.Cash) "" else String.format(Locale.US, "%.2f", total)
        errorText = ""
        step = PaymentStep.Details
    }

    fun confirmPayment() {
        val selected = method ?: return
        if (!canContinue) return
        scope.launch {
            processing = true
            errorText = ""
            val payment = SalePaymentInfo(
                tipoPago = selected.code,
                montoRecibido = if (selected == PaymentMethod.Cash) received else total,
                vuelto = if (selected == PaymentMethod.Cash) change else 0.0,
            )
            onPay(payment, customerInfo, receiptType).fold(
                onSuccess = {
                    processing = false
                    onCobroExitoso(it, receiptType, customerInfo)
                    onDismiss()
                },
                onFailure = {
                    processing = false
                    errorText = it.message ?: "No se pudo registrar la venta."
                },
            )
        }
    }

    Dialog(
        onDismissRequest = { if (!processing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(18.dp).widthIn(max = 780.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            contentColor = PaymentText,
            shadowElevation = 14.dp,
        ) {
            Column(
                Modifier.fillMaxWidth().heightIn(max = 680.dp).verticalScroll(rememberScrollState()).padding(24.dp),
            ) {
                Text("Pagar venta", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(20.dp))
                Text("TOTAL", modifier = Modifier.align(Alignment.CenterHorizontally), color = PaymentMuted, fontWeight = FontWeight.SemiBold)
                Text(
                    money(total),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(16.dp))
                ReceiptTypeSelector(
                    selected = receiptType,
                    online = isOnline,
                    onSelect = { selectedType ->
                        if (selectedType != receiptType) {
                            receiptType = selectedType
                            val requiredLength = if (selectedType == TipoComprobanteEmision.FACTURA) 11 else 8
                            val compatibleClient = initialClient?.takeIf {
                                selectedType != TipoComprobanteEmision.SOLO_TICKET && it.document.filter(Char::isDigit).length == requiredLength
                            }
                            selectedClient = compatibleClient
                            customerName = compatibleClient?.let { it.businessName.ifBlank { it.name } }.orEmpty()
                            customerDoc = compatibleClient?.document?.filter(Char::isDigit).orEmpty()
                            errorText = ""
                        }
                    },
                )
                if (!isOnline) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Sin Internet solo se puede emitir ticket. La boleta o factura electrónica requiere validación del servidor.",
                        color = PaymentBrand,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                if (receiptType != TipoComprobanteEmision.SOLO_TICKET) {
                    Spacer(Modifier.height(14.dp))
                    ReceiptCustomerSection(
                        receiptType = receiptType,
                        clients = clients,
                        selectedClient = selectedClient,
                        customerName = customerName,
                        customerDoc = customerDoc,
                        onClientSelected = { client ->
                            selectedClient = client
                            customerName = client?.let { it.businessName.ifBlank { it.name } }.orEmpty()
                            customerDoc = client?.document.orEmpty()
                            errorText = ""
                        },
                        onNameChange = {
                            selectedClient = null
                            customerName = it
                            errorText = ""
                        },
                        onDocChange = { value ->
                            val maxLength = if (receiptType == TipoComprobanteEmision.FACTURA) 11 else 8
                            val document = value.filter(Char::isDigit).take(maxLength)
                            val hadSelectedClient = selectedClient != null
                            val matchingClient = clients.firstOrNull {
                                it.active && it.document.filter(Char::isDigit) == document && document.length == maxLength
                            }
                            selectedClient = matchingClient
                            customerDoc = document
                            customerName = when {
                                matchingClient != null -> matchingClient.businessName.ifBlank { matchingClient.name }
                                document.isBlank() && hadSelectedClient -> ""
                                else -> customerName
                            }
                            errorText = ""
                        },
                    )
                }
                if (hasCustomerData && !customerValid) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (receiptType == TipoComprobanteEmision.FACTURA) "Ingrese RUC de 11 digitos y razon social." else "Ingrese DNI de 8 digitos y nombre del cliente.",
                        color = PaymentBrand,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(Modifier.height(22.dp))

                if (step == PaymentStep.Methods) {
                    PaymentMethodSelection(onSelect = ::selectMethod)
                    Spacer(Modifier.height(24.dp))
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.End).width(140.dp).height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) { Text("Cancelar") }
                } else {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick = {
                                step = PaymentStep.Methods
                                method = null
                                receivedText = ""
                            },
                        ) { Text("Cambiar método", color = PaymentBrand, fontWeight = FontWeight.SemiBold) }
                    }
                    PaymentDetails(
                        method = method!!,
                        total = total,
                        cashierName = cashierName,
                        receivedText = receivedText,
                        onReceivedChange = { value ->
                            if (value.matches(Regex("^\\d*[.,]?\\d{0,2}$"))) receivedText = value
                            errorText = ""
                        },
                        change = change,
                    )
                    if (!isOnline && method != PaymentMethod.Cash) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Modo offline: confirma el pago en el POS, terminal o billetera antes de continuar. La app lo registrará pendiente de sincronización, pero no puede autorizarlo con el banco.",
                            color = Color(0xFFC2410C),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    if (errorText.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))
                        Text(errorText, color = PaymentBrand, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(
                            onClick = onDismiss,
                            enabled = !processing,
                            modifier = Modifier.width(130.dp).height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                        ) { Text("Cancelar") }
                        Spacer(Modifier.width(10.dp))
                        Button(
                            onClick = ::confirmPayment,
                            enabled = canContinue,
                            modifier = Modifier.width(150.dp).height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PaymentBrand, contentColor = Color.White),
                        ) { Text(if (processing) "Procesando..." else "Continuar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptTypeSelector(
    selected: TipoComprobanteEmision,
    online: Boolean,
    onSelect: (TipoComprobanteEmision) -> Unit,
) {
    Text("Comprobante", color = PaymentMuted, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(7.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            TipoComprobanteEmision.SOLO_TICKET to "Ticket",
            TipoComprobanteEmision.BOLETA to "Boleta",
            TipoComprobanteEmision.FACTURA to "Factura",
        ).forEach { (type, label) ->
            val active = selected == type
            OutlinedButton(
                onClick = { onSelect(type) },
                enabled = online || type == TipoComprobanteEmision.SOLO_TICKET,
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, if (active) PaymentBrand else PaymentBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (active) Color(0xFFF3F4F6) else Color.White,
                    contentColor = if (active) PaymentBrand else PaymentMuted,
                ),
            ) { Text(label, maxLines = 1) }
        }
    }
}

@Composable
private fun ReceiptCustomerSection(
    receiptType: TipoComprobanteEmision,
    clients: List<ClientRow>,
    selectedClient: ClientRow?,
    customerName: String,
    customerDoc: String,
    onClientSelected: (ClientRow?) -> Unit,
    onNameChange: (String) -> Unit,
    onDocChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val activeClients = remember(clients) { clients.filter { it.active } }
    val isInvoice = receiptType == TipoComprobanteEmision.FACTURA
    val manualEntry = selectedClient == null && (customerName.isNotBlank() || customerDoc.isNotBlank())

    Text("Cliente del comprobante", color = PaymentMuted, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(7.dp))
    if (!manualEntry) {
        Box(Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, PaymentBorder),
            ) {
                Text(
                    selectedClient?.let { it.businessName.ifBlank { it.name } } ?: "Cliente genérico (sin datos)",
                    modifier = Modifier.weight(1f),
                    color = PaymentText,
                    maxLines = 1,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.widthIn(min = 260.dp, max = 420.dp).heightIn(max = 280.dp),
            ) {
                DropdownMenuItem(
                    text = { Text("Cliente genérico (sin datos)") },
                    onClick = {
                        onClientSelected(null)
                        expanded = false
                    },
                )
                activeClients.forEach { client ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(client.businessName.ifBlank { client.name })
                                if (client.document.isNotBlank()) Text(client.document, color = PaymentMuted, style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        onClick = {
                            onClientSelected(client)
                            expanded = false
                        },
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    } else {
        Text(
            "Datos ingresados manualmente. Borre el nombre y documento para volver a seleccionar un cliente.",
            color = PaymentMuted,
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(Modifier.height(8.dp))
    }
    OutlinedTextField(
        value = customerDoc,
        onValueChange = onDocChange,
        label = { Text(if (isInvoice) "RUC" else "DNI") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = customerName,
        onValueChange = onNameChange,
        label = { Text(if (isInvoice) "Razon social" else "Nombre del cliente") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
    )
}

@Composable
private fun PaymentMethodSelection(onSelect: (PaymentMethod) -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val columns = if (maxWidth >= 620.dp) 4 else 2
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = columns,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PaymentMethod.entries.forEach { method ->
                Surface(
                    modifier = Modifier
                        .width(if (columns == 4) 150.dp else 138.dp)
                        .height(122.dp)
                        .clickable { onSelect(method) },
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, PaymentBorder),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(method.icon, contentDescription = null, modifier = Modifier.size(34.dp), tint = PaymentMuted)
                        Spacer(Modifier.height(10.dp))
                        Text(method.label, fontWeight = FontWeight.SemiBold, color = PaymentMuted, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentDetails(
    method: PaymentMethod,
    total: Double,
    cashierName: String,
    receivedText: String,
    onReceivedChange: (String) -> Unit,
    change: Double,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        if (maxWidth >= 620.dp) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                PaymentAmountSection(method, total, receivedText, onReceivedChange, change, Modifier.weight(1f))
                CashierSection(method, cashierName, Modifier.weight(1f))
            }
        } else {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                PaymentAmountSection(method, total, receivedText, onReceivedChange, change, Modifier.fillMaxWidth())
                CashierSection(method, cashierName, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun PaymentAmountSection(
    method: PaymentMethod,
    total: Double,
    receivedText: String,
    onReceivedChange: (String) -> Unit,
    change: Double,
    modifier: Modifier,
) {
    Column(modifier) {
        if (method == PaymentMethod.Cash) {
            OutlinedTextField(
                value = receivedText,
                onValueChange = onReceivedChange,
                label = { Text("Valor del pago en efectivo *") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
            )
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(Modifier.weight(1f), color = PaymentBorder)
                Text("Opciones rápidas", modifier = Modifier.padding(horizontal = 10.dp), color = PaymentMuted)
                HorizontalDivider(Modifier.weight(1f), color = PaymentBorder)
            }
            Spacer(Modifier.height(10.dp))
            quickCashAmounts(total).forEach { amount ->
                OutlinedButton(
                    onClick = { onReceivedChange(String.format(Locale.US, "%.2f", amount)) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, PaymentBorder),
                ) { Text(money(amount), color = PaymentText) }
                Spacer(Modifier.height(8.dp))
            }
            Text("Vuelto: ${money(change)}", fontWeight = FontWeight.SemiBold, color = PaymentBrand)
        } else {
            Text("Método seleccionado", color = PaymentMuted)
            Spacer(Modifier.height(6.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF7F8FA), border = BorderStroke(1.dp, PaymentBorder)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(method.icon, contentDescription = null, tint = PaymentBrand)
                    Spacer(Modifier.width(10.dp))
                    Text(method.label, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    Text(money(total), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CashierSection(method: PaymentMethod, cashierName: String, modifier: Modifier) {
    Column(modifier) {
        Text("Cajero", color = PaymentMuted)
        Spacer(Modifier.height(6.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF7F8FA),
            border = BorderStroke(1.dp, PaymentBorder),
        ) {
            Column(Modifier.padding(14.dp)) {
                Text(cashierName.ifBlank { "Cajero autenticado" }, fontWeight = FontWeight.SemiBold)
                Text("Venta asociada a esta sesión de caja", color = PaymentMuted, style = MaterialTheme.typography.bodySmall)
            }
        }
        if (method == PaymentMethod.Cash) {
            Spacer(Modifier.height(14.dp))
            Text("El vuelto se calcula automáticamente según el importe recibido.", color = PaymentMuted, style = MaterialTheme.typography.bodySmall)
        }
    }
}
