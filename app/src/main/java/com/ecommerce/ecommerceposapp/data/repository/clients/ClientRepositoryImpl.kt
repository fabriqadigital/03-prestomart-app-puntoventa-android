package com.ecommerce.ecommerceposapp.data.repository.clients

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.clients.ClientRealm
import com.ecommerce.ecommerceposapp.data.local.sync.OutboxRealm
import com.ecommerce.ecommerceposapp.data.remote.api.ClientApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository
import java.io.IOException
import java.util.UUID
import org.json.JSONObject

class ClientRepositoryImpl(context: Context) : ClientRepository {
    private val db = RealmDataSource(context)
    private val api = ClientApiDataSource(context)
    private val prefs = context.getSharedPreferences("pos_prefs", Context.MODE_PRIVATE)

    override fun listClients(): List<ClientRow> {
        return api.list().fold(
            onSuccess = { rows ->
                replaceCache(rows)
                prefs.edit().putBoolean(REAL_CLIENT_CACHE_READY, true).apply()
                cachedClients()
            },
            onFailure = {
                if (prefs.getBoolean(REAL_CLIENT_CACHE_READY, false)) cachedClients()
                else {
                    clearLegacyCache()
                    throw it
                }
            },
        )
    }

    override fun listClientsPage(page: Int, perPage: Int, search: String): ServerPage<ClientRow> {
        return api.listPage(page, perPage, search).fold(
            onSuccess = { remote ->
                cacheRows(remote.rows)
                prefs.edit().putBoolean(REAL_CLIENT_CACHE_READY, true).apply()
                mergePendingClients(remote, page, search)
            },
            onFailure = { error ->
                if (!prefs.getBoolean(REAL_CLIENT_CACHE_READY, false)) throw error
                val filtered = cachedClients().filter { row ->
                    search.isBlank() || listOf(row.name, row.lastName, row.businessName, row.email, row.document, row.phone)
                        .any { it.contains(search, ignoreCase = true) }
                }
                val start = (page - 1).coerceAtLeast(0) * perPage
                ServerPage(filtered.drop(start).take(perPage), filtered.size, page, perPage)
            },
        )
    }

    override fun upsertClient(row: ClientRow): Result<String> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        if (row.document.isBlank()) return Result.failure(Exception("Numero de documento obligatorio."))
        if (row.webAccess && row.email.isBlank()) return Result.failure(Exception("Correo obligatorio para activar el acceso web."))
        val remote = api.save(row)
        if (remote.isSuccess) {
            api.list().onSuccess { rows ->
                replaceCache(rows)
                prefs.edit().putBoolean(REAL_CLIENT_CACHE_READY, true).apply()
            }
            return remote
        }
        val error = remote.exceptionOrNull()
        if (!error.canQueueOffline()) return Result.failure(error ?: Exception("No se pudo guardar el cliente."))

