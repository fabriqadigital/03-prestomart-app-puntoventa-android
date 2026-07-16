package com.ecommerce.ecommerceposapp.data.repository.suppliers

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.suppliers.SupplierRealm
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.SupplierRow
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository

class SupplierRepositoryImpl(context: Context) : SupplierRepository {
    private val db = RealmDataSource(context)
    override fun listSuppliers() = db.query { realm -> realm.where(SupplierRealm::class.java).equalTo("active", true).findAll().map { SupplierRow(it.id, it.businessName, it.ruc, it.phone, it.active) } }
    override fun upsertSupplier(row: SupplierRow): Result<Unit> {
        if (row.businessName.isBlank()) return Result.failure(Exception("Razon social obligatoria."))
        db.write { realm -> val id = if (row.id == 0L) db.nextId(realm, SupplierRealm::class.java) else row.id; realm.insertOrUpdate(SupplierRealm().apply { this.id = id; businessName = row.businessName.trim(); ruc = row.ruc.trim(); phone = row.phone.trim(); active = row.active }) }
        return Result.success(Unit)
    }
    override fun deleteSupplier(id: Long): Result<Unit> { db.write { realm -> realm.where(SupplierRealm::class.java).equalTo("id", id).findFirst()?.active = false }; return Result.success(Unit) }
}
