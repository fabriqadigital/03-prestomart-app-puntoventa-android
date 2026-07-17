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
)
