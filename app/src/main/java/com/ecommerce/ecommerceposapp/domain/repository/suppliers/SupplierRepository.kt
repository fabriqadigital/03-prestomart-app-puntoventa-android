package com.ecommerce.ecommerceposapp.domain.repository.suppliers

import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow

interface SupplierRepository {
    fun listSuppliers(): List<SupplierRow>
    fun upsertSupplier(row: SupplierRow): Result<Unit>
    fun deleteSupplier(id: Long): Result<Unit>
}
