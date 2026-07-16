package com.ecommerce.ecommerceposapp.domain.model.catalog

data class CategoryItem(
    val id: Long,
    val name: String,
    val active: Boolean = true,
)

data class SubcategoryItem(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val active: Boolean = true,
)

data class ProductItem(
    val id: Long,
    val categoryId: Long,
    val subcategoryId: Long = 0,
    val name: String,
    val price: Double,
    val stock: Double,
    val code: String = "",
    val imageUrl: String = "",
    val active: Boolean = true,
)
