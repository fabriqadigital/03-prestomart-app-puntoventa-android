package com.ecommerce.ecommerceposapp.domain.repository.products

import com.ecommerce.ecommerceposapp.domain.ProductAdminRow

interface ProductRepository {
    fun listProductsAdmin(): List<ProductAdminRow>
    fun upsertProduct(row: ProductAdminRow): Result<Unit>
    fun deleteProduct(id: Long): Result<Unit>
}
