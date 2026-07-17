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
    val totalSales: Double,
    val expectedCash: Double,
    val income: Double,
    val expenses: Double,
)
