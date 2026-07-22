package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductTypeRow
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ProductApiDataSource(context: Context) {
    private val sessionStore = ApiSessionStore(context)
    private val urlResolver = ApiUrlResolver(sessionStore)
    private val httpClient = ApiHttpClient(context).client

    fun normalizedImageUrl(raw: String): String = urlResolver.normalizeAssetUrl(raw)

    fun listTypes(): Result<List<ProductTypeRow>> = runCatching {
        val request = Request.Builder()
            .url(urlResolver.endpoint(ApiConfig.PRODUCT_TYPE_LIST))
            .get()
            .header("Accept", "application/json")
            .build()
        httpClient.newCall(request).execute().use { response ->
            val payload = response.body?.string().orEmpty()
            val json = runCatching { JSONObject(payload) }.getOrNull()
            if (!response.isSuccessful || json?.optBoolean("success", true) == false) {
                throw Exception(json?.optString("message")?.takeIf { it.isNotBlank() }
                    ?: "No se pudieron cargar las etiquetas (${response.code}).")
            }
            val rows = json?.optJSONArray("result") ?: return@use emptyList()
            buildList {
                for (index in 0 until rows.length()) {
                    val item = rows.optJSONObject(index) ?: continue
                    val id = item.optLong("id_producto_tipo")
                    val name = item.optString("nombre").trim()
                    if (id > 0L && name.isNotBlank()) add(ProductTypeRow(id, name))
                }
            }
        }
    }

    fun save(row: ProductAdminRow): Result<Long> {
        if (sessionStore.token.isBlank()) return Result.failure(Exception("Inicia sesion en linea para guardar productos en produccion."))
        val path = if (row.id > 0L) ApiConfig.PRODUCT_UPDATE else ApiConfig.PRODUCT_CREATE
        return runCatching { postMultipart(urlResolver.endpoint(path), row) }
    }

    fun delete(id: Long): Result<Unit> {
        if (sessionStore.token.isBlank()) return Result.failure(Exception("Inicia sesion en linea para eliminar productos."))
        return runCatching {
            val payload = JSONObject().put("id", id).put("id_producto", id)
            val request = Request.Builder()
                .url(urlResolver.endpoint(ApiConfig.PRODUCT_DELETE))
                .delete(payload.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .header("Accept", "application/json")
                .build()
            httpClient.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                val json = runCatching { JSONObject(body) }.getOrNull()
                if (!response.isSuccessful || json?.optBoolean("success", true) == false) {
                    throw Exception(json?.optString("message")?.takeIf { it.isNotBlank() }
                        ?: "No se pudo eliminar el producto (${response.code}).")
                }
            }
        }
    }

    private fun postMultipart(url: String, row: ProductAdminRow): Long {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("nombre", row.name.trim())
            .addFormDataPart("descripcion", row.description.trim())
            .addFormDataPart("ubicacion", row.location.trim())
            .addFormDataPart("precio", row.price.toString())
            .addFormDataPart("precio_old", row.oldPrice.emptyIfZero())
            .addFormDataPart("precio_costo", row.costPrice.emptyIfZero())
            .addFormDataPart("precio_mayorista", row.wholesalePrice.emptyIfZero())
            .addFormDataPart("precio_mayorista_old", row.wholesaleOldPrice.emptyIfZero())
            .addFormDataPart("precio_yape", row.yapePrice.emptyIfZero())
            .addFormDataPart("stock", row.stock.toString())
            .addFormDataPart("stock_minimo", row.minimumStock.emptyIfZero())
            .addFormDataPart("id_producto_categoria", row.categoryId.toString())
            .apply {
                val ids = row.subcategoryIds.ifEmpty { listOfNotNull(row.subcategoryId.takeIf { it > 0L }) }
                if (ids.isEmpty()) addFormDataPart("id_producto_categoria_sub", "")
                else ids.distinct().forEach { addFormDataPart("id_producto_categoria_sub[]", it.toString()) }
            }
            .addFormDataPart("id_producto_tipo", row.productTypeId.takeIf { it > 0L }?.toString().orEmpty())
            .addFormDataPart("Activo", if (row.active) "S" else "N")
            .addFormDataPart("codigo_producto_new", row.code.trim())
            .addFormDataPart("codigo_barra", row.barcode.ifBlank { row.code }.trim())
            .addFormDataPart("canal_venta", row.salesChannel)
            .addFormDataPart("ratings_enabled", if (row.ratingsEnabled) "1" else "0")
            .addFormDataPart("admin_rating", row.adminRating.emptyIfZero())
            .addFormDataPart("numero_estrellas", row.adminRating.emptyIfZero())
            .addFormDataPart("peso_kilogramo", row.weightKg.emptyIfZero())
            .addFormDataPart("corte_tiempo_promocion", row.promoCutoffTime.trim())
            .addFormDataPart("corte_tiempo_sabado", row.saturdayCutoffTime.trim())
            .addFormDataPart("oferta_maxima_cantidad", row.offerMaxQuantity.emptyIfZero())
            .addFormDataPart("oferta_maxima_cantidad_por_precio", row.offerMaxQuantityPrice.emptyIfZero())
            .addFormDataPart("paquete_medidas", row.packageMeasures.trim())
            .addFormDataPart("paquete_dimencion", row.packageDimension.trim())
            .addFormDataPart("meta_titulo_producto", row.metaTitle.trim())
            .addFormDataPart("meta_descripcion_producto", row.metaDescription.trim())
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
            return row.id.takeIf { it > 0L }
                ?: json?.optJSONObject("result")?.optLong("id_producto")?.takeIf { it > 0L }
                ?: throw Exception("El backend guardo el producto pero no devolvio su identificador.")
        }
    }

    private fun Double.emptyIfZero(): String = if (this == 0.0) "" else toString()
}
