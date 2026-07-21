package com.ecommerce.ecommerceposapp.domain.model.cash

data class CashRegister(
    val id: Long,
    val code: String,
    val name: String,
    val branch: String,
    val active: Boolean,
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
    val cashAmount: Double,
    val deposit: Double,
    val expectedCash: Double,
    val totalFlow: Double,
    val income: Double,
    val expenses: Double,
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
