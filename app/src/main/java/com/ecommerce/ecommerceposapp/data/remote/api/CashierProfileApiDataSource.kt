package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import java.io.File
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject

class CashierProfileApiDataSource(private val context: Context) {
    private val sessionStore = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(sessionStore)
    private val client = ApiHttpClient(context).client

    fun fetch(session: UserSession): Result<UserSession> = runCatching {
        val url = resolver.endpoint("${ApiConfig.POS_PREFIX}/cajeros/obtener").toHttpUrl().newBuilder()
            .addQueryParameter("id", session.cashierId.toString())
            .addQueryParameter("id_cajero", session.cashierId.toString())
            .build()
        val request = Request.Builder().url(url).get().build()
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val json = JSONObject(body)
            if (!response.isSuccessful || !json.optBoolean("success", true)) {
                error(json.cleanString("message").ifBlank { "No se pudo consultar el perfil." })
            }
            val row = json.optJSONArray("result")?.optJSONObject(0)
                ?: json.optJSONObject("result")
                ?: error("El backend no devolvio la ficha del cajero.")
            val cashier = row.optJSONObject("cajero")
                ?: row.optJSONObject("cashier")
                ?: row.optJSONObject("usuario_cajero")
                ?: row
            val cashRegisterId = cashier.optLongFlexible("id_caja_default", "id_caja", "caja_default_id")
                .takeIf { it > 0L }
                ?: session.defaultCashRegisterId
            val cashRegisterName = cashier.firstString("caja_default_nombre", "caja_nombre", "nombre_caja")
                .ifBlank { row.firstString("caja_default_nombre", "caja_nombre", "nombre_caja") }
                .ifBlank { cashier.optJSONObject("caja")?.firstString("nombre", "name", "nombre_caja").orEmpty() }
                .ifBlank { row.optJSONObject("caja")?.firstString("nombre", "name", "nombre_caja").orEmpty() }
                .ifBlank { session.defaultCashRegisterName }
            val cashRegisterBranch = if (cashRegisterId > 0L) {
                CashApiDataSource(context).listCashRegisters().getOrNull()
                    ?.firstOrNull { it.id == cashRegisterId }
                    ?.branch
                    .orEmpty()
            } else {
                ""
            }
            val branch = cashier.firstString(
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
                .ifBlank { row.firstString("sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch", "tienda", "local", "sede") }
                .ifBlank { cashier.nestedFirstString("sucursal", "nombre", "name", "descripcion", "nombre_sucursal") }
                .ifBlank { row.nestedFirstString("sucursal", "nombre", "name", "descripcion", "nombre_sucursal") }
                .ifBlank { cashier.nestedFirstString("caja", "sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch") }
                .ifBlank { row.nestedFirstString("caja", "sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch") }
                .ifBlank { cashier.nestedFirstString("caja_default", "sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch") }
                .ifBlank { row.nestedFirstString("caja_default", "sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch") }
                .ifBlank { cashRegisterBranch }
            val address = cashier.firstString("direccion", "direccion_residencial", "domicilio", "address")
                .ifBlank { row.firstString("direccion", "direccion_residencial", "domicilio", "address") }
            val avatar = cashier.firstString("avatar_data_uri", "avatar", "foto", "foto_perfil", "profile_photo", "profile_image", "imagen")
                .ifBlank { row.firstString("avatar_data_uri", "avatar", "foto", "foto_perfil", "profile_photo", "profile_image", "imagen") }
            val avatarBase64 = cashier.firstString("avatar_base64", "foto_base64", "profile_photo_base64", "imagen_base64")
                .ifBlank { row.firstString("avatar_base64", "foto_base64", "profile_photo_base64", "imagen_base64") }
            session.copy(
                name = cashier.firstString("nombres", "name", "nombre").ifBlank { row.firstString("nombres", "name", "nombre") }.ifBlank { session.name },
                lastName = cashier.firstString("apellidos", "last_name", "apellido").ifBlank { row.firstString("apellidos", "last_name", "apellido") }.ifBlank { session.lastName },
                email = cashier.firstString("email", "correo").ifBlank { row.firstString("email", "correo") }.ifBlank { session.email },
                documentType = cashier.firstString("documento_tipo", "tipo_documento").ifBlank { row.firstString("documento_tipo", "tipo_documento") }.ifBlank { session.documentType },
                document = cashier.firstString("documento_numero", "numero_documento", "documento", "dni", "nro_documento").ifBlank { row.firstString("documento_numero", "numero_documento", "documento", "dni", "nro_documento") }.ifBlank { session.document },
                phone = cashier.firstString("telefono", "celular", "numero_celular", "phone").ifBlank { row.firstString("telefono", "celular", "numero_celular", "phone") }.ifBlank { session.phone },
                address = address.ifBlank { session.address },
                branchName = branch.ifBlank { session.branchName },
                defaultCashRegisterId = cashRegisterId,
                defaultCashRegisterName = cashRegisterName,
                cashierState = row.firstString("estado", "estado_cajero").ifBlank { session.cashierState },
                avatar = avatar.ifBlank { session.avatar },
                avatarBase64 = avatarBase64.ifBlank { session.avatarBase64 },
            ).also(::persist)
        }
    }

