package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class ProductImageApiDataSource(context: Context) {
    private val sessionStore = ApiSessionStore(context)

    fun download(sourceUrl: String, target: File): Boolean {
        return runCatching {
            val conn = (URL(sourceUrl).openConnection() as HttpURLConnection).apply {
                connectTimeout = 8000
                readTimeout = 12000
                requestMethod = "GET"
                sessionStore.token.takeIf { it.isNotBlank() }?.let { setRequestProperty("Authorization", "Bearer $it") }
                sessionStore.hostHeader.takeIf { it.isNotBlank() }?.let { setRequestProperty("Host", it) }
            }
            conn.inputStream.use { input ->
                target.outputStream().use { output -> input.copyTo(output) }
            }
            target.length() > 0L
        }.getOrDefault(false)
    }
}
