package com.ecommerce.ecommerceposapp.data.repository.suppliers

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.suppliers.SupplierRealm
import com.ecommerce.ecommerceposapp.data.remote.api.SupplierApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository

class SupplierRepositoryImpl(context: Context) : SupplierRepository {
    private val db = RealmDataSource(context)
    private val api = SupplierApiDataSource(context)

    override fun listSuppliers(): List<SupplierRow> {
        return runCatching { api.list() }
            .onSuccess { rows -> cacheLocally(rows) }
            .getOrElse { readLocalCache() }
    }

    override fun upsertSupplier(row: SupplierRow): Result<Unit> {
        if (row.businessName.isBlank()) return Result.failure(Exception("Razon social obligatoria."))
        if (row.ruc.isBlank()) return Result.failure(Exception("RUC obligatorio."))
        return api.save(row).onSuccess { if (row.id > 0L) cacheRow(row) }
    }

    override fun deleteSupplier(id: Long): Result<Unit> {
        return api.delete(id).onSuccess {
            db.write { realm ->
                realm.where(SupplierRealm::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
            }
        }
    }

    private fun cacheLocally(rows: List<SupplierRow>) {
        db.write { realm ->
            realm.where(SupplierRealm::class.java).findAll().deleteAllFromRealm()
            rows.forEach { row -> realm.insertOrUpdate(row.toRealm()) }
        }
    }

    private fun cacheRow(row: SupplierRow) {
        db.write { realm -> realm.insertOrUpdate(row.toRealm()) }
    }

    private fun readLocalCache(): List<SupplierRow> = db.query { realm ->
        realm.where(SupplierRealm::class.java).equalTo("active", true).findAll().map { it.toRow() }
    }

    private fun SupplierRow.toRealm() = SupplierRealm().also { r ->
        r.id = id; r.codigoProveedor = codigoProveedor; r.businessName = businessName; r.ruc = ruc
        r.correo = correo; r.phone = phone; r.direccion = direccion; r.personaContacto = personaContacto
        r.cargoContacto = cargoContacto; r.telefonoContacto = telefonoContacto; r.correoContacto = correoContacto
        r.calificacion = calificacion; r.estado = estado; r.fechaRegistro = fechaRegistro
        r.observaciones = observaciones; r.banco = banco; r.cuenta = cuenta; r.cci = cci
        r.active = estado == "Activo"
    }

    private fun SupplierRealm.toRow() = SupplierRow(
        id = id, codigoProveedor = codigoProveedor, businessName = businessName, ruc = ruc, correo = correo,
        phone = phone, direccion = direccion, personaContacto = personaContacto, cargoContacto = cargoContacto,
        telefonoContacto = telefonoContacto, correoContacto = correoContacto, calificacion = calificacion,
        estado = estado, fechaRegistro = fechaRegistro, observaciones = observaciones, banco = banco,
        cuenta = cuenta, cci = cci, active = active,
    )
}
