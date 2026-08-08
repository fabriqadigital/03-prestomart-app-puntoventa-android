package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.cash.CashRegister
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryPage
import java.text.SimpleDateFormat
import java.util.Locale
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class CashApiDataSource(context: Context) {
    private val session = ApiSessionStore(context)
    private val resolver = ApiUrlResolver(session)
    private val client = ApiHttpClient(context).client
    private val jsonType = "application/json; charset=utf-8".toMediaType()

    fun listCashRegisters(): Result<List<CashRegister>> = runCatching {
        executeGet(ApiConfig.CASH_REGISTER_LIST).resultArray().mapNotNull { item ->
            val id = item.optLong("id_caja")
            if (id <= 0L) null else CashRegister(
                id = id,
                code = item.cleanString("codigo"),
                name = item.cleanString("nombre"),
                branch = item.firstString("sucursal", "sucursal_nombre", "nombre_sucursal", "branch_name", "branch", "tienda", "local", "sede")
                    .ifBlank { item.nestedFirstString("sucursal", "nombre", "name", "descripcion", "nombre_sucursal") },
                active = item.optBooleanFlexible("status", true),
                ruc = item.emitterString("ruc", "empresa_ruc", "emisor_ruc", "tienda_ruc"),
                businessName = item.emitterString("razon_social", "empresa_razon_social", "emisor_razon_social", "nombre_comercial"),
                address = item.emitterString("direccion_fiscal", "empresa_direccion", "emisor_direccion", "direccion", "address"),
            )
        }
    }

    fun findOpenSession(cashierId: Long): Result<CashSession?> = runCatching {
        // Do not send an exact status label here. The web backend has used both
        // "Abierta" and other equivalent representations, so an exact server-side
        // filter can hide a perfectly valid session opened from another client.
        executeGet(ApiConfig.CASH_SESSION_LIST, mapOf("id_cajero" to cashierId.toString()))
            .resultArray()
            .map(::parseSession)
            .filter { it.id > 0L && it.cashierId == cashierId && it.status.isOpenCashStatus() }
            .maxByOrNull { it.openedAt }
    }

    fun findOpenSessionForRegister(cashRegisterId: Long): Result<CashSession?> = runCatching {
        executeGet(ApiConfig.CASH_SESSION_LIST, mapOf("id_caja" to cashRegisterId.toString()))
            .resultArray()
            .map(::parseSession)
            .filter { it.id > 0L && it.cashRegisterId == cashRegisterId && it.status.isOpenCashStatus() }
            .maxByOrNull { it.openedAt }
    }

    fun openSession(cashRegisterId: Long, cashierId: Long, openingAmount: Double): Result<CashSession> = runCatching {
        val response = executePost(
            ApiConfig.CASH_SESSION_OPEN,
            JSONObject()
                .put("id_caja", cashRegisterId)
                .put("id_cajero", cashierId)
                .put("monto_inicial", openingAmount),
        )
        parseSession(response.optJSONObject("result") ?: throw Exception("El backend no devolvio la sesion de caja."))
    }

    fun summary(sessionId: Long): Result<CashSummary> = runCatching {
        val item = executeGet(ApiConfig.CASH_SESSION_SUMMARY, mapOf("id_caja_sesion" to sessionId.toString()))
            .optJSONObject("result") ?: JSONObject()
        val totalesPago = item.optJSONObject("totales_pago") ?: JSONObject()
        val totalVentas = item.optDoubleFlexible("total_ventas")
        val ingresos = item.optDoubleFlexible("ingresos")
        val egresos = item.optDoubleFlexible("egresos")
        CashSummary(
            openingAmount = item.optDoubleFlexible("monto_inicial"),
            totalSales = totalVentas,
            cashAmount = totalesPago.optDoubleFlexible("efectivo"),
            deposit = totalesPago.optDoubleFlexible("tarjeta") +
                    totalesPago.optDoubleFlexible("transferencia") +
                    totalesPago.optDoubleFlexible("yape") +
                    totalesPago.optDoubleFlexible("plin") +
                    totalesPago.optDoubleFlexible("otros"),
            expectedCash = item.optDoubleFlexible("efectivo_esperado"),
            totalFlow = totalVentas + ingresos - egresos,
            income = ingresos,
            expenses = egresos,
        )
    }

    fun closeSession(sessionId: Long, countedCash: Double, observations: String): Result<Unit> = runCatching {
        executePost(
            ApiConfig.CASH_SESSION_CLOSE,
            JSONObject().put("id_caja_sesion", sessionId).put("efectivo_contado", countedCash).put("observaciones", observations),
        )
        Unit
    }

    fun cancelSale(saleId: Long, comment: String, restoreStock: Boolean): Result<Unit> = runCatching {
        require(saleId > 0L) { "Venta invalida." }
        require(comment.trim().length >= 5) { "Ingrese un motivo de anulacion." }
        executePost(
            ApiConfig.CASH_SALE_CANCEL,
            JSONObject()
                .put("id_venta", saleId)
                .put("comentario", comment.trim())
                .put("actualizar_stock", restoreStock),
        )
        Unit
    }

    fun listSales(sessionId: Long, page: Int, perPage: Int, search: String): Result<SalesHistoryPage> = runCatching {
        val root = executeGet(ApiConfig.CASH_SALES, mapOf(
            "id_caja_sesion" to sessionId.toString(),
            "page" to page.toString(),
            "per_page" to perPage.toString(),
            "search" to search,
        ))
        val result = root.optJSONObject("result") ?: JSONObject()
        val data = result.optJSONArray("data") ?: JSONArray()
        val rows = (0 until data.length()).mapNotNull(data::optJSONObject).map { item ->
            SalesHistoryRow(
                ventaId = item.optLong("id_venta"),
                numeroComprobante = item.cleanString("numero"),
                tipoComprobante = item.cleanString("tipo_comprobante").ifBlank { "TICK" },
                fechaMillis = parseDate(item.cleanString("fecha")),
                clienteNombre = item.cleanString("cliente_nombre"),
                cajeroNombre = item.cleanString("usuario_nombre"),
                tipoPago = "",
                total = item.optDoubleFlexible("total"),
                estado = item.cleanString("estado"),
                idCliente = item.optLong("id_cliente"),
            )
        }
        SalesHistoryPage(
            rows = rows,
            total = result.optInt("total", rows.size),
            page = result.optInt("current_page", page),
            perPage = result.optInt("per_page", perPage),
        )
    }

    fun getSaleReceipt(saleId: Long): Result<CompletedSaleReceipt> = runCatching {
        val result = executeGet(ApiConfig.CASH_SALE_DETAIL, mapOf("id_venta" to saleId.toString())).optJSONObject("result")
            ?: throw Exception("El backend no devolvio el detalle de venta.")
        val sale = result.optJSONObject("venta") ?: throw Exception("Venta no encontrada.")
        val details = result.optJSONArray("detalles") ?: JSONArray()
        val payments = result.optJSONArray("pagos") ?: JSONArray()
        val lines = (0 until details.length()).mapNotNull { index ->
            val item = details.optJSONObject(index) ?: return@mapNotNull null
            CartLine(
                productId = item.optLong("id_producto"),
                productName = item.cleanString("producto_nombre"),
                unitPrice = item.optDoubleFlexible("precio_unitario"),
                quantity = item.optDoubleFlexible("cantidad").toInt(),
            )
        }
        val total = sale.optDoubleFlexible("total")
        val subtotal = total / 1.18
        val firstPayment = payments.optJSONObject(0)
        val emisorRuc = sale.emitterString("ruc", "empresa_ruc", "emisor_ruc", "tienda_ruc")
            .ifBlank { result.emitterString("ruc", "empresa_ruc", "emisor_ruc", "tienda_ruc") }
        val emisorRazonSocial = sale.emitterString("razon_social", "empresa_razon_social", "emisor_razon_social", "nombre_comercial")
            .ifBlank { result.emitterString("razon_social", "empresa_razon_social", "emisor_razon_social", "nombre_comercial") }
        val emisorDireccion = sale.emitterString("direccion_fiscal", "empresa_direccion", "emisor_direccion", "direccion", "address")
            .ifBlank { result.emitterString("direccion_fiscal", "empresa_direccion", "emisor_direccion", "direccion", "address") }
        CompletedSaleReceipt(
            ventaId = sale.optLong("id_venta"),
            numeroTicket = sale.cleanString("numero"),
            subtotal = subtotal,
            igv = total - subtotal,
            total = total,
            tipoPago = firstPayment?.cleanString("metodo").orEmpty(),
            montoRecibido = sale.optDoubleFlexible("monto_recibido").takeIf { it > 0.0 }
                ?: firstPayment?.optDoubleFlexible("monto")
                ?: total,
            vuelto = sale.optDoubleFlexible("vuelto").coerceAtLeast(0.0),
            fechaMillis = parseDate(sale.cleanString("fecha")),
            lines = lines,
            vendedorNombre = sale.cleanString("cajero_nombre").ifBlank { sale.cleanString("usuario_nombre") },
            idCliente = sale.optLong("id_cliente"),
            clienteNombre = sale.cleanString("cliente_nombre"),
            clienteDocumento = sale.cleanString("cliente_documento"),
            emisorRuc = emisorRuc,
            emisorRazonSocial = emisorRazonSocial,
            emisorDireccion = emisorDireccion,
        )
    }

    fun cashFlow(
        sessionId: Long? = null,
        fechaInicio: String? = null,
        fechaFin: String? = null,
    ): Result<List<com.ecommerce.ecommerceposapp.domain.model.cash.CashFlowItem>> = runCatching {
        val params = buildMap {
            sessionId?.let { put("id_caja_sesion", it.toString()) }
            fechaInicio?.let { put("fecha_inicio", it) }
            fechaFin?.let { put("fecha_fin", it) }
        }
        executeGet(ApiConfig.CASH_FLOW, params).resultArray().map { item ->
            com.ecommerce.ecommerceposapp.domain.model.cash.CashFlowItem(
                flujoId = item.cleanString("flujo_id"),
                fecha = parseDate(item.cleanString("fecha")),
                razonSocial = item.cleanString("tienda_razon_social"),
                tipoMovimiento = item.cleanString("tipo_movimiento"),
                tipoTransaccion = item.cleanString("tipo_transaccion"),
                origen = item.cleanString("origen"),
                tipoPago = item.cleanString("tipo_pago"),
                comentario = item.cleanString("comentario"),
                importe = item.optDoubleFlexible("importe"),
                cajaNombre = item.cleanString("caja_nombre"),
                sucursal = item.cleanString("sucursal"),
                cajeroNombre = item.cleanString("cajero_nombre"),
            )
        }
    }

    fun registerMovement(
        sessionId: Long,
        cashRegisterId: Long,
        tipo: String,
        monto: Double,
        motivo: String,
        observaciones: String,
    ): Result<Unit> = runCatching {
        require(monto > 0.0) { "Ingrese un monto válido." }
        require(motivo.trim().isNotBlank()) { "Ingrese el motivo del movimiento." }
        executePost(
            ApiConfig.CASH_MOVEMENT_REGISTER,
            JSONObject()
                .put("id_caja_sesion", sessionId)
                .put("id_caja", cashRegisterId)
                .put("tipo", tipo)
                .put("motivo", motivo.trim())
                .put("monto", monto)
                .put("observaciones", observaciones.trim().ifBlank { JSONObject.NULL }),
        )
        Unit
    }
    private fun executeGet(path: String, params: Map<String, String> = emptyMap()): JSONObject {
        val url = resolver.endpoint(path).toHttpUrl().newBuilder().apply {
            params.forEach { (key, value) -> addQueryParameter(key, value) }
        }.build()
        return execute(Request.Builder().url(url).get().build())
    }

    private fun executePost(path: String, payload: JSONObject): JSONObject = execute(
        Request.Builder().url(resolver.endpoint(path)).post(payload.toString().toRequestBody(jsonType)).build(),
    )

    private fun execute(request: Request): JSONObject {
        client.newCall(request).execute().use { response ->
            val body = response.body?.string().orEmpty()
            val json = runCatching { JSONObject(body) }.getOrNull()
            if (!response.isSuccessful || json?.optBoolean("success", false) != true) {
                throw Exception(json?.optString("message")?.takeIf { it.isNotBlank() } ?: "No se pudo completar la operacion de caja (${response.code}).")
            }
            return json
        }
    }

    private fun parseSession(item: JSONObject) = CashSession(
        id = item.firstLong("id_caja_sesion", "sesion_id", "id"),
        cashRegisterId = item.firstLong("id_caja", "caja_id"),
        cashRegisterName = item.firstString("caja_nombre", "nombre_caja"),
        cashierId = item.firstLong("id_cajero", "cajero_id", "id_usuario", "usuario_id"),
        cashierName = item.firstString("cajero_nombre", "nombre_cajero", "usuario_nombre"),
        openedAt = parseDate(item.firstString("hora_apertura", "fecha_apertura", "opened_at")),
        openingAmount = item.firstDouble("monto_inicial", "monto_apertura", "opening_amount"),
        status = item.firstString("estado", "status"),
    )

    private fun String.isOpenCashStatus(): Boolean {
        val value = trim().lowercase(Locale.ROOT)
        return value.isBlank() ||
            value in setOf("a", "1", "abierta", "abierto", "open", "opened", "activa", "activo", "vigente")
    }

    private fun JSONObject.resultArray(): List<JSONObject> {
        val array = optJSONArray("result") ?: JSONArray()
        return (0 until array.length()).mapNotNull(array::optJSONObject)
    }

    private fun JSONObject.cleanString(key: String): String {
        val raw = opt(key).takeUnless { it == null || it == JSONObject.NULL || it is JSONObject || it is JSONArray }
        val value = raw?.toString()?.trim().orEmpty()
        return value.takeUnless { it.equals("null", ignoreCase = true) }.orEmpty()
    }

    private fun JSONObject.firstString(vararg keys: String): String {
        keys.forEach { key ->
            val value = cleanString(key)
            if (value.isNotBlank()) return value
        }
        return ""
    }

    private fun JSONObject.firstLong(vararg keys: String): Long {
        keys.forEach { key ->
            when (val value = opt(key)) {
                is Number -> if (value.toLong() > 0L) return value.toLong()
                is String -> value.trim().toLongOrNull()?.takeIf { it > 0L }?.let { return it }
            }
        }
        return 0L
    }

    private fun JSONObject.firstDouble(vararg keys: String): Double {
        keys.forEach { key ->
            if (has(key) && !isNull(key)) return optDoubleFlexible(key)
        }
        return 0.0
    }

    private fun JSONObject.nestedFirstString(containerKey: String, vararg keys: String): String {
        val nested = optJSONObject(containerKey) ?: return ""
        return nested.firstString(*keys)
    }

    private fun JSONObject.emitterString(vararg keys: String): String {
        firstString(*keys).takeIf { it.isNotBlank() }?.let { return it }
        for (container in listOf("emisor", "empresa", "tienda", "sucursal", "caja", "company", "store")) {
            nestedFirstString(container, *keys).takeIf { it.isNotBlank() }?.let { return it }
        }
        return ""
    }

    private fun JSONObject.optDoubleFlexible(key: String): Double = when (val value = opt(key)) {
        is Number -> value.toDouble()
        is String -> value.replace(",", ".").toDoubleOrNull() ?: 0.0
        else -> 0.0
    }

    private fun JSONObject.optBooleanFlexible(key: String, fallback: Boolean = false): Boolean = when (val value = opt(key)) {
        is Boolean -> value
        is Number -> value.toInt() != 0
        is String -> value.equals("true", true) || value.equals("s", true) || value == "1" || value.equals("activa", true)
        else -> fallback
    }

    private fun parseDate(value: String): Long {
        val patterns = listOf("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ssXXX")
        return patterns.firstNotNullOfOrNull { pattern ->
            runCatching { SimpleDateFormat(pattern, Locale.US).parse(value)?.time }.getOrNull()
        } ?: 0L
    }
}
