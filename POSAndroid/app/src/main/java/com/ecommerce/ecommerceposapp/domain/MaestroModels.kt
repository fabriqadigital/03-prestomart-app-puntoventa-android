package com.ecommerce.ecommerceposapp.domain

data class UserRow(
    val id: Long,
    val email: String,
    val name: String,
    val role: String,
    val active: Boolean,
)

data class ClientRow(
    val id: Long,
    val name: String,
    val document: String,
    val phone: String,
    val active: Boolean = true,
)

data class SupplierRow(
    val id: Long,
    val businessName: String,
    val ruc: String,
    val phone: String,
    val active: Boolean = true,
)

data class ProductAdminRow(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val code: String,
    val imageUrl: String = "",
    val price: Double,
    val stock: Double,
    val active: Boolean,
)

data class CategoryAdminRow(
    val id: Long,
    val name: String,
    val active: Boolean,
)
