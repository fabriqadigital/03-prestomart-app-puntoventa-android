package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class UserApiDataSource(context: Context) {
    private val session  = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client   = ApiHttpClient(context).client
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    // ── "yyyy-MM-dd HH:mm:ss" → millis UTC ──────────────────────────────────
    private fun parseCreatedAt(raw: String): Long =
        runCatching {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(raw)?.time ?: 0L
        }.getOrDefault(0L)

    // ── list() — parsea array JSON plano devuelto por Laravel ->get() ────────
    fun list(): Result<List<UserRow>> = authenticated {
        val json = execute(Request.Builder().url(resolver.endpoint(ApiConfig.USER_LIST)).get().build())

        // La API devuelve: { "result": [ {...}, {...} ] }  (array desde ->get())
        val rawResult = json.opt("result")
        val rows: List<JSONObject> = when (rawResult) {
            is JSONArray -> (0 until rawResult.length()).mapNotNull { rawResult.optJSONObject(it) }
            is JSONObject -> {
                // Fallback: algunas versiones devuelven { "0": {...}, "1": {...} }
                rawResult.keys().asSequence()
                    .filter { it.all(Char::isDigit) }
                    .mapNotNull { rawResult.optJSONObject(it) }
                    .toList()
            }
            else -> emptyList()
        }

        rows.mapNotNull { row ->
            val id = row.optLong("id")
            if (id <= 0L) return@mapNotNull null
            // "name" en la BD es el username/nombre del usuario
            val username = row.optString("username").ifBlank { row.optString("name") }
            val name     = row.optString("name").ifBlank { username }.ifBlank { row.optString("email") }
            UserRow(
                id         = id,
                email      = row.optString("email"),
                name       = name,
                username   = username,
                role       = row.optString("role").ifBlank { "Cliente web" },
                active     = row.optString("Activo", "S").equals("S", true) &&
                             row.optInt("is_blocked", 0) == 0,
                isBlocked  = row.optInt("is_blocked", 0),
                createdAt  = parseCreatedAt(row.optString("created_at")),
            )
        }
    }

    // ── save() ───────────────────────────────────────────────────────────────
    fun save(row: UserRow, password: String?): Result<Unit> = authenticated {
        val payload = JSONObject().apply {
            if (row.id > 0L) put("id", row.id)
            put("name",   row.name.trim())
            put("email",  row.email.trim())
            put("Activo", if (row.active) "S" else "N")
            if (!password.isNullOrBlank()) put("password", password)
        }
        val body    = payload.toString().toRequestBody(jsonType)
        val builder = Request.Builder()
            .url(resolver.endpoint(if (row.id > 0L) ApiConfig.USER_UPDATE else ApiConfig.USER_CREATE))
        execute(if (row.id > 0L) builder.put(body).build() else builder.post(body).build())
        Unit
    }

    // ── delete() ─────────────────────────────────────────────────────────────
    fun delete(id: Long): Result<Unit> = authenticated {
        val body = JSONObject().put("id", id).toString().toRequestBody(jsonType)
        execute(Request.Builder().url(resolver.endpoint(ApiConfig.USER_DELETE)).delete(body).build())
        Unit
    }

    // ── helpers ──────────────────────────────────────────────────────────────
    private fun <T> authenticated(block: () -> T): Result<T> {
        if (session.token.isBlank())
            return Result.failure(Exception("Inicia sesión en línea para consultar usuarios web."))
        return runCatching(block)
    }

    private fun execute(request: Request): JSONObject {
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val json = runCatching { JSONObject(body) }.getOrNull()
            if (!response.isSuccessful || json?.optBoolean("success", false) != true) {
                throw Exception(
                    json?.optString("message")?.takeIf(String::isNotBlank)
                        ?: "No se pudo completar la operación de usuarios (${response.code})."
                )
            }
            return json
        }
    }
}
