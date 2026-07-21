package com.ecommerce.ecommerceposapp.domain.model.clients

data class ClientRow(
    val id: Long,
    val name: String,
    val document: String,
    val phone: String,
    val active: Boolean = true,
    val lastName: String = "",
    val email: String = "",
    val address: String = "",
    val businessName: String = "",
    val newPassword: String = "",
    val branchName: String = "",
    val userId: Long = 0L,
    val personType: String = "Natural",
    val documentType: String = "DNI",
    val alias: String = "",
    val gender: String = "",
    val maritalStatus: String = "",
    val discountPercentage: Double = 0.0,
    val observations: String = "",
    val webAccess: Boolean = false,
)
