package com.ecommerce.ecommerceposapp.domain.usecase.suppliers

import com.ecommerce.ecommerceposapp.domain.SupplierRow
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository

class GetSuppliersUseCase(private val repository: SupplierRepository) { operator fun invoke() = repository.listSuppliers() }
class SaveSupplierUseCase(private val repository: SupplierRepository) { operator fun invoke(row: SupplierRow) = repository.upsertSupplier(row) }
class DeleteSupplierUseCase(private val repository: SupplierRepository) { operator fun invoke(id: Long) = repository.deleteSupplier(id) }
