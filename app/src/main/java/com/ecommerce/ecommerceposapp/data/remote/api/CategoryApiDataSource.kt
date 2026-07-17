package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CategoryApiDataSource(context: Context) {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client = ApiHttpClient(context).client
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    fun saveCategory(row: CategoryAdminRow): Result<Long> = authenticated {
        val payload = JSONObject().apply {
            if (row.id > 0L) put("id", row.id)
            put("nombre", row.name.trim())
            put("Activo", if (row.active) "S" else "N")
        }
        executeWrite(
            path = if (row.id > 0L) ApiConfig.CATEGORY_UPDATE else ApiConfig.CATEGORY_CREATE,
            method = if (row.id > 0L) "PUT" else "POST",
            payload = payload,
        ).let { response ->
            row.id.takeIf { it > 0L }
                ?: response.optJSONObject("result")?.optLong("id_producto_categoria")?.takeIf { it > 0L }
                ?: throw Exception("El backend no devolvio el ID de la categoria creada.")
        }
    }

    fun saveSubcategory(row: SubcategoryAdminRow): Result<Long> = authenticated {
        val payload = JSONObject().apply {
            if (row.id > 0L) put("id", row.id)
            put("id_producto_categoria", row.categoryId)
            put("nombre", row.name.trim())
            put("Activo", if (row.active) "S" else "N")
        }
        executeWrite(
            path = if (row.id > 0L) ApiConfig.SUBCATEGORY_UPDATE else ApiConfig.SUBCATEGORY_CREATE,
            method = if (row.id > 0L) "PUT" else "POST",
            payload = payload,
        ).let { response ->
            row.id.takeIf { it > 0L }
                ?: response.optJSONObject("result")?.optLong("id_producto_categoria_sub")?.takeIf { it > 0L }
                ?: throw Exception("El backend no devolvio el ID de la subcategoria creada.")
        }
    }

    fun deleteCategory(id: Long): Result<Unit> = delete(ApiConfig.CATEGORY_DELETE, id)
    fun deleteSubcategory(id: Long): Result<Unit> = delete(ApiConfig.SUBCATEGORY_DELETE, id)

    private fun delete(path: String, id: Long): Result<Unit> = authenticated {
        executeWrite(path, "DELETE", JSONObject().put("id", id))
        Unit
    }

    private fun executeWrite(path: String, method: String, payload: JSONObject): JSONObject {
        val request = Request.Builder()
            .url(resolver.endpoint(path))
            .method(method, payload.toString().toRequestBody(jsonType))
            .build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val json = runCatching { JSONObject(body) }.getOrElse {
                throw Exception("Respuesta invalida del backend de categorias (${response.code}).")
            }
            if (!response.isSuccessful || !json.optBoolean("success", true)) {
                throw Exception(json.optString("message", "No se pudo completar la operacion."))
            }
            return json
        }
    }

    private fun <T> authenticated(block: () -> T): Result<T> {
        if (session.token.isBlank()) {
            return Result.failure(Exception("Inicia sesion en linea para modificar categorias del backend."))
        }
        return runCatching(block)
    }
}
