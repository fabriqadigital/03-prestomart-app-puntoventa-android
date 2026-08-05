package com.ecommerce.ecommerceposapp.data.printer

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import java.io.ByteArrayOutputStream
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class EscPosReceiptPrinter(private val context: Context) {
    fun print(
        receipt: CompletedSaleReceipt,
        issued: ComprobanteEmitidoResult,
        customerName: String,
        customerDocument: String,
    ): Result<String> = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) {
            error("Autorice Dispositivos cercanos para usar la ticketera.")
        }
        val adapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
            ?: error("Este dispositivo no tiene Bluetooth.")
        check(adapter.isEnabled) { "Active el Bluetooth del Zebra." }
        val paired = adapter.bondedDevices.orEmpty()
        val ranked = paired.map { device ->
            val name = device.name.orEmpty().uppercase(Locale.ROOT)
            val score = when {
                "E802" in name -> 4
                "POS" in name -> 3
                "PRINTER" in name -> 2
                "THERMAL" in name -> 1
                else -> 0
            }
            device to score
        }.sortedByDescending { it.second }
        val printer = ranked.firstOrNull { it.second > 0 }?.first
            ?: paired.singleOrNull()
            ?: error("Empareje únicamente la ticketera POS-E802 o asígnele un nombre que contenga E802/Printer.")

        adapter.cancelDiscovery()
        val socket = printer.createRfcommSocketToServiceRecord(SPP_UUID)
        try {
            socket.connect()
            socket.outputStream.use { output ->
                output.write(buildTicket(receipt, issued, customerName, customerDocument))
                output.flush()
                Thread.sleep(PRINT_DRAIN_DELAY_MS)
            }
        } finally {
            runCatching { socket.close() }
        }
        "Ticket enviado a ${printer.name ?: printer.address}."
    }

    private fun buildTicket(
        receipt: CompletedSaleReceipt,
        issued: ComprobanteEmitidoResult,
        customerName: String,
        customerDocument: String,
    ): ByteArray = ByteArrayOutputStream().apply {
        command(0x1B, 0x40) // Inicializar
        align(1)
        bold(true)
        line(issued.emisorRazonSocial.ifBlank { "EMISOR" })
        bold(false)
        line("RUC: ${issued.emisorRuc}")
        issued.emisorDireccion.takeIf(String::isNotBlank)?.let { line(it) }
        separator()
        bold(true)
        line(receiptTitle(issued.tipoSunat))
        line(issued.numeroCompleto)
        bold(false)
        separator()
        align(0)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("America/Lima")
        }
        line("Emision: ${dateFormat.format(Date(receipt.fechaMillis))}")
        if (customerName.isNotBlank()) line("Cliente: $customerName")
        if (customerDocument.isNotBlank()) line("Documento: $customerDocument")
        line("Cajero: ${receipt.vendedorNombre}")
        separator()
        receipt.lines.forEach { item ->
            line(item.productName)
            val left = "${item.quantity} x S/ ${money(item.unitPrice)}"
            line(columns(left, "S/ ${money(item.lineTotal)}"))
        }
        separator()
        line(columns("OP. GRAVADAS", "S/ ${money(receipt.subtotal)}"))
        line(columns("IGV (18%)", "S/ ${money(receipt.igv)}"))
        bold(true)
        doubleSize(true)
        line(columns("TOTAL", "S/ ${money(receipt.total)}", 24))
        doubleSize(false)
        bold(false)
        line("Pago: ${paymentLabel(receipt.tipoPago)}")
        line(columns("Recibido", "S/ ${money(receipt.montoRecibido)}"))
        line(columns("Vuelto", "S/ ${money(receipt.vuelto)}"))
        issued.totalLetras.takeIf(String::isNotBlank)?.let { line(it) }
        issued.qrPayload.takeIf(String::isNotBlank)?.let {
            align(1)
            qr(it)
        }
        line("")
        bold(true)
        line("Gracias por su compra / Vuelva pronto")
        bold(false)
        command(0x1B, 0x64, 0x05)
        command(0x1D, 0x56, 0x42, 0x00)
    }.toByteArray()

    private fun ByteArrayOutputStream.line(value: String) {
        write(ascii(value).toByteArray(Charsets.US_ASCII))
        write('\n'.code)
    }

    private fun ByteArrayOutputStream.separator() = line("-".repeat(COLUMNS))
    private fun ByteArrayOutputStream.align(value: Int) = command(0x1B, 0x61, value)
    private fun ByteArrayOutputStream.bold(enabled: Boolean) = command(0x1B, 0x45, if (enabled) 1 else 0)
    private fun ByteArrayOutputStream.doubleSize(enabled: Boolean) = command(0x1D, 0x21, if (enabled) 0x11 else 0)
    private fun ByteArrayOutputStream.command(vararg values: Int) = write(values.map(Int::toByte).toByteArray())

    private fun ByteArrayOutputStream.qr(payload: String) {
        val data = payload.toByteArray(Charsets.UTF_8)
        command(0x1D, 0x28, 0x6B, 0x04, 0x00, 0x31, 0x41, 0x32, 0x00)
        command(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, 0x06)
        command(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x32)
        val length = data.size + 3
        command(0x1D, 0x28, 0x6B, length and 0xFF, (length shr 8) and 0xFF, 0x31, 0x50, 0x30)
        write(data)
        command(0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30)
    }

    private fun columns(left: String, right: String, width: Int = COLUMNS): String {
        val safeRight = ascii(right).take(width)
        val safeLeft = ascii(left).take((width - safeRight.length - 1).coerceAtLeast(0))
        return safeLeft + " ".repeat((width - safeLeft.length - safeRight.length).coerceAtLeast(1)) + safeRight
    }

    private fun ascii(value: String): String = Normalizer.normalize(value, Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("[^\\x20-\\x7E]"), "?")

    private fun money(value: Double): String = String.format(Locale.US, "%.2f", value)
    private fun paymentLabel(code: String): String = when (code) {
        "EFE" -> "EFECTIVO"
        "TAR" -> "TARJETA"
        "YAP" -> "YAPE"
        "PLN" -> "PLIN"
        else -> code
    }
    private fun receiptTitle(type: String): String = when (type) {
        "01" -> "FACTURA ELECTRONICA"
        "03" -> "BOLETA DE VENTA ELECTRONICA"
        else -> "TICKET DE VENTA"
    }

    private companion object {
        const val COLUMNS = 48
        const val PRINT_DRAIN_DELAY_MS = 1_200L
        val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}
