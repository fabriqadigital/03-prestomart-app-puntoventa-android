package com.ecommerce.ecommerceposapp.domain.repository.clients

import com.ecommerce.ecommerceposapp.domain.ClientRow

interface ClientRepository {
    fun listClients(): List<ClientRow>
    fun upsertClient(row: ClientRow): Result<Unit>
    fun deleteClient(id: Long): Result<Unit>
}
