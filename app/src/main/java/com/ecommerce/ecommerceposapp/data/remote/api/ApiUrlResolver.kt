package com.ecommerce.ecommerceposapp.data.remote.api

import java.net.URL

class ApiUrlResolver(
    private val sessionStore: ApiSessionStore,
) {
    fun endpoint(path: String): String {
        val base = sessionStore.baseUrl.trimEnd('/')
        val apiBase = if (base.endsWith("/api")) base else "$base/api"
        return "$apiBase${path.ensureLeadingSlash()}"
    }

    fun normalizeAssetUrl(raw: String): String {
        val input = raw.trim()
        if (input.isBlank() || input.startsWith("file://")) return input
        val base = sessionStore.baseUrl
        val fullUrl = when {
            input.startsWith("http://") || input.startsWith("https://") -> input
            input.startsWith("/") -> "$base$input"
            else -> "$base/$input"
        }
        return runCatching {
            val parsed = URL(fullUrl)
            if (parsed.host == "localhost" || parsed.host == "127.0.0.1" || parsed.host.endsWith(".localhost")) {
                val port = if (parsed.port > 0) ":${parsed.port}" else ""
                "http://10.0.3.2$port${parsed.file}"
            } else {
                fullUrl
            }
        }.getOrDefault(fullUrl)
    }

    private fun String.ensureLeadingSlash(): String = if (startsWith("/")) this else "/$this"
}
