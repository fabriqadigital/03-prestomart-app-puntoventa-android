package com.ecommerce.ecommerceposapp.domain.repository.suppliers

import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage

interface SupplierRepository {
    fun listSuppliers(): List<SupplierRow>
    fun listSuppliersPage(page: Int, perPage: Int, search: String, status: String?): ServerPage<SupplierRow>
    fun upsertSupplier(row: SupplierRow): Result<Unit>
    fun deleteSupplier(id: Long): Result<Unit>
}
