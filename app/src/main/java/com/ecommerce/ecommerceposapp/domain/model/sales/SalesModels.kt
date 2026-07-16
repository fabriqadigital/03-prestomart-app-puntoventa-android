package com.ecommerce.ecommerceposapp.domain.model.sales

data class CartLine(
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
) {
    val lineTotal: Double get() = unitPrice * quantity
}

data class SalePaymentInfo(
    val tipoPago: String,
    val montoRecibido: Double,
    val vuelto: Double,
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
)
