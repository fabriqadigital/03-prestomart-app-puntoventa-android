package com.ecommerce.ecommerceposapp.domain.usecase.products

import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) { operator fun invoke() = repository.listProductsAdmin() }
class GetProductsPageUseCase(private val repository: ProductRepository) { operator fun invoke(page: Int, perPage: Int, search: String, categoryId: Long?, subcategoryId: Long?) = repository.listProductsAdminPage(page, perPage, search, categoryId, subcategoryId) }
class SaveProductUseCase(private val repository: ProductRepository) { operator fun invoke(row: ProductAdminRow) = repository.upsertProduct(row) }
class DeactivateProductUseCase(private val repository: ProductRepository) { operator fun invoke(id: Long) = repository.deleteProduct(id) }
