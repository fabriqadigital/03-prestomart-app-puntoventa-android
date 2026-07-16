package com.ecommerce.ecommerceposapp.presentation.pos

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.round
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun r2(x: Double) = round(x * 100) / 100

private fun formatFechaHoraPeru(millis: Long): Pair<String, String> {
    val tz = TimeZone.getTimeZone("America/Lima")
    val df = SimpleDateFormat("dd/MM/yyyy", Locale.US).apply { timeZone = tz }
    val tf = SimpleDateFormat("HH:mm:ss", Locale.US).apply { timeZone = tz }
    return df.format(Date(millis)) to tf.format(Date(millis))
}

private fun mapTipoPagoEtiqueta(codigo: String): String = when (codigo) {
    "EFE" -> "EFECTIVO"
    "TAR" -> "TARJETA"
    "YAP" -> "YAPE"
    "PLN" -> "PLIN"
    else -> codigo
}

private fun tituloComprobante(tipoSunat: String): String = when (tipoSunat) {
    "03" -> "BOLETA DE VENTA ELECTRÓNICA"
    "01" -> "FACTURA DE VENTA ELECTRÓNICA"
    else -> "TICKET DE VENTA"
}

private fun sanitizePhone51(input: String): String? {
    val d = input.filter { it.isDigit() }
    if (d.length < 9) return null
    return when {
        d.startsWith("51") && d.length >= 11 -> d
        d.length == 9 -> "51$d"
        else -> "51" + d.takeLast(9)
    }
}

private fun buildReceiptShareText(
    receipt: CompletedSaleReceipt,
    emitido: ComprobanteEmitidoResult,
    clienteNombre: String?,
    clienteDoc: String?,
): String = buildString {
    appendLine(emitido.emisorRazonSocial)
    appendLine("RUC: ${emitido.emisorRuc}")
    appendLine()
    appendLine("${tituloComprobante(emitido.tipoSunat)} ${emitido.numeroCompleto}")
    appendLine("F. Emisión: ${formatFechaHoraPeru(receipt.fechaMillis).first}")
    appendLine("TOTAL: S/ ${"%.2f".format(Locale.US, receipt.total)}")
    if (!clienteNombre.isNullOrBlank()) appendLine("Cliente: $clienteNombre")
    if (!clienteDoc.isNullOrBlank()) appendLine("Doc.: $clienteDoc")
}

private fun openWhatsapp(context: android.content.Context, phone51: String, message: String) {
    val enc = URLEncoder.encode(message, Charsets.UTF_8.name())
    val uri = android.net.Uri.parse("https://wa.me/$phone51?text=$enc")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
    context.startActivity(intent)
}

private suspend fun encodeQrBitmap(text: String, size: Int = 240): Bitmap = withContext(Dispatchers.Default) {
    val matrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    bmp
}

@Composable
fun EmitirComprobanteDialog(
    onDismiss: () -> Unit,
    onElegir: (TipoComprobanteEmision) -> Unit,
) {
    val header = Color(0xFFa82024)
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 8.dp,
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth().background(header).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Venta registrada — ¿Emitir comprobante electrónico?",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Seleccione el tipo de comprobante a emitir:",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = { onElegir(TipoComprobanteEmision.BOLETA) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(Icons.Filled.Description, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Boleta")
                        }
                        OutlinedButton(
                            onClick = { onElegir(TipoComprobanteEmision.FACTURA) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Icon(Icons.Filled.Description, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Factura")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { onElegir(TipoComprobanteEmision.SOLO_TICKET) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Filled.Print, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Solo ticket")
                    }
                }
            }
        }
    }
}

