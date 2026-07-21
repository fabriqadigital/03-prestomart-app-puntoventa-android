package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class UserApiDataSource(context: Context) {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client = ApiHttpClient(context).client
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    fun list(): Result<List<UserRow>> = authenticated {
        val json = execute(Request.Builder().url(resolver.endpoint(ApiConfig.USER_LIST)).get().build())
        val rows = json.optJSONArray("result") ?: JSONArray()
        (0 until rows.length()).mapNotNull { index ->
            rows.optJSONObject(index)?.let { row ->
                val id = row.optLong("id")
                if (id <= 0L) null else UserRow(
                    id = id,
                    email = row.optString("email"),
                    name = row.optString("name").ifBlank { row.optString("email") },
                    role = row.optString("role").ifBlank { "Cliente web" },
                    active = row.optString("Activo", "S").equals("S", true) && row.optInt("is_blocked", 0) == 0,
                )
            }
        }
    }

    fun save(row: UserRow, password: String?): Result<Unit> = authenticated {
        val payload = JSONObject().apply {
            if (row.id > 0L) put("id", row.id)
            put("name", row.name.trim())
            put("email", row.email.trim())
            put("Activo", if (row.active) "S" else "N")
            if (!password.isNullOrBlank()) put("password", password)
        }
        val body = payload.toString().toRequestBody(jsonType)
        val builder = Request.Builder().url(resolver.endpoint(if (row.id > 0L) ApiConfig.USER_UPDATE else ApiConfig.USER_CREATE))
        execute(if (row.id > 0L) builder.put(body).build() else builder.post(body).build())
        Unit
    }

    fun delete(id: Long): Result<Unit> = authenticated {
        val body = JSONObject().put("id", id).toString().toRequestBody(jsonType)
        execute(Request.Builder().url(resolver.endpoint(ApiConfig.USER_DELETE)).delete(body).build())
        Unit
    }

    private fun <T> authenticated(block: () -> T): Result<T> {
        if (session.token.isBlank()) return Result.failure(Exception("Inicia sesion en linea para consultar usuarios web."))
        return runCatching(block)
    }

    private fun execute(request: Request): JSONObject {
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val json = runCatching { JSONObject(body) }.getOrNull()
            if (!response.isSuccessful || json?.optBoolean("success", false) != true) {
                throw Exception(json?.optString("message")?.takeIf(String::isNotBlank) ?: "No se pudo completar la operacion de usuarios (${response.code}).")
            }
            return json
        }
    }
}
