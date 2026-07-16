package com.ecommerce.ecommerceposapp.data.repository.users

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.users.UserRealm
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.repository.users.UserRepository
import java.security.MessageDigest
import io.realm.Case

class UserRepositoryImpl(context: Context) : UserRepository {
    private val db = RealmDataSource(context)
    override fun listUsers() = db.query { realm -> realm.where(UserRealm::class.java).equalTo("active", true).findAll().map { UserRow(it.id, it.email, it.name, it.role, it.active) } }
    override fun upsertUser(row: UserRow, plainPassword: String?): Result<Unit> {
        if (row.email.isBlank() || row.name.isBlank()) return Result.failure(Exception("Nombre y correo son obligatorios."))
        val taken = db.query { realm -> realm.where(UserRealm::class.java).equalTo("email", row.email, Case.INSENSITIVE).findFirst()?.let { it.id != row.id } == true }
        if (taken) return Result.failure(Exception("Ya existe otro usuario con ese correo."))
        val existing = db.query { realm -> realm.where(UserRealm::class.java).equalTo("id", row.id).findFirst()?.password }
        val password = when { !plainPassword.isNullOrBlank() -> hash(plainPassword); existing != null -> existing; else -> return Result.failure(Exception("Indique contrasena para el usuario nuevo.")) }
        db.write { realm -> val id = if (row.id == 0L) db.nextId(realm, UserRealm::class.java) else row.id; realm.insertOrUpdate(UserRealm().apply { this.id = id; email = row.email.trim(); name = row.name.trim(); role = row.role.ifBlank { "admin" }; active = row.active; this.password = password }) }
        return Result.success(Unit)
    }
    override fun deleteUser(id: Long, currentUserId: Long): Result<Unit> {
        if (id == currentUserId) return Result.failure(Exception("No puede eliminar el usuario de la sesion actual."))
        db.write { realm -> realm.where(UserRealm::class.java).equalTo("id", id).findFirst()?.active = false }
        return Result.success(Unit)
    }
    private fun hash(value: String): String = MessageDigest.getInstance("SHA-256").digest(value.toByteArray()).joinToString("") { "%02x".format(it) }
}
