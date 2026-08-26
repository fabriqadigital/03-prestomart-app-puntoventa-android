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
    val originalPrice: Double? = null,
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
    val currencyCode: String = "PEN",
    val exchangeRate: Double = 1.0,
    val totalAmountInCurrency: Double = 0.0,
)

object CurrencyFormatter {
    fun formatAmount(amount: Double, currencyCode: String): String {
        val normalized = currencyCode.uppercase()
        val value = java.math.BigDecimal.valueOf(amount).setScale(2, java.math.RoundingMode.HALF_UP)
        return if (normalized == "USD") {
            "$ ${value.toPlainString()}"
        } else {
            "S/ ${value.toPlainString()}"
        }
    }

    fun convertToCurrency(amount: Double, currencyCode: String, exchangeRate: Double): Double {
        if (exchangeRate <= 0.0) return amount
        return when (currencyCode.uppercase()) {
            "USD" -> java.math.BigDecimal.valueOf(amount)
                .divide(java.math.BigDecimal.valueOf(exchangeRate), 2, java.math.RoundingMode.HALF_UP)
                .toDouble()
            else -> java.math.BigDecimal.valueOf(amount).setScale(2, java.math.RoundingMode.HALF_UP).toDouble()
        }
    }

    fun convertToBaseCurrency(amount: Double, currencyCode: String, exchangeRate: Double): Double {
        if (currencyCode.uppercase() != "USD" || exchangeRate <= 0.0) return amount
        return java.math.BigDecimal.valueOf(amount)
            .multiply(java.math.BigDecimal.valueOf(exchangeRate))
            .setScale(2, java.math.RoundingMode.HALF_UP)
            .toDouble()
    }
}

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
    val currencyCode: String = "PEN",
    val exchangeRate: Double = 1.0,
    val totalAmountInCurrency: Double = 0.0,
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
    val currencyCode: String = "PEN",
    val exchangeRate: Double = 1.0,
    val totalAmountInCurrency: Double = 0.0,
)

data class SalesHistoryPage(
    val rows: List<SalesHistoryRow>,
    val total: Int,
    val page: Int,
    val perPage: Int,
)
