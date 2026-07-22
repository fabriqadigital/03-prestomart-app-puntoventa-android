package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context

class ApiSessionStore(context: Context) {
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)

    init {
        val savedBaseUrl = prefs.getString("api_base_url", null)?.trim()?.trimEnd('/')
        val productionBaseUrl = ApiConfig.PRODUCTION_BASE_URL.trimEnd('/')
        val editor = prefs.edit()
        if (!savedBaseUrl.isNullOrBlank() && !savedBaseUrl.equals(productionBaseUrl, ignoreCase = true)) {
            editor.clear()
        }
        editor
            .putString("api_base_url", ApiConfig.PRODUCTION_BASE_URL)
            .putString("api_host_header", "")
            .apply()
    }

    val baseUrl: String
        get() = ApiConfig.configuredBaseUrl(prefs.getString("api_base_url", null))

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
        get() = ApiConfig.configuredHostHeader(prefs.getString("api_host_header", ""))
}
