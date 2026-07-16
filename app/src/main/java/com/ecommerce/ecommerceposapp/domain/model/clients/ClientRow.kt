package com.ecommerce.ecommerceposapp.domain.model.clients

data class ClientRow(
    val id: Long,
    val name: String,
    val document: String,
    val phone: String,
    val active: Boolean = true,
)
