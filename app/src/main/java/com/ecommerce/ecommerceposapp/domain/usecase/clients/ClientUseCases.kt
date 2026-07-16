package com.ecommerce.ecommerceposapp.domain.usecase.clients

import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository

class GetClientsUseCase(private val repository: ClientRepository) { operator fun invoke() = repository.listClients() }
class SaveClientUseCase(private val repository: ClientRepository) { operator fun invoke(row: ClientRow) = repository.upsertClient(row) }
class DeleteClientUseCase(private val repository: ClientRepository) { operator fun invoke(id: Long) = repository.deleteClient(id) }
