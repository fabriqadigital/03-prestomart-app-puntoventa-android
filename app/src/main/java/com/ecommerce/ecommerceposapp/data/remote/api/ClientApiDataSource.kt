package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class ClientApiDataSource(context: Context) {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client = ApiHttpClient(context).client
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun list(): Result<List<ClientRow>> = authenticated {
        execute(Request.Builder().url(resolver.endpoint(ApiConfig.CLIENT_LIST)).get().build())
            .resultArray()
            .mapNotNull(::parseClient)
    }

    fun save(row: ClientRow): Result<Unit> = authenticated {
        val payload = JSONObject().apply {
            if (row.id > 0L) put("id", row.id)
            put("name", row.name.trim())
            put("email", row.email.trim())
            put("documento", row.document.trim())
            put("telefono", row.phone.trim())
            put("direccion", row.address.trim())
            put("razon_social", row.businessName.trim())
            if (row.newPassword.isNotBlank()) put("password", row.newPassword)
            put("Activo", if (row.active) "S" else "N")
        }
        val body = payload.toString().toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(resolver.endpoint(if (row.id > 0L) ApiConfig.CLIENT_UPDATE else ApiConfig.CLIENT_CREATE))
            .post(body)
            .build()
        execute(request)
        Unit
    }

    fun delete(id: Long): Result<Unit> = authenticated {
        val body = JSONObject().put("id", id).toString().toRequestBody(jsonMediaType)
        execute(Request.Builder().url(resolver.endpoint(ApiConfig.CLIENT_DELETE)).delete(body).build())
        Unit
    }

    private fun <T> authenticated(block: () -> T): Result<T> {
        if (session.token.isBlank()) {
            return Result.failure(Exception("Inicia sesion en linea para administrar clientes del backend."))
        }
        return runCatching(block)
    }

    private fun execute(request: Request): JSONObject {
        client.newCall(request).execute().use { response ->
            val payload = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw Exception("Error del backend de clientes (${response.code}): ${payload.take(180)}")
            }
            val json = JSONObject(payload)
            if (!json.optBoolean("success", true)) {
                throw Exception(json.optString("message", "No se pudo completar la operacion."))
            }
            return json
        }
    }

    private fun JSONObject.resultArray(): List<JSONObject> {
        val value = opt("result")
        val array = when (value) {
            is JSONArray -> value
            is JSONObject -> value.optJSONArray("data") ?: JSONArray().put(value)
            else -> JSONArray()
        }
        return (0 until array.length()).mapNotNull(array::optJSONObject)
    }

    private fun parseClient(json: JSONObject): ClientRow? {
        val id = json.optLong("id", 0L).takeIf { it > 0L } ?: json.optLong("id_usuario", 0L)
        if (id <= 0L) return null
        return ClientRow(
            id = id,
            name = json.firstString("name", "nombre", "usuario_nombre"),
            document = json.firstString("documento", "numero_documento", "dni", "document"),
            phone = json.firstString("telefono", "celular", "phone"),
            active = json.optString("Activo", "S").equals("S", true),
            lastName = "",
            email = json.firstString("email", "correo"),
            address = json.firstString("direccion", "address"),
            businessName = json.firstString("razon_social", "business_name"),
            branchName = json.firstString("sucursal_nombre", "nombre_sucursal", "sucursal", "branch_name"),
        )
    }

    private fun JSONObject.cleanString(key: String): String {
        val value = opt(key)
        return if (value == null || value == JSONObject.NULL) "" else value.toString().trim()
    }

    private fun JSONObject.firstString(vararg keys: String): String =
        keys.firstNotNullOfOrNull { key -> cleanString(key).takeIf { it.isNotBlank() } }.orEmpty()
}
