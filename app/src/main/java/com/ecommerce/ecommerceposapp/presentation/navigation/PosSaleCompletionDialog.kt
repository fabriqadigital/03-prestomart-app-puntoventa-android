package com.ecommerce.ecommerceposapp.presentation.navigation

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.R
import com.ecommerce.ecommerceposapp.data.remote.api.ReceiptDeliveryApiDataSource
import com.ecommerce.ecommerceposapp.data.sync.OfflineReceiptDeliveryQueue
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.presentation.pos.buildReceiptShareText
import com.ecommerce.ecommerceposapp.presentation.pos.createReceiptPdfForSharing
import com.ecommerce.ecommerceposapp.presentation.pos.openWhatsapp
import com.ecommerce.ecommerceposapp.presentation.pos.sanitizePhone51
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val CompletionBrand = Color(0xFFFD0505)
private val CompletionText = Color(0xFF111827)
private val CompletionMuted = Color(0xFF64748B)
private val CompletionBorder = Color(0xFFD7DCE3)

private enum class ShareMode { Email, Whatsapp }

private fun receiptTypeLabel(type: TipoComprobanteEmision): String = when (type) {
    TipoComprobanteEmision.BOLETA -> "Boleta"
    TipoComprobanteEmision.FACTURA -> "Factura"
    TipoComprobanteEmision.SOLO_TICKET -> "Ticket"
}

private fun Context.hasValidatedInternet(): Boolean {
    val connectivity = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivity.activeNetwork ?: return false
    val capabilities = connectivity.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

@Composable
internal fun GeneratingReceiptDialog(
    type: TipoComprobanteEmision,
    onPrintTicket: () -> Unit,
    onContinueSelling: () -> Unit,
) {
    Dialog(onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(20.dp).widthIn(max = 720.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            contentColor = CompletionText,
            shadowElevation = 14.dp,
        ) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(color = CompletionBrand, strokeWidth = 3.dp, modifier = Modifier.size(58.dp))
                Spacer(Modifier.height(24.dp))
                Text("Generando ${receiptTypeLabel(type).lowercase()}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(14.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().widthIn(max = 440.dp).height(7.dp),
                    color = CompletionBrand,
                    trackColor = Color(0xFFE5E7EB),
                )
                Spacer(Modifier.height(52.dp))
                OutlinedButton(
                    onClick = onPrintTicket,
                    modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CompletionBorder),
                ) {
                    Icon(Icons.Filled.Print, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Imprimir ticket")
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onContinueSelling,
                    modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CompletionBrand, contentColor = Color.White),
                ) { Text("Seguir vendiendo") }
            }
        }
    }
}

