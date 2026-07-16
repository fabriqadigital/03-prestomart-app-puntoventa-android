package com.ecommerce.ecommerceposapp.domain

data class UserSession(
    val id: Long,
    val email: String,
    val name: String,
    val role: String,
    val offlineSession: Boolean,
)

data class CategoryItem(
    val id: Long,
    val name: String,
    val active: Boolean = true,
)

data class SubcategoryItem(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val active: Boolean = true,
)

data class ProductItem(
    val id: Long,
    val categoryId: Long,
    val subcategoryId: Long = 0,
    val name: String,
    val price: Double,
    val stock: Double,
    val code: String = "",
    val imageUrl: String = "",
    val active: Boolean = true,
)

data class CartLine(
    val productId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
) {
    val lineTotal: Double get() = unitPrice * quantity
}

/** Datos de cobro al registrar en `finanza_ventas` (POS). */
data class SalePaymentInfo(
    val tipoPago: String,
    val montoRecibido: Double,
    val vuelto: Double,
)

/** Resultado de registrar la venta (para vista previa y comprobantes). */
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

/** Cabecera emitida o equivalente para ticket solo. */
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

data class SyncModuleStatus(
    val key: String,
    val label: String,
    val lastSyncAt: Long,
)
