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
import com.ecommerce.ecommerceposapp.data.remote.api.ApiConfig
import com.ecommerce.ecommerceposapp.data.remote.api.ApiSessionStore
import com.ecommerce.ecommerceposapp.data.remote.api.AuthApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.ClientApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.CashApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.ProductImageApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.PosSaleApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.RemoteCatalogDataSource
import com.ecommerce.ecommerceposapp.data.repository.pos.AmountInWordsFormatter
import com.ecommerce.ecommerceposapp.data.repository.products.ProductRepositoryImpl
import com.ecommerce.ecommerceposapp.data.security.OfflineCredentialVerifier
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.model.sync.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.catalog.CategoryItem
import com.ecommerce.ecommerceposapp.domain.model.clients.ClientRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductItem
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.catalog.SubcategoryItem
import com.ecommerce.ecommerceposapp.domain.model.suppliers.SupplierRow
import com.ecommerce.ecommerceposapp.domain.model.users.UserRow
import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashRegister
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary
import com.ecommerce.ecommerceposapp.domain.repository.categories.CategoryRepository
import com.ecommerce.ecommerceposapp.domain.repository.clients.ClientRepository
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository
import com.ecommerce.ecommerceposapp.domain.repository.suppliers.SupplierRepository
import com.ecommerce.ecommerceposapp.domain.repository.users.UserRepository
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository as DomainAuthRepository
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository as DomainCatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.sync.SyncRepository as DomainSyncRepository
import io.realm.Realm
import io.realm.RealmObject
import java.net.URL
import java.io.IOException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.io.File
import kotlin.math.round
import org.json.JSONObject