@Composable
internal fun SaleCompletedDialog(
    receipt: CompletedSaleReceipt,
    issued: ComprobanteEmitidoResult,
    type: TipoComprobanteEmision,
    initialPhone: String,
    onPhoneChanged: (String) -> Unit,
    clienteNombre: String,
    clienteDoc: String,
    onPrint: () -> Unit,
    onNewSale: () -> Unit,
) {
    val context = LocalContext.current
    var shareMode by remember { mutableStateOf(ShareMode.Email) }
    var destination by remember(initialPhone) { mutableStateOf("") }
    var notice by remember { mutableStateOf("") }
    var sharingPdf by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(notice) {
        if (notice.isNotBlank()) {
            delay(3_500)
            notice = ""
        }
    }

    fun share() {
        when (shareMode) {
            ShareMode.Email -> {
                if (!destination.contains('@') || sharingPdf) return
                scope.launch {
                    sharingPdf = true
                    notice = ""
                    var generatedPdf: java.io.File? = null
                    runCatching {
                        val pdf = withContext(Dispatchers.IO) {
                            createReceiptPdfForSharing(context, receipt, issued, clienteNombre, clienteDoc, null)
                        }
                        generatedPdf = pdf
                        withContext(Dispatchers.IO) {
                            ReceiptDeliveryApiDataSource(context).sendByEmail(
                                destination,
                                issued.numeroCompleto,
                                clienteNombre.ifBlank { issued.receptorNombre },
                                pdf,
                            ).getOrThrow()
                        }
                    }.onSuccess {
                        notice = "Comprobante enviado por correo."
                    }.onFailure {
                        val queued = generatedPdf?.let { pdf ->
                            OfflineReceiptDeliveryQueue(context).enqueueEmail(
                                email = destination,
                                receiptNumber = issued.numeroCompleto,
                                customerName = clienteNombre.ifBlank { issued.receptorNombre },
                                sourcePdf = pdf,
                            )
                        }
                        notice = if (queued?.isSuccess == true) {
                            "Sin conexión. El correo quedó en cola y se enviará automáticamente."
                        } else {
                            it.message ?: "No se pudo enviar ni encolar el comprobante."
                        }
                    }
                    sharingPdf = false
                }
            }
            ShareMode.Whatsapp -> {
                val digits = destination.filter(Char::isDigit)
                if (digits.length < 9 || sharingPdf || !context.hasValidatedInternet()) {
                    if (!context.hasValidatedInternet()) notice = "WhatsApp no está disponible sin conexión. Puede usar correo y quedará en cola."
                    return
                }
                scope.launch {
                    sharingPdf = true
                    runCatching {
                        val pdf = withContext(Dispatchers.IO) {
                            createReceiptPdfForSharing(context, receipt, issued, clienteNombre, clienteDoc, null)
                        }
                        val link = withContext(Dispatchers.IO) {
                            ReceiptDeliveryApiDataSource(context).requestWhatsappLink(
                                issued.numeroCompleto,
                                clienteNombre.ifBlank { issued.receptorNombre },
                                pdf,
                            ).getOrThrow()
                        }
                        val phone51 = sanitizePhone51(digits) ?: error("Número de WhatsApp inválido.")
                        val message = buildReceiptShareText(receipt, issued, clienteNombre, clienteDoc, link.url)
                        openWhatsapp(context, phone51, message)
                    }.onSuccess {
                        notice = "WhatsApp abierto con el comprobante."
                    }.onFailure {
                        notice = it.message ?: "No se pudo abrir WhatsApp con el comprobante. Verifique su conexión."
                    }
                    sharingPdf = false
                }
            }
        }
    }

    val destinationValid = when (shareMode) {
        ShareMode.Email -> destination.contains('@')
        ShareMode.Whatsapp -> destination.filter(Char::isDigit).length >= 9 && context.hasValidatedInternet()
    }

    Dialog(onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(18.dp).widthIn(max = 760.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                contentColor = CompletionText,
                shadowElevation = 14.dp,
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 720.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(26.dp),
                ) {
                Text("${receiptTypeLabel(type)} guardada", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(24.dp))
                Surface(
                    modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally),
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(3.dp, CompletionBrand),
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = CompletionBrand, modifier = Modifier.padding(13.dp))
                }
                Spacer(Modifier.height(14.dp))
                Text("¡Muy bien!", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text("Tu venta se guardó con éxito", modifier = Modifier.align(Alignment.CenterHorizontally), color = CompletionMuted, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF7F8FA),
                ) {
                    Column(Modifier.padding(horizontal = 18.dp, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total S/ ${String.format(Locale.US, "%.2f", receipt.total)}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Vuelto S/ ${String.format(Locale.US, "%.2f", receipt.vuelto)}", color = CompletionBrand, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(22.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text("Compartir por:", color = CompletionMuted)
                    Spacer(Modifier.width(10.dp))
                    ShareButton(ShareMode.Email, shareMode, "Correo", { tint ->
                        Icon(Icons.Filled.Email, contentDescription = "Correo", tint = tint)
                    }) { shareMode = it; destination = ""; notice = "" }
                    ShareButton(ShareMode.Whatsapp, shareMode, "WhatsApp", {
                        Image(painterResource(R.drawable.ic_whatsapp), contentDescription = "WhatsApp", modifier = Modifier.size(24.dp))
                    }, enabled = context.hasValidatedInternet()) { shareMode = it; destination = initialPhone; notice = "" }
                }
                Spacer(Modifier.height(10.dp))
                when (shareMode) {
                    ShareMode.Email -> OutlinedTextField(
                        destination, { destination = it }, label = { Text("Enviar ${receiptTypeLabel(type).lowercase()} por correo") },
                        placeholder = { Text("ejemplo@correo.com") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                    )
                    ShareMode.Whatsapp -> OutlinedTextField(
                        destination, {
                            destination = it.filter { char -> char.isDigit() }.take(11)
                            onPhoneChanged(destination)
                        }, label = { Text("Teléfono del cliente") },
                        prefix = { Text("+51 ") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    )
                }
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = ::share,
                    enabled = destinationValid && !sharingPdf,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    if (shareMode == ShareMode.Email) {
                        Icon(Icons.Filled.Email, contentDescription = null)
                    } else {
                        Image(painterResource(R.drawable.ic_whatsapp), contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(7.dp))
                    Text(if (sharingPdf) "Preparando PDF..." else "Enviar")
                }
                Spacer(Modifier.height(22.dp))
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val compact = maxWidth < 420.dp
                    if (compact) {
                        Column(Modifier.fillMaxWidth()) {
                            PrintButton(onPrint, Modifier.fillMaxWidth())
                            Spacer(Modifier.height(10.dp))
                            NewSaleButton(onNewSale, Modifier.fillMaxWidth())
                        }
                    } else {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            PrintButton(onPrint, Modifier.width(150.dp))
                            Spacer(Modifier.width(10.dp))
                            NewSaleButton(onNewSale, Modifier.width(160.dp))
                        }
                    }
                }
                }
            }
            if (notice.isNotBlank()) {
                CompletionNotice(
                    message = notice,
                    isError = notice.startsWith("No ", ignoreCase = true),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(horizontal = 24.dp, vertical = 22.dp),
                )
            }
        }
    }
}

@Composable
private fun CompletionNotice(message: String, isError: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.widthIn(max = 520.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isError) Color(0xFF991B1B) else Color(0xFF166534),
        contentColor = Color.White,
        shadowElevation = 16.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                if (isError) Icons.Filled.ErrorOutline else Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Text(message, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PrintButton(onClick: () -> Unit, modifier: Modifier) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(50.dp), shape = RoundedCornerShape(8.dp)) {
        Icon(Icons.Filled.Print, contentDescription = null)
        Spacer(Modifier.width(7.dp))
        Text("Imprimir")
    }
}

@Composable
private fun NewSaleButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CompletionBrand, contentColor = Color.White),
    ) { Text("Nueva venta") }
}

@Composable
private fun ShareButton(
    mode: ShareMode,
    selected: ShareMode,
    description: String,
    icon: @Composable (tint: Color) -> Unit,
    enabled: Boolean = true,
    onSelect: (ShareMode) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (mode == selected) Color(0xFFF3F4F6) else Color.White,
        border = BorderStroke(1.dp, if (mode == selected) CompletionBrand else Color.Transparent),
    ) {
        IconButton(onClick = { onSelect(mode) }, enabled = enabled) {
            icon(if (mode == selected) CompletionBrand else CompletionMuted)
        }
    }
}
