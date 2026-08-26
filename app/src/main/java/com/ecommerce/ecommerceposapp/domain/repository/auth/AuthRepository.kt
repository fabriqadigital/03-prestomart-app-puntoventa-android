package com.ecommerce.ecommerceposapp.domain.repository.auth

import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession

interface AuthRepository {
    fun login(email: String, password: String): Result<UserSession>
    fun loginOffline(email: String, password: String): Result<UserSession>
    fun canLoginOffline(email: String): Boolean
    fun offlineLoginEmail(): String?
    fun getSession(): UserSession?
    fun enterOfflineMode(): UserSession? = getSession()
    fun resumeOnlineSession(): UserSession?
    /** Retorna true si existe un token JWT guardado (no implica que sea válido en el servidor). */
    fun hasStoredToken(): Boolean
    fun logout()
}
