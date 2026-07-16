package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import org.json.JSONObject

data class ApiSession(
    val baseUrl: String,
    val hostHeader: String?,
    val token: String,
    val userId: Long,
    val name: String,
)

class AuthApiDataSource(context: Context) {
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)
    private val candidates = listOf(ApiBaseCandidate(ApiConfig.PRODUCTION_BASE_URL))

    fun login(email: String, password: String): ApiSession? {
        val preferredBase = prefs.getString("api_base_url", null)
        val preferredHost = prefs.getString("api_host_header", null)?.takeIf { it.isNotBlank() }
        val preferred = preferredBase?.let { ApiBaseCandidate(it, preferredHost) }
        val bases = listOfNotNull(preferred) + candidates.filter { it.baseUrl != preferredBase }
        for (base in bases) {
            val loginUrl = "${base.baseUrl}/api${ApiConfig.LOGIN}"
            val fromJson = runCatching { tryLoginJson(loginUrl, base, email, password) }.getOrNull()
            if (fromJson != null) return fromJson
            val fromForm = runCatching { tryLoginForm(loginUrl, base, email, password) }.getOrNull()
            if (fromForm != null) return fromForm
        }
        return null
    }

    private fun tryLoginJson(url: String, base: ApiBaseCandidate, email: String, password: String): ApiSession? {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 7000
            readTimeout = 7000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
        }
        val body = JSONObject().apply {
            put("email", email)
            put("password", password)
        }.toString()
        OutputStreamWriter(conn.outputStream).use { it.write(body) }
        if (conn.responseCode !in 200..299) return null
        val payload = conn.inputStream.bufferedReader().use { it.readText() }
        return parseLoginPayload(base, payload)
    }

    private fun tryLoginForm(url: String, base: ApiBaseCandidate, email: String, password: String): ApiSession? {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 7000
            readTimeout = 7000
            doOutput = true
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
        }
        val body = "email=${URLEncoder.encode(email, "UTF-8")}&password=${URLEncoder.encode(password, "UTF-8")}"
        OutputStreamWriter(conn.outputStream).use { it.write(body) }
        if (conn.responseCode !in 200..299) return null
        val payload = conn.inputStream.bufferedReader().use { it.readText() }
        return parseLoginPayload(base, payload)
    }

    private fun parseLoginPayload(base: ApiBaseCandidate, payload: String): ApiSession? {
        val obj = runCatching { JSONObject(payload) }.getOrNull() ?: return null
        val token = obj.optString("token", obj.optString("access_token", obj.optString("jwt", ""))).trim()
        if (token.isBlank()) return null
        val userObj = obj.optJSONObject("user") ?: obj.optJSONObject("usuario")
        val userId = userObj?.optLong("id", 1L) ?: 1L
        val name = userObj?.optString("name", userObj.optString("nombre", "admin")) ?: "admin"
        return ApiSession(baseUrl = base.baseUrl, hostHeader = base.hostHeader, token = token, userId = userId, name = name)
    }

    private data class ApiBaseCandidate(val baseUrl: String, val hostHeader: String? = null)
}
