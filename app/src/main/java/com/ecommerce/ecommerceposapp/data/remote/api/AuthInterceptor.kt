package com.ecommerce.ecommerceposapp.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionStore: ApiSessionStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionStore.token
        val request = if (token.isBlank()) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }
        return chain.proceed(request)
    }
}
