package com.ecommerce.ecommerceposapp.domain.model.products

data class ProductAdminRow(
    val id: Long,
    val categoryId: Long,
    val subcategoryId: Long = 0,
    val name: String,
    val code: String,
    val imageUrl: String = "",
    val price: Double,
    val stock: Double,
    val active: Boolean,
)
