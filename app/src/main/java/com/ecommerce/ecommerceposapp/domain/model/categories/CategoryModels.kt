package com.ecommerce.ecommerceposapp.domain.model.categories

data class CategoryAdminRow(
    val id: Long,
    val name: String,
    val active: Boolean,
)

data class SubcategoryAdminRow(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val active: Boolean,
)
