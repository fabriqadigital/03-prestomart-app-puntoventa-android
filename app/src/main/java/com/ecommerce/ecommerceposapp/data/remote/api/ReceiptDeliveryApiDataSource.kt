package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject

class ReceiptDeliveryApiDataSource(context: Context) {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client = ApiHttpClient(context).client

    fun sendByEmail(email: String, receiptNumber: String, customerName: String, pdf: File): Result<Unit> = runCatching {
        val normalizedEmail = email.trim()
        require(EMAIL_REGEX.matches(normalizedEmail)) { "Ingrese un correo valido." }
        require(pdf.exists() && pdf.length() > 0L) { "No se pudo preparar el PDF del comprobante." }

        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("email", normalizedEmail)
            .addFormDataPart("numero_comprobante", receiptNumber.trim())
            .addFormDataPart("cliente_nombre", customerName.trim())
            .addFormDataPart("pdf", pdf.name, pdf.asRequestBody("application/pdf".toMediaType()))
            .build()

        val request = Request.Builder()
            .url(resolver.endpoint(ApiConfig.RECEIPT_SEND_EMAIL))
            .post(body)
            .header("Accept", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
            val bodyText = response.body?.string().orEmpty()
            val json = runCatching { JSONObject(bodyText) }.getOrNull()
            if (!response.isSuccessful || json?.optBoolean("success", false) != true) {
                throw Exception(
                    json?.optString("message")?.takeIf { it.isNotBlank() }
                        ?: "No se pudo enviar el comprobante por correo (${response.code}).",
                )
            }
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", RegexOption.IGNORE_CASE)
    }
}
