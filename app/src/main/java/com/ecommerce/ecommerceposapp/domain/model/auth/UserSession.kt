package com.ecommerce.ecommerceposapp.domain.model.auth

data class UserSession(
    val id: Long,
    val email: String,
    val name: String,
    val role: String,
    val offlineSession: Boolean,
    val cashierId: Long = 0L,
    val defaultCashRegisterId: Long = 0L,
    val defaultCashRegisterName: String = "",
    val document: String = "",
    val phone: String = "",
    val address: String = "",
    val branchName: String = "",
    val lastName: String = "",
    val documentType: String = "DNI",
    val cashierState: String = "Activo",
)
