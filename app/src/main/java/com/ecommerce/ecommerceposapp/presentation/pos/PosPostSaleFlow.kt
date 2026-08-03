package com.ecommerce.ecommerceposapp.presentation.pos

import android.content.Intent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.FileProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.data.remote.api.ReceiptDeliveryApiDataSource
import com.ecommerce.ecommerceposapp.R
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.net.URLEncoder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.round
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

private fun resolveReceiptCustomer(
    emitido: ComprobanteEmitidoResult,
    clienteNombre: String?,
    clienteDoc: String?,
): Pair<String, String> {
    val name = clienteNombre?.trim().takeUnless { it.isNullOrBlank() } ?: emitido.receptorNombre.trim()
    val document = clienteDoc?.trim().takeUnless { it.isNullOrBlank() } ?: emitido.receptorDocumento.trim()
    return name to document
}

internal fun sanitizePhone51(input: String): String? {
    val d = input.filter { it.isDigit() }
    if (d.length < 9) return null
    return when {
        d.startsWith("51") && d.length >= 11 -> d
        d.length == 9 -> "51$d"
        else -> "51" + d.takeLast(9)
    }
}

internal fun buildReceiptShareText(
    receipt: CompletedSaleReceipt,
    emitido: ComprobanteEmitidoResult,
    clienteNombre: String?,
    clienteDoc: String?,
    ticketUrl: String? = null,
): String = buildString {
    val (customerName, customerDocument) = resolveReceiptCustomer(emitido, clienteNombre, clienteDoc)
    appendLine(emitido.emisorRazonSocial)
    appendLine("RUC: ${emitido.emisorRuc}")
    appendLine()
    appendLine("${tituloComprobante(emitido.tipoSunat)} ${emitido.numeroCompleto}")
    appendLine("F. Emisión: ${formatFechaHoraPeru(receipt.fechaMillis).first}")
    appendLine("TOTAL: S/ ${"%.2f".format(Locale.US, receipt.total)}")
    if (customerName.isNotBlank()) appendLine("Cliente: $customerName")
    if (customerDocument.isNotBlank()) appendLine("Doc.: $customerDocument")
    if (!ticketUrl.isNullOrBlank()) {
        appendLine()
        appendLine("Ver/descargar tu comprobante:")
        appendLine(ticketUrl)
    }
}

internal fun openWhatsapp(context: android.content.Context, phone51: String, message: String) {
    val enc = URLEncoder.encode(message, Charsets.UTF_8.name())
    val uri = android.net.Uri.parse("https://wa.me/$phone51?text=$enc")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
    context.startActivity(intent)
}

private fun Context.hasValidatedInternet(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = manager.activeNetwork ?: return false
    val capabilities = manager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

internal fun shareReceiptPdfToWhatsapp(
    context: android.content.Context,
    pdf: File,
    message: String,
): Result<Unit> = runCatching {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdf)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, message)
        setPackage("com.whatsapp")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

private fun encodeQrBitmapNow(text: String, size: Int = 240): Bitmap {
    val matrix = QRCodeWriter().encode(
        text,
        BarcodeFormat.QR_CODE,
        size,
        size,
        mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.Q,
            EncodeHintType.CHARACTER_SET to Charsets.UTF_8.name(),
            EncodeHintType.MARGIN to 2,
        ),
    )
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(x, y, if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bmp
}

private suspend fun encodeQrBitmap(text: String, size: Int = 240): Bitmap = withContext(Dispatchers.Default) {
    encodeQrBitmapNow(text, size)
}