    fun update(
        session: UserSession,
        name: String,
        lastName: String,
        email: String,
        document: String,
        phone: String,
        address: String,
        currentPassword: String,
        password: String,
        passwordConfirmation: String,
        imageUri: Uri? = null,
    ): Result<UserSession> = runCatching {
        if (sessionStore.token.isBlank()) error("Debes estar conectado para actualizar tu perfil.")
        if (password.isNotBlank() && password != passwordConfirmation) error("Las contrasenas no coinciden.")

        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("id", session.cashierId.toString())
            .addFormDataPart("nombres", name.trim())
            .addFormDataPart("apellidos", lastName.trim())
            .addFormDataPart("email", email.trim())
            .addFormDataPart("documento_tipo", session.documentType)
            .addFormDataPart("documento_numero", document.trim())
            .addFormDataPart("telefono", phone.trim())
            .addFormDataPart("direccion", address.trim())
            .addFormDataPart("sucursal", session.branchName)
            .addFormDataPart("estado", session.cashierState)
            .apply {
                if (session.defaultCashRegisterId > 0L) addFormDataPart("id_caja_default", session.defaultCashRegisterId.toString())
                if (password.isNotBlank()) {
                    addFormDataPart("current_password", currentPassword)
                    addFormDataPart("password", password)
                    addFormDataPart("password_confirmation", passwordConfirmation)
                    addFormDataPart("notificar_email", "1")
                    addFormDataPart("security_event", "password_changed")
                }
                imageUri?.let { uri ->
                    val (file, mimeType) = copyProfileImageToCache(uri)
                    addFormDataPart("image", file.name, file.asRequestBody(mimeType.toMediaType()))
                }
            }
            .build()

        val request = Request.Builder()
            .url(resolver.endpoint(ApiConfig.CASHIER_UPDATE))
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string().orEmpty()
            val parsed = runCatching { JSONObject(responseBody) }.getOrNull()
            if (!response.isSuccessful || parsed?.optBoolean("success", true) == false) {
                error(parsed?.cleanString("message")?.takeIf { it.isNotBlank() } ?: "No se pudo actualizar el perfil (${response.code}).")
            }
        }
        fetch(
            session.copy(
                name = name.trim(),
                lastName = lastName.trim(),
                email = email.trim(),
                document = document.trim(),
                phone = phone.trim(),
                address = address.trim(),
            ),
        ).getOrElse {
            session.copy(
                name = name.trim(),
                lastName = lastName.trim(),
                email = email.trim(),
                document = document.trim(),
                phone = phone.trim(),
                address = address.trim(),
            ).also(::persist)
        }
    }

    private fun persist(value: UserSession) {
        context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString("session_name", value.name)
            .putString("session_last_name", value.lastName)
            .putString("session_email", value.email)
            .putString("session_document_type", value.documentType)
            .putString("session_document", value.document)
            .putString("session_phone", value.phone)
            .putString("session_address", value.address)
            .putString("session_branch_name", value.branchName)
            .putString("session_default_cash_register_name", value.defaultCashRegisterName)
            .putString("session_cashier_state", value.cashierState)
            .putString("session_avatar", value.avatar)
            .putString("session_avatar_base64", value.avatarBase64)
            .apply()
    }

    private fun copyProfileImageToCache(uri: Uri): Pair<File, String> {
        val mimeType = context.contentResolver.getType(uri)?.takeIf { it.startsWith("image/") } ?: "image/jpeg"
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            ?.takeIf { it.isNotBlank() }
            ?: "jpg"
        val file = File(context.cacheDir, "cashier_profile_${System.currentTimeMillis()}.$extension")
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use(input::copyTo)
        } ?: error("Imagen invalida.")
        return file to mimeType
    }

    private fun JSONObject.firstString(vararg keys: String): String {
        keys.forEach { key ->
            val value = cleanString(key)
            if (value.isNotBlank()) return value
        }
        return ""
    }

    private fun JSONObject.cleanString(key: String): String {
        val raw = opt(key)?.takeUnless { it == JSONObject.NULL || it is JSONObject || it is org.json.JSONArray }
        val value = raw?.toString()?.trim().orEmpty()
        return value.takeUnless { it.equals("null", ignoreCase = true) }.orEmpty()
    }

    private fun JSONObject.nestedFirstString(containerKey: String, vararg keys: String): String {
        val nested = optJSONObject(containerKey) ?: return ""
        return nested.firstString(*keys)
            .ifBlank { nested.optJSONObject("sucursal")?.firstString(*keys).orEmpty() }
            .ifBlank { nested.optJSONObject("sucursal")?.firstString("nombre", "name", "descripcion", "nombre_sucursal").orEmpty() }
    }

    private fun JSONObject.optLongFlexible(vararg keys: String): Long {
        keys.forEach { key ->
            when (val value = opt(key)) {
                is Number -> if (value.toLong() > 0L) return value.toLong()
                is String -> value.trim().toLongOrNull()?.takeIf { it > 0L }?.let { return it }
            }
        }
        return 0L
    }
}
