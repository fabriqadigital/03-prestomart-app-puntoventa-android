package com.ecommerce.ecommerceposapp.domain.usecase.suppliers

import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository

class GetSuppliersUseCase(private val repository: SupplierRepository) {
    operator fun invoke(page: Int = 1, perPage: Int = 20, search: String = "", status: String? = null) =
        repository.listSuppliersPage(page, perPage, search, status)
}
class SaveSupplierUseCase(private val repository: SupplierRepository) { operator fun invoke(row: SupplierRow) = repository.upsertSupplier(row) }
class DeleteSupplierUseCase(private val repository: SupplierRepository) { operator fun invoke(id: Long) = repository.deleteSupplier(id) }
