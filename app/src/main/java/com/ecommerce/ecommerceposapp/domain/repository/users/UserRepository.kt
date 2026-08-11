package com.ecommerce.ecommerceposapp.domain.repository.users

import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage

interface UserRepository {
    fun listUsers(): List<UserRow>
    fun listUsersPage(page: Int, perPage: Int, search: String): ServerPage<UserRow>
    fun upsertUser(row: UserRow, plainPassword: String?): Result<Unit>
    fun deleteUser(id: Long, currentUserId: Long): Result<Unit>
}
