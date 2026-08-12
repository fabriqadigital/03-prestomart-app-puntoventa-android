package com.ecommerce.ecommerceposapp.domain.model.cash

data class CashRegister(
    val id: Long,
    val code: String,
    val name: String,
    val branch: String,
    val active: Boolean,
    val ruc: String = "",
    val businessName: String = "",
    val address: String = "",
)

data class CashSession(
    val id: Long,
    val cashRegisterId: Long,
    val cashRegisterName: String,
    val cashierId: Long,
    val cashierName: String,
    val openedAt: Long,
    val openingAmount: Double,
    val status: String,
)

data class CashSummary(
    val openingAmount: Double,
    val totalSales: Double,
    val salesCount: Int = 0,
    val cashAmount: Double,
    val deposit: Double,
    val expectedCash: Double,
    val totalFlow: Double,
    val income: Double,
    val expenses: Double,
    val cardAmount: Double = 0.0,
    val yapeAmount: Double = 0.0,
    val plinAmount: Double = 0.0,
    val transferAmount: Double = 0.0,
    val otherAmount: Double = 0.0,
)

data class CashFlowItem(
    val flujoId: String,
    val fecha: Long,
    val razonSocial: String,
    val sucursal: String,
    val cajaNombre: String,
    val cajeroNombre: String,
    val tipoMovimiento: String,
    val tipoTransaccion: String,
    val origen: String,
    val tipoPago: String,
    val comentario: String,
    val importe: Double,
)