internal fun createReceiptPdfForSharing(
    context: android.content.Context,
    receipt: CompletedSaleReceipt,
    emitido: ComprobanteEmitidoResult,
    clienteNombre: String?,
    clienteDoc: String?,
    qrBitmap: Bitmap?,
): File {
    val (customerName, customerDocument) = resolveReceiptCustomer(emitido, clienteNombre, clienteDoc)
    val resolvedQrBitmap = qrBitmap ?: emitido.qrPayload.takeIf { it.isNotBlank() }
        ?.let { runCatching { encodeQrBitmapNow(it) }.getOrNull() }
    val folder = File(context.cacheDir, "shared_receipts").apply { mkdirs() }
    folder.listFiles()?.sortedByDescending { it.lastModified() }?.drop(30)?.forEach { it.delete() }
    val safeNumber = emitido.numeroCompleto.replace(Regex("[^A-Za-z0-9_-]"), "_")
    val output = File(folder, "comprobante_$safeNumber.pdf")
    val document = PdfDocument()
    val page = document.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create())
    val canvas = page.canvas
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val pageWidth = 595f
    val left = 48f
    val right = 547f
    val brand = android.graphics.Color.rgb(168, 32, 36)
    val ink = android.graphics.Color.rgb(24, 24, 27)
    val muted = android.graphics.Color.rgb(100, 116, 139)
    val softRed = android.graphics.Color.rgb(255, 244, 244)

    fun drawText(
        value: String,
        x: Float,
        baseline: Float,
        size: Float = 10f,
        color: Int = ink,
        bold: Boolean = false,
        align: Paint.Align = Paint.Align.LEFT,
    ) {
        paint.style = Paint.Style.FILL
        paint.textSize = size
        paint.color = color
        paint.isFakeBoldText = bold
        paint.textAlign = align
        canvas.drawText(value, x, baseline, paint)
    }

    fun fitText(value: String, maxWidth: Float, size: Float = 10f): String {
        paint.textSize = size
        if (paint.measureText(value) <= maxWidth) return value
        var shortened = value
        while (shortened.length > 3 && paint.measureText("$shortened...") > maxWidth) shortened = shortened.dropLast(1)
        return "$shortened..."
    }

    fun rule(y: Float, color: Int = android.graphics.Color.rgb(226, 232, 240)) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(left, y, right, y, paint)
    }

    canvas.drawColor(android.graphics.Color.WHITE)
    paint.color = brand
    paint.style = Paint.Style.FILL
    canvas.drawRect(0f, 0f, pageWidth, 118f, paint)
    val company = emitido.emisorRazonSocial.trim().ifBlank { "EMISOR NO CONFIGURADO" }.uppercase(Locale("es", "PE"))
    drawText(fitText(company, 475f, 18f), pageWidth / 2f, 42f, 18f, android.graphics.Color.WHITE, true, Paint.Align.CENTER)
    drawText("RUC: ${emitido.emisorRuc.ifBlank { "—" }}", pageWidth / 2f, 66f, 11f, android.graphics.Color.WHITE, false, Paint.Align.CENTER)
    if (emitido.emisorDireccion.isNotBlank()) {
        drawText(fitText(emitido.emisorDireccion, 475f, 10f), pageWidth / 2f, 88f, 10f, android.graphics.Color.WHITE, false, Paint.Align.CENTER)
    }

    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 1.5f
    paint.color = brand
    canvas.drawRoundRect(android.graphics.RectF(left, 136f, right, 196f), 8f, 8f, paint)
    drawText(tituloComprobante(emitido.tipoSunat), pageWidth / 2f, 160f, 12f, brand, true, Paint.Align.CENTER)
    drawText(emitido.numeroCompleto, pageWidth / 2f, 183f, 15f, ink, true, Paint.Align.CENTER)

    val (fecha, hora) = formatFechaHoraPeru(receipt.fechaMillis)
    var y = 226f
    drawText("EMISIÓN", left, y, 8f, muted, true)
    drawText("$fecha  $hora", left, y + 16f, 10f)
    drawText("CLIENTE", 300f, y, 8f, muted, true)
    drawText(fitText(customerName.ifBlank { "—" }, 240f), 300f, y + 16f, 10f, ink, true)
    y += 38f
    drawText("DOCUMENTO", left, y, 8f, muted, true)
    drawText(customerDocument.ifBlank { "—" }, left, y + 16f, 10f)
    drawText("CAJERO", 300f, y, 8f, muted, true)
    drawText(fitText(receipt.vendedorNombre, 240f), 300f, y + 16f, 10f)
    y += 34f

    paint.style = Paint.Style.FILL
    paint.color = softRed
    canvas.drawRoundRect(android.graphics.RectF(left, y, right, y + 26f), 5f, 5f, paint)
    drawText("CANT.", 58f, y + 17f, 9f, brand, true)
    drawText("DESCRIPCIÓN", 108f, y + 17f, 9f, brand, true)
    drawText("IMPORTE", 535f, y + 17f, 9f, brand, true, Paint.Align.RIGHT)
    y += 40f
    receipt.lines.forEach { item ->
        drawText(item.quantity.toString(), 72f, y, 10f, ink, true, Paint.Align.CENTER)
        drawText(fitText(item.productName, 330f, 10f), 108f, y, 10f)
        drawText("S/ ${"%.2f".format(Locale.US, item.lineTotal)}", 535f, y, 10f, ink, true, Paint.Align.RIGHT)
        y += 18f
        rule(y)
        y += 10f
    }

    y += 4f
    drawText("OP. GRAVADAS", 390f, y, 9f, muted, false, Paint.Align.RIGHT)
    drawText("S/ ${"%.2f".format(Locale.US, receipt.subtotal)}", 535f, y, 10f, ink, false, Paint.Align.RIGHT)
    y += 18f
    drawText("IGV (18%)", 390f, y, 9f, muted, false, Paint.Align.RIGHT)
    drawText("S/ ${"%.2f".format(Locale.US, receipt.igv)}", 535f, y, 10f, ink, false, Paint.Align.RIGHT)
    y += 25f
    drawText("TOTAL", 390f, y, 13f, brand, true, Paint.Align.RIGHT)
    drawText("S/ ${"%.2f".format(Locale.US, receipt.total)}", 535f, y, 16f, brand, true, Paint.Align.RIGHT)
    y += 24f
    drawText(fitText(emitido.totalLetras, 485f, 9f), left, y, 9f, muted)
    y += 24f

    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 1f
    paint.color = android.graphics.Color.rgb(226, 232, 240)
    canvas.drawRoundRect(android.graphics.RectF(left, y, right, y + 70f), 7f, 7f, paint)
    drawText("PAGO", left + 12f, y + 18f, 8f, muted, true)
    drawText(mapTipoPagoEtiqueta(receipt.tipoPago), left + 12f, y + 38f, 10f, ink, true)
    drawText("RECIBIDO", 280f, y + 18f, 8f, muted, true)
    drawText("S/ ${"%.2f".format(Locale.US, receipt.montoRecibido)}", 280f, y + 38f, 10f)
    drawText("VUELTO", 430f, y + 18f, 8f, muted, true)
    drawText("S/ ${"%.2f".format(Locale.US, receipt.vuelto)}", 430f, y + 38f, 11f, brand, true)
    y += 84f

    resolvedQrBitmap?.let { qr ->
        val available = (790f - y).coerceAtLeast(0f)
        val qrSize = minOf(118f, available)
        if (qrSize >= 72f) {
            val qrLeft = (pageWidth - qrSize) / 2f
            canvas.drawBitmap(qr, null, android.graphics.RectF(qrLeft, y, qrLeft + qrSize, y + qrSize), paint)
            y += qrSize + 12f
        }
    }
    drawText("Gracias por su compra", pageWidth / 2f, y.coerceAtMost(812f), 9f, muted, false, Paint.Align.CENTER)
    document.finishPage(page)
    output.outputStream().use(document::writeTo)
    document.close()
    return output
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
    initialPhone: String = "",
    onDismiss: () -> Unit,
    onEnviarTelefono: (String) -> Unit,
    onElegirCliente: (ClientRow) -> Unit,
) {
    var busqueda by remember { mutableStateOf("") }
    var manual by remember(initialPhone) { mutableStateOf(initialPhone.filter(Char::isDigit)) }
    val filtrados = remember(clients, busqueda) {
        clients.filter { it.active }.filter {
            busqueda.isBlank() ||
                it.name.contains(busqueda, ignoreCase = true) ||
                it.phone.contains(busqueda, ignoreCase = true) ||
                it.document.contains(busqueda, ignoreCase = true)
        }
    }
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = Color(0xFF16A34A),
        unfocusedBorderColor = Color(0xFFCBD5E1),
        focusedLabelColor = Color(0xFF15803D),
        unfocusedLabelColor = Color(0xFF64748B),
        cursorColor = Color(0xFF15803D),
    )
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .widthIn(max = 520.dp)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            contentColor = Color(0xFF172033),
            shadowElevation = 18.dp,
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Enviar por WhatsApp", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Seleccione un cliente o ingrese un celular.", color = Color(0xFF64748B), style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color(0xFF64748B))
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "CLIENTES",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    label = { Text("Buscar cliente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .heightIn(min = 100.dp, max = 220.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                ) {
                    if (filtrados.isEmpty()) {
                        Box(Modifier.fillMaxWidth().height(130.dp), contentAlignment = Alignment.Center) {
                            Text("No se encontraron clientes", color = Color(0xFF94A3B8))
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            items(filtrados, key = { it.id }) { c ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = c.phone.isNotBlank()) { onElegirCliente(c) }
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                ) {
                                    Text(
                                        c.name.ifBlank { "Cliente sin nombre" },
                                        color = Color(0xFF1E293B),
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                    )
                                    Text(
                                        c.phone.ifBlank { "Sin teléfono registrado" },
                                        color = if (c.phone.isNotBlank()) Color(0xFF64748B) else Color(0xFF94A3B8),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                HorizontalDivider(color = Color(0xFFF1F5F9))
                            }
                        }
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 14.dp), color = Color(0xFFE2E8F0))
                OutlinedTextField(
                    value = manual,
                    onValueChange = { manual = it },
                    label = { Text("O número de celular") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("987654321 o 51987654321") },
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors,
                )
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar", color = Color(0xFF475569)) }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { sanitizePhone51(manual)?.let(onEnviarTelefono) },
                        enabled = sanitizePhone51(manual) != null,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A), contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                    ) { Text("Abrir WhatsApp") }
                }
            }
        }
    }
}

