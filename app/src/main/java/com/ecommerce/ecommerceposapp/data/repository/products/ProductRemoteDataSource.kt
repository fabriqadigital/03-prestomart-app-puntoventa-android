package com.ecommerce.ecommerceposapp.data.repository.products

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.ProductAdminRow
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

class ProductRemoteDataSource(context: Context) {
    private val prefs = context.getSharedPreferences("pos_prefs", Context.MODE_PRIVATE)
    private val productionBaseUrl = "https://prestomartperu.com"

    fun normalizedImageUrl(raw: String): String {
        val input = raw.trim()
        if (input.isBlank() || input.startsWith("file://")) return input
        val base = prefs.getString("api_base_url", null)?.trim().takeUnless { it.isNullOrBlank() } ?: productionBaseUrl
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
            } else fullUrl
        }.getOrDefault(fullUrl)
    }

    fun save(row: ProductAdminRow): Result<Unit> {
        val token = prefs.getString("api_token", "")?.trim().orEmpty()
        if (token.isBlank()) return Result.failure(Exception("Inicia sesion en linea para guardar productos en produccion."))
        val base = prefs.getString("api_base_url", null)?.trim().takeUnless { it.isNullOrBlank() } ?: productionBaseUrl
        return runCatching {
            postMultipart("$base${if (row.id > 0L) "/api/producto/actualizar" else "/api/producto/crear"}", row)
        }
    }

    private fun postMultipart(url: String, row: ProductAdminRow) {
        val boundary = "----PrestoMartPos${System.currentTimeMillis()}"
        val end = "\r\n"
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 12000
            readTimeout = 20000
            doInput = true
            doOutput = true
            useCaches = false
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            setRequestProperty("Authorization", "Bearer ${prefs.getString("api_token", "")?.trim().orEmpty()}")
            prefs.getString("api_host_header", "")?.trim()?.takeIf { it.isNotBlank() }?.let { setRequestProperty("Host", it) }
        }
        DataOutputStream(conn.outputStream).use { out ->
            fun field(name: String, value: String?) {
                out.writeBytes("--$boundary$end")
                out.writeBytes("Content-Disposition: form-data; name=\"$name\"$end$end")
                out.writeBytes((value ?: "") + end)
            }
            field("nombre", row.name.trim())
            field("precio", row.price.toString())
            field("stock", row.stock.toString())
            field("id_producto_categoria", row.categoryId.toString())
            field("id_producto_categoria_sub", row.subcategoryId.takeIf { it > 0L }?.toString() ?: "")
            field("Activo", if (row.active) "S" else "N")
            field("codigo_producto_new", row.code.trim())
            field("codigo_barra", row.code.trim())
            field("canal_venta", "ambos")
            if (row.id > 0L) field("id_producto", row.id.toString())
            row.imageUrl.removePrefix("file://").let(::File)
                .takeIf { row.imageUrl.startsWith("file://") && it.exists() && it.length() > 0L }
                ?.let { image ->
                    val mime = when (image.extension.lowercase()) {
                        "png" -> "image/png"
                        "webp" -> "image/webp"
                        "gif" -> "image/gif"
                        else -> "image/jpeg"
                    }
                    out.writeBytes("--$boundary$end")
                    out.writeBytes("Content-Disposition: form-data; name=\"pro_imagen\"; filename=\"${image.name}\"$end")
                    out.writeBytes("Content-Type: $mime$end$end")
                    image.inputStream().use { it.copyTo(out) }
                    out.writeBytes(end)
                }
            out.writeBytes("--$boundary--$end")
        }
        val code = conn.responseCode
        val payload = if (code in 200..299) {
            conn.inputStream.bufferedReader().use { it.readText() }
        } else {
            conn.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        }
        if (code !in 200..299) throw Exception("No se pudo guardar en produccion ($code): ${payload.take(180)}")
        val response = runCatching { JSONObject(payload) }.getOrNull()
        if (response?.optBoolean("success", true) == false) {
            throw Exception(response.optString("message", "No se pudo guardar en produccion."))
        }
    }
}
