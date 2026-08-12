package com.ecommerce.ecommerceposapp.data.repository.suppliers

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.suppliers.SupplierRealm
import com.ecommerce.ecommerceposapp.data.local.sync.OutboxRealm
import com.ecommerce.ecommerceposapp.data.remote.api.SupplierApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository
import java.io.IOException
import java.util.UUID
import org.json.JSONObject

class SupplierRepositoryImpl(context: Context) : SupplierRepository {
    private val db = RealmDataSource(context)
    private val api = SupplierApiDataSource(context)

    override fun listSuppliers(): List<SupplierRow> {
        return runCatching { api.list() }
            .onSuccess { rows -> cacheLocally(rows) }
            .getOrElse { readLocalCache() }
    }

    override fun listSuppliersPage(page: Int, perPage: Int, search: String, status: String?): ServerPage<SupplierRow> {
        return runCatching { api.listPage(page, perPage, search, status) }
            .onSuccess { remote -> remote.rows.forEach(::cacheRow) }
            .getOrElse {
                val filtered = readLocalCache().filter { row ->
                    (status.isNullOrBlank() || row.estado == status) &&
                    (search.isBlank() || listOf(row.businessName, row.ruc, row.codigoProveedor, row.correo, row.phone)
                        .any { value -> value.contains(search, ignoreCase = true) }
                    )
                }
                val start = (page - 1).coerceAtLeast(0) * perPage
                ServerPage(filtered.drop(start).take(perPage), filtered.size, page, perPage)
            }
    }

    override fun upsertSupplier(row: SupplierRow): Result<Unit> {
        if (row.businessName.isBlank()) return Result.failure(Exception("Razon social obligatoria."))
        if (row.ruc.isBlank()) return Result.failure(Exception("RUC obligatorio."))
        val remote = api.save(row)
        if (remote.isSuccess) {
            runCatching { api.list() }.onSuccess(::cacheLocally)
            return remote
        }
        val error = remote.exceptionOrNull()
        if (!error.canQueueOffline()) return Result.failure(error ?: Exception("No se pudo guardar el proveedor."))
        val now = System.currentTimeMillis()
        val localId = row.id.takeIf { it != 0L } ?: -(now + (0..999).random())
        val local = row.copy(id = localId)
        db.write { realm ->
            realm.insertOrUpdate(local.toRealm())
            realm.where(OutboxRealm::class.java)
                .equalTo("operation", "UPSERT_SUPPLIER")
                .equalTo("aggregateLocalId", localId)
                .findAll()
                .deleteAllFromRealm()
            realm.insert(OutboxRealm().apply {
                id = UUID.randomUUID().toString()
                moduleKey = "proveedores"
                operation = "UPSERT_SUPPLIER"
                aggregateType = "supplier"
                aggregateLocalId = localId
                payloadJson = local.toJson().toString()
                createdAt = now
                updatedAt = now
                state = "PENDING"
            })
        }
        return Result.success(Unit)
    }

    override fun deleteSupplier(id: Long): Result<Unit> {
        if (id < 0L) {
            db.write { realm ->
                realm.where(OutboxRealm::class.java)
                    .equalTo("operation", "UPSERT_SUPPLIER")
                    .equalTo("aggregateLocalId", id)
                    .findAll()
                    .deleteAllFromRealm()
                realm.where(SupplierRealm::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
            }
            return Result.success(Unit)
        }
        val remote = api.delete(id)
        if (remote.isSuccess) {
            db.write { realm -> realm.where(SupplierRealm::class.java).equalTo("id", id).findFirst()?.deleteFromRealm() }
            return remote
        }
        val error = remote.exceptionOrNull()
        if (!error.canQueueOffline()) return Result.failure(error ?: Exception("No se pudo eliminar el proveedor."))
        val now = System.currentTimeMillis()
        db.write { realm ->
            realm.where(SupplierRealm::class.java).equalTo("id", id).findFirst()?.active = false
            realm.insert(OutboxRealm().apply {
                this.id = UUID.randomUUID().toString()
                moduleKey = "proveedores"
                operation = "DELETE_SUPPLIER"
                aggregateType = "supplier"
                aggregateLocalId = id
                payloadJson = JSONObject().put("id", id).toString()
                createdAt = now
                updatedAt = now
                state = "PENDING"
            })
        }
        return Result.success(Unit)
    }

    fun remoteRows(): List<SupplierRow> = api.list()

    fun pushRemote(row: SupplierRow): Result<Unit> = api.save(row)

    fun deleteRemote(id: Long): Result<Unit> = api.delete(id)

    private fun cacheLocally(rows: List<SupplierRow>) {
        db.write { realm ->
            val pendingIds = realm.where(OutboxRealm::class.java)
                .equalTo("moduleKey", "proveedores")
                .findAll()
                .map { it.aggregateLocalId }
                .toSet()
            realm.where(SupplierRealm::class.java).findAll()
                .filter { it.id !in pendingIds }
                .forEach { it.deleteFromRealm() }
            rows.forEach { row -> if (row.id !in pendingIds) realm.insertOrUpdate(row.toRealm()) }
        }
    }

    private fun cacheRow(row: SupplierRow) {
        db.write { realm -> realm.insertOrUpdate(row.toRealm()) }
    }

    private fun readLocalCache(): List<SupplierRow> = db.query { realm ->
        realm.where(SupplierRealm::class.java).equalTo("active", true).findAll()
            .map { it.toRow() }
            .sortedWith(compareByDescending<SupplierRow> { it.id < 0L }.thenBy { it.id })
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

    private fun SupplierRow.toJson() = JSONObject()
        .put("codigo_proveedor", codigoProveedor)
        .put("business_name", businessName)
        .put("ruc", ruc)
        .put("email", correo)
        .put("phone", phone)
        .put("address", direccion)
        .put("contact_name", personaContacto)
        .put("contact_role", cargoContacto)
        .put("contact_phone", telefonoContacto)
        .put("contact_email", correoContacto)
        .put("rating", calificacion)
        .put("status", estado)
        .put("registration_date", fechaRegistro)
        .put("observations", observaciones)
        .put("bank", banco)
        .put("account", cuenta)
        .put("cci", cci)
        .put("active", active)

    private fun Throwable?.canQueueOffline(): Boolean {
        var current = this
        while (current != null) {
            if (current is IOException) return true
            current = current.cause
        }
        return this?.message?.contains("sesion en linea", ignoreCase = true) == true ||
            this?.message?.contains("sesión en línea", ignoreCase = true) == true
    }
}
