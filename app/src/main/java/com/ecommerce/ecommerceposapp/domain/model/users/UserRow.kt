package com.ecommerce.ecommerceposapp.domain.model.users

data class UserRow(
    val id: Long,
    val email: String,
    val name: String,
    val role: String,
    val active: Boolean,
)