class PosRepositoryImpl(private val context: Context) :
    DomainAuthRepository,
    DomainCatalogRepository,
    DomainSyncRepository {

    private val prefs = context.getSharedPreferences("pos_prefs", Context.MODE_PRIVATE)
    private val offlineCredentials = OfflineCredentialVerifier(prefs)
    private val authApi = AuthApiDataSource(context)
    private val clientApi = ClientApiDataSource(context)
    private val cashApi = CashApiDataSource(context)
    private val remoteCatalog = RemoteCatalogDataSource(context)
    private val remoteSale = PosSaleApiDataSource(context)
    private val productImagesApi = ProductImageApiDataSource(context)
    private val pendingProducts = ProductRepositoryImpl(context)
    private var lastCashRegisters: List<CashRegister> = emptyList()
    private val allSyncModules = listOf("productos", "imagenes_productos", "categorias", "subcategorias", "clientes", "proveedores", "usuarios", "ventas")

    override fun login(email: String, password: String): Result<UserSession> {
        if (email.isBlank() || password.isBlank()) return Result.failure(Exception("Completa usuario y contraseña."))
        val online = onlineLogin(email, password)
        if (online.isSuccess) return online
        val error = online.exceptionOrNull()
        if (error !is IOException) return online
        val cached = getSession()
        if (cached == null || cached.email.trim().lowercase() != email.trim().lowercase()) {
            return Result.failure(Exception("Sin Internet. Este cajero todavía no tiene acceso offline en este dispositivo."))
        }
        if (!offlineCredentials.verify(email, password.toCharArray())) {
            return Result.failure(Exception("No se pudo validar el acceso offline. Conéctate a Internet para renovar la autorización."))
        }
        val offline = cached.copy(offlineSession = true)
        saveSession(offline)
        return Result.success(offline)
    }

    override fun canLoginOffline(email: String): Boolean = offlineCredentials.isAvailableFor(email)

    override fun loginOffline(email: String, password: String): Result<UserSession> {
        val cached = getSession()
        if (cached == null || cached.email.trim().lowercase() != email.trim().lowercase()) {
            return Result.failure(Exception("Este cajero todavía no tiene acceso offline en este dispositivo."))
        }
        if (!offlineCredentials.verify(email, password.toCharArray())) {
            return Result.failure(Exception("Credenciales offline incorrectas. Conéctate para renovar el acceso."))
        }
        return Result.success(cached.copy(offlineSession = true).also(::saveSession))
    }

    override fun getSession(): UserSession? {
        val id = prefs.getLong("session_user_id", 0)
        if (id <= 0) return null
        return UserSession(
            id = id,
            email = prefs.getString("session_email", "") ?: "",
            name = prefs.getString("session_name", "") ?: "",
            role = prefs.getString("session_role", "admin") ?: "admin",
            offlineSession = prefs.getBoolean("session_offline", false),
            cashierId = prefs.getLong("session_cashier_id", 0L),
            defaultCashRegisterId = prefs.getLong("session_default_cash_register_id", 0L),
            defaultCashRegisterName = prefs.getString("session_default_cash_register_name", "") ?: "",
            document = prefs.getString("session_document", "") ?: "",
            phone = prefs.getString("session_phone", "") ?: "",
            address = prefs.getString("session_address", "") ?: "",
            branchName = prefs.getString("session_branch_name", "") ?: "",
            lastName = prefs.getString("session_last_name", "") ?: "",
            documentType = prefs.getString("session_document_type", "DNI") ?: "DNI",
            cashierState = prefs.getString("session_cashier_state", "Activo") ?: "Activo",
            avatar = prefs.getString("session_avatar", "") ?: "",
            avatarBase64 = prefs.getString("session_avatar_base64", "") ?: "",
        )
    }

    override fun logout() {
        offlineCredentials.clear()
        prefs.edit()
            .remove("session_user_id")
            .remove("session_email")
            .remove("session_name")
            .remove("session_role")
            .remove("session_offline")
            .remove("session_cashier_id")
            .remove("session_default_cash_register_id")
            .remove("session_default_cash_register_name")
            .remove("api_token")
            .remove("api_refresh_token")
            .remove("pos_cash_session_id")
            .remove("session_avatar")
            .remove("session_avatar_base64")
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
        val featuredIds = prefs.getStringSet("featured_pos_products", emptySet()).orEmpty()
        it.where(ProductRealm::class.java).equalTo("active", true).findAll()
            .filter { p -> p.canalVenta.trim().lowercase() in setOf("fisica", "ambos") }
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
                    barcode = p.barcode,
                    imageUrl = resolvedUrl,
                    salesChannel = p.canalVenta.ifBlank { "ambos" },
                    featuredInPos = p.id.toString() in featuredIds,
                    active = p.active,
                )
            }
            .sortedWith(compareByDescending<ProductItem> { it.featuredInPos }.thenBy { it.name.lowercase(Locale.getDefault()) })
    }

    override fun setProductFeatured(productId: Long, featured: Boolean) {
        val ids = prefs.getStringSet("featured_pos_products", emptySet()).orEmpty().toMutableSet()
        if (featured) ids += productId.toString() else ids -= productId.toString()
        prefs.edit().putStringSet("featured_pos_products", ids).apply()
    }

    override fun refreshCatalog(): Result<Unit> {
        val session = getSession() ?: return Result.failure(Exception("Sin sesion de usuario."))
        return syncModules(session, setOf("categorias", "subcategorias", "productos"))
    }

    override fun registerSale(
        lines: List<CartLine>,
        payment: SalePaymentInfo,
        idCliente: Long,
        customerInfo: ReceiptCustomerInfo,
        receiptType: TipoComprobanteEmision,
    ): Result<CompletedSaleReceipt> {
        if (lines.isEmpty()) return Result.failure(Exception("El carrito está vacío."))
        val session = getSession() ?: return Result.failure(Exception("Sin sesión de usuario."))
        val cashSessionId = prefs.getLong("pos_cash_session_id", 0L)
        val registered = remoteSale.registerSale(
            lines,
            payment,
            idCliente,
            cashSessionId,
            customerInfo,
            receiptType,
        ).getOrElse { return Result.failure(it) }
        val linesCopy = lines.map { it.copy() }
        val fechaMillis = System.currentTimeMillis()
        lateinit var receipt: CompletedSaleReceipt
        realmWrite { realm ->
            val sesionId = cashSessionId
            val total = round(lines.sumOf { it.lineTotal } * 100) / 100
            val subtotal = round((total / 1.18) * 100) / 100
            val igv = round((total - subtotal) * 100) / 100
            val ventaId = registered.id
            val numero = registered.number
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
                val lineTotal = round(line.unitPrice * line.quantity * 100) / 100
                val lineSub = round((lineTotal / 1.18) * 100) / 100
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
                clienteNombre = customerInfo.name.trim(),
                clienteDocumento = customerInfo.document.filter(Char::isDigit),
            )
        }
        return Result.success(receipt)
    }

    override fun emitComprobanteForVenta(ventaId: Long, tipo: TipoComprobanteEmision, idCliente: Long, customerInfo: ReceiptCustomerInfo): Result<ComprobanteEmitidoResult> {
        if (tipo == TipoComprobanteEmision.SOLO_TICKET) {
            return realmQuery { realm ->
                val venta = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
                    ?: return@realmQuery Result.failure(Exception("Venta no encontrada."))
                val emisor = realm.where(FinanzaEmisorConfigRealm::class.java).equalTo("activo", true).findFirst()
                    ?: realm.where(FinanzaEmisorConfigRealm::class.java).findFirst()
                val ruc = emisor?.ruc ?: ""
                val rs = emisor?.razonSocial ?: ""
                val dir = emisor?.direccion ?: ""
                val client = venta.idCliente.takeIf { it > 0L }?.let { clientId ->
                    realm.where(ClientRealm::class.java).equalTo("id", clientId).findFirst()
                }
                val letras = AmountInWordsFormatter.soles(venta.total)
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
                        receptorNombre = client?.name.orEmpty(),
                        receptorDocumento = client?.document.orEmpty(),
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
            val idCli = (if (customerInfo.id > 0L) customerInfo.id else if (idCliente > 0L) idCliente else venta.idCliente).coerceAtLeast(0L)
            val client = if (idCli > 0L) {
                realm.where(ClientRealm::class.java).equalTo("id", idCli).findFirst()
            } else {
                null
            }
            val manualDoc = customerInfo.document.trim()
            val manualName = customerInfo.name.trim()
            serieRow.correlativoActual = serieRow.correlativoActual + 1
            val corr = serieRow.correlativoActual
            val serie = serieRow.serie
            val numeroCompleto = "$serie-${String.format(Locale.US, "%08d", corr)}"
            val fechaSunat = formatFechaSunat(venta.fechaVenta)
            val letras = AmountInWordsFormatter.soles(venta.total)
            val (receptorTipo, receptorNum, receptorNombre) = when {
                manualDoc.length == 11 -> Triple("6", manualDoc, manualName.ifBlank { "CLIENTE" })
                manualDoc.isNotBlank() -> Triple("1", manualDoc, manualName.ifBlank { "CLIENTE" })
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
                val precioConIgv = round(d.precioUnitario * 100) / 100
                val subLinea = round(d.subtotal * 100) / 100
                val totalLinea = round(d.precioUnitario * d.cantidad * 100) / 100
                val igvLinea = round((totalLinea - subLinea) * 100) / 100
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
                        valorUnitario = round((d.precioUnitario / 1.18) * 100) / 100
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
                    receptorNombre = receptorNombre,
                    receptorDocumento = receptorNum,
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

    override fun listSalesHistory(): List<SalesHistoryRow> {
        val sessionId = prefs.getLong("pos_cash_session_id", 0L)
        if (sessionId <= 0L) return emptyList()
        return cashApi.listSales(sessionId).getOrThrow()
    }

    override fun getSaleReceipt(ventaId: Long): Result<CompletedSaleReceipt> = cashApi.getSaleReceipt(ventaId).map { remote ->
        realmQuery { realm ->
            val localSale = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
            val localReceipt = realm.where(FinanzaComprobanteRealm::class.java)
                .equalTo("idVenta", ventaId)
                .sort("id", io.realm.Sort.DESCENDING)
                .findFirst()
            remote.copy(
                subtotal = localSale?.subtotal?.takeIf { it > 0.0 } ?: remote.subtotal,
                igv = localSale?.igv?.takeIf { it > 0.0 } ?: remote.igv,
                montoRecibido = localSale?.montoRecibido?.takeIf { it > 0.0 } ?: remote.montoRecibido,
                vuelto = localSale?.vuelto?.takeIf { it > 0.0 } ?: remote.vuelto,
                clienteNombre = remote.clienteNombre.ifBlank { localReceipt?.receptorRazonSocial.orEmpty() },
                clienteDocumento = remote.clienteDocumento.ifBlank { localReceipt?.receptorNumDoc.orEmpty() },
            )
        }
    }

    override fun listCashRegisters(): Result<List<CashRegister>> = cashApi.listCashRegisters()
        .onSuccess { registers ->
            lastCashRegisters = registers
            val preferredId = prefs.getLong("pos_cash_register_id", 0L).takeIf { it > 0L }
                ?: getSession()?.defaultCashRegisterId?.takeIf { it > 0L }
            cacheEmitterFromCashRegisters(registers, preferredId)
        }
        .recoverCatching { error ->
            if (error !is IOException) throw error
            val session = getSession() ?: throw error
            listOf(CashRegister(session.defaultCashRegisterId, "", session.defaultCashRegisterName, "", true))
                .filter { it.id > 0L }
        }

    override fun findOpenCashSession(cashierId: Long): Result<CashSession?> = cashApi.findOpenSession(cashierId)
        .onSuccess { session ->
            cacheCashSession(session)
            session?.let { cacheEmitterFromCashRegisters(lastCashRegisters, it.cashRegisterId) }
        }
        .recoverCatching { error -> if (error is IOException) cachedCashSession(cashierId) else throw error }

    override fun openCashSession(cashRegisterId: Long, cashierId: Long, openingAmount: Double): Result<CashSession> =
        cashApi.openSession(cashRegisterId, cashierId, openingAmount).onSuccess {
            cacheCashSession(it)
            cacheEmitterFromCashRegisters(lastCashRegisters, it.cashRegisterId)
        }

    override fun cashSummary(sessionId: Long): Result<CashSummary> = cashApi.summary(sessionId)

    override fun closeCashSession(sessionId: Long, countedCash: Double, observations: String): Result<Unit> =
        cashApi.closeSession(sessionId, countedCash, observations).onSuccess {
            cacheCashSession(null)
        }

    override fun cancelSale(ventaId: Long, comment: String, restoreStock: Boolean): Result<Unit> =
        cashApi.cancelSale(ventaId, comment, restoreStock)

    private fun cacheCashSession(session: CashSession?) {
        prefs.edit().apply {
            putLong("pos_cash_session_id", session?.id ?: 0L)
            putLong("pos_cash_register_id", session?.cashRegisterId ?: 0L)
            putString("pos_cash_register_name", session?.cashRegisterName ?: "")
            putLong("pos_cashier_id", session?.cashierId ?: 0L)
            putString("pos_cashier_name", session?.cashierName ?: "")
            putLong("pos_cash_opened_at", session?.openedAt ?: 0L)
            putFloat("pos_cash_opening_amount", (session?.openingAmount ?: 0.0).toFloat())
            apply()
        }
    }

    private fun cachedCashSession(cashierId: Long): CashSession? {
        val id = prefs.getLong("pos_cash_session_id", 0L)
        if (id <= 0L || prefs.getLong("pos_cashier_id", cashierId) != cashierId) return null
        return CashSession(
            id = id,
            cashRegisterId = prefs.getLong("pos_cash_register_id", 0L),
            cashRegisterName = prefs.getString("pos_cash_register_name", "") ?: "",
            cashierId = cashierId,
            cashierName = prefs.getString("pos_cashier_name", "") ?: "",
            openedAt = prefs.getLong("pos_cash_opened_at", 0L),
            openingAmount = prefs.getFloat("pos_cash_opening_amount", 0f).toDouble(),
            status = "Abierta",
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
        if ("productos" in selected) pendingProducts.syncPendingProducts()
        val remote = remoteCatalog.fetchBestEffort()
        val remoteClients = if ("clientes" in selected) clientApi.list().getOrNull() else null
        val now = System.currentTimeMillis()
        if ("categorias" in selected && remote.categories.isEmpty()) {
            return Result.failure(Exception("No se recibieron categorías desde ${ApiSessionStore(context).baseUrl}."))
        }
        if ("productos" in selected && remote.products.isEmpty()) {
            return Result.failure(Exception("No se recibieron productos desde ${ApiSessionStore(context).baseUrl}."))
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
                realm.where(ProductRealm::class.java).notEqualTo("syncState", "PENDING").findAll().deleteAllFromRealm()
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
                    val pendingLocal = realm.where(ProductRealm::class.java)
                        .equalTo("id", rp.id)
                        .equalTo("syncState", "PENDING")
                        .findFirst()
                    if (pendingLocal != null) return@forEach
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
                        barcode = rp.barcode
                        slug = rp.slug
                        description = rp.description
                        location = rp.location
                        canalVenta = rp.salesChannel.ifBlank { "ambos" }
                        imageUrl = normalizedProductImageUrl(id, rp.imageUrl)
                        price = rp.price
                        stock = rp.stock
                        oldPrice = rp.oldPrice
                        costPrice = rp.costPrice
                        wholesalePrice = rp.wholesalePrice
                        wholesaleOldPrice = rp.wholesaleOldPrice
                        yapePrice = rp.yapePrice
                        minimumStock = rp.minimumStock
                        productTypeId = rp.productTypeId
                        ratingsEnabled = rp.ratingsEnabled
                        adminRating = rp.adminRating
                        packageMeasures = rp.packageMeasures
                        packageDimension = rp.packageDimension
                        weightKg = rp.weightKg
                        promoCutoffTime = rp.promoCutoffTime
                        saturdayCutoffTime = rp.saturdayCutoffTime
                        offerMaxQuantity = rp.offerMaxQuantity
                        offerMaxQuantityPrice = rp.offerMaxQuantityPrice
                        metaTitle = rp.metaTitle
                        metaDescription = rp.metaDescription
                        active = rp.active
                        localCreatedAt = rp.createdAt
                        remoteCreatedAt = rp.createdAt
                        remoteUpdatedAt = rp.updatedAt
                        syncState = "SYNCED"
                        syncError = ""
                    })
                }
            }
            if (includeImageSync) {
                // Limpiar URLs de picsum (no hay I/O de red aquí)
                realm.where(ProductRealm::class.java).findAll().forEach { p ->
                    if (p.imageUrl.contains("picsum.photos", ignoreCase = true)) p.imageUrl = ""
                }
            }
            if (remoteClients != null) {
                realm.where(ClientRealm::class.java).findAll().deleteAllFromRealm()
                remoteClients.forEach { client ->
                    realm.insertOrUpdate(ClientRealm().apply {
                        id = client.id
                        name = client.name
                        document = client.document
                        phone = client.phone
                        lastName = client.lastName
                        email = client.email
                        address = client.address
                        businessName = client.businessName
                        active = client.active
                    })
                }
                prefs.edit().putBoolean("clients_remote_cache_ready", true).apply()
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

    private fun cacheEmitterFromCashRegisters(registers: List<CashRegister>, preferredCashRegisterId: Long? = null) {
        val validRegisters = registers.filter { it.ruc.isNotBlank() && it.businessName.isNotBlank() }
        val emitter = validRegisters.firstOrNull { it.id == preferredCashRegisterId } ?: validRegisters.firstOrNull()
        realmWrite { realm ->
            if (emitter == null) {
                realm.where(FinanzaEmisorConfigRealm::class.java).findAll().deleteAllFromRealm()
                return@realmWrite
            }
            realm.where(FinanzaEmisorConfigRealm::class.java).findAll().forEach { it.activo = false }
            realm.insertOrUpdate(
                FinanzaEmisorConfigRealm().apply {
                    id = 1
                    ruc = emitter.ruc
                    razonSocial = emitter.businessName
                    direccion = emitter.address
                    ubigeo = ""
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
    private fun onlineLogin(email: String, password: String): Result<UserSession> {
        val apiSession = authApi.login(email, password).getOrElse { return Result.failure(it) }
        if (apiSession.userId <= 0L || apiSession.cashierId <= 0L) {
            return Result.failure(Exception("La cuenta no tiene un perfil de cajero POS válido."))
        }
        val user = UserSession(
            id = apiSession.userId,
            email = email,
            name = apiSession.name,
            role = "Cajero POS",
            offlineSession = false,
            cashierId = apiSession.cashierId,
            defaultCashRegisterId = apiSession.defaultCashRegisterId,
            defaultCashRegisterName = apiSession.defaultCashRegisterName,
            document = apiSession.document,
            phone = apiSession.phone,
            address = apiSession.address,
            branchName = apiSession.branchName,
            lastName = apiSession.lastName,
            documentType = apiSession.documentType,
            cashierState = apiSession.cashierState,
            avatar = apiSession.avatar,
            avatarBase64 = apiSession.avatarBase64,
        )
        prefs.edit()
            .putString("api_base_url", apiSession.baseUrl)
            .putString("api_host_header", apiSession.hostHeader ?: "")
            .putString("api_token", apiSession.token)
            .putString("api_refresh_token", apiSession.refreshToken)
            .apply()
        saveSession(user)
        offlineCredentials.remember(email, password.toCharArray())
        return Result.success(user)
    }

    private fun saveSession(user: UserSession) {
        prefs.edit()
            .putLong("session_user_id", user.id)
            .putString("session_email", user.email)
            .putString("session_name", user.name)
            .putString("session_role", user.role)
            .putBoolean("session_offline", user.offlineSession)
            .putLong("session_cashier_id", user.cashierId)
            .putLong("session_default_cash_register_id", user.defaultCashRegisterId)
            .putString("session_default_cash_register_name", user.defaultCashRegisterName)
            .putString("session_document", user.document)
            .putString("session_phone", user.phone)
            .putString("session_address", user.address)
            .putString("session_branch_name", user.branchName)
            .putString("session_last_name", user.lastName)
            .putString("session_document_type", user.documentType)
            .putString("session_cashier_state", user.cashierState)
            .putString("session_avatar", user.avatar)
            .putString("session_avatar_base64", user.avatarBase64)
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
            ?: ApiConfig.DEFAULT_BASE_URL

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
            if (productImagesApi.download(task.sourceUrl, target)) {
                updates[task.id] = "file://${target.absolutePath}"
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

    private fun <T> realmQuery(block: (Realm) -> T): T = Realm.getDefaultInstance().use(block)
    private fun realmWrite(block: (Realm) -> Unit) = Realm.getDefaultInstance().use { realm ->
        realm.executeTransaction { block(it) }
    }
}

