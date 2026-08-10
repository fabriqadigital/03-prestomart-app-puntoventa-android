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

data class ProductConversion(
    val id: Long,
    val name: String,
    val code: String = "",
    val stockFactor: Double,
    val finalPrice: Double,
    val active: Boolean = true,
    val order: Int = 0,
)

data class ProductItem(
    val id: Long,
    val categoryId: Long,
    val subcategoryId: Long = 0,
    val name: String,
    val price: Double,
    val stock: Double,
    val code: String = "",
    val barcode: String = "",
    val imageUrl: String = "",
    val salesChannel: String = "ambos",
    val featuredInPos: Boolean = false,
    val active: Boolean = true,
    val conversions: List<ProductConversion> = emptyList(),
)
