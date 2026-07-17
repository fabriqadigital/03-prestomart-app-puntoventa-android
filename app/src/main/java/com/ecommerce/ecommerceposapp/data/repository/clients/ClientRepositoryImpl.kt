package com.ecommerce.ecommerceposapp.data.repository.clients

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.clients.ClientRealm
import com.ecommerce.ecommerceposapp.data.remote.api.ClientApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository

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

    override fun upsertClient(row: ClientRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        if (row.email.isBlank()) return Result.failure(Exception("Correo obligatorio."))
        if (row.id == 0L && row.newPassword.isBlank()) return Result.failure(Exception("Contrasena obligatoria para un usuario nuevo."))
        return api.save(row).onSuccess {
            api.list().onSuccess { rows ->
                replaceCache(rows)
                prefs.edit().putBoolean(REAL_CLIENT_CACHE_READY, true).apply()
            }
        }
    }

    override fun deleteClient(id: Long): Result<Unit> {
        return api.delete(id).onSuccess {
            api.list().onSuccess(::replaceCache)
            db.write { realm -> realm.where(ClientRealm::class.java).equalTo("id", id).findFirst()?.active = false }
        }
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
            )
        }
    }

    private fun replaceCache(rows: List<ClientRow>) {
        db.write { realm ->
            realm.where(ClientRealm::class.java).findAll().deleteAllFromRealm()
            rows.forEach { row ->
                realm.insertOrUpdate(ClientRealm().apply {
                    id = row.id
                    name = row.name
                    document = row.document
                    phone = row.phone
                    lastName = row.lastName
                    email = row.email
                    address = row.address
                    businessName = row.businessName
                    branchName = row.branchName
                    active = row.active
                })
            }
        }
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
