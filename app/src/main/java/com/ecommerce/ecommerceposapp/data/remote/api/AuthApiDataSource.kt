package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import android.util.Log
import com.ecommerce.ecommerceposapp.BuildConfig
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
    val avatar: String,
    val avatarBase64: String,
)

class AuthApiDataSource(context: Context) {
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)

    fun login(email: String, password: String): Result<ApiSession> {
        var attemptedUrl = ""
        return runCatching {
        val baseUrl = ApiConfig.configuredBaseUrl(prefs.getString("api_base_url", null))
        val hostHeader = ApiConfig.configuredHostHeader(prefs.getString("api_host_header", null)).ifBlank { null }
        attemptedUrl = "$baseUrl/api${ApiConfig.CASHIER_LOGIN}"
        if (BuildConfig.DEBUG) Log.d(TAG, "POST $attemptedUrl")
        val connection = (URL(attemptedUrl).openConnection() as HttpURLConnection).apply {
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
            if (BuildConfig.DEBUG) Log.w(TAG, "Login rejected by $attemptedUrl with HTTP ${connection.responseCode}: $responseBody")
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
            defaultCashRegisterName = cashier.firstString("caja_default_nombre", "caja_nombre", "nombre_caja")
                .ifBlank { cashier.nestedFirstString("caja", "nombre", "name", "nombre_caja") }
                .ifBlank { cashier.nestedFirstString("caja_default", "nombre", "name", "nombre_caja") },
            document = cashier.firstString("documento_numero", "numero_documento", "documento", "dni", "nro_documento").ifBlank { user.firstString("documento_numero", "numero_documento", "documento", "dni") },
            phone = cashier.firstString("telefono", "celular", "numero_celular", "phone").ifBlank { user.firstString("telefono", "celular", "phone") },
            address = cashier.firstString("direccion", "domicilio", "address").ifBlank { user.firstString("direccion", "domicilio", "address") },
            branchName = cashier.firstString(
                "sucursal",
                "sucursal_nombre",
                "nombre_sucursal",
                "branch_name",
                "branch",
                "tienda",
                "tienda_nombre",
                "local",
                "local_nombre",
                "sede",
                "sede_nombre",
            )
                .ifBlank { cashier.nestedFirstString("sucursal", "nombre", "name", "descripcion", "nombre_sucursal") }
                .ifBlank { cashier.nestedFirstString("caja", "sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch") }
                .ifBlank { cashier.nestedFirstString("caja_default", "sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch") }
                .ifBlank { user.firstString("sucursal_nombre", "nombre_sucursal", "branch_name", "branch", "sede_nombre", "local_nombre") },
            lastName = cashier.firstString("apellidos"),
            documentType = cashier.firstString("documento_tipo").ifBlank { "DNI" },
            cashierState = cashier.firstString("estado").ifBlank { "Activo" },
            avatar = cashier.firstString("avatar_data_uri", "avatar", "foto", "foto_perfil", "profile_photo", "profile_image", "imagen")
                .ifBlank { user.firstString("avatar_data_uri", "avatar", "foto", "foto_perfil", "profile_photo", "profile_image", "imagen") },
            avatarBase64 = cashier.firstString("avatar_base64", "foto_base64", "profile_photo_base64", "imagen_base64")
                .ifBlank { user.firstString("avatar_base64", "foto_base64", "profile_photo_base64", "imagen_base64") },
        )
        }.onFailure { error ->
            if (BuildConfig.DEBUG) Log.e(TAG, "Login request failed for $attemptedUrl", error)
        }
    }

    private fun JSONObject.firstString(vararg keys: String): String = keys.firstNotNullOfOrNull { key ->
        opt(key)
            ?.takeUnless { it == JSONObject.NULL || it is JSONObject || it is org.json.JSONArray }
            ?.toString()
            ?.trim()
            ?.takeIf { it.isNotBlank() && !it.equals("null", ignoreCase = true) }
    }.orEmpty()

    private fun JSONObject.nestedFirstString(containerKey: String, vararg keys: String): String {
        val nested = optJSONObject(containerKey) ?: return ""
        return nested.firstString(*keys)
            .ifBlank { nested.optJSONObject("sucursal")?.firstString(*keys).orEmpty() }
            .ifBlank { nested.optJSONObject("sucursal")?.firstString("nombre", "name", "descripcion", "nombre_sucursal").orEmpty() }
    }

    private companion object {
        const val TAG = "AuthApiDataSource"
    }
}
