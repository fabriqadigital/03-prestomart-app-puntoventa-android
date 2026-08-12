package com.ecommerce.ecommerceposapp.domain.usecase.users

import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.repository.users.UserRepository

class GetUsersUseCase(private val repository: UserRepository) { operator fun invoke() = repository.listUsers() }
class GetUsersPageUseCase(private val repository: UserRepository) { operator fun invoke(page: Int, perPage: Int, search: String) = repository.listUsersPage(page, perPage, search) }
class SaveUserUseCase(private val repository: UserRepository) { operator fun invoke(row: UserRow, password: String?) = repository.upsertUser(row, password) }
class DeleteUserUseCase(private val repository: UserRepository) { operator fun invoke(id: Long, currentUserId: Long) = repository.deleteUser(id, currentUserId) }
