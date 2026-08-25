package com.ecommerce.ecommerceposapp.domain.model.products

import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductConversion

data class ProductTypeRow(
    val id: Long,
    val name: String,
)

data class ProductAdminRow(
    val id: Long,
    val categoryId: Long,
    val subcategoryId: Long = 0,
    val subcategoryIds: List<Long> = emptyList(),
    val name: String,
    val code: String,
    val barcode: String = "",
    val slug: String = "",
    val description: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val price: Double,
    val oldPrice: Double = 0.0,
    val costPrice: Double = 0.0,
    val wholesalePrice: Double = 0.0,
    val wholesaleOldPrice: Double = 0.0,
    val yapePrice: Double = 0.0,
    val stock: Double,
    val stockWeb: Double = 0.0,
    val minimumStock: Double = 0.0,
    val productTypeId: Long = 0L,
    val salesChannel: String = "ambos",
    val saleType: String = "UNIDAD",
    val ratingsEnabled: Boolean = false,
    val adminRating: Double = 0.0,
    val packageMeasures: String = "",
    val packageDimension: String = "",
    val weightKg: Double = 0.0,
    val promoCutoffTime: String = "",
    val saturdayCutoffTime: String = "",
    val offerMaxQuantity: Double = 0.0,
    val offerMaxQuantityPrice: Double = 0.0,
    val metaTitle: String = "",
    val metaDescription: String = "",
    val active: Boolean,
    val conversions: List<ProductConversion> = emptyList(),
    val syncState: String = "SYNCED",
)
