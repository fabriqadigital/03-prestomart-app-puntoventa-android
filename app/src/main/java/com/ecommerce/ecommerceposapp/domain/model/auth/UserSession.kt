package com.ecommerce.ecommerceposapp.domain.model.auth

data class UserSession(
    val id: Long,
    val email: String,
    val name: String,
    val role: String,
    val offlineSession: Boolean,
)
