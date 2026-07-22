package com.ecommerce.ecommerceposapp.domain.repository.products

import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductTypeRow

interface ProductRepository {
    fun listProductsAdmin(): List<ProductAdminRow>
    fun listProductTypes(): Result<List<ProductTypeRow>>
    fun upsertProduct(row: ProductAdminRow): Result<Unit>
    fun deleteProduct(id: Long): Result<Unit>
    fun syncPendingProducts(): Result<Int>
}
