package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class PosSaleApiDataSource(context: Context) {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client = ApiHttpClient(context).client
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    fun registerSale(
        lines: List<CartLine>,
        payment: SalePaymentInfo,
        clientId: Long,
        cashSessionId: Long,
        customerInfo: ReceiptCustomerInfo,
        receiptType: TipoComprobanteEmision,
        idempotencyKey: String? = null,
    ): Result<RegisteredPosSale> {
        if (session.token.isBlank()) {
            return Result.failure(Exception("Se necesita conexion autenticada para actualizar el stock del backend."))
        }
        return runCatching {
            require(cashSessionId > 0L) { "Debes abrir una caja antes de vender." }
            val total = lines.sumOf { it.lineTotal }
            val payload = JSONObject().apply {
                put("id_cliente", clientId.coerceAtLeast(0L))
                put("cliente_nombre", customerInfo.name.trim())
                put("cliente_documento", customerInfo.document.filter(Char::isDigit))
                put("tipo_comprobante", receiptType.name)
                put("id_caja_sesion", cashSessionId)
                put("tipo_pago", payment.tipoPago)
                put("monto_recibido", payment.montoRecibido)
                put("vuelto", payment.vuelto)
                put("total", total)
                put("pagos", JSONArray().put(JSONObject().apply {
                    put("metodo", payment.tipoPago)
                    put("monto", total)
                }))
                put("productos", JSONArray().apply {
                    lines.forEach { line ->
                        put(JSONObject().apply {
                            put("id_producto", line.productId)
                            put("cantidad", line.quantity)
                            put("precio_unitario", line.unitPrice)
                        })
                    }
                })
            }
            val requestBuilder = Request.Builder()
                .url(resolver.endpoint(ApiConfig.SALE_REGISTER))
                .post(payload.toString().toRequestBody(jsonType))
                .header("Accept", "application/json")
            idempotencyKey?.takeIf { it.isNotBlank() }?.let {
                requestBuilder.header("Idempotency-Key", it)
            }
            val request = requestBuilder.build()
            client.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                val json = runCatching { JSONObject(body) }.getOrNull()
                if (!response.isSuccessful || json?.optBoolean("success", false) != true) {
                    throw Exception(
                        json?.optString("message")?.takeIf { it.isNotBlank() }
                            ?: "No se pudo registrar la venta en el backend (${response.code}).",
                    )
                }
                val result = json.optJSONObject("result") ?: throw Exception("El backend no devolvio la venta registrada.")
                RegisteredPosSale(
                    id = result.optLong("id_venta"),
                    number = result.optString("numero").ifBlank { "VENTA-${result.optLong("id_venta")}" },
                )
            }
        }
    }
}

data class RegisteredPosSale(val id: Long, val number: String)
