package com.ecommerce.ecommerceposapp.domain.repository.auth

import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession

interface AuthRepository {
    fun login(email: String, password: String): Result<UserSession>
    fun loginOffline(email: String, password: String): Result<UserSession>
    fun canLoginOffline(email: String): Boolean
    fun getSession(): UserSession?
    fun logout()
}
