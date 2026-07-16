package com.ecommerce.ecommerceposapp.data.repository.clients

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.clients.ClientRealm
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository

class ClientRepositoryImpl(context: Context) : ClientRepository {
    private val db = RealmDataSource(context)
    override fun listClients() = db.query { realm -> realm.where(ClientRealm::class.java).equalTo("active", true).findAll().map { ClientRow(it.id, it.name, it.document, it.phone, it.active) } }
    override fun upsertClient(row: ClientRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        db.write { realm -> val id = if (row.id == 0L) db.nextId(realm, ClientRealm::class.java) else row.id; realm.insertOrUpdate(ClientRealm().apply { this.id = id; name = row.name.trim(); document = row.document.trim(); phone = row.phone.trim(); active = row.active }) }
        return Result.success(Unit)
    }
    override fun deleteClient(id: Long): Result<Unit> { db.write { realm -> realm.where(ClientRealm::class.java).equalTo("id", id).findFirst()?.active = false }; return Result.success(Unit) }
}
