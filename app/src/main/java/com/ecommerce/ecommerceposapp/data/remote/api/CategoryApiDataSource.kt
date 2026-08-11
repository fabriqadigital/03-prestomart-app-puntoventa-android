package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import java.net.URLEncoder

class CategoryApiDataSource(context: Context) {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client = ApiHttpClient(context).client
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    fun listCategoriesPage(page: Int, perPage: Int, search: String): Result<ServerPage<CategoryAdminRow>> = authenticated {
        val url = resolver.endpoint(ApiConfig.CATEGORY_LIST) +
            "?page=${page.coerceAtLeast(1)}&per_page=$perPage&search=${URLEncoder.encode(search.trim(), "UTF-8")}"
        val request = Request.Builder().url(url).get().build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val root = runCatching { JSONObject(body) }.getOrElse { throw Exception("Respuesta invalida del backend de categorias (${response.code}).") }
            if (!response.isSuccessful || !root.optBoolean("success", true)) throw Exception(root.optString("message", "No se pudo listar categorias."))
            val rawResult = root.opt("result")
            val result = rawResult as? JSONObject
            val data = result?.optJSONArray("data") ?: (rawResult as? JSONArray) ?: JSONArray()
            val allRows = (0 until data.length()).mapNotNull { index ->
                val item = data.optJSONObject(index) ?: return@mapNotNull null
                val id = item.optLong("id_producto_categoria", item.optLong("id"))
                if (id <= 0L) null else CategoryAdminRow(
                    id = id,
                    name = item.optString("nombre", item.optString("name")),
                    active = !item.optString("Activo", "S").equals("N", true),
                )
            }
            if (result != null) {
                ServerPage(allRows, result.optInt("total", allRows.size), result.optInt("current_page", page), result.optInt("per_page", perPage))
            } else {
                val query = search.trim().lowercase()
                val filtered = allRows.filter { query.isBlank() || it.name.lowercase().contains(query) }
                val start = ((page.coerceAtLeast(1) - 1) * perPage).coerceAtMost(filtered.size)
                ServerPage(filtered.drop(start).take(perPage), filtered.size, page, perPage)
            }
        }
    }

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