@Composable
fun VistaPreviaReciboDialog(
    receipt: CompletedSaleReceipt,
    emitido: ComprobanteEmitidoResult,
    clienteNombre: String?,
    clienteDoc: String?,
    whatsappPhone: String,
    clients: List<ClientRow> = emptyList(),
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val (fecha, hora) = formatFechaHoraPeru(receipt.fechaMillis)
    val (customerName, customerDocument) = resolveReceiptCustomer(emitido, clienteNombre, clienteDoc)
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showWhatsappDestination by remember { mutableStateOf(false) }
    var whatsappBusy by remember { mutableStateOf(false) }
    var whatsappNotice by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    LaunchedEffect(emitido.qrPayload) {
        qrBitmap = encodeQrBitmap(emitido.qrPayload)
    }

    fun sendToWhatsapp(phone: String) {
        if (!context.hasValidatedInternet()) {
            whatsappNotice = "WhatsApp no está disponible sin conexión."
            showWhatsappDestination = false
            return
        }
        scope.launch {
            whatsappBusy = true
            whatsappNotice = ""
            runCatching {
                val pdf = withContext(Dispatchers.IO) {
                    createReceiptPdfForSharing(context, receipt, emitido, customerName, customerDocument, qrBitmap)
                }
                val link = withContext(Dispatchers.IO) {
                    ReceiptDeliveryApiDataSource(context).requestWhatsappLink(
                        emitido.numeroCompleto,
                        customerName,
                        pdf,
                    ).getOrThrow()
                }
                val phone51 = sanitizePhone51(phone) ?: error("Número de WhatsApp inválido.")
                openWhatsapp(
                    context,
                    phone51,
                    buildReceiptShareText(receipt, emitido, customerName, customerDocument, link.url),
                )
            }.onSuccess {
                whatsappNotice = "WhatsApp abierto con el comprobante."
                showWhatsappDestination = false
            }.onFailure {
                whatsappNotice = it.message ?: "No se pudo abrir WhatsApp. Verifique su conexión."
            }
            whatsappBusy = false
        }
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
                        Text("Cliente: ${customerName.ifBlank { "—" }}", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text("Doc.: ${customerDocument.ifBlank { "—" }}", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        HorizontalDivider(Modifier.padding(vertical = 6.dp))
                        Text(
                            "CANT  UND  DESCRIPCIÓN          PRECIO",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        receipt.lines.forEach { line ->
                            val desc = line.productName.take(28)
                            val puIgv = r2(line.unitPrice)
                            val totIgv = r2(line.lineTotal)
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
                        Spacer(Modifier.height(6.dp))
                        Text("CONDICIÓN DE PAGO: Contado", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        Text(
                            "PAGOS: ${mapTipoPagoEtiqueta(receipt.tipoPago)} S/ ${"%.2f".format(Locale.US, receipt.montoRecibido)}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                        )
                        Text(
                            "VUELTO: S/ ${"%.2f".format(Locale.US, receipt.vuelto)}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                        )
                        Text("Cajero: ${receipt.vendedorNombre}", fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                        qrBitmap?.let { bmp ->
                            Spacer(Modifier.height(8.dp))
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "QR",
                                modifier = Modifier.size(140.dp).align(Alignment.CenterHorizontally),
                            )
                        }
                        Text("Gracias por su compra / Vuelva pronto", fontFamily = FontFamily.Monospace, fontSize = 8.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    if (whatsappNotice.isNotBlank()) {
                        Text(
                            whatsappNotice,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (whatsappNotice.startsWith("WhatsApp abierto")) Color(0xFF15803D) else Color(0xFFB91C1C),
                        )
                    } else Spacer(Modifier.weight(1f))
                    OutlinedButton(onClick = onDismiss) { Text("Cancelar") }
                    OutlinedButton(
                        onClick = { showWhatsappDestination = true },
                        enabled = context.hasValidatedInternet() && !whatsappBusy,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_whatsapp),
                            contentDescription = "Enviar por WhatsApp",
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(if (whatsappBusy) "Preparando..." else "WhatsApp")
                    }
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
    if (showWhatsappDestination) {
        WhatsappDestinoDialog(
            clients = clients,
            initialPhone = whatsappPhone,
            onDismiss = { showWhatsappDestination = false },
            onEnviarTelefono = ::sendToWhatsapp,
            onElegirCliente = { sendToWhatsapp(it.phone) },
        )
    }
}
