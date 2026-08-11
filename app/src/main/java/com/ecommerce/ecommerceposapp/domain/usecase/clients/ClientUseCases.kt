package com.ecommerce.ecommerceposapp.domain.usecase.clients

import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository

class GetClientsUseCase(private val repository: ClientRepository) {
    operator fun invoke(page: Int = 1, perPage: Int = 20, search: String = "") =
        repository.listClientsPage(page, perPage, search)
}
class SaveClientUseCase(private val repository: ClientRepository) { operator fun invoke(row: ClientRow): Result<String> = repository.upsertClient(row) }
class DeleteClientUseCase(private val repository: ClientRepository) { operator fun invoke(id: Long) = repository.deleteClient(id) }
