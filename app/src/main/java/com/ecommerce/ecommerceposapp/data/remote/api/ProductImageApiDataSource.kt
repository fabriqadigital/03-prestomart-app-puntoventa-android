package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

sealed interface ProductImageDownloadResult {
    data object Downloaded : ProductImageDownloadResult
    data class Missing(val httpCode: Int) : ProductImageDownloadResult
    data class Failed(val reason: String) : ProductImageDownloadResult
}

class ProductImageApiDataSource(context: Context) {
    private val sessionStore = ApiSessionStore(context)

    fun download(sourceUrl: String, target: File): ProductImageDownloadResult {
        var lastReason = "Error de red"
        repeat(MAX_ATTEMPTS) { attempt ->
            val result = downloadOnce(sourceUrl, target)
            when (result) {
                ProductImageDownloadResult.Downloaded -> return result
                is ProductImageDownloadResult.Missing -> return result
                is ProductImageDownloadResult.Failed -> lastReason = result.reason
            }
            if (attempt < MAX_ATTEMPTS - 1) Thread.sleep(RETRY_DELAY_MS * (attempt + 1))
        }
        return ProductImageDownloadResult.Failed(lastReason)
    }

    private fun downloadOnce(sourceUrl: String, target: File): ProductImageDownloadResult {
        val partial = File(target.parentFile, "${target.name}.part")
        partial.delete()
        var connection: HttpURLConnection? = null
        return try {
            val safeUrl = sourceUrl.replace(" ", "%20")
            connection = (URL(safeUrl).openConnection() as HttpURLConnection).apply {
                connectTimeout = 10_000
                readTimeout = 20_000
                requestMethod = "GET"
                instanceFollowRedirects = true
                setRequestProperty("Accept", "image/*")
                sessionStore.token.takeIf { it.isNotBlank() }
                    ?.let { setRequestProperty("Authorization", "Bearer $it") }
                sessionStore.hostHeader.takeIf { it.isNotBlank() }
                    ?.let { setRequestProperty("Host", it) }
            }
            val code = connection.responseCode
            when {
                code == HttpURLConnection.HTTP_NOT_FOUND || code == HttpURLConnection.HTTP_GONE -> {
                    ProductImageDownloadResult.Missing(code)
                }
                code !in 200..299 -> ProductImageDownloadResult.Failed("HTTP $code")
                connection.contentType.orEmpty().let { type ->
                    type.isNotBlank() &&
                        !type.startsWith("image/", ignoreCase = true) &&
                        !type.startsWith("application/octet-stream", ignoreCase = true)
                } -> {
                    ProductImageDownloadResult.Failed("El servidor no devolvió una imagen")
                }
                else -> {
                    connection.inputStream.use { input ->
                        partial.outputStream().use { output -> input.copyTo(output) }
                    }
                    if (partial.length() <= 0L) {
                        ProductImageDownloadResult.Failed("Archivo vacío")
                    } else {
                        if (target.exists()) target.delete()
                        if (!partial.renameTo(target)) {
                            partial.copyTo(target, overwrite = true)
                            partial.delete()
                        }
                        ProductImageDownloadResult.Downloaded
                    }
                }
            }
        } catch (error: Exception) {
            partial.delete()
            ProductImageDownloadResult.Failed(error.message ?: error.javaClass.simpleName)
        } finally {
            connection?.disconnect()
        }
    }

    private companion object {
        const val MAX_ATTEMPTS = 3
        const val RETRY_DELAY_MS = 400L
    }
}
