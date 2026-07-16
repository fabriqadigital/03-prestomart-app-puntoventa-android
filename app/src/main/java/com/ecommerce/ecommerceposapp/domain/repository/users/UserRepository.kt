package com.ecommerce.ecommerceposapp.domain.repository.users

import com.ecommerce.ecommerceposapp.domain.UserRow

interface UserRepository {
    fun listUsers(): List<UserRow>
    fun upsertUser(row: UserRow, plainPassword: String?): Result<Unit>
    fun deleteUser(id: Long, currentUserId: Long): Result<Unit>
}
