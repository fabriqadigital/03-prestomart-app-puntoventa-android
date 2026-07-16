package com.ecommerce.ecommerceposapp.domain.repository.auth

import com.ecommerce.ecommerceposapp.domain.UserSession

enum class LoginMode { OfflineOnly, OnlineOnly }

interface AuthRepository {
    fun login(email: String, password: String, mode: LoginMode): Result<UserSession>
    fun getSession(): UserSession?
    fun logout()
    fun canUseOfflineLogin(): Boolean
}
