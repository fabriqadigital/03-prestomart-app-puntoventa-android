package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject

class ProductApiDataSource(context: Context) {
    private val sessionStore = ApiSessionStore(context)
    private val urlResolver = ApiUrlResolver(sessionStore)
    private val httpClient = ApiHttpClient(context).client

    fun normalizedImageUrl(raw: String): String = urlResolver.normalizeAssetUrl(raw)

    fun save(row: ProductAdminRow): Result<Unit> {
        if (sessionStore.token.isBlank()) return Result.failure(Exception("Inicia sesion en linea para guardar productos en produccion."))
        val path = if (row.id > 0L) ApiConfig.PRODUCT_UPDATE else ApiConfig.PRODUCT_CREATE
        return runCatching { postMultipart(urlResolver.endpoint(path), row) }
    }

    private fun postMultipart(url: String, row: ProductAdminRow) {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("nombre", row.name.trim())
            .addFormDataPart("precio", row.price.toString())
            .addFormDataPart("stock", row.stock.toString())
            .addFormDataPart("id_producto_categoria", row.categoryId.toString())
            .addFormDataPart("id_producto_categoria_sub", row.subcategoryId.takeIf { it > 0L }?.toString() ?: "")
            .addFormDataPart("Activo", if (row.active) "S" else "N")
            .addFormDataPart("codigo_producto_new", row.code.trim())
            .addFormDataPart("codigo_barra", row.code.trim())
            .addFormDataPart("canal_venta", "ambos")
            .apply {
                if (row.id > 0L) addFormDataPart("id_producto", row.id.toString())
                row.imageUrl.removePrefix("file://").let(::File)
                    .takeIf { row.imageUrl.startsWith("file://") && it.exists() && it.length() > 0L }
                    ?.let { image ->
                        val mime = when (image.extension.lowercase()) {
                            "png" -> "image/png"
                            "webp" -> "image/webp"
                            "gif" -> "image/gif"
                            else -> "image/jpeg"
                        }.toMediaType()
                        addFormDataPart("pro_imagen", image.name, image.asRequestBody(mime))
                    }
            }
            .build()

        val request = Request.Builder()
            .url(url)
            .post(body)
            .header("Accept", "application/json")
            .build()

        httpClient.newCall(request).execute().use { response ->
            val payload = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw Exception("No se pudo guardar en produccion (${response.code}): ${payload.take(180)}")
            val json = runCatching { JSONObject(payload) }.getOrNull()
            if (json?.optBoolean("success", true) == false) {
                throw Exception(json.optString("message", "No se pudo guardar en produccion."))
            }
        }
    }
}
