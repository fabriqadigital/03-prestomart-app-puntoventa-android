package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context

class ApiSessionStore(context: Context) {
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)

    val baseUrl: String
        get() = prefs.getString("api_base_url", null)?.trim().takeUnless { it.isNullOrBlank() }
            ?: ApiConfig.PRODUCTION_BASE_URL

    val token: String
        get() = prefs.getString("api_token", "")?.trim().orEmpty()

    val refreshToken: String
        get() = prefs.getString("api_refresh_token", "")?.trim().orEmpty()

    fun updateTokens(token: String, refreshToken: String) {
        prefs.edit()
            .putString("api_token", token)
            .putString("api_refresh_token", refreshToken)
            .apply()
    }

    val hostHeader: String
        get() = prefs.getString("api_host_header", "")?.trim().orEmpty()
}
