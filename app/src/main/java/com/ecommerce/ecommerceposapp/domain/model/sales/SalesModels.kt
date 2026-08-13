package com.ecommerce.ecommerceposapp.domain.model.sales

import java.math.BigDecimal
import kotlin.math.roundToInt

data class CartLine(
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Double,
    val conversionId: Long? = null,
    val conversionName: String = "",
    val stockFactor: Double = 1.0,
    val saleType: String = "UNIDAD",
) {
    val lineTotal: Double get() = unitPrice * quantity
    val lineKey: String get() = "$productId:${conversionId ?: 0L}"
    val displayName: String get() {
        val conversion = conversionName.trim()
        if (conversion.isBlank() || productName.trim().endsWith(conversion, ignoreCase = true)) return productName.trim()
        return "${productName.trim()} $conversion"
    }
    val isBulk: Boolean get() = saleType.equals("A_GRANEL", ignoreCase = true)
    val quantityText: String
        get() = if (isBulk) {
            BigDecimal.valueOf(quantity).stripTrailingZeros().toPlainString()
        } else {
            quantity.roundToInt().toString()
        }
    val quantityUnit: String get() = if (isBulk) "kg" else "und"
}

data class SalePaymentInfo(
    val tipoPago: String,
    val montoRecibido: Double,
    val vuelto: Double,
    val aplicarRedondeo: Boolean = false,
)

data class CompletedSaleReceipt(
    val ventaId: Long,
    val numeroTicket: String,
    val subtotal: Double,
    val igv: Double,
    val total: Double,
    val tipoPago: String,
    val montoRecibido: Double,
    val vuelto: Double,
    val fechaMillis: Long,
    val lines: List<CartLine>,
    val vendedorNombre: String,
    val idCliente: Long,
    val clienteNombre: String = "",
    val clienteDocumento: String = "",
    val emisorRuc: String = "",
    val emisorRazonSocial: String = "",
    val emisorDireccion: String = "",
    val descuento: Double = 0.0,
    val descuentoPorcentaje: Double = 0.0,
    /** lineKeys de las líneas del carrito que llevan el descuento activo (por línea). */
    val descuentoLineKeys: Set<String> = emptySet(),
)

data class ReceiptCustomerInfo(
    val id: Long = 0L,
    val name: String = "",
    val document: String = "",
)

enum class TipoComprobanteEmision {
    BOLETA,
    FACTURA,
    SOLO_TICKET,
}

data class ComprobanteEmitidoResult(
    val tipoSunat: String,
    val numeroCompleto: String,
    val serie: String,
    val correlativo: Int,
    val qrPayload: String,
    val emisorRuc: String,
    val emisorRazonSocial: String,
    val emisorDireccion: String,
    val totalLetras: String,
    val receptorNombre: String = "",
    val receptorDocumento: String = "",
)

data class SalesHistoryRow(
    val ventaId: Long,
    val numeroComprobante: String,
    val tipoComprobante: String,
    val fechaMillis: Long,
    val clienteNombre: String,
    val cajeroNombre: String,
    val tipoPago: String,
    val total: Double,
    val estado: String,
    val idCliente: Long,
    val cancellationStatus: String = "",
)

data class SalesHistoryPage(
    val rows: List<SalesHistoryRow>,
    val total: Int,
    val page: Int,
    val perPage: Int,
)
