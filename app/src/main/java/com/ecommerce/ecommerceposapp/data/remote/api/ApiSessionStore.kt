package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.BuildConfig

class ApiSessionStore(context: Context) {
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)

    init {
        val savedBaseUrl = prefs.getString("api_base_url", null)?.trim()?.trimEnd('/')
        val activeBaseUrl = ApiConfig.DEFAULT_BASE_URL
        val editor = prefs.edit()
        if (!savedBaseUrl.isNullOrBlank() && !savedBaseUrl.equals(activeBaseUrl, ignoreCase = true)) {
            editor.clear()
        }
        editor
            .putString("api_base_url", activeBaseUrl)
            .putString("api_host_header", BuildConfig.API_HOST_HEADER)
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
