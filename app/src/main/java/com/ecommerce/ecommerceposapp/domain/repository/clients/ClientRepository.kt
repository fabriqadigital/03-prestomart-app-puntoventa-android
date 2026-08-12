package com.ecommerce.ecommerceposapp.domain.repository.clients

import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage

interface ClientRepository {
    fun listClients(): List<ClientRow>
    fun listClientsPage(page: Int, perPage: Int, search: String): ServerPage<ClientRow>
    fun upsertClient(row: ClientRow): Result<String>
    fun deleteClient(id: Long): Result<Unit>
}
