package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CashierProfileApiDataSource(private val context: Context) {
    private val sessionStore = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(sessionStore)
    private val client = ApiHttpClient(context).client

    fun fetch(session: UserSession): Result<UserSession> = runCatching {
        val url = resolver.endpoint("${ApiConfig.POS_PREFIX}/cajeros/obtener").toHttpUrl().newBuilder()
            .addQueryParameter("id", session.cashierId.toString()).build()
        val request = Request.Builder().url(url).get().build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val json = JSONObject(body)
            if (!response.isSuccessful || !json.optBoolean("success", true)) error(json.optString("message", "No se pudo consultar el perfil."))
            val row = json.optJSONArray("result")?.optJSONObject(0) ?: error("El backend no devolvió la ficha del cajero.")
            session.copy(
                name = row.optString("nombres", session.name),
                lastName = row.optString("apellidos", session.lastName),
                email = row.optString("email", session.email),
                documentType = row.optString("documento_tipo", session.documentType),
                document = row.optString("documento_numero", session.document),
                phone = row.optString("telefono", session.phone),
                address = row.optString("direccion", session.address),
                branchName = row.optString("sucursal", session.branchName),
                defaultCashRegisterName = row.optString("caja_default_nombre", session.defaultCashRegisterName),
                cashierState = row.optString("estado", session.cashierState),
            ).also(::persist)
        }
    }

    fun update(session: UserSession, name: String, lastName: String, email: String, document: String, phone: String, address: String, password: String): Result<Unit> = runCatching {
        if (sessionStore.token.isBlank()) error("Debes estar conectado para actualizar tu perfil.")
        val json = JSONObject().apply {
            put("id", session.cashierId)
            put("nombres", name.trim())
            put("apellidos", lastName.trim())
            put("email", email.trim())
            put("documento_tipo", session.documentType)
            put("documento_numero", document.trim())
            put("telefono", phone.trim())
            put("direccion", address.trim())
            put("sucursal", session.branchName)
            if (session.defaultCashRegisterId > 0L) put("id_caja_default", session.defaultCashRegisterId)
            put("estado", session.cashierState)
            if (password.isNotBlank()) put("password", password)
        }
        val request = Request.Builder().url(resolver.endpoint(ApiConfig.CASHIER_UPDATE))
            .put(json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())).build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val parsed = runCatching { JSONObject(body) }.getOrNull()
            if (!response.isSuccessful || parsed?.optBoolean("success", true) == false) error(parsed?.optString("message")?.takeIf { it.isNotBlank() } ?: "No se pudo actualizar el perfil (${response.code}).")
        }
        context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString("session_name", name.trim()).putString("session_email", email.trim())
            .putString("session_document", document.trim()).putString("session_phone", phone.trim())
            .putString("session_address", address.trim()).apply()
    }

    private fun persist(value: UserSession) {
        context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString("session_name", value.name).putString("session_last_name", value.lastName)
            .putString("session_email", value.email).putString("session_document_type", value.documentType)
            .putString("session_document", value.document).putString("session_phone", value.phone)
            .putString("session_address", value.address).putString("session_branch_name", value.branchName)
            .putString("session_default_cash_register_name", value.defaultCashRegisterName)
            .putString("session_cashier_state", value.cashierState).apply()
    }
}