@Composable
fun WhatsappDestinoDialog(
    clients: List<ClientRow>,
    onDismiss: () -> Unit,
    onEnviarTelefono: (String) -> Unit,
    onElegirCliente: (ClientRow) -> Unit,
) {
    var busqueda by remember { mutableStateOf("") }
    var manual by remember { mutableStateOf("") }
    val filtrados = remember(clients, busqueda) {
        clients.filter { it.active }.filter {
            busqueda.isBlank() ||
                it.name.contains(busqueda, ignoreCase = true) ||
                it.phone.contains(busqueda, ignoreCase = true) ||
                it.document.contains(busqueda, ignoreCase = true)
        }.take(40)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enviar por WhatsApp") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    "Busque un cliente o escriba un celular (9 dígitos o con código 51).",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    label = { Text("Buscar cliente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .height(160.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    filtrados.forEach { c ->
                        Text(
                            "${c.name} — ${c.phone.ifBlank { "sin teléfono" }}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (c.phone.isNotBlank()) onElegirCliente(c)
                                }
                                .padding(vertical = 6.dp),
                            color = if (c.phone.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        )
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                OutlinedTextField(
                    value = manual,
                    onValueChange = { manual = it },
                    label = { Text("O número de celular") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("987654321 o 51987654321") },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    sanitizePhone51(manual)?.let(onEnviarTelefono)
                },
                enabled = sanitizePhone51(manual) != null,
            ) { Text("Abrir WhatsApp") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
    )
}

@Composable
fun VistaPreviaReciboDialog(
    receipt: CompletedSaleReceipt,
    emitido: ComprobanteEmitidoResult,
    clienteNombre: String?,
    clienteDoc: String?,
    idClienteVenta: Long,
    clients: List<ClientRow>,
    catalog: CatalogRepository,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val (fecha, hora) = formatFechaHoraPeru(receipt.fechaMillis)
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(emitido.qrPayload) {
        qrBitmap = encodeQrBitmap(emitido.qrPayload)
    }
    var showWa by remember { mutableStateOf(false) }

    val shareText = remember(receipt, emitido, clienteNombre, clienteDoc) {
        buildReceiptShareText(receipt, emitido, clienteNombre, clienteDoc)
    }

    fun enviarWa(phone51: String) {
        openWhatsapp(context, phone51, shareText)
    }

    fun intentarWhatsappRegistrado() {
        val tel = catalog.getClienteTelefono(idClienteVenta)
        val p51 = tel?.let { sanitizePhone51(it) }
        if (p51 != null) enviarWa(p51) else showWa = true
    }

    if (showWa) {
        WhatsappDestinoDialog(
            clients = clients,
            onDismiss = { showWa = false },
            onEnviarTelefono = { p ->
                enviarWa(p)
                showWa = false
            },
            onElegirCliente = { c ->
                val p = sanitizePhone51(c.phone)
                if (p != null) {
                    catalog.actualizarClienteEnVenta(receipt.ventaId, c.id)
                    enviarWa(p)
                    showWa = false
                }
            },
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 10.dp,
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().background(Color(0xFFa82024)).padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "VISTA PREVIA — COMPROBANTE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, "Cerrar", tint = Color.White)
                    }
                }
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 320.dp)
                            .background(Color.White)
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                    ) {
                        Text(
                            emitido.emisorRazonSocial.uppercase(Locale("es", "PE")),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            "RUC: ${emitido.emisorRuc}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        if (emitido.emisorDireccion.isNotBlank()) {
                            Text(
                                emitido.emisorDireccion,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            tituloComprobante(emitido.tipoSunat),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            emitido.numeroCompleto,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(6.dp))
                        Text("F. Emisión: $fecha", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text("H. Emisión: $hora", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text("F. Vencimiento: $fecha", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text("Cliente: ${clienteNombre?.ifBlank { "—" } ?: "—"}", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text("Doc.: ${clienteDoc?.ifBlank { "—" } ?: "—"}", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        HorizontalDivider(Modifier.padding(vertical = 6.dp))
                        Text(
                            "CANT  UND  DESCRIPCIÓN          PRECIO",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        receipt.lines.forEach { line ->
                            val desc = line.productName.take(28)
                            val puIgv = r2(line.unitPrice * 1.18)
                            val totIgv = r2(line.lineTotal * 1.18)
                            Text(
                                "${line.quantity} und  $desc",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                            )
                            Text(
                                "S/ ${"%.2f".format(Locale.US, totIgv)}  (S/ ${"%.2f".format(Locale.US, puIgv)} c/u)",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.sp,
                            )
                        }
                        HorizontalDivider(Modifier.padding(vertical = 6.dp))
                        Text(
                            "OP. GRAVADAS: S/ ${"%.2f".format(Locale.US, receipt.subtotal)}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                        )
                        Text(
                            "IGV (18%): S/ ${"%.2f".format(Locale.US, receipt.igv)}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                        )
                        Text(
                            "TOTAL A PAGAR: S/ ${"%.2f".format(Locale.US, receipt.total)}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            emitido.totalLetras,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                        qrBitmap?.let { bmp ->
                            Spacer(Modifier.height(8.dp))
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "QR",
                                modifier = Modifier.size(140.dp).align(Alignment.CenterHorizontally),
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("CONDICIÓN DE PAGO: Contado", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text(
                            "PAGOS: ${mapTipoPagoEtiqueta(receipt.tipoPago)} S/ ${"%.2f".format(Locale.US, receipt.montoRecibido)}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                        )
                        Text("Vendedor: ${receipt.vendedorNombre}", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text("Gracias por su compra / Vuelva pronto", fontFamily = FontFamily.Monospace, fontSize = 8.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF25D366), RoundedCornerShape(8.dp))
                            .clickable { intentarWhatsappRegistrado() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, "Enviar por WhatsApp", tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(
                        onClick = { /* impresión térmica: integrar en fase siguiente */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFfd0505)),
                    ) {
                        Icon(Icons.Filled.Print, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Imprimir")
                    }
                }
            }
        }
    }
}
