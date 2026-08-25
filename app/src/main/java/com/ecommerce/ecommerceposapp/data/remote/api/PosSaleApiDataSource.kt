package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.PosPaymentRounding
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.round

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
        descuentoPorcentaje: Double = 0.0,
        descuentoMonto: Double = 0.0,
    ): Result<RegisteredPosSale> {
        if (session.token.isBlank()) {
            return Result.failure(Exception("Se necesita conexion autenticada para actualizar el stock del backend."))
        }
        return runCatching {
            require(cashSessionId > 0L) { "Debes abrir una caja antes de vender." }
            val exactTotal = PosPaymentRounding.exactTotal(lines.sumOf { it.lineTotal })
            val pct = descuentoPorcentaje.coerceIn(0.0, 100.0)
            // El monto llega calculado por el cliente (solo sobre los productos seleccionados).
            val montoDescuento = descuentoMonto.coerceAtLeast(0.0)
            // El descuento se aplica antes de las reglas de redondeo de pago.
            val baseFinal = round((exactTotal - montoDescuento) * 100) / 100
            val total = PosPaymentRounding.finalTotal(baseFinal, payment.tipoPago, payment.aplicarRedondeo)
            val effectivePayment = PosPaymentRounding.normalizedPayment(payment, baseFinal)
            val payload = JSONObject().apply {
                put("id_cliente", clientId.coerceAtLeast(0L))
                put("cliente_nombre", customerInfo.name.trim())
                put("cliente_documento", customerInfo.document.filter(Char::isDigit))
                put("tipo_comprobante", receiptType.name)
                put("id_caja_sesion", cashSessionId)
                put("tipo_pago", payment.tipoPago)
                put("currency_code", payment.currencyCode.ifBlank { "PEN" })
                put("exchange_rate", payment.exchangeRate.takeIf { it > 0.0 } ?: 1.0)
                put("total_amount_in_currency", payment.totalAmountInCurrency.takeIf { it > 0.0 } ?: total)
                put("monto_recibido", effectivePayment.montoRecibido)
                put("vuelto", effectivePayment.vuelto)
                put("aplicar_redondeo", payment.aplicarRedondeo)
                put("descuento_porcentaje", pct)
                put("descuento_monto", montoDescuento)
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
                            line.conversionId?.let { put("id_producto_conversion", it) }
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
