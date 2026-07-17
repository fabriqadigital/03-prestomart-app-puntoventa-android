package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class SupplierApiDataSource(context: Context) {
    private val sessionStore = ApiSessionStore(context)
    private val urlResolver = ApiUrlResolver(sessionStore)
    private val httpClient = ApiHttpClient(context).client
    private val jsonMedia = "application/json; charset=utf-8".toMediaType()

    /**
     * Las rutas de proveedores viven en /api/post/... en el backend, pero
     * ApiUrlResolver.endpoint() no antepone /api (se usa así en otros data sources
     * que sí lo agregan por su cuenta, ej. RemoteCatalogDataSource). Por eso aca
     * armamos la URL final directamente para no tocar el resolver compartido.
     */
    private fun endpoint(path: String): String {
        val base = urlResolver.endpoint(path)
        return if (base.contains("/api/")) base else base.replaceFirst(sessionStore.baseUrl, "${sessionStore.baseUrl}/api")
    }

    fun list(): List<SupplierRow> {
        val url = endpoint(ApiConfig.SUPPLIER_LIST)
        val request = Request.Builder()
            .url(url)
            .get()
            .header("Accept", "application/json")
            .build()
        httpClient.newCall(request).execute().use { response ->
            val payload = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw Exception("No se pudo listar proveedores (${response.code}): ${payload.take(180)}")
            val json = JSONObject(payload)
            val result = json.optJSONArray("result") ?: JSONArray()
            return (0 until result.length()).map { i -> parseSupplier(result.getJSONObject(i)) }
        }
    }

    fun save(row: SupplierRow): Result<Unit> = runCatching {
        if (sessionStore.token.isBlank()) throw Exception("Inicia sesion en linea para guardar proveedores en produccion.")
        val path = if (row.id > 0L) ApiConfig.SUPPLIER_UPDATE else ApiConfig.SUPPLIER_CREATE
        val body = buildJson(row).toString().toRequestBody(jsonMedia)
        val builder = Request.Builder().url(endpoint(path)).header("Accept", "application/json")
        val request = if (row.id > 0L) builder.put(body).build() else builder.post(body).build()
        execute(request)
    }

    fun updateEstado(id: Long, estado: String): Result<Unit> = runCatching {
        val body = JSONObject().apply { put("id", id); put("estado", estado) }.toString().toRequestBody(jsonMedia)
        val request = Request.Builder()
            .url(endpoint(ApiConfig.SUPPLIER_UPDATE_ESTADO))
            .put(body)
            .header("Accept", "application/json")
            .build()
        execute(request)
    }

    fun delete(id: Long): Result<Unit> = runCatching {
        val body = JSONObject().apply { put("id", id) }.toString().toRequestBody(jsonMedia)
        val request = Request.Builder()
            .url(endpoint(ApiConfig.SUPPLIER_DELETE))
            .delete(body)
            .header("Accept", "application/json")
            .build()
        execute(request)
    }

    private fun execute(request: Request) {
        httpClient.newCall(request).execute().use { response ->
            val payload = response.body?.string().orEmpty()
            if (!response.isSuccessful) throw Exception("No se pudo guardar en produccion (${response.code}): ${payload.take(180)}")
            val json = runCatching { JSONObject(payload) }.getOrNull()
            if (json?.optBoolean("success", true) == false) {
                throw Exception(json.optString("message", "No se pudo completar la operacion."))
            }
        }
    }

    private fun buildJson(row: SupplierRow) = JSONObject().apply {
        if (row.id > 0L) put("id", row.id)
        put("codigo_proveedor", row.codigoProveedor.trim())
        put("razon_social", row.businessName.trim())
        put("ruc", row.ruc.trim())
        put("correo", row.correo.trim())
        put("telefono", row.phone.trim())
        put("direccion", row.direccion.trim())
        put("persona_contacto", row.personaContacto.trim())
        put("cargo_contacto", row.cargoContacto.trim())
        put("telefono_contacto", row.telefonoContacto.trim())
        put("correo_contacto", row.correoContacto.trim())
        put("calificacion", row.calificacion)
        put("estado", row.estado)
        put("fecha_registro", row.fechaRegistro)
        put("observaciones", row.observaciones.trim())
        put("banco", row.banco.trim())
        put("cuenta", row.cuenta.trim())
        put("cci", row.cci.trim())
    }

    private fun parseSupplier(o: JSONObject) = SupplierRow(
        id = o.optLong("id_proveedor"),
        codigoProveedor = o.optString("codigo_proveedor", ""),
        businessName = o.optString("razon_social", ""),
        ruc = o.optString("ruc", ""),
        correo = o.optString("correo", ""),
        phone = o.optString("telefono", ""),
        direccion = o.optString("direccion", ""),
        personaContacto = o.optString("persona_contacto", ""),
        cargoContacto = o.optString("cargo_contacto", ""),
        telefonoContacto = o.optString("telefono_contacto", ""),
        correoContacto = o.optString("correo_contacto", ""),
        calificacion = o.optInt("calificacion", 0),
        estado = o.optString("estado", "Activo"),
        fechaRegistro = o.optString("fecha_registro", ""),
        observaciones = o.optString("observaciones", ""),
        banco = o.optString("banco", ""),
        cuenta = o.optString("cuenta", ""),
        cci = o.optString("cci", ""),
        active = o.optString("estado", "Activo") == "Activo",
    )
}