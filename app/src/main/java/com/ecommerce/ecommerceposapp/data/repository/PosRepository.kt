package com.ecommerce.ecommerceposapp.data.repository

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.categories.CategoryRealm
import com.ecommerce.ecommerceposapp.data.local.categories.SubcategoryRealm
import com.ecommerce.ecommerceposapp.data.local.clients.ClientRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaCajaRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaComprobanteSerieRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaEmisorConfigRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaSesionCajaRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaComprobanteDetalleRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaComprobanteRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaVentaDetalleRealm
import com.ecommerce.ecommerceposapp.data.local.FinanzaVentaRealm
import com.ecommerce.ecommerceposapp.data.local.products.ProductRealm
import com.ecommerce.ecommerceposapp.data.local.suppliers.SupplierRealm
import com.ecommerce.ecommerceposapp.data.local.sync.SyncModuleStateRealm
import com.ecommerce.ecommerceposapp.data.local.sync.SyncStateRealm
import com.ecommerce.ecommerceposapp.data.local.users.UserRealm
import com.ecommerce.ecommerceposapp.domain.CartLine
import com.ecommerce.ecommerceposapp.domain.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.domain.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.CategoryItem
import com.ecommerce.ecommerceposapp.domain.ClientRow
import com.ecommerce.ecommerceposapp.domain.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.ProductItem
import com.ecommerce.ecommerceposapp.domain.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.SubcategoryItem
import com.ecommerce.ecommerceposapp.domain.SupplierRow
import com.ecommerce.ecommerceposapp.domain.UserRow
import com.ecommerce.ecommerceposapp.domain.UserSession
import com.ecommerce.ecommerceposapp.domain.repository.categories.CategoryRepository
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository
import com.ecommerce.ecommerceposapp.domain.repository.users.UserRepository
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository as DomainAuthRepository
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository as DomainCatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository as DomainSyncRepository
import com.ecommerce.ecommerceposapp.domain.repository.auth.LoginMode as DomainLoginMode
import io.realm.Realm
import io.realm.RealmObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.io.File
import java.io.OutputStreamWriter
import kotlin.math.round
import kotlin.math.roundToInt
import org.json.JSONArray
import org.json.JSONObject

enum class LoginMode { OfflineOnly, OnlineOnly }

interface AuthRepository {
    fun login(email: String, password: String, mode: LoginMode): Result<UserSession>
    fun getSession(): UserSession?
    fun logout()
    /** True si ya hay usuario local y catálogo sincronizado al menos una vez (permite modo offline). */
    fun canUseOfflineLogin(): Boolean
}

interface CatalogRepository {
    fun categories(): List<CategoryItem>
    fun subcategories(): List<SubcategoryItem>
    fun products(): List<ProductItem>
    fun registerSale(lines: List<CartLine>, payment: SalePaymentInfo, idCliente: Long): Result<CompletedSaleReceipt>
    fun emitComprobanteForVenta(ventaId: Long, tipo: TipoComprobanteEmision, idCliente: Long): Result<ComprobanteEmitidoResult>
    /** Nombre y número de documento del cliente, si existe en maestros. */
    fun getClienteDisplay(idCliente: Long): Pair<String, String>?
    /** Teléfono del cliente en maestros (vacío si no hay). */
    fun getClienteTelefono(idCliente: Long): String?
    /** Asocia un cliente ya existente a la venta (p. ej. tras buscarlo al enviar WhatsApp). */
    fun actualizarClienteEnVenta(ventaId: Long, idCliente: Long): Result<Unit>
    fun listSalesHistory(): List<SalesHistoryRow>
    fun getSaleReceipt(ventaId: Long): Result<CompletedSaleReceipt>
}

interface SyncRepository {
    fun hasInitialSync(userId: Long): Boolean
    fun syncInitialData(user: UserSession): Result<Unit>
    fun listSyncModuleStatus(): List<SyncModuleStatus>
    fun syncModules(user: UserSession, modules: Set<String>): Result<Unit>
}

