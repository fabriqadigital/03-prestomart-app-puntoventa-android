package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage
import okhttp3.HttpUrl.Companion.toHttpUrl
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

    fun listPage(page: Int, perPage: Int, search: String): Result<ServerPage<ClientRow>> = authenticated {
        val url = resolver.endpoint(ApiConfig.CLIENT_LIST).toHttpUrl().newBuilder()
            .addQueryParameter("page", page.coerceAtLeast(1).toString())
            .addQueryParameter("per_page", perPage.toString())
            .apply { if (search.isNotBlank()) addQueryParameter("search", search.trim()) }
            .build()
        val result = execute(Request.Builder().url(url).get().build()).optJSONObject("result") ?: JSONObject()
        val data = result.optJSONArray("data") ?: JSONArray()
        ServerPage(
            rows = (0 until data.length()).mapNotNull(data::optJSONObject).mapNotNull(::parseClient),
            total = result.optInt("total", data.length()),
            page = result.optInt("current_page", page),
            perPage = result.optInt("per_page", perPage),
        )
    }

    fun save(row: ClientRow): Result<String> = authenticated {
        val payload = JSONObject().apply {
            if (row.id > 0L) put("id", row.id)
            if (row.userId > 0L) put("id_usuario", row.userId)
            put("tipo_persona", row.personType)
            put("tipo_documento", row.documentType)
            put("numero_documento", row.document.trim())
            put("nombre", row.name.trim())
            put("apellido", row.lastName.trim())
            put("razon_social", row.businessName.trim())
            put("telefono", row.phone.trim())
            put("direccion", row.address.trim())
            put("alias", row.alias.trim())
            put("correo", row.email.trim())
            put("genero", row.gender)
            put("estado_civil", row.maritalStatus)
            put("descuento_porcentaje", row.discountPercentage)
            put("observaciones", row.observations.trim())
            put("estado", if (row.active) "Activo" else "Inactivo")
            put("acceso_sistema", row.webAccess)
        }
        val body = payload.toString().toRequestBody(jsonMediaType)
        val builder = Request.Builder().url(resolver.endpoint(if (row.id > 0L) ApiConfig.CLIENT_UPDATE else ApiConfig.CLIENT_CREATE))
        val request = if (row.id > 0L) builder.put(body).build() else builder.post(body).build()
        execute(request).optString("message", "Cliente guardado.")
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
        val id = json.optLong("id_cliente_pos", 0L).takeIf { it > 0L } ?: json.optLong("id", 0L)
        if (id <= 0L) return null
        return ClientRow(
            id = id,
            name = json.firstString("nombre", "name", "usuario_nombre"),
            document = json.firstString("numero_documento", "documento", "dni", "document"),
            phone = json.firstString("telefono", "celular", "phone"),
            active = json.firstString("estado", "Activo").ifBlank { "Activo" }.let { it.equals("Activo", true) || it.equals("S", true) },
            lastName = json.firstString("apellido"),
            email = json.firstString("correo", "email", "usuario_email"),
            address = json.firstString("direccion", "address"),
            businessName = json.firstString("razon_social", "business_name"),
            branchName = json.firstString("sucursal_nombre", "nombre_sucursal", "sucursal", "branch_name"),
            userId = json.optLong("id_usuario", 0L),
            personType = json.firstString("tipo_persona").ifBlank { "Natural" },
            documentType = json.firstString("tipo_documento").ifBlank { "DNI" },
            alias = json.firstString("alias"),
            gender = json.firstString("genero"),
            maritalStatus = json.firstString("estado_civil"),
            discountPercentage = json.optDouble("descuento_porcentaje", 0.0),
            observations = json.firstString("observaciones"),
            webAccess = json.optBoolean("acceso_sistema", json.optLong("id_usuario", 0L) > 0L),
        )
    }

    private fun JSONObject.cleanString(key: String): String {
        val value = opt(key)
        return if (value == null || value == JSONObject.NULL) "" else value.toString().trim()
    }

    private fun JSONObject.firstString(vararg keys: String): String =
        keys.firstNotNullOfOrNull { key -> cleanString(key).takeIf { it.isNotBlank() } }.orEmpty()
}