        val now = System.currentTimeMillis()
        val localId = row.id.takeIf { it != 0L } ?: -(now + (0..999).random())
        val local = row.copy(id = localId)
        db.write { realm ->
            realm.insertOrUpdate(local.toRealm())
            realm.where(OutboxRealm::class.java)
                .equalTo("operation", "UPSERT_CLIENT")
                .equalTo("aggregateLocalId", localId)
                .findAll()
                .deleteAllFromRealm()
            realm.insert(OutboxRealm().apply {
                id = UUID.randomUUID().toString()
                moduleKey = "clientes"
                operation = "UPSERT_CLIENT"
                aggregateType = "client"
                aggregateLocalId = localId
                payloadJson = local.toJson().toString()
                createdAt = now
                updatedAt = now
                state = "PENDING"
            })
        }
        prefs.edit().putBoolean(REAL_CLIENT_CACHE_READY, true).apply()
        return Result.success("Cliente guardado localmente. Pendiente de sincronizacion.")
    }

    override fun deleteClient(id: Long): Result<Unit> {
        if (id < 0L) {
            db.write { realm ->
                realm.where(OutboxRealm::class.java)
                    .equalTo("operation", "UPSERT_CLIENT")
                    .equalTo("aggregateLocalId", id)
                    .findAll()
                    .deleteAllFromRealm()
                realm.where(ClientRealm::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
            }
            return Result.success(Unit)
        }
        val remote = api.delete(id)
        if (remote.isSuccess) {
            db.write { realm -> realm.where(ClientRealm::class.java).equalTo("id", id).findFirst()?.active = false }
            return remote
        }
        val error = remote.exceptionOrNull()
        if (!error.canQueueOffline()) return Result.failure(error ?: Exception("No se pudo eliminar el cliente."))
        val now = System.currentTimeMillis()
        db.write { realm ->
            realm.where(ClientRealm::class.java).equalTo("id", id).findFirst()?.active = false
            realm.insert(OutboxRealm().apply {
                this.id = UUID.randomUUID().toString()
                moduleKey = "clientes"
                operation = "DELETE_CLIENT"
                aggregateType = "client"
                aggregateLocalId = id
                payloadJson = JSONObject().put("id", id).toString()
                createdAt = now
                updatedAt = now
                state = "PENDING"
            })
        }
        return Result.success(Unit)
    }

    // El servidor no conoce los clientes creados offline (siguen en el outbox
    // hasta que se sincronizan), asi que se anteponen en la pagina 1 para que no
    // "desaparezcan" de la lista mientras esten pendientes.
    private fun mergePendingClients(remote: ServerPage<ClientRow>, page: Int, search: String): ServerPage<ClientRow> {
        if (page != 1) return remote
        val existingIds = remote.rows.map { it.id }.toSet()
        val pending = cachedClients().filter { row ->
            row.id < 0L && row.id !in existingIds &&
                (search.isBlank() || listOf(row.name, row.lastName, row.businessName, row.email, row.document, row.phone)
                    .any { it.contains(search, ignoreCase = true) })
        }
        if (pending.isEmpty()) return remote
        return remote.copy(rows = pending + remote.rows, total = remote.total + pending.size)
    }

    private fun cachedClients() = db.query { realm ->
        realm.where(ClientRealm::class.java).equalTo("active", true).findAll().map {
            ClientRow(
                id = it.id,
                name = it.name.cleanNullableText(),
                document = it.document.cleanNullableText(),
                phone = it.phone.cleanNullableText(),
                active = it.active,
                lastName = it.lastName.cleanNullableText(),
                email = it.email.cleanNullableText(),
                address = it.address.cleanNullableText(),
                businessName = it.businessName.cleanNullableText(),
                branchName = it.branchName.cleanNullableText(),
                userId = it.userId,
                personType = it.personType.cleanNullableText().ifBlank { "Natural" },
                documentType = it.documentType.cleanNullableText().ifBlank { "DNI" },
                alias = it.alias.cleanNullableText(),
                gender = it.gender.cleanNullableText(),
                maritalStatus = it.maritalStatus.cleanNullableText(),
                discountPercentage = it.discountPercentage,
                observations = it.observations.cleanNullableText(),
                webAccess = it.webAccess,
            )
        }.sortedWith(compareByDescending<ClientRow> { it.id < 0L }.thenBy { it.id })
    }

    private fun replaceCache(rows: List<ClientRow>) {
        db.write { realm ->
            val pendingIds = realm.where(OutboxRealm::class.java)
                .equalTo("moduleKey", "clientes")
                .findAll()
                .map { it.aggregateLocalId }
                .toSet()
            realm.where(ClientRealm::class.java).findAll()
                .filter { it.id !in pendingIds }
                .forEach { it.deleteFromRealm() }
            rows.forEach { row ->
                if (row.id !in pendingIds) realm.insertOrUpdate(row.toRealm())
            }
        }
    }

    private fun cacheRows(rows: List<ClientRow>) {
        db.write { realm -> rows.forEach { realm.insertOrUpdate(it.toRealm()) } }
    }

    private fun ClientRow.toRealm() = ClientRealm().also {
        it.id = id
        it.name = name
        it.document = document
        it.phone = phone
        it.lastName = lastName
        it.email = email
        it.address = address
        it.businessName = businessName
        it.branchName = branchName
        it.userId = userId
        it.personType = personType
        it.documentType = documentType
        it.alias = alias
        it.gender = gender
        it.maritalStatus = maritalStatus
        it.discountPercentage = discountPercentage
        it.observations = observations
        it.webAccess = webAccess
        it.active = active
    }

    private fun ClientRow.toJson() = JSONObject()
        .put("name", name)
        .put("document", document)
        .put("phone", phone)
        .put("active", active)
        .put("last_name", lastName)
        .put("email", email)
        .put("address", address)
        .put("business_name", businessName)
        .put("branch_name", branchName)
        .put("user_id", userId)
        .put("person_type", personType)
        .put("document_type", documentType)
        .put("alias", alias)
        .put("gender", gender)
        .put("marital_status", maritalStatus)
        .put("discount_percentage", discountPercentage)
        .put("observations", observations)
        .put("web_access", webAccess)

    private fun Throwable?.canQueueOffline(): Boolean {
        var current = this
        while (current != null) {
            if (current is IOException) return true
            current = current.cause
        }
        return this?.message?.contains("sesion en linea", ignoreCase = true) == true ||
            this?.message?.contains("sesión en línea", ignoreCase = true) == true
    }

    private fun clearLegacyCache() {
        db.write { realm -> realm.where(ClientRealm::class.java).findAll().deleteAllFromRealm() }
    }

    private fun String.cleanNullableText(): String =
        trim().takeUnless { it.equals("null", ignoreCase = true) } ?: ""

    private companion object {
        const val REAL_CLIENT_CACHE_READY = "clients_remote_cache_ready"
    }
}
