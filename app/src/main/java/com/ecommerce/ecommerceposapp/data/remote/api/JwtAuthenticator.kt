package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class JwtAuthenticator(context: Context) : Authenticator {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val refreshClient = OkHttpClient()
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.responseCount() >= 2) return null
        val failedToken = response.request.header("Authorization")?.removePrefix("Bearer ").orEmpty()

        synchronized(refreshLock) {
            val currentToken = session.token
            if (currentToken.isNotBlank() && currentToken != failedToken) {
                return response.request.newBuilder().header("Authorization", "Bearer $currentToken").build()
            }

            val refreshToken = session.refreshToken
            if (refreshToken.isBlank()) return null
            val payload = JSONObject().put("refreshToken", refreshToken)
            val builder = Request.Builder()
                .url(resolver.endpoint(ApiConfig.AUTH_REFRESH))
                .post(payload.toString().toRequestBody(jsonType))
                .header("Accept", "application/json")
            session.hostHeader.takeIf { it.isNotBlank() }?.let { builder.header("Host", it) }

            return runCatching {
                refreshClient.newCall(builder.build()).execute().use { refreshResponse ->
                    if (!refreshResponse.isSuccessful) return null
                    val json = JSONObject(refreshResponse.body?.string().orEmpty())
                    val newToken = json.optString("token").ifBlank { json.optJSONObject("result")?.optString("token").orEmpty() }
                    val newRefresh = json.optString("refreshToken").ifBlank {
                        json.optJSONObject("result")?.optString("refreshToken").orEmpty()
                    }
                    if (newToken.isBlank() || newRefresh.isBlank()) return null
                    session.updateTokens(newToken, newRefresh)
                    response.request.newBuilder().header("Authorization", "Bearer $newToken").build()
                }
            }.getOrNull()
        }
    }

    private fun Response.responseCount(): Int {
        var count = 1
        var prior = priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

    private companion object {
        val refreshLock = Any()
    }
}
