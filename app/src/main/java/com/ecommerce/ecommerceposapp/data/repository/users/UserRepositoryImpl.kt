package com.ecommerce.ecommerceposapp.data.repository.users

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.users.UserRealm
import com.ecommerce.ecommerceposapp.data.remote.api.UserApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage
import com.ecommerce.ecommerceposapp.domain.repository.users.UserRepository

class UserRepositoryImpl(context: Context) : UserRepository {
    private val db = RealmDataSource(context)
    private val api = UserApiDataSource(context)
    private val prefs = context.getSharedPreferences("pos_prefs", Context.MODE_PRIVATE)

    override fun listUsers(): List<UserRow> = api.list().fold(
        onSuccess = { rows ->
            replaceCache(rows)
            prefs.edit().putBoolean(REMOTE_USERS_READY, true).apply()
            cachedUsers()
        },
        onFailure = { error ->
            if (prefs.getBoolean(REMOTE_USERS_READY, false)) cachedUsers() else throw error
        },
    )

    override fun listUsersPage(page: Int, perPage: Int, search: String): ServerPage<UserRow> =
        api.listPage(page, perPage, search).fold(
            onSuccess = { serverPage ->
                prefs.edit().putBoolean(REMOTE_USERS_READY, true).apply()
                serverPage
            },
            onFailure = { error ->
                if (!prefs.getBoolean(REMOTE_USERS_READY, false)) throw error
                val query = search.trim().lowercase()
                val filtered = cachedUsers().filter {
                    query.isBlank() || it.name.lowercase().contains(query) ||
                        it.email.lowercase().contains(query) || it.username.lowercase().contains(query)
                }
                val start = ((page.coerceAtLeast(1) - 1) * perPage).coerceAtMost(filtered.size)
                ServerPage(filtered.drop(start).take(perPage), filtered.size, page, perPage)
            },
        )

    override fun upsertUser(row: UserRow, plainPassword: String?): Result<Unit> {
        if (row.email.isBlank() || row.name.isBlank()) return Result.failure(Exception("Nombre y correo son obligatorios."))
        if (row.id == 0L && (plainPassword?.length ?: 0) < 8) return Result.failure(Exception("La contrasena debe tener al menos 8 caracteres."))
        return api.save(row, plainPassword).onSuccess { api.list().onSuccess(::replaceCache) }
    }

    override fun deleteUser(id: Long, currentUserId: Long): Result<Unit> {
        if (id == currentUserId) return Result.failure(Exception("No puede inactivar el usuario de la sesion actual."))
        return api.delete(id).onSuccess { api.list().onSuccess(::replaceCache) }
    }

    private fun cachedUsers(): List<UserRow> = db.query { realm ->
        realm.where(UserRealm::class.java).findAll().map { UserRow(it.id, it.email, it.name, it.role, it.active) }
    }

    private fun replaceCache(rows: List<UserRow>) {
        db.write { realm ->
            val passwords = realm.where(UserRealm::class.java).findAll().associate { it.id to it.password }
            realm.where(UserRealm::class.java).findAll().deleteAllFromRealm()
            rows.forEach { row ->
                realm.insertOrUpdate(UserRealm().apply {
                    id = row.id
                    email = row.email
                    name = row.name
                    role = row.role
                    active = row.active
                    password = passwords[row.id].orEmpty()
                })
            }
        }
    }

    private companion object {
        const val REMOTE_USERS_READY = "users_remote_cache_ready"
    }
}
