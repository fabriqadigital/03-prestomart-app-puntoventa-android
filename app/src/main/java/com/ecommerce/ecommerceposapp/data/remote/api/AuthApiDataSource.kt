package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

data class ApiSession(
    val baseUrl: String,
    val hostHeader: String?,
    val token: String,
    val refreshToken: String,
    val userId: Long,
    val name: String,
    val cashierId: Long,
    val defaultCashRegisterId: Long,
    val defaultCashRegisterName: String,
    val document: String,
    val phone: String,
    val address: String,
    val branchName: String,
    val lastName: String,
    val documentType: String,
    val cashierState: String,
)

class AuthApiDataSource(context: Context) {
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)

    fun login(email: String, password: String): Result<ApiSession> = runCatching {
        val baseUrl = prefs.getString("api_base_url", null)?.trim().takeUnless { it.isNullOrBlank() }
            ?: ApiConfig.PRODUCTION_BASE_URL
        val hostHeader = prefs.getString("api_host_header", null)?.trim().takeUnless { it.isNullOrBlank() }
        val connection = (URL("$baseUrl/api${ApiConfig.CASHIER_LOGIN}").openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 10_000
            readTimeout = 15_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            hostHeader?.let { setRequestProperty("Host", it) }
        }
        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(JSONObject().put("email", email).put("password", password).toString())
        }
        val responseBody = (if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream)
            ?.bufferedReader()?.use { it.readText() }.orEmpty()
        val response = runCatching { JSONObject(responseBody) }.getOrNull()
        if (connection.responseCode == 429) {
            throw Exception("Demasiados intentos de inicio de sesión. Espera un minuto antes de volver a intentarlo.")
        }
        if (connection.responseCode !in 200..299 || response?.optBoolean("success", false) != true) {
            throw Exception(response?.optString("message")?.takeIf { it.isNotBlank() } ?: "No se pudo iniciar sesion como cajero.")
        }
        val user = response.optJSONObject("user") ?: JSONObject()
        val cashier = response.optJSONObject("cajero") ?: JSONObject()
        ApiSession(
            baseUrl = baseUrl,
            hostHeader = hostHeader,
            token = response.optString("token").trim().ifBlank { throw Exception("El backend no devolvio el token de acceso.") },
            refreshToken = response.optString("refreshToken").trim(),
            userId = user.optLong("id"),
            name = cashier.firstString("nombres").ifBlank { user.optString("name").trim() },
            cashierId = cashier.optLong("id_cajero"),
            defaultCashRegisterId = cashier.optLong("id_caja_default"),
            defaultCashRegisterName = cashier.optString("caja_default_nombre").trim(),
            document = cashier.firstString("documento_numero", "numero_documento", "documento", "dni", "nro_documento").ifBlank { user.firstString("documento_numero", "numero_documento", "documento", "dni") },
            phone = cashier.firstString("telefono", "celular", "numero_celular", "phone").ifBlank { user.firstString("telefono", "celular", "phone") },
            address = cashier.firstString("direccion", "domicilio", "address").ifBlank { user.firstString("direccion", "domicilio", "address") },
            branchName = cashier.firstString("sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name")
                .ifBlank { cashier.optJSONObject("sucursal")?.firstString("nombre", "name").orEmpty() }
                .ifBlank { user.firstString("sucursal_nombre", "nombre_sucursal", "branch_name") },
            lastName = cashier.firstString("apellidos"),
            documentType = cashier.firstString("documento_tipo").ifBlank { "DNI" },
            cashierState = cashier.firstString("estado").ifBlank { "Activo" },
        )
    }

    private fun JSONObject.firstString(vararg keys: String): String = keys.firstNotNullOfOrNull { key ->
        opt(key)?.takeUnless { it == JSONObject.NULL }?.toString()?.trim()?.takeIf { it.isNotBlank() }
    }.orEmpty()
}