class PosRepositoryImpl(private val context: Context) :
    DomainAuthRepository,
    DomainCatalogRepository,
    DomainSyncRepository {

    override fun login(email: String, password: String, mode: DomainLoginMode): Result<UserSession> =
        login(email, password, when (mode) {
            DomainLoginMode.OfflineOnly -> LoginMode.OfflineOnly
            DomainLoginMode.OnlineOnly -> LoginMode.OnlineOnly
        })

    private val prefs = context.getSharedPreferences("pos_prefs", Context.MODE_PRIVATE)
    private data class ApiBaseCandidate(val baseUrl: String, val hostHeader: String? = null)
    private val remoteApiBaseCandidates = listOf(
        ApiBaseCandidate("https://prestomartperu.com"),
    )
    private val allSyncModules = listOf("productos", "imagenes_productos", "categorias", "subcategorias", "clientes", "proveedores", "usuarios", "ventas")

    /**
     * Si Realm aún no tiene usuario + sync inicial, rellena el mismo demo que tras sincronizar.
     * Así el modo offline está disponible desde el primer arranque (POS demo sin pasar obligatoriamente por online).
     */
    private fun ensureLocalDataForOfflineLogin() {
        val ready = realmQuery { realm ->
            realm.where(UserRealm::class.java).equalTo("active", true).count() > 0L &&
                realm.where(SyncStateRealm::class.java).equalTo("id", 1L).findFirst()?.initialSyncDone == true
        }
        if (ready) return
        syncInitialData(
            UserSession(
                id = 1L,
                email = "admin@gmail.com",
                name = "admin",
                role = "Cajero admin",
                offlineSession = false,
            ),
        )
    }

    override fun canUseOfflineLogin(): Boolean {
        ensureLocalDataForOfflineLogin()
        return realmQuery { realm ->
            val users = realm.where(UserRealm::class.java).equalTo("active", true).count()
            val sync = realm.where(SyncStateRealm::class.java).equalTo("id", 1L).findFirst()
            users > 0L && sync?.initialSyncDone == true
        }
    }

    fun login(email: String, password: String, mode: LoginMode): Result<UserSession> {
        if (email.isBlank() || password.isBlank()) return Result.failure(Exception("Completa usuario y contraseña."))
        if (mode == LoginMode.OfflineOnly && !canUseOfflineLogin()) {
            return Result.failure(Exception("Primera vez o sin sincronización: use modo en línea y sincronice el catálogo."))
        }
        return when (mode) {
            LoginMode.OfflineOnly -> offlineLogin(email, password)
            LoginMode.OnlineOnly -> onlineLogin(email, password)
        }
    }

    override fun getSession(): UserSession? {
        val id = prefs.getLong("session_user_id", 0)
        if (id <= 0) return null
        return UserSession(
            id = id,
            email = prefs.getString("session_email", "") ?: "",
            name = prefs.getString("session_name", "") ?: "",
            role = prefs.getString("session_role", "admin") ?: "admin",
            offlineSession = prefs.getBoolean("session_offline", true),
        )
    }

    override fun logout() {
        prefs.edit()
            .remove("session_user_id")
            .remove("session_email")
            .remove("session_name")
            .remove("session_role")
            .remove("session_offline")
            .apply()
    }

    override fun categories(): List<CategoryItem> = realmQuery {
        it.where(CategoryRealm::class.java).equalTo("active", true).findAll()
            .map { c -> CategoryItem(c.id, c.name, c.active) }
    }

    override fun subcategories(): List<SubcategoryItem> = realmQuery {
        it.where(SubcategoryRealm::class.java).equalTo("active", true).findAll()
            .filter { s ->
                it.where(CategoryRealm::class.java)
                    .equalTo("id", s.categoryId)
                    .equalTo("active", true)
                    .findFirst() != null
            }
            .map { s -> SubcategoryItem(s.id, s.categoryId, s.name, s.active) }
    }

    override fun products(): List<ProductItem> = realmQuery {
        it.where(ProductRealm::class.java).equalTo("active", true).findAll()
            .filter { p -> productHasActiveCategoryPath(it, p) }
            .map { p ->
                val rawUrl = normalizedProductImageUrl(p.id, p.imageUrl)
                // Si es file://, verifica que el archivo exista (p.ej. tras reinstalación)
                val resolvedUrl = if (rawUrl.startsWith("file://")) {
                    if (File(rawUrl.removePrefix("file://")).exists()) rawUrl else ""
                } else {
                    rawUrl
                }
                ProductItem(
                    id = p.id,
                    categoryId = p.categoryId,
                    subcategoryId = p.subcategoryId,
                    name = p.name,
                    price = p.price,
                    stock = p.stock,
                    code = p.codigo,
                    imageUrl = resolvedUrl,
                    active = p.active,
                )
            }
    }

    override fun registerSale(lines: List<CartLine>, payment: SalePaymentInfo, idCliente: Long): Result<CompletedSaleReceipt> {
        if (lines.isEmpty()) return Result.failure(Exception("El carrito está vacío."))
        val session = getSession() ?: return Result.failure(Exception("Sin sesión de usuario."))
        val linesCopy = lines.map { it.copy() }
        val fechaMillis = System.currentTimeMillis()
        lateinit var receipt: CompletedSaleReceipt
        realmWrite { realm ->
            ensureFinanzaSeed(realm)
            val sesionId = ensureSesionAbierta(realm, session.id)
            val subtotal = round(lines.sumOf { it.lineTotal } * 100) / 100
            val igv = round(subtotal * 0.18 * 100) / 100
            val total = round((subtotal + igv) * 100) / 100
            val ventaId = nextId(realm, FinanzaVentaRealm::class.java)
            val numero = "TICK-$ventaId-${System.currentTimeMillis() % 1_000_000}"
            realm.insertOrUpdate(
                FinanzaVentaRealm().apply {
                    id = ventaId
                    numeroComprobante = numero
                    tipoComprobante = "TICK"
                    idSesion = sesionId
                    idUsuario = session.id
                    this.idCliente = idCliente.coerceAtLeast(0L)
                    fechaVenta = fechaMillis
                    this.subtotal = subtotal
                    this.igv = igv
                    descuento = 0.0
                    this.total = total
                    tipoPago = payment.tipoPago
                    montoRecibido = payment.montoRecibido
                    vuelto = payment.vuelto
                    estado = "A"
                    motivoAnulacion = ""
                },
            )
            lines.forEach { line ->
                val detId = nextId(realm, FinanzaVentaDetalleRealm::class.java)
                val lineSub = round(line.unitPrice * line.quantity * 100) / 100
                realm.insertOrUpdate(
                    FinanzaVentaDetalleRealm().apply {
                        id = detId
                        idVenta = ventaId
                        idProducto = line.productId
                        nombreProducto = line.productName
                        codigoBarras = ""
                        cantidad = line.quantity.toDouble()
                        precioUnitario = line.unitPrice
                        descuento = 0.0
                        this.subtotal = lineSub
                    },
                )
                val product = realm.where(ProductRealm::class.java).equalTo("id", line.productId).findFirst() ?: return@forEach
                product.stock = (product.stock - line.quantity).coerceAtLeast(0.0)
            }
            receipt = CompletedSaleReceipt(
                ventaId = ventaId,
                numeroTicket = numero,
                subtotal = subtotal,
                igv = igv,
                total = total,
                tipoPago = payment.tipoPago,
                montoRecibido = payment.montoRecibido,
                vuelto = payment.vuelto,
                fechaMillis = fechaMillis,
                lines = linesCopy,
                vendedorNombre = session.name,
                idCliente = idCliente.coerceAtLeast(0L),
            )
        }
        return Result.success(receipt)
    }

    override fun emitComprobanteForVenta(ventaId: Long, tipo: TipoComprobanteEmision, idCliente: Long): Result<ComprobanteEmitidoResult> {
        if (tipo == TipoComprobanteEmision.SOLO_TICKET) {
            return realmQuery { realm ->
                val venta = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
                    ?: return@realmQuery Result.failure(Exception("Venta no encontrada."))
                val emisor = realm.where(FinanzaEmisorConfigRealm::class.java).equalTo("activo", true).findFirst()
                    ?: realm.where(FinanzaEmisorConfigRealm::class.java).findFirst()
                val ruc = emisor?.ruc ?: ""
                val rs = emisor?.razonSocial ?: ""
                val dir = emisor?.direccion ?: ""
                val letras = montoEnLetrasSoles(venta.total)
                val qr = buildQrPayload(
                    ruc = ruc,
                    tipoDoc = "TICK",
                    serie = "",
                    correlativo = 0,
                    igv = venta.igv,
                    total = venta.total,
                    fechaSunat = formatFechaSunat(venta.fechaVenta),
                    numeroCompleto = venta.numeroComprobante,
                )
                Result.success(
                    ComprobanteEmitidoResult(
                        tipoSunat = "TICK",
                        numeroCompleto = venta.numeroComprobante,
                        serie = "",
                        correlativo = 0,
                        qrPayload = qr,
                        emisorRuc = ruc,
                        emisorRazonSocial = rs,
                        emisorDireccion = dir,
                        totalLetras = letras,
                    ),
                )
            }
        }
        var out: Result<ComprobanteEmitidoResult> = Result.failure(Exception("Sin resultado."))
        realmWrite { realm ->
            ensureFinanzaSeed(realm)
            val venta = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
            if (venta == null) {
                out = Result.failure(Exception("Venta no encontrada."))
                return@realmWrite
            }
            val tipoSunat = if (tipo == TipoComprobanteEmision.BOLETA) "03" else "01"
            val serieRow = realm.where(FinanzaComprobanteSerieRealm::class.java)
                .equalTo("tipoComprobante", tipoSunat)
                .equalTo("activo", true)
                .findFirst()
            if (serieRow == null) {
                out = Result.failure(Exception("No hay serie configurada para el comprobante."))
                return@realmWrite
            }
            val emisor = realm.where(FinanzaEmisorConfigRealm::class.java).equalTo("activo", true).findFirst()
                ?: realm.where(FinanzaEmisorConfigRealm::class.java).findFirst()
            if (emisor == null) {
                out = Result.failure(Exception("Falta configuración del emisor."))
                return@realmWrite
            }
            val idCli = (if (idCliente > 0L) idCliente else venta.idCliente).coerceAtLeast(0L)
            val client = if (idCli > 0L) {
                realm.where(ClientRealm::class.java).equalTo("id", idCli).findFirst()
            } else {
                null
            }
            serieRow.correlativoActual = serieRow.correlativoActual + 1
            val corr = serieRow.correlativoActual
            val serie = serieRow.serie
            val numeroCompleto = "$serie-${String.format(Locale.US, "%08d", corr)}"
            val fechaSunat = formatFechaSunat(venta.fechaVenta)
            val letras = montoEnLetrasSoles(venta.total)
            val (receptorTipo, receptorNum, receptorNombre) = when {
                client == null -> Triple("0", "", "CLIENTE VARIOS")
                client.document.trim().length == 11 -> Triple("6", client.document.trim(), client.name.ifBlank { "CLIENTE" })
                else -> Triple("1", client.document.trim().ifBlank { "00000000" }, client.name.ifBlank { "CLIENTE" })
            }
            val comprobanteId = nextId(realm, FinanzaComprobanteRealm::class.java)
            val qr = buildQrPayload(
                ruc = emisor.ruc,
                tipoDoc = tipoSunat,
                serie = serie,
                correlativo = corr,
                igv = venta.igv,
                total = venta.total,
                fechaSunat = fechaSunat,
                numeroCompleto = numeroCompleto,
            )
            realm.insertOrUpdate(
                FinanzaComprobanteRealm().apply {
                    id = comprobanteId
                    tipoComprobante = tipoSunat
                    this.serie = serie
                    correlativo = corr
                    this.numeroCompleto = numeroCompleto
                    idVenta = ventaId
                    idComprobanteRef = 0L
                    serieRef = ""
                    correlativoRef = 0
                    tipoNotaCredito = ""
                    motivoNota = ""
                    emisorRuc = emisor.ruc
                    emisorRazonSocial = emisor.razonSocial
                    emisorDireccion = emisor.direccion
                    emisorUbigeo = emisor.ubigeo
                    receptorTipoDoc = receptorTipo
                    receptorNumDoc = receptorNum
                    receptorRazonSocial = receptorNombre
                    receptorDireccion = ""
                    receptorEmail = ""
                    subtotalGravado = venta.subtotal
                    subtotalInafecto = 0.0
                    subtotalExonerado = 0.0
                    totalIgv = venta.igv
                    totalDescuentos = venta.descuento
                    total = venta.total
                    totalLetras = letras
                    formaPago = "Contado"
                    montoPagado = venta.montoRecibido
                    estadoSunat = "REGISTRADO_LOCAL"
                    codigoRespuesta = ""
                    mensajeRespuesta = ""
                    hashCdr = ""
                    nombreXml = ""
                    rutaXml = ""
                    rutaCdr = ""
                    rutaPdf = ""
                    codigoHash = ""
                    fechaEmision = fechaSunat
                    fechaEnvioSunat = 0L
                    fechaRespuestaSunat = 0L
                    idUsuario = venta.idUsuario
                },
            )
            val detalles = realm.where(FinanzaVentaDetalleRealm::class.java).equalTo("idVenta", ventaId).findAll()
            var orden = 0
            detalles.forEach { d ->
                orden += 1
                val precioConIgv = round(d.precioUnitario * 1.18 * 100) / 100
                val subLinea = round(d.subtotal * 100) / 100
                val igvLinea = round(subLinea * 0.18 * 100) / 100
                val totalLinea = round((subLinea + igvLinea) * 100) / 100
                val detId = nextId(realm, FinanzaComprobanteDetalleRealm::class.java)
                realm.insertOrUpdate(
                    FinanzaComprobanteDetalleRealm().apply {
                        id = detId
                        idComprobante = comprobanteId
                        this.orden = orden
                        codigoProducto = d.idProducto.toString()
                        descripcion = d.nombreProducto
                        unidadMedida = "NIU"
                        cantidad = d.cantidad
                        valorUnitario = round(d.precioUnitario * 100) / 100
                        precioUnitario = precioConIgv
                        descuentoMonto = d.descuento
                        subtotalLinea = subLinea
                        this.igvLinea = igvLinea
                        this.totalLinea = totalLinea
                        tipoAfectacionIgv = "10"
                    },
                )
            }
            venta.numeroComprobante = numeroCompleto
            venta.tipoComprobante = tipoSunat
            if (idCli > 0L) venta.idCliente = idCli
            out = Result.success(
                ComprobanteEmitidoResult(
                    tipoSunat = tipoSunat,
                    numeroCompleto = numeroCompleto,
                    serie = serie,
                    correlativo = corr,
                    qrPayload = qr,
                    emisorRuc = emisor.ruc,
                    emisorRazonSocial = emisor.razonSocial,
                    emisorDireccion = emisor.direccion,
                    totalLetras = letras,
                ),
            )
        }
        return out
    }

    override fun getClienteDisplay(idCliente: Long): Pair<String, String>? {
        if (idCliente <= 0L) return null
        return realmQuery { realm ->
            val c = realm.where(ClientRealm::class.java).equalTo("id", idCliente).findFirst() ?: return@realmQuery null
            c.name to c.document
        }
    }

    override fun getClienteTelefono(idCliente: Long): String? {
        if (idCliente <= 0L) return null
        return realmQuery { realm ->
            realm.where(ClientRealm::class.java).equalTo("id", idCliente).findFirst()?.phone?.trim()?.takeIf { it.isNotBlank() }
        }
    }

    override fun actualizarClienteEnVenta(ventaId: Long, idCliente: Long): Result<Unit> {
        if (ventaId <= 0L) return Result.failure(Exception("Venta inválida."))
        realmWrite { realm ->
            val v = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
                ?: throw Exception("Venta no encontrada.")
            v.idCliente = idCliente.coerceAtLeast(0L)
        }
        return Result.success(Unit)
    }

    override fun listSalesHistory(): List<SalesHistoryRow> = realmQuery { realm ->
        realm.where(FinanzaVentaRealm::class.java)
            .findAll()
            .sortedByDescending { it.fechaVenta }
            .map { v ->
                val clienteNombre = if (v.idCliente > 0L) {
                    realm.where(ClientRealm::class.java).equalTo("id", v.idCliente).findFirst()?.name
                } else {
                    null
                } ?: "Sin cliente"
                val cajeroNombre = realm.where(UserRealm::class.java).equalTo("id", v.idUsuario).findFirst()?.name ?: "admin"
                SalesHistoryRow(
                    ventaId = v.id,
                    numeroComprobante = v.numeroComprobante,
                    tipoComprobante = v.tipoComprobante,
                    fechaMillis = v.fechaVenta,
                    clienteNombre = clienteNombre,
                    cajeroNombre = cajeroNombre,
                    tipoPago = v.tipoPago,
                    total = v.total,
                    estado = v.estado,
                    idCliente = v.idCliente,
                )
            }
    }

    override fun getSaleReceipt(ventaId: Long): Result<CompletedSaleReceipt> = realmQuery { realm ->
        val v = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
            ?: return@realmQuery Result.failure(Exception("Venta no encontrada."))
        val lines = realm.where(FinanzaVentaDetalleRealm::class.java).equalTo("idVenta", ventaId).findAll()
            .map { d ->
                CartLine(
                    productId = d.idProducto,
                    productName = d.nombreProducto,
                    unitPrice = d.precioUnitario,
                    quantity = d.cantidad.toInt().coerceAtLeast(1),
                )
            }
        val vendedor = realm.where(UserRealm::class.java).equalTo("id", v.idUsuario).findFirst()?.name ?: "admin"
        Result.success(
            CompletedSaleReceipt(
                ventaId = v.id,
                numeroTicket = v.numeroComprobante,
                subtotal = v.subtotal,
                igv = v.igv,
                total = v.total,
                tipoPago = v.tipoPago,
                montoRecibido = v.montoRecibido,
                vuelto = v.vuelto,
                fechaMillis = v.fechaVenta,
                lines = lines,
                vendedorNombre = vendedor,
                idCliente = v.idCliente,
            ),
        )
    }

    override fun hasInitialSync(userId: Long): Boolean = realmQuery {
        it.where(SyncStateRealm::class.java).equalTo("id", 1L).findFirst()
            ?.let { state -> state.initialSyncDone && state.syncedUserId == userId } ?: false
    }

    override fun syncInitialData(user: UserSession): Result<Unit> {
        return syncModules(user, allSyncModules.toSet())
    }

    override fun listSyncModuleStatus(): List<SyncModuleStatus> = realmQuery { realm ->
        allSyncModules.map { key ->
            val label = when (key) {
                "imagenes_productos" -> "Imágenes de productos"
                else -> key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "PE")) else it.toString() }
            }
            val row = realm.where(SyncModuleStateRealm::class.java).equalTo("moduleKey", key).findFirst()
            SyncModuleStatus(key = key, label = label, lastSyncAt = row?.lastSyncAt ?: 0L)
        }
    }

    override fun syncModules(user: UserSession, modules: Set<String>): Result<Unit> {
        val selected = modules.intersect(allSyncModules.toSet())
        if (selected.isEmpty()) return Result.failure(Exception("Seleccione al menos un módulo para sincronizar."))
        val includeImageSync = "imagenes_productos" in selected
        val remote = fetchRemoteCatalogBestEffort()
        val now = System.currentTimeMillis()
        if ("categorias" in selected && remote.categories.isEmpty()) {
            return Result.failure(Exception("No se recibieron categorias desde https://prestomartperu.com."))
        }
        if ("productos" in selected && remote.products.isEmpty()) {
            return Result.failure(Exception("No se recibieron productos desde https://prestomartperu.com."))
        }

        // Fase 1: escribir datos en Realm (sin I/O de red dentro de la transacción)
        realmWrite { realm ->
            if ("categorias" in selected) {
                realm.where(CategoryRealm::class.java).findAll().deleteAllFromRealm()
                remote.categories.forEach { rc ->
                    realm.insertOrUpdate(CategoryRealm().apply {
                        id = rc.id
                        name = rc.name
                        active = rc.active
                    })
                }
            }
            if ("subcategorias" in selected && remote.subcategories.isNotEmpty()) {
                realm.where(SubcategoryRealm::class.java).findAll().deleteAllFromRealm()
                remote.subcategories.forEach { rs ->
                    val category = realm.where(CategoryRealm::class.java).equalTo("id", rs.categoryId).findFirst()
                    if (category != null) {
                        realm.insertOrUpdate(SubcategoryRealm().apply {
                            id = rs.id
                            categoryId = rs.categoryId
                            name = rs.name
                            active = rs.active && category.active
                        })
                    }
                }
            }
            if ("productos" in selected) {
                realm.where(ProductRealm::class.java).findAll().deleteAllFromRealm()
                remote.products.mapNotNull { rp ->
                    if (rp.subcategoryId <= 0L) rp.subcategoryName?.trim().takeUnless { n -> n.isNullOrBlank() }?.let { rp.categoryId to it } else null
                }.distinct().forEach { (categoryId, subcatName) ->
                    if (categoryId > 0L && realm.where(CategoryRealm::class.java).equalTo("id", categoryId).findFirst() != null) {
                        val existing = realm.where(SubcategoryRealm::class.java)
                            .equalTo("categoryId", categoryId)
                            .equalTo("name", subcatName, io.realm.Case.INSENSITIVE)
                            .findFirst()
                        if (existing == null) {
                            realm.insertOrUpdate(SubcategoryRealm().apply {
                                id = nextId(realm, SubcategoryRealm::class.java)
                                this.categoryId = categoryId
                                name = subcatName
                                active = true
                            })
                        }
                    }
                }
                remote.products.forEach { rp ->
                    val remoteCatName = rp.categoryName?.trim().orEmpty()
                    val catId = if (remoteCatName.isNotBlank()) {
                        realm.where(CategoryRealm::class.java).equalTo("name", remoteCatName, io.realm.Case.INSENSITIVE).findFirst()?.id
                    } else {
                        null
                    } ?: rp.categoryId.takeIf { it > 0L } ?: return@forEach
                    val subcatId = resolveSubcategoryId(realm, catId, rp.subcategoryId, rp.subcategoryName)
                    realm.insertOrUpdate(ProductRealm().apply {
                        id = if (rp.id > 0L) rp.id else nextId(realm, ProductRealm::class.java)
                        categoryId = catId
                        subcategoryId = subcatId
                        name = rp.name
                        codigo = rp.code
                        imageUrl = normalizedProductImageUrl(id, rp.imageUrl)
                        price = rp.price
                        stock = rp.stock
                        active = rp.active
                    })
                }
            }
            if (includeImageSync) {
                // Limpiar URLs de picsum (no hay I/O de red aquí)
                realm.where(ProductRealm::class.java).findAll().forEach { p ->
                    if (p.imageUrl.contains("picsum.photos", ignoreCase = true)) p.imageUrl = ""
                }
            }
            if ("clientes" in selected && realm.where(ClientRealm::class.java).count() == 0L) {
                realm.insertOrUpdate(ClientRealm().apply {
                    id = 1
                    name = "Consumidor final"
                    document = ""
                    phone = ""
                    active = true
                })
            }
            if ("proveedores" in selected && realm.where(SupplierRealm::class.java).count() == 0L) {
                realm.insertOrUpdate(SupplierRealm().apply {
                    id = 1
                    businessName = "Proveedor demo"
                    ruc = ""
                    phone = ""
                    active = true
                })
            }
            // Tras migración/borrado de Realm puede haber sesión sin fila local: sin esto nunca se habilita login offline.
            if ("usuarios" in selected && realm.where(UserRealm::class.java).equalTo("id", user.id).findFirst() == null) {
                realm.insertOrUpdate(UserRealm().apply {
                    id = user.id
                    email = user.email.trim()
                    name = user.name
                    role = user.role.ifBlank { "admin" }
                    password = hash("123456789")
                    active = true
                })
            }
            realm.insertOrUpdate(SyncStateRealm().apply {
                id = 1
                syncedUserId = user.id
                initialSyncDone = true
            })
            if ("ventas" in selected) ensureFinanzaSeed(realm)
            selected.forEach { key ->
                realm.insertOrUpdate(
                    SyncModuleStateRealm().apply {
                        moduleKey = key
                        lastSyncAt = now
                    },
                )
            }
        }

        // Fase 2: descargar imágenes FUERA de la transacción (I/O de red separado)
        if (includeImageSync) {
            cacheProductImages()
        }

        return Result.success(Unit)
    }

    private fun ensureFinanzaSeed(realm: Realm) {
        if (realm.where(FinanzaCajaRealm::class.java).count() == 0L) {
            realm.insertOrUpdate(
                FinanzaCajaRealm().apply {
                    id = 1
                    nombreCaja = "Caja principal"
                    descripcion = "Caja POS (offline)"
                    activo = true
                    createdAt = System.currentTimeMillis()
                    updatedAt = 0L
                },
            )
        }
        if (realm.where(FinanzaEmisorConfigRealm::class.java).count() == 0L) {
            realm.insertOrUpdate(
                FinanzaEmisorConfigRealm().apply {
                    id = 1
                    ruc = "20123456789"
                    razonSocial = "Minimarket Demo SAC"
                    direccion = "Av. Demo 123 — Lima"
                    ubigeo = "150101"
                    activo = true
                },
            )
        }
        if (realm.where(FinanzaComprobanteSerieRealm::class.java).count() == 0L) {
            realm.insertOrUpdate(
                FinanzaComprobanteSerieRealm().apply {
                    id = 1
                    tipoComprobante = "03"
                    serie = "B001"
                    correlativoActual = 0
                    activo = true
                },
            )
        }
        if (realm.where(FinanzaComprobanteSerieRealm::class.java).equalTo("tipoComprobante", "01").count() == 0L) {
            realm.insertOrUpdate(
                FinanzaComprobanteSerieRealm().apply {
                    id = nextId(realm, FinanzaComprobanteSerieRealm::class.java)
                    tipoComprobante = "01"
                    serie = "F001"
                    correlativoActual = 0
                    activo = true
                },
            )
        }
    }

    private fun ensureSesionAbierta(realm: Realm, userId: Long): Long {
        val cajaId = 1L
        val abierta = realm.where(FinanzaSesionCajaRealm::class.java)
            .equalTo("idCaja", cajaId)
            .equalTo("estado", "A")
            .findFirst()
        if (abierta != null) return abierta.id
        val newId = nextId(realm, FinanzaSesionCajaRealm::class.java)
        realm.insertOrUpdate(
            FinanzaSesionCajaRealm().apply {
                id = newId
                idCaja = cajaId
                idUsuario = userId
                fechaApertura = System.currentTimeMillis()
                montoApertura = 0.0
                fechaCierre = 0L
                montoCierre = 0.0
                estado = "A"
                observaciones = ""
            },
        )
        return newId
    }

    private fun upsertUser(row: UserRow, plainPassword: String?): Result<Unit> {
        if (row.email.isBlank() || row.name.isBlank()) return Result.failure(Exception("Nombre y correo son obligatorios."))
        val emailTaken = realmQuery { realm ->
            val u = realm.where(UserRealm::class.java)
                .equalTo("email", row.email, io.realm.Case.INSENSITIVE)
                .findFirst()
            u != null && u.id != row.id
        }
        if (emailTaken) return Result.failure(Exception("Ya existe otro usuario con ese correo."))
        val existingPassword = realmQuery { realm ->
            realm.where(UserRealm::class.java).equalTo("id", row.id).findFirst()?.password
        }
        val pwd = when {
            !plainPassword.isNullOrBlank() -> hash(plainPassword)
            existingPassword != null -> existingPassword
            else -> return Result.failure(Exception("Indique contraseña para el usuario nuevo."))
        }
        realmWrite { realm ->
            val id = if (row.id == 0L) nextId(realm, UserRealm::class.java) else row.id
            realm.insertOrUpdate(UserRealm().apply {
                this.id = id
                email = row.email.trim()
                name = row.name.trim()
                role = row.role.ifBlank { "admin" }
                active = row.active
                password = pwd
            })
        }
        return Result.success(Unit)
    }

    private fun deleteUser(id: Long, currentUserId: Long): Result<Unit> {
        if (id == currentUserId) return Result.failure(Exception("No puede eliminar el usuario de la sesión actual."))
        realmWrite { realm ->
            val u = realm.where(UserRealm::class.java).equalTo("id", id).findFirst() ?: return@realmWrite
            u.active = false
        }
        return Result.success(Unit)
    }

    private fun listSuppliers(): List<SupplierRow> = realmQuery { realm ->
        realm.where(SupplierRealm::class.java).equalTo("active", true).findAll().map { s ->
            SupplierRow(s.id, s.businessName, s.ruc, s.phone, s.active)
        }
    }

    private fun upsertSupplier(row: SupplierRow): Result<Unit> {
        if (row.businessName.isBlank()) return Result.failure(Exception("Razón social obligatoria."))
        realmWrite { realm ->
            val id = if (row.id == 0L) nextId(realm, SupplierRealm::class.java) else row.id
            realm.insertOrUpdate(SupplierRealm().apply {
                this.id = id
                businessName = row.businessName.trim()
                ruc = row.ruc.trim()
                phone = row.phone.trim()
                active = row.active
            })
        }
        return Result.success(Unit)
    }

    private fun deleteSupplier(id: Long): Result<Unit> {
        realmWrite { realm ->
            val s = realm.where(SupplierRealm::class.java).equalTo("id", id).findFirst() ?: return@realmWrite
            s.active = false
        }
        return Result.success(Unit)
    }

    private fun onlineLogin(email: String, password: String): Result<UserSession> {
        val apiSession = fetchApiSession(email, password)
        if (apiSession != null) {
            val user = UserSession(
                id = if (apiSession.userId > 0L) apiSession.userId else 1L,
                email = email,
                name = apiSession.name.ifBlank { "admin" },
                role = "Cajero admin",
                offlineSession = false,
            )
            realmWrite { realm ->
                realm.insertOrUpdate(UserRealm().apply {
                    id = user.id
                    this.email = user.email
                    name = user.name
                    this.password = hash(password)
                    role = user.role
                    active = true
                })
            }
            prefs.edit()
                .putString("api_base_url", apiSession.baseUrl)
                .putString("api_host_header", apiSession.hostHeader ?: "")
                .putString("api_token", apiSession.token)
                .apply()
            saveSession(user)
            return Result.success(user)
        }
        if (email != "admin@gmail.com" || password != "123456789") {
            return Result.failure(Exception("Credenciales inválidas para API y modo demo local."))
        }
        val user = UserSession(1, email, "admin", "Cajero admin", offlineSession = false)
        realmWrite { realm ->
            realm.insertOrUpdate(UserRealm().apply {
                id = user.id
                this.email = user.email
                name = user.name
                this.password = hash(password)
                role = user.role
                active = true
            })
        }
        saveSession(user)
        return Result.success(user)
    }

    private fun offlineLogin(email: String, password: String): Result<UserSession> {
        // No devolver UserRealm fuera de realmQuery: .use {} cierra la instancia y el objeto queda inválido.
        val user = realmQuery { realm ->
            val u = realm.where(UserRealm::class.java).equalTo("email", email, io.realm.Case.INSENSITIVE).findFirst()
                ?: return@realmQuery null
            LocalUserRow(
                id = u.id,
                email = u.email,
                name = u.name,
                role = u.role,
                passwordHash = u.password,
                active = u.active,
            )
        } ?: return Result.failure(Exception("No existe usuario local. Use modo en línea la primera vez."))

        if (!user.active) return Result.failure(Exception("Usuario inactivo."))
        if (user.passwordHash != hash(password)) return Result.failure(Exception("Contraseña offline incorrecta."))
        if (!hasInitialSync(user.id)) return Result.failure(Exception("Sincronización inicial pendiente para este usuario."))

        val session = UserSession(user.id, user.email, user.name, user.role, offlineSession = true)
        saveSession(session)
        return Result.success(session)
    }

    private fun saveSession(user: UserSession) {
        prefs.edit()
            .putLong("session_user_id", user.id)
            .putString("session_email", user.email)
            .putString("session_name", user.name)
            .putString("session_role", user.role)
            .putBoolean("session_offline", user.offlineSession)
            .apply()
    }

    private fun formatFechaSunat(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("America/Lima")
        }
        return sdf.format(Date(millis))
    }

    private fun buildQrPayload(
        ruc: String,
        tipoDoc: String,
        serie: String,
        correlativo: Int,
        igv: Double,
        total: Double,
        fechaSunat: String,
        numeroCompleto: String,
    ): String = listOf(
        ruc,
        tipoDoc,
        serie,
        String.format(Locale.US, "%08d", correlativo),
        String.format(Locale.US, "%.2f", igv),
        String.format(Locale.US, "%.2f", total),
        fechaSunat,
        "0",
        numeroCompleto,
        "",
    ).joinToString("|")

    private fun montoEnLetrasSoles(monto: Double): String {
        val entero = monto.toLong().coerceAtLeast(0L)
        val centavos = ((monto - entero) * 100).roundToInt().coerceIn(0, 99)
        val letras = enteroEnLetrasEsp(entero).uppercase(Locale("es", "PE"))
        return "Son: $letras CON ${String.format(Locale.US, "%02d", centavos)}/100 SOLES"
    }

    private fun enteroEnLetrasEsp(n: Long): String {
        if (n == 0L) return "cero"
        if (n < 0L) return "menos ${enteroEnLetrasEsp(-n)}"
        if (n >= 1_000_000) return n.toString()
        val parts = mutableListOf<String>()
        var rest = n
        if (rest >= 1000) {
            val miles = rest / 1000
            rest %= 1000
            parts += when {
                miles == 1L -> "mil"
                else -> "${letrasMenorMil(miles)} mil"
            }
        }
        if (rest > 0L) parts += letrasMenorMil(rest)
        return parts.joinToString(" ").trim()
    }

    private fun letrasMenorMil(n: Long): String {
        require(n in 0..999)
        if (n == 0L) return ""
        if (n < 16) return unidadesCortas(n.toInt())
        if (n < 20) return when (n.toInt()) {
            16 -> "dieciséis"
            17 -> "diecisiete"
            18 -> "dieciocho"
            19 -> "diecinueve"
            else -> ""
        }
        if (n < 100) {
            val u = (n % 10).toInt()
            val d = (n / 10).toInt()
            if (d == 2 && u > 0) return "veinti${unidadesCortas(u)}"
            val dec = decenas[d]
            return if (u == 0) dec else "$dec y ${unidadesCortas(u)}"
        }
        val c = (n / 100).toInt()
        val rest = n % 100
        val ciento = when {
            c == 1 && rest == 0L -> "cien"
            c == 1 -> "ciento"
            else -> centenas[c]
        }
        if (rest == 0L) return ciento
        return "$ciento ${letrasMenorMil(rest)}".trim()
    }

    private fun unidadesCortas(i: Int): String = when (i) {
        0 -> "cero"
        1 -> "uno"
        2 -> "dos"
        3 -> "tres"
        4 -> "cuatro"
        5 -> "cinco"
        6 -> "seis"
        7 -> "siete"
        8 -> "ocho"
        9 -> "nueve"
        10 -> "diez"
        11 -> "once"
        12 -> "doce"
        13 -> "trece"
        14 -> "catorce"
        15 -> "quince"
        else -> ""
    }

    private val decenas = arrayOf("", "", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa")
    private val centenas = arrayOf("", "cien", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos")

    private fun nextId(realm: Realm, clazz: Class<out RealmObject>): Long {
        val max = realm.where(clazz).max("id") as Number?
        return (max?.toLong() ?: 0L) + 1L
    }

    private fun resolveSubcategoryId(realm: Realm, categoryId: Long, remoteSubcategoryId: Long, remoteSubcategoryName: String?): Long {
        val byId = if (remoteSubcategoryId > 0L) {
            realm.where(SubcategoryRealm::class.java)
                .equalTo("id", remoteSubcategoryId)
                .equalTo("categoryId", categoryId)
                .findFirst()
        } else {
            null
        }
        if (byId != null) return byId.id
        val name = remoteSubcategoryName?.trim().orEmpty()
        if (name.isBlank()) return 0L
        return realm.where(SubcategoryRealm::class.java)
            .equalTo("categoryId", categoryId)
            .equalTo("name", name, io.realm.Case.INSENSITIVE)
            .findFirst()
            ?.id ?: 0L
    }

    private fun productHasActiveCategoryPath(realm: Realm, product: ProductRealm): Boolean {
        val category = realm.where(CategoryRealm::class.java)
            .equalTo("id", product.categoryId)
            .equalTo("active", true)
            .findFirst() ?: return false
        if (!category.active) return false
        if (product.subcategoryId <= 0L) return true
        return realm.where(SubcategoryRealm::class.java)
            .equalTo("id", product.subcategoryId)
            .equalTo("categoryId", product.categoryId)
            .equalTo("active", true)
            .findFirst() != null
    }

    private fun hash(value: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(value.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun normalizedProductImageUrl(productId: Long, raw: String): String {
        val input = raw.trim()
        if (input.isBlank()) return ""
        if (input.startsWith("file://")) return input

        val preferredBase = prefs.getString("api_base_url", null)
            ?: remoteApiBaseCandidates.first().baseUrl

        // Construir URL completa si es ruta relativa
        val fullUrl = when {
            input.startsWith("http://") || input.startsWith("https://") -> input
            input.startsWith("/") -> "$preferredBase$input"
            else -> "$preferredBase/$input"
        }

        // Reemplazar cualquier hostname *.localhost o 127.0.0.1 con la IP del emulador,
        // preservando el puerto original (ej: prestomart.localhost:81 → 10.0.3.2:81)
        return runCatching {
            val u = URL(fullUrl)
            val host = u.host
            if (host == "localhost" || host == "127.0.0.1" || host.endsWith(".localhost")) {
                val port = if (u.port > 0) ":${u.port}" else ""
                "http://10.0.3.2$port${u.file}"
            } else {
                fullUrl
            }
        }.getOrDefault(fullUrl)
    }

    private data class RemoteProductSeed(
        val id: Long,
        val categoryId: Long,
        val subcategoryId: Long,
        val categoryName: String?,
        val subcategoryName: String?,
        val name: String,
        val code: String,
        val imageUrl: String,
        val price: Double,
        val stock: Double,
        val active: Boolean,
    )

    private data class RemoteCategorySeed(
        val id: Long,
        val name: String,
        val active: Boolean,
    )

    private data class RemoteSubcategorySeed(
        val id: Long,
        val categoryId: Long,
        val name: String,
        val active: Boolean,
    )

    private data class RemoteCatalogSeed(
        val categories: List<RemoteCategorySeed> = emptyList(),
        val subcategories: List<RemoteSubcategorySeed> = emptyList(),
        val products: List<RemoteProductSeed> = emptyList(),
    )

    private fun fetchRemoteCatalogBestEffort(): RemoteCatalogSeed {
        val preferredBase = prefs.getString("api_base_url", null)
        val preferredHost = prefs.getString("api_host_header", null)?.takeIf { it.isNotBlank() }
        val preferred = preferredBase?.let { ApiBaseCandidate(it, preferredHost) }
        val bases = listOfNotNull(preferred) + remoteApiBaseCandidates.filter { it.baseUrl != preferredBase }
        for (base in bases) {
            val catalog = runCatching {
                fetchCatalogFromEndpoint("${base.baseUrl}/api/app/finanza/sync/catalog", base)
            }.getOrNull()
            if (catalog != null && (catalog.categories.isNotEmpty() || catalog.products.isNotEmpty())) {
                val categories = catalog.categories.ifEmpty {
                    runCatching {
                        fetchCategoriesFromEndpoint("${base.baseUrl}/api/producto_categoria/listar", base)
                    }.getOrDefault(emptyList())
                }
                val subcategories = catalog.subcategories.ifEmpty {
                    runCatching {
                        fetchSubcategoriesFromEndpoint("${base.baseUrl}/api/producto_categoria_sub/listar", base)
                    }.getOrDefault(emptyList())
                }
                return catalog.copy(categories = categories, subcategories = subcategories)
            }
            val categories = runCatching {
                fetchCategoriesFromEndpoint("${base.baseUrl}/api/producto_categoria/listar", base)
            }.getOrDefault(emptyList())
            if (categories.isNotEmpty()) {
                val subcategories = runCatching {
                    fetchSubcategoriesFromEndpoint("${base.baseUrl}/api/producto_categoria_sub/listar", base)
                }.getOrDefault(emptyList())
                return RemoteCatalogSeed(categories = categories, subcategories = subcategories)
            }
        }
        return RemoteCatalogSeed()
    }

    private fun cacheProductImages() {
        val dir = File(context.filesDir, "product_images").apply { mkdirs() }

        // Fase A: leer datos de productos fuera de transacción
        data class ImageTask(val id: Long, val sourceUrl: String, val ext: String)

        val tasks = realmQuery { realm ->
            realm.where(ProductRealm::class.java).equalTo("active", true).findAll()
                .mapNotNull { p ->
                    val source = normalizedProductImageUrl(p.id, p.imageUrl)
                    if (source.isBlank() || source.startsWith("file://")) return@mapNotNull null
                    val ext = when {
                        source.contains(".png", ignoreCase = true) -> "png"
                        source.contains(".webp", ignoreCase = true) -> "webp"
                        else -> "jpg"
                    }
                    ImageTask(p.id, source, ext)
                }
        }

        if (tasks.isEmpty()) return

        // Fase B: descargar imágenes (sin Realm abierto)
        val updates = mutableMapOf<Long, String>()
        tasks.forEach { task ->
            val target = File(dir, "p_${task.id}.${task.ext}")
            runCatching {
                val conn = (URL(task.sourceUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 8000
                    readTimeout = 12000
                    requestMethod = "GET"
                    addAuthHeaderIfAvailable(this)
                    // Agregar Host header: usa prefs si hay, sino deduce desde candidatos hardcodeados
                    val savedHost = prefs.getString("api_host_header", "")?.trim().orEmpty()
                    if (savedHost.isNotBlank()) {
                        setRequestProperty("Host", savedHost)
                    } else {
                        val urlHost = runCatching { URL(task.sourceUrl).host }.getOrNull()
                        remoteApiBaseCandidates
                            .firstOrNull { runCatching { URL(it.baseUrl).host == urlHost }.getOrNull() == true }
                            ?.hostHeader
                            ?.let { setRequestProperty("Host", it) }
                    }
                }
                conn.inputStream.use { input ->
                    target.outputStream().use { output -> input.copyTo(output) }
                }
                if (target.length() > 0) {
                    updates[task.id] = "file://${target.absolutePath}"
                }
            }
        }

        // Fase C: guardar rutas file:// en Realm con transacción separada
        if (updates.isNotEmpty()) {
            realmWrite { realm ->
                updates.forEach { (id, filePath) ->
                    realm.where(ProductRealm::class.java).equalTo("id", id).findFirst()?.imageUrl = filePath
                }
            }
        }
    }

    private fun fetchCatalogFromEndpoint(url: String, base: ApiBaseCandidate): RemoteCatalogSeed {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 6000
            readTimeout = 6000
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
            addAuthHeaderIfAvailable(this)
        }
        conn.inputStream.bufferedReader().use { reader ->
            return parseRemoteCatalog(reader.readText().trim(), base.baseUrl)
        }
    }

    private fun fetchCategoriesFromEndpoint(url: String, base: ApiBaseCandidate): List<RemoteCategorySeed> {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 6000
            readTimeout = 6000
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
            addAuthHeaderIfAvailable(this)
        }
        conn.inputStream.bufferedReader().use { reader ->
            return parseRemoteCategories(reader.readText().trim())
        }
    }

    private fun fetchSubcategoriesFromEndpoint(url: String, base: ApiBaseCandidate): List<RemoteSubcategorySeed> {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 6000
            readTimeout = 6000
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
            addAuthHeaderIfAvailable(this)
        }
        conn.inputStream.bufferedReader().use { reader ->
            return parseRemoteSubcategories(reader.readText().trim())
        }
    }

    private fun fetchProductsFromEndpoint(url: String, base: ApiBaseCandidate): List<RemoteProductSeed> {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 6000
            readTimeout = 6000
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
            addAuthHeaderIfAvailable(this)
        }
        conn.inputStream.bufferedReader().use { reader ->
            val payload = reader.readText().trim()
            return parseRemoteProducts(payload, base.baseUrl)
        }
    }

    private fun parseRemoteCatalog(payload: String, base: String): RemoteCatalogSeed {
        val root: Any = if (payload.trim().startsWith("[")) JSONArray(payload) else JSONObject(payload)
        val rootObj = root as? JSONObject
        val categoriesArray = rootObj?.optArrayAny("categories", "categorias", "categorias_producto", "producto_categoria")
        val subcategoriesArray = rootObj?.optArrayAny("subcategories", "subcategorias", "producto_categoria_sub", "categoria_sub")
        val productsArray = rootObj?.optArrayAny("products", "productos", "items", "articulos", "result")
            ?: (root as? JSONArray)
        return RemoteCatalogSeed(
            categories = categoriesArray?.let { parseRemoteCategories(it) }.orEmpty(),
            subcategories = subcategoriesArray?.let { parseRemoteSubcategories(it) }.orEmpty(),
            products = productsArray?.let { parseRemoteProducts(it, base) }.orEmpty(),
        )
    }

    private fun parseRemoteCategories(payload: String): List<RemoteCategorySeed> {
        val root: Any = if (payload.trim().startsWith("[")) JSONArray(payload) else JSONObject(payload)
        val array = (root as? JSONObject)?.optArrayAny("result", "categories", "categorias", "data")
            ?: (root as? JSONArray)
            ?: return emptyList()
        return parseRemoteCategories(array)
    }

    private fun parseRemoteCategories(array: JSONArray): List<RemoteCategorySeed> {
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            val id = obj.optAnyLong("id_producto_categoria", "category_id", "id_categoria", "id")
            val name = obj.optAnyString("nombre", "name", "category_name", "categoria")
            if (id <= 0L || name.isBlank()) null else RemoteCategorySeed(id, name, obj.optActive())
        }.distinctBy { it.id }
    }

    private fun parseRemoteSubcategories(payload: String): List<RemoteSubcategorySeed> {
        val root: Any = if (payload.trim().startsWith("[")) JSONArray(payload) else JSONObject(payload)
        val array = (root as? JSONObject)?.optArrayAny("result", "subcategories", "subcategorias", "data")
            ?: (root as? JSONArray)
            ?: return emptyList()
        return parseRemoteSubcategories(array)
    }

    private fun parseRemoteSubcategories(array: JSONArray): List<RemoteSubcategorySeed> {
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            val id = obj.optAnyLong("id_producto_categoria_sub", "subcategory_id", "id_subcategoria", "subcategoria_id", "id")
            val categoryId = obj.optAnyLong("id_producto_categoria", "category_id", "id_categoria", "categoria_id")
            val name = obj.optAnyString("nombre", "name", "subcategory_name", "subcategoria", "nombre_subcategoria")
            if (id <= 0L || categoryId <= 0L || name.isBlank()) null else RemoteSubcategorySeed(id, categoryId, name, obj.optActive())
        }.distinctBy { it.id }
    }

    private fun JSONObject.optArrayAny(vararg keys: String): JSONArray? {
        keys.forEach { key ->
            val value = opt(key)
            if (value is JSONArray) return value
        }
        return null
    }

    private fun JSONObject.optAnyString(vararg keys: String): String {
        keys.forEach { key ->
            val raw = opt(key)
            val value = when (raw) {
                is String -> raw
                is Number -> raw.toString()
                is Boolean -> raw.toString()
                else -> ""
            }.trim()
            if (value.isNotBlank() && !value.equals("null", ignoreCase = true)) return value
        }
        return ""
    }

    private fun JSONObject.optAnyLong(vararg keys: String): Long {
        keys.forEach { key ->
            when (val raw = opt(key)) {
                is Number -> return raw.toLong()
                is String -> raw.trim().toLongOrNull()?.let { return it }
            }
        }
        return 0L
    }

    private fun JSONObject.optActive(default: Boolean = true): Boolean {
        val raw = opt("Activo").takeUnless { it == null }
            ?: opt("Active").takeUnless { it == null }
            ?: opt("active").takeUnless { it == null }
            ?: opt("activo").takeUnless { it == null }
            ?: return default
        return when (raw) {
            is Boolean -> raw
            is Number -> raw.toInt() != 0
            is String -> raw.trim().let { value ->
                value.equals("S", true) ||
                    value.equals("SI", true) ||
                    value.equals("TRUE", true) ||
                    value == "1" ||
                    value.equals("A", true)
            }
            else -> default
        }
    }

    private fun parseRemoteProducts(payload: String, base: String): List<RemoteProductSeed> {
        val root: Any = if (payload.trim().startsWith("[")) JSONArray(payload) else JSONObject(payload)
        return parseRemoteProducts(root, base)
    }

    private fun parseRemoteProducts(root: Any, base: String): List<RemoteProductSeed> {
        val imageFields = listOf("url_imagen", "image_url", "image", "imagen", "foto", "thumbnail", "thumb", "imagen_url", "img", "photo")
        val nameFields = listOf("nombre", "name", "title", "producto", "descripcion")
        val priceFields = listOf("precio", "price", "pvp", "sale_price", "precio_venta")
        val stockFields = listOf("stock", "cantidad", "existencia", "stock_actual")
        val codeFields = listOf("codigo_producto", "code", "codigo", "sku", "barcode", "codigo_barras")
        val categoryIdFields = listOf("id_producto_categoria", "category_id", "id_categoria", "categoria_id", "id_categoria_producto")
        val subcategoryIdFields = listOf("id_producto_categoria_sub", "subcategory_id", "id_subcategoria", "subcategoria_id")
        val categoryNameFields = listOf("category_name", "categoria", "category", "nombre_categoria")
        val subcategoryNameFields = listOf("subcategory_name", "subcategoria", "subcategory", "nombre_subcategoria", "nombre_sub_categoria")

        fun optAnyString(obj: JSONObject, keys: List<String>): String {
            keys.forEach { k ->
                val v = obj.optString(k, "").trim()
                if (v.isNotBlank() && !v.equals("null", ignoreCase = true)) return v
            }
            return ""
        }

        fun optAnyDouble(obj: JSONObject, keys: List<String>): Double {
            keys.forEach { k ->
                val raw = obj.opt(k)
                when (raw) {
                    is Number -> return raw.toDouble()
                    is String -> raw.replace(",", ".").toDoubleOrNull()?.let { return it }
                }
            }
            return 0.0
        }

        fun optAnyLong(obj: JSONObject, keys: List<String>): Long {
            keys.forEach { k ->
                val raw = obj.opt(k)
                when (raw) {
                    is Number -> return raw.toLong()
                    is String -> raw.toLongOrNull()?.let { return it }
                }
            }
            return 0L
        }

        fun normalizeImage(rawImage: String): String {
            val raw = rawImage.trim()
            return when {
                raw.isBlank() -> ""
                raw.startsWith("http://") || raw.startsWith("https://") -> raw
                raw.startsWith("/") -> "$base$raw"
                else -> "$base/$raw"
            }
        }

        fun mapObj(obj: JSONObject): RemoteProductSeed? {
            val name = optAnyString(obj, nameFields)
            val price = optAnyDouble(obj, priceFields)
            if (name.isBlank() || price <= 0.0) return null
            // Soporta id_producto (prestomart), id, product_id
            val id = obj.optLong("id_producto", obj.optLong("id", obj.optLong("product_id", 0L)))
            val categoryId = optAnyLong(obj, categoryIdFields).takeIf { it > 0L } ?: 1L
            val subcategoryId = optAnyLong(obj, subcategoryIdFields)
            val categoryName = optAnyString(obj, categoryNameFields).ifBlank { null }
            val subcategoryName = optAnyString(obj, subcategoryNameFields).ifBlank { null }
            val code = optAnyString(obj, codeFields)
            val imageUrl = normalizeImage(optAnyString(obj, imageFields))
            val stock = optAnyDouble(obj, stockFields)
            return RemoteProductSeed(
                id = id,
                categoryId = categoryId,
                subcategoryId = subcategoryId,
                categoryName = categoryName,
                subcategoryName = subcategoryName,
                name = name,
                code = code,
                imageUrl = imageUrl,
                price = price,
                stock = stock,
                active = obj.optActive(),
            )
        }

        fun collectObjects(node: Any?, out: MutableList<JSONObject>) {
            when (node) {
                is JSONObject -> {
                    out += node
                    val it = node.keys()
                    while (it.hasNext()) {
                        val k = it.next()
                        collectObjects(node.opt(k), out)
                    }
                }
                is JSONArray -> {
                    for (i in 0 until node.length()) collectObjects(node.opt(i), out)
                }
            }
        }

        val allObjects = mutableListOf<JSONObject>()
        collectObjects(root, allObjects)
        return allObjects.mapNotNull { mapObj(it) }.distinctBy { "${it.id}-${it.code}-${it.name}" }
    }

    private data class ApiSession(val baseUrl: String, val hostHeader: String?, val token: String, val userId: Long, val name: String)

    private fun fetchApiSession(email: String, password: String): ApiSession? {
        val candidates = remoteApiBaseCandidates
        for (base in candidates) {
            val loginUrl = "${base.baseUrl}/api/login"
            val fromJson = runCatching { tryLoginJson(loginUrl, base, email, password) }.getOrNull()
            if (fromJson != null) return fromJson
            val fromForm = runCatching { tryLoginForm(loginUrl, base, email, password) }.getOrNull()
            if (fromForm != null) return fromForm
        }
        return null
    }

    private fun tryLoginJson(url: String, base: ApiBaseCandidate, email: String, password: String): ApiSession? {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 7000
            readTimeout = 7000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
        }
        val body = JSONObject().apply {
            put("email", email)
            put("password", password)
        }.toString()
        OutputStreamWriter(conn.outputStream).use { it.write(body) }
        if (conn.responseCode !in 200..299) return null
        val payload = conn.inputStream.bufferedReader().use { it.readText() }
        return parseLoginPayload(base, payload)
    }

    private fun tryLoginForm(url: String, base: ApiBaseCandidate, email: String, password: String): ApiSession? {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 7000
            readTimeout = 7000
            doOutput = true
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
        }
        val body = "email=${URLEncoder.encode(email, "UTF-8")}&password=${URLEncoder.encode(password, "UTF-8")}"
        OutputStreamWriter(conn.outputStream).use { it.write(body) }
        if (conn.responseCode !in 200..299) return null
        val payload = conn.inputStream.bufferedReader().use { it.readText() }
        return parseLoginPayload(base, payload)
    }

    private fun parseLoginPayload(base: ApiBaseCandidate, payload: String): ApiSession? {
        val obj = runCatching { JSONObject(payload) }.getOrNull() ?: return null
        val token = obj.optString("token", obj.optString("access_token", obj.optString("jwt", ""))).trim()
        if (token.isBlank()) return null
        val userObj = obj.optJSONObject("user") ?: obj.optJSONObject("usuario")
        val userId = userObj?.optLong("id", 1L) ?: 1L
        val name = if (userObj != null) {
            userObj.optString("name", userObj.optString("nombre", "admin"))
        } else {
            "admin"
        }
        return ApiSession(baseUrl = base.baseUrl, hostHeader = base.hostHeader, token = token, userId = userId, name = name)
    }

    private fun addAuthHeaderIfAvailable(conn: HttpURLConnection) {
        val token = prefs.getString("api_token", "")?.trim().orEmpty()
        if (token.isNotBlank()) conn.setRequestProperty("Authorization", "Bearer $token")
    }

    private fun addHostHeaderIfAvailable(conn: HttpURLConnection) {
        val host = prefs.getString("api_host_header", "")?.trim().orEmpty()
        if (host.isNotBlank()) conn.setRequestProperty("Host", host)
    }

    private fun <T> realmQuery(block: (Realm) -> T): T = Realm.getDefaultInstance().use(block)
    private fun realmWrite(block: (Realm) -> Unit) = Realm.getDefaultInstance().use { realm ->
        realm.executeTransaction { block(it) }
    }
}

/** Copia de campos de [UserRealm] válida tras cerrar la instancia de Realm. */
private data class LocalUserRow(
    val id: Long,
    val email: String,
    val name: String,
    val role: String,
    val passwordHash: String,
    val active: Boolean,
)
