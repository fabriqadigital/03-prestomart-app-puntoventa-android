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
import com.ecommerce.ecommerceposapp.data.local.sync.OutboxRealm
import com.ecommerce.ecommerceposapp.data.local.sync.SyncIdMapRealm
import com.ecommerce.ecommerceposapp.data.local.users.UserRealm
import com.ecommerce.ecommerceposapp.data.remote.api.ApiConfig
import com.ecommerce.ecommerceposapp.data.remote.api.ApiSessionStore
import com.ecommerce.ecommerceposapp.data.remote.api.AuthApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.ClientApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.CashApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.CategoryApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.ProductImageApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.ProductImageDownloadResult
import com.ecommerce.ecommerceposapp.data.remote.api.PosSaleApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.ReceiptDeliveryApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.RemoteCatalogDataSource
import com.ecommerce.ecommerceposapp.data.repository.pos.AmountInWordsFormatter
import com.ecommerce.ecommerceposapp.data.repository.products.ProductRepositoryImpl
import com.ecommerce.ecommerceposapp.data.repository.suppliers.SupplierRepositoryImpl
import com.ecommerce.ecommerceposapp.data.security.OfflineCredentialVerifier
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.ComprobanteEmitidoResult
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryRow
import com.ecommerce.ecommerceposapp.domain.model.sales.SalesHistoryPage
import com.ecommerce.ecommerceposapp.domain.model.sales.paginateSalesHistoryLocal
import com.ecommerce.ecommerceposapp.domain.model.sales.mergePendingSalesWithRemotePage
import com.ecommerce.ecommerceposapp.domain.model.sync.SyncModuleStatus
import com.ecommerce.ecommerceposapp.domain.model.sync.SyncProgress
import com.ecommerce.ecommerceposapp.domain.sync.SyncPlan
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
import java.util.UUID
import java.io.File
import kotlin.math.round
import org.json.JSONArray
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
    private val categoryApi = CategoryApiDataSource(context)
    private val remoteCatalog = RemoteCatalogDataSource(context)
    private val remoteSale = PosSaleApiDataSource(context)
    private val receiptDelivery = ReceiptDeliveryApiDataSource(context)
    private val productImagesApi = ProductImageApiDataSource(context)
    private val pendingProducts = ProductRepositoryImpl(context)
    private val supplierCatalog = SupplierRepositoryImpl(context)
    private var lastCashRegisters: List<CashRegister> = emptyList()
    private val allSyncModules = SyncPlan.orderedModules

    override fun login(email: String, password: String): Result<UserSession> {
        if (email.isBlank() || password.isBlank()) return Result.failure(Exception("Completa usuario y contraseña."))
        val online = onlineLogin(email, password)
        if (online.isSuccess) return online
        val error = online.exceptionOrNull()
        if (error !is IOException) return online
        val cached = getSession() ?: getOfflineProfile()
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

    override fun offlineLoginEmail(): String? = offlineCredentials.availableEmail()

    override fun loginOffline(email: String, password: String): Result<UserSession> {
        val cached = getSession() ?: getOfflineProfile()
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
        prefs.edit()
            .remove("session_user_id")
            .remove("session_email")
            .remove("session_name")
            .remove("session_role")
            .remove("session_offline")
            .remove("session_cashier_id")
            .remove("session_default_cash_register_id")
            .remove("session_default_cash_register_name")
            .remove("session_avatar")
            .remove("session_avatar_base64")
            .apply()
    }

    override fun hasStoredToken(): Boolean =
        prefs.getString("api_token", "").orEmpty().isNotBlank()

    override fun categories(): List<CategoryItem> = realmQuery {
        it.where(CategoryRealm::class.java).equalTo("active", true).findAll()
            .map { c -> CategoryItem(c.id, c.name, c.active) }
            .sortedWith(compareByDescending<CategoryItem> { category -> category.id < 0L }.thenBy { category -> category.id })
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
            .sortedWith(compareByDescending<SubcategoryItem> { subcategory -> subcategory.id < 0L }.thenBy { subcategory -> subcategory.id })
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
        if (cashSessionId == 0L) return Result.failure(Exception("Abre una caja antes de registrar ventas."))
        val linesCopy = lines.map { it.copy() }
        val fechaMillis = System.currentTimeMillis()
        val outboxId = UUID.randomUUID().toString()
        lateinit var receipt: CompletedSaleReceipt
        realmWrite { realm ->
            val sesionId = cashSessionId
            val total = round(lines.sumOf { it.lineTotal } * 100) / 100
            val subtotal = round((total / 1.18) * 100) / 100
            val igv = round((total - subtotal) * 100) / 100
            lines.forEach { line ->
                val stock = realm.where(ProductRealm::class.java).equalTo("id", line.productId).findFirst()?.stock
                    ?: throw IllegalStateException("El producto ${line.productName} ya no existe localmente.")
                require(stock >= line.quantity) { "Stock local insuficiente para ${line.productName}." }
            }
            val ventaId = nextLocalId(realm, FinanzaVentaRealm::class.java)
            // Mantiene un identificador presentable mientras el backend asigna
            // el número definitivo durante la sincronización.
            val numero = "POS-${fechaMillis}-${ventaId.toString().removePrefix("-")}"
            realm.insertOrUpdate(
                FinanzaVentaRealm().apply {
                    id = ventaId
                    numeroComprobante = numero
                    tipoComprobante = "TICK"
                    idSesion = sesionId
                    idUsuario = session.id
                    this.idCliente = idCliente
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
            realm.insert(
                OutboxRealm().apply {
                    id = outboxId
                    moduleKey = "ventas"
                    operation = "CREATE_SALE"
                    aggregateType = "sale"
                    aggregateLocalId = ventaId
                    payloadJson = salePayloadJson(
                        lines = lines,
                        payment = payment,
                        clientId = idCliente,
                        cashSessionId = cashSessionId,
                        customerInfo = customerInfo,
                        receiptType = receiptType,
                    )
                    createdAt = fechaMillis
                    updatedAt = fechaMillis
                    state = "PENDING"
                },
            )
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
                idCliente = idCliente,
                clienteNombre = customerInfo.name.trim(),
                clienteDocumento = customerInfo.document.filter(Char::isDigit),
            )
        }
        val dependenciesResolved =
            cashSessionId > 0L &&
                lines.all { it.productId > 0L } &&
                idCliente >= 0L
        if (!session.offlineSession && ApiSessionStore(context).token.isNotBlank() && dependenciesResolved) {
            val pending = PendingOutbox(
                id = outboxId,
                operation = "CREATE_SALE",
                aggregateLocalId = receipt.ventaId,
                payloadJson = salePayloadJson(
                    lines = lines,
                    payment = payment,
                    clientId = idCliente,
                    cashSessionId = cashSessionId,
                    customerInfo = customerInfo,
                    receiptType = receiptType,
                ),
                attemptCount = 0,
            )
            pushPendingSale(pending).onSuccess {
                val remoteId = resolveRemoteId("sale", receipt.ventaId)
                return getSaleReceipt(remoteId).recover { receipt.copy(ventaId = remoteId) }
            }
        }
        return Result.success(receipt)
    }

    override fun emitComprobanteForVenta(ventaId: Long, tipo: TipoComprobanteEmision, idCliente: Long, customerInfo: ReceiptCustomerInfo): Result<ComprobanteEmitidoResult> {
        val remoteReceipt = cashApi.getSaleReceipt(ventaId).getOrNull()
        if (remoteReceipt != null) {
            return runCatching {
                val emitter = realmQuery { realm ->
                    val config = realm.where(FinanzaEmisorConfigRealm::class.java)
                        .equalTo("activo", true)
                        .findFirst()
                        ?: realm.where(FinanzaEmisorConfigRealm::class.java).findFirst()
                    Triple(
                        remoteReceipt.emisorRuc.ifBlank { config?.ruc.orEmpty() },
                        remoteReceipt.emisorRazonSocial.ifBlank { config?.razonSocial.orEmpty() },
                        remoteReceipt.emisorDireccion.ifBlank { config?.direccion.orEmpty() },
                    )
                }
                val tipoSunat = when (tipo) {
                    TipoComprobanteEmision.FACTURA -> "01"
                    TipoComprobanteEmision.BOLETA -> "03"
                    TipoComprobanteEmision.SOLO_TICKET -> "TICK"
                }
                val number = remoteReceipt.numeroTicket
                val serie = number.substringBeforeLast('-', missingDelimiterValue = "")
                val correlativo = number.substringAfterLast('-', missingDelimiterValue = "").toIntOrNull() ?: 0
                ComprobanteEmitidoResult(
                    tipoSunat = tipoSunat,
                    numeroCompleto = number,
                    serie = serie,
                    correlativo = correlativo,
                    qrPayload = buildQrPayload(
                        ruc = emitter.first,
                        tipoDoc = tipoSunat,
                        serie = serie,
                        correlativo = correlativo,
                        igv = remoteReceipt.igv,
                        total = remoteReceipt.total,
                        fechaSunat = formatFechaSunat(remoteReceipt.fechaMillis),
                        receptorDocumento = customerInfo.document.ifBlank { remoteReceipt.clienteDocumento },
                    ),
                    emisorRuc = emitter.first,
                    emisorRazonSocial = emitter.second,
                    emisorDireccion = emitter.third,
                    totalLetras = AmountInWordsFormatter.soles(remoteReceipt.total),
                    receptorNombre = customerInfo.name.ifBlank { remoteReceipt.clienteNombre },
                    receptorDocumento = customerInfo.document.ifBlank { remoteReceipt.clienteDocumento },
                )
            }
        }
        if (tipo != TipoComprobanteEmision.SOLO_TICKET) {
            if (ventaId < 0L) {
                return Result.failure(
                    Exception(
                        "La venta quedó guardada, pero la boleta o factura requiere Internet. " +
                            "Sincroniza la venta y emite el comprobante desde el historial.",
                    ),
                )
            }
            val expectedType = if (tipo == TipoComprobanteEmision.FACTURA) "01" else "03"
            val existing = realmQuery { realm ->
                val receipt = realm.where(FinanzaComprobanteRealm::class.java)
                    .equalTo("idVenta", ventaId)
                    .equalTo("tipoComprobante", expectedType)
                    .sort("id", io.realm.Sort.DESCENDING)
                    .findFirst()
                    ?: return@realmQuery null
                ComprobanteEmitidoResult(
                    tipoSunat = receipt.tipoComprobante,
                    numeroCompleto = receipt.numeroCompleto,
                    serie = receipt.serie,
                    correlativo = receipt.correlativo,
                    qrPayload = buildQrPayload(
                        ruc = receipt.emisorRuc,
                        tipoDoc = receipt.tipoComprobante,
                        serie = receipt.serie,
                        correlativo = receipt.correlativo,
                        igv = receipt.totalIgv,
                        total = receipt.total,
                        fechaSunat = receipt.fechaEmision,
                        receptorDocumento = receipt.receptorNumDoc,
                    ),
                    emisorRuc = receipt.emisorRuc,
                    emisorRazonSocial = receipt.emisorRazonSocial,
                    emisorDireccion = receipt.emisorDireccion,
                    totalLetras = receipt.totalLetras,
                    receptorNombre = receipt.receptorRazonSocial,
                    receptorDocumento = receipt.receptorNumDoc,
                )
            }
            if (existing != null) return Result.success(existing)
        }
        if (tipo == TipoComprobanteEmision.SOLO_TICKET) {
            return realmQuery { realm ->
                val venta = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
                    ?: return@realmQuery Result.failure(Exception("Venta no encontrada."))
                val emisor = realm.where(FinanzaEmisorConfigRealm::class.java).equalTo("activo", true).findFirst()
                    ?: realm.where(FinanzaEmisorConfigRealm::class.java).findFirst()
                val ruc = emisor?.ruc ?: ""
                val rs = emisor?.razonSocial ?: ""
                val dir = emisor?.direccion ?: ""
                val client = venta.idCliente.takeIf { it != 0L }?.let { clientId ->
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
                    receptorDocumento = client?.document.orEmpty(),
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
            val idCli = when {
                customerInfo.id != 0L -> customerInfo.id
                idCliente != 0L -> idCliente
                else -> venta.idCliente
            }
            val client = if (idCli != 0L) {
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
                receptorDocumento = receptorNum,
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
            if (idCli != 0L) venta.idCliente = idCli
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
        if (idCliente == 0L) return null
        return realmQuery { realm ->
            val c = realm.where(ClientRealm::class.java).equalTo("id", idCliente).findFirst() ?: return@realmQuery null
            c.name to c.document
        }
    }

    override fun getClienteTelefono(idCliente: Long): String? {
        if (idCliente == 0L) return null
        return realmQuery { realm ->
            realm.where(ClientRealm::class.java).equalTo("id", idCliente).findFirst()?.phone?.trim()?.takeIf { it.isNotBlank() }
        }
    }

    override fun actualizarClienteEnVenta(ventaId: Long, idCliente: Long): Result<Unit> {
        if (ventaId <= 0L) return Result.failure(Exception("Venta inválida."))
        realmWrite { realm ->
            val v = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
                ?: throw Exception("Venta no encontrada.")
            v.idCliente = idCliente
        }
        return Result.success(Unit)
    }

    override fun listSalesHistory(page: Int, perPage: Int, search: String): SalesHistoryPage {
        val sessionId = prefs.getLong("pos_cash_session_id", 0L)
        if (sessionId == 0L) return SalesHistoryPage(emptyList(), 0, page, perPage)
        val local = localSalesHistory(sessionId)
        val offlineSession = getSession()?.offlineSession == true || ApiSessionStore(context).token.isBlank()
        fun localPage() = paginateSalesHistoryLocal(local, page, perPage, search)
        if (sessionId < 0L || offlineSession) return localPage()

        return cashApi.listSales(sessionId, page, perPage, search).fold(
            onSuccess = { remote ->
                val localBySaleId = local.associateBy { it.ventaId }
                val enrichedRemote = remote.rows.map { row ->
                    if (!row.clienteNombre.isGenericCustomerName()) return@map row
                    val resolvedName = localBySaleId[row.ventaId]?.clienteNombre
                        ?.takeUnless { it.isGenericCustomerName() }
                        .orEmpty()
                        .ifBlank { localReceiptCustomerName(row.ventaId) }
                        .ifBlank {
                            cashApi.getSaleReceipt(row.ventaId)
                                .getOrNull()
                                ?.clienteNombre
                                ?.takeUnless { it.isGenericCustomerName() }
                                .orEmpty()
                        }
                    if (resolvedName.isBlank()) row else row.copy(clienteNombre = resolvedName)
                }
                val remoteBySaleId = enrichedRemote.associateBy { it.ventaId }
                val enrichedLocal = local.map { row ->
                    val remoteName = remoteBySaleId[row.ventaId]?.clienteNombre.orEmpty()
                    if (row.clienteNombre.isGenericCustomerName() && !remoteName.isGenericCustomerName()) {
                        row.copy(clienteNombre = remoteName)
                    } else {
                        row
                    }
                }
                mergePendingSalesWithRemotePage(remote.copy(rows = enrichedRemote), enrichedLocal, search)
            },
            onFailure = { error ->
                if (error.isNetworkFailure()) localPage() else throw error
            },
        )
    }

    override fun resumeOnlineSession(): UserSession? {
        if (!hasStoredToken()) return null
        val online = getSession()?.copy(offlineSession = false) ?: return null
        saveSession(online)
        return online
    }

    private fun localSalesHistory(sessionId: Long): List<SalesHistoryRow> = realmQuery { realm ->
        realm.where(FinanzaVentaRealm::class.java)
            .equalTo("idSesion", sessionId)
            .findAll()
            .map { sale ->
                val registeredClientName = sale.idCliente.takeIf { it != 0L }?.let { clientId ->
                    realm.where(ClientRealm::class.java)
                        .equalTo("id", clientId)
                        .findFirst()
                        ?.name
                        .orEmpty()
                }.orEmpty()
                val receiptCustomerName = realm.where(FinanzaComprobanteRealm::class.java)
                    .equalTo("idVenta", sale.id)
                    .sort("id", io.realm.Sort.DESCENDING)
                    .findFirst()
                    ?.receptorRazonSocial
                    .orEmpty()
                    .takeUnless { it.equals("CLIENTE VARIOS", ignoreCase = true) }
                    .orEmpty()
                val clientName = receiptCustomerName.ifBlank { registeredClientName }
                SalesHistoryRow(
                    ventaId = sale.id,
                    numeroComprobante = sale.numeroComprobante,
                    tipoComprobante = sale.tipoComprobante,
                    fechaMillis = sale.fechaVenta,
                    clienteNombre = clientName,
                    cajeroNombre = getSession()?.name.orEmpty(),
                    tipoPago = sale.tipoPago,
                    total = sale.total,
                    estado = if (sale.estado == "A") "Completada" else "Anulada",
                    idCliente = sale.idCliente,
                )
            }
            .sortedByDescending { it.fechaMillis }
    }

    override fun getSaleReceipt(ventaId: Long): Result<CompletedSaleReceipt> {
        val remoteResult = cashApi.getSaleReceipt(ventaId).map { remote ->
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
        return remoteResult.recoverCatching { error ->
            localSaleReceipt(ventaId) ?: throw error
        }
    }

    private fun localSaleReceipt(ventaId: Long): CompletedSaleReceipt? = realmQuery { realm ->
        val sale = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst() ?: return@realmQuery null
        val details = realm.where(FinanzaVentaDetalleRealm::class.java).equalTo("idVenta", ventaId).findAll()
        val lines = details.map {
            CartLine(
                productId = it.idProducto,
                productName = it.nombreProducto,
                unitPrice = it.precioUnitario,
                quantity = it.cantidad.toInt().coerceAtLeast(1),
            )
        }
        val localReceipt = realm.where(FinanzaComprobanteRealm::class.java)
            .equalTo("idVenta", ventaId)
            .sort("id", io.realm.Sort.DESCENDING)
            .findFirst()
        val client = sale.idCliente.takeIf { it != 0L }?.let { id ->
            realm.where(ClientRealm::class.java).equalTo("id", id).findFirst()
        }
        CompletedSaleReceipt(
            ventaId = sale.id,
            numeroTicket = sale.numeroComprobante,
            subtotal = sale.subtotal,
            igv = sale.igv,
            total = sale.total,
            tipoPago = sale.tipoPago,
            montoRecibido = sale.montoRecibido.takeIf { it > 0.0 } ?: sale.total,
            vuelto = sale.vuelto.coerceAtLeast(0.0),
            fechaMillis = sale.fechaVenta,
            lines = lines,
            vendedorNombre = getSession()?.name.orEmpty(),
            idCliente = sale.idCliente,
            clienteNombre = localReceipt?.receptorRazonSocial.orEmpty().ifBlank { client?.name.orEmpty() },
            clienteDocumento = localReceipt?.receptorNumDoc.orEmpty().ifBlank { client?.document.orEmpty() },
        )
    }

    override fun listCashRegisters(): Result<List<CashRegister>> = cashApi.listCashRegisters()
        .onSuccess { registers ->
            lastCashRegisters = registers
            cacheCashRegisters(registers)
            val preferredId = prefs.getLong("pos_cash_register_id", 0L).takeIf { it > 0L }
                ?: getSession()?.defaultCashRegisterId?.takeIf { it > 0L }
            cacheEmitterFromCashRegisters(registers, preferredId)
        }
        .recoverCatching { error ->
            if (!error.isNetworkFailure()) throw error
            val cached = cachedCashRegisters()
            if (cached.isNotEmpty()) {
                lastCashRegisters = cached
                cached
            } else {
                val session = getSession() ?: throw error
                listOf(CashRegister(session.defaultCashRegisterId, "", session.defaultCashRegisterName, "", true))
                    .filter { it.id > 0L }
            }
        }

    private fun cacheCashRegisters(registers: List<CashRegister>) {
        realmWrite { realm ->
            realm.where(FinanzaCajaRealm::class.java).findAll().deleteAllFromRealm()
            registers.forEach { register ->
                realm.insertOrUpdate(FinanzaCajaRealm().apply {
                    id = register.id
                    nombreCaja = register.name
                    descripcion = register.branch
                    activo = register.active
                    updatedAt = System.currentTimeMillis()
                })
            }
        }
    }

    private fun cachedCashRegisters(): List<CashRegister> = realmQuery { realm ->
        realm.where(FinanzaCajaRealm::class.java)
            .equalTo("activo", true)
            .findAll()
            .map {
                CashRegister(
                    id = it.id,
                    code = "",
                    name = it.nombreCaja,
                    branch = it.descripcion,
                    active = it.activo,
                )
            }
    }

    override fun findOpenCashSession(cashierId: Long): Result<CashSession?> {
        val cached = cachedCashSession(cashierId)
        if (cached != null) {
            cacheEmitterFromCashRegisters(lastCashRegisters, cached.cashRegisterId)
            return Result.success(cached)
        }
        return cashApi.findOpenSession(cashierId)
            .onSuccess { session ->
                cacheCashSession(session)
                session?.let { cacheEmitterFromCashRegisters(lastCashRegisters, it.cashRegisterId) }
            }
            .recoverCatching { error -> if (error is IOException) cachedCashSession(cashierId) else throw error }
    }

    override fun openCashSession(cashRegisterId: Long, cashierId: Long, openingAmount: Double): Result<CashSession> {
        val offlineSession = getSession()?.offlineSession == true || ApiSessionStore(context).token.isBlank()
        val remote = if (offlineSession) {
            Result.failure(IOException("Modo offline"))
        } else {
            cashApi.openSession(cashRegisterId, cashierId, openingAmount)
        }
        remote.onSuccess {
            cacheCashSession(it)
            cacheEmitterFromCashRegisters(lastCashRegisters, it.cashRegisterId)
        }
        if (remote.isFailure && !remote.exceptionOrNull().isNetworkFailure()) {
            // The session may have been opened from the web (or another device)
            // between the initial lookup and this request. In that case the
            // backend rejects a duplicate opening; adopt its current session.
            val current = cashApi.findOpenSession(cashierId).getOrNull()
            if (current != null) {
                cacheCashSession(current)
                cacheEmitterFromCashRegisters(lastCashRegisters, current.cashRegisterId)
                return Result.success(current)
            }
            val occupied = cashApi.findOpenSessionForRegister(cashRegisterId).getOrNull()
            if (occupied != null && occupied.cashierId != cashierId) {
                val registerName = occupied.cashRegisterName.ifBlank {
                    lastCashRegisters.firstOrNull { it.id == cashRegisterId }?.name ?: "seleccionada"
                }
                val cashierName = occupied.cashierName.ifBlank { "otro cajero" }
                return Result.failure(
                    Exception("La caja $registerName está siendo utilizada por $cashierName. Selecciona otra caja o solicita que ese cajero cierre su sesión."),
                )
            }
        }
        if (remote.isSuccess || !remote.exceptionOrNull().isNetworkFailure()) return remote

        val now = System.currentTimeMillis()
        val local = CashSession(
            id = -now,
            cashRegisterId = cashRegisterId,
            cashRegisterName = lastCashRegisters.firstOrNull { it.id == cashRegisterId }?.name
                ?: getSession()?.defaultCashRegisterName.orEmpty(),
            cashierId = cashierId,
            cashierName = getSession()?.name.orEmpty(),
            openedAt = now,
            openingAmount = openingAmount,
            status = "Abierta (pendiente de sincronizar)",
        )
        realmWrite { realm ->
            realm.insert(
                OutboxRealm().apply {
                    id = UUID.randomUUID().toString()
                    moduleKey = "caja"
                    operation = "OPEN_CASH"
                    aggregateType = "cash_session"
                    aggregateLocalId = local.id
                    payloadJson = JSONObject()
                        .put("cash_register_id", cashRegisterId)
                        .put("cashier_id", cashierId)
                        .put("opening_amount", openingAmount)
                        .toString()
                    createdAt = now
                    updatedAt = now
                    state = "PENDING"
                },
            )
        }
        cacheCashSession(local)
        return Result.success(local)
    }

    override fun cashSummary(sessionId: Long): Result<CashSummary> {
        val offlineSession = getSession()?.offlineSession == true || ApiSessionStore(context).token.isBlank()
        if (sessionId > 0L && !offlineSession) {
            val remote = cashApi.summary(sessionId)
            if (remote.isSuccess || !remote.exceptionOrNull().isNetworkFailure()) return remote
        }
        return Result.success(localCashSummary(sessionId))
    }

    override fun closeCashSession(sessionId: Long, countedCash: Double, observations: String): Result<Unit> {
        val offlineSession = getSession()?.offlineSession == true || ApiSessionStore(context).token.isBlank()
        if (sessionId > 0L && !offlineSession) {
            val remote = cashApi.closeSession(sessionId, countedCash, observations)
            if (remote.isSuccess) {
                cacheCashSession(null)
                return Result.success(Unit)
            }
            if (!remote.exceptionOrNull().isNetworkFailure()) return remote
        }
        val now = System.currentTimeMillis()
        realmWrite { realm ->
            realm.insert(
                OutboxRealm().apply {
                    id = UUID.randomUUID().toString()
                    moduleKey = "caja"
                    operation = "CLOSE_CASH"
                    aggregateType = "cash_session"
                    aggregateLocalId = sessionId
                    payloadJson = JSONObject()
                        .put("cash_session_id", sessionId)
                        .put("counted_cash", countedCash)
                        .put("observations", observations.trim())
                        .toString()
                    createdAt = now
                    updatedAt = now
                    state = "PENDING"
                },
            )
        }
        cacheCashSession(null)
        return Result.success(Unit)
    }

    private fun localCashSummary(sessionId: Long): CashSummary = realmQuery { realm ->
        val sales = realm.where(FinanzaVentaRealm::class.java)
            .equalTo("idSesion", sessionId)
            .equalTo("estado", "A")
            .findAll()
        val opening = prefs.getFloat("pos_cash_opening_amount", 0f).toDouble()
        val totalSales = sales.sumOf { it.total }
        val cash = sales.filter { it.tipoPago.equals("EFE", true) || it.tipoPago.contains("efectivo", true) }
            .sumOf { it.total }
        CashSummary(
            openingAmount = opening,
            totalSales = totalSales,
            cashAmount = cash,
            deposit = totalSales - cash,
            expectedCash = opening + cash,
            totalFlow = opening + totalSales,
            income = 0.0,
            expenses = 0.0,
        )
    }

    override fun cancelSale(ventaId: Long, comment: String, restoreStock: Boolean): Result<Unit> {
        val localExists = realmQuery { realm ->
            realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst() != null
        }
        if (!localExists) return cashApi.cancelSale(ventaId, comment, restoreStock)

        val offlineSession = getSession()?.offlineSession == true || ApiSessionStore(context).token.isBlank()
        if (ventaId > 0L && !offlineSession) {
            val remote = cashApi.cancelSale(ventaId, comment, restoreStock)
            if (remote.isSuccess) {
                cancelSaleLocally(ventaId, comment, restoreStock)
                return remote
            }
            if (!remote.exceptionOrNull().isNetworkFailure()) return remote
        }

        cancelSaleLocally(ventaId, comment, restoreStock)
        val now = System.currentTimeMillis()
        realmWrite { realm ->
            realm.insert(OutboxRealm().apply {
                id = UUID.randomUUID().toString()
                moduleKey = "ventas"
                operation = "CANCEL_SALE"
                aggregateType = "sale"
                aggregateLocalId = ventaId
                payloadJson = JSONObject()
                    .put("sale_id", ventaId)
                    .put("comment", comment.trim())
                    .put("restore_stock", restoreStock)
                    .toString()
                createdAt = now
                updatedAt = now
                state = "PENDING"
            })
        }
        return Result.success(Unit)
    }

    private fun cancelSaleLocally(ventaId: Long, comment: String, restoreStock: Boolean) {
        realmWrite { realm ->
            val sale = realm.where(FinanzaVentaRealm::class.java).equalTo("id", ventaId).findFirst()
                ?: return@realmWrite
            if (sale.estado == "N") return@realmWrite
            sale.estado = "N"
            sale.motivoAnulacion = comment.trim()
            if (restoreStock) {
                realm.where(FinanzaVentaDetalleRealm::class.java)
                    .equalTo("idVenta", ventaId)
                    .findAll()
                    .forEach { detail ->
                        realm.where(ProductRealm::class.java)
                            .equalTo("id", detail.idProducto)
                            .findFirst()
                            ?.let { product -> product.stock += detail.cantidad }
                    }
            }
        }
    }

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
        if (id == 0L || prefs.getLong("pos_cashier_id", cashierId) != cashierId) return null
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

    override fun syncInitialData(
        user: UserSession,
        onProgress: (SyncProgress) -> Unit,
    ): Result<Unit> {
        return syncModules(user, allSyncModules.toSet(), onProgress)
    }

    override fun listSyncModuleStatus(): List<SyncModuleStatus> = realmQuery { realm ->
        allSyncModules.map { key ->
            val label = when (key) {
                "imagenes_productos" -> "Imágenes de productos"
                else -> key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "PE")) else it.toString() }
            }
            val row = realm.where(SyncModuleStateRealm::class.java).equalTo("moduleKey", key).findFirst()
            val localProductPending = if (key == "productos") {
                realm.where(ProductRealm::class.java)
                    .equalTo("syncState", "PENDING")
                    .count()
            } else {
                0L
            }
            SyncModuleStatus(
                key = key,
                label = label,
                lastSyncAt = row?.lastSyncAt ?: 0L,
                pendingCount = realm.where(OutboxRealm::class.java)
                    .equalTo("moduleKey", key)
                    .equalTo("state", "PENDING")
                    .count() + localProductPending,
                failedCount = realm.where(OutboxRealm::class.java)
                    .equalTo("moduleKey", key)
                    .equalTo("state", "FAILED")
                    .count(),
            )
        }
    }

    override fun syncModules(
        user: UserSession,
        modules: Set<String>,
        onProgress: (SyncProgress) -> Unit,
    ): Result<Unit> {
        val selected = SyncPlan.expand(modules)
        if (selected.isEmpty()) return Result.failure(Exception("Seleccione al menos un módulo para sincronizar."))
        realmWrite { realm ->
            selected.forEach { module ->
                realm.where(OutboxRealm::class.java)
                    .equalTo("moduleKey", module)
                    .findAll()
                    .forEach {
                        it.nextAttemptAt = 0L
                        if (it.state == "FAILED") it.state = "PENDING"
                    }
            }
        }
        val selectedInOrder = allSyncModules.filter { it in selected }
        var completedModules = 0
        fun reportProgress(completedModuleKey: String) {
            completedModules++
            val activeModule = selectedInOrder.getOrNull(completedModules) ?: completedModuleKey
            onProgress(
                SyncProgress(
                    activeModuleKey = activeModule,
                    completedModules = completedModules,
                    totalModules = selectedInOrder.size,
                ),
            )
        }
        onProgress(
            SyncProgress(
                activeModuleKey = selectedInOrder.first(),
                completedModules = 0,
                totalModules = selectedInOrder.size,
            ),
        )
        val includeImageSync = "imagenes_productos" in selected
        var remote = remoteCatalog.fetchBestEffort()
        // Al recuperar la conexión Android puede validar la red unos instantes
        // antes de que el host termine de responder. Reintentar evita abortar una
        // sincronización completa por una primera respuesta vacía/transitoria.
        if ("productos" in selected && remote.products.isEmpty()) {
            remote = remoteCatalog.fetchBestEffort()
        }
        val remoteClients = if ("clientes" in selected) clientApi.list().getOrNull() else null
        val now = System.currentTimeMillis()
        val cachedProductImageFiles = if ("productos" in selected) {
            realmQuery { realm ->
                realm.where(ProductRealm::class.java).findAll().mapNotNull { product ->
                    product.imageUrl.takeIf { path ->
                        path.startsWith("file://") && File(path.removePrefix("file://")).exists()
                    }?.let { product.id to it }
                }.toMap()
            }
        } else {
            emptyMap()
        }
        if ("categorias" in selected && remote.categories.isEmpty()) {
            return Result.failure(Exception("No se recibieron categorías desde ${ApiSessionStore(context).baseUrl}."))
        }
        if ("productos" in selected && remote.products.isEmpty()) {
            return Result.failure(Exception("No se recibieron productos desde ${ApiSessionStore(context).baseUrl}."))
        }

        // Fase 1: escribir datos en Realm (sin I/O de red dentro de la transacción)
        realmWrite { realm ->
            if ("categorias" in selected) {
                val pendingIds = realm.where(OutboxRealm::class.java)
                    .equalTo("operation", "UPSERT_CATEGORY")
                    .findAll()
                    .map { it.aggregateLocalId }
                    .toSet()
                remote.categories.forEach { rc ->
                    if (rc.id in pendingIds) return@forEach
                    realm.insertOrUpdate(CategoryRealm().apply {
                        id = rc.id
                        name = rc.name
                        active = rc.active
                    })
                }
            }
            if ("subcategorias" in selected && remote.subcategories.isNotEmpty()) {
                val pendingIds = realm.where(OutboxRealm::class.java)
                    .equalTo("operation", "UPSERT_SUBCATEGORY")
                    .findAll()
                    .map { it.aggregateLocalId }
                    .toSet()
                remote.subcategories.forEach { rs ->
                    if (rs.id in pendingIds) return@forEach
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
                        imageUrl = cachedProductImageFiles[id]
                            ?: normalizedProductImageUrl(id, rp.imageUrl)
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
                val pendingIds = realm.where(OutboxRealm::class.java)
                    .equalTo("moduleKey", "clientes")
                    .findAll()
                    .map { it.aggregateLocalId }
                    .toSet()
                realm.where(ClientRealm::class.java).findAll()
                    .filter { it.id !in pendingIds }
                    .forEach { it.deleteFromRealm() }
                remoteClients.forEach { client ->
                    if (client.id in pendingIds) return@forEach
                    realm.insertOrUpdate(ClientRealm().apply {
                        id = client.id
                        name = client.name
                        document = client.document
                        phone = client.phone
                        lastName = client.lastName
                        email = client.email
                        address = client.address
                        businessName = client.businessName
                        branchName = client.branchName
                        userId = client.userId
                        personType = client.personType
                        documentType = client.documentType
                        alias = client.alias
                        gender = client.gender
                        maritalStatus = client.maritalStatus
                        discountPercentage = client.discountPercentage
                        observations = client.observations
                        webAccess = client.webAccess
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
        }

        // Fase 2: descargar imágenes FUERA de la transacción (I/O de red separado)
        val catalogOperations = buildSet {
            if ("categorias" in selected) {
                add("UPSERT_CATEGORY")
                add("DELETE_CATEGORY")
            }
            if ("subcategorias" in selected) {
                add("UPSERT_SUBCATEGORY")
                add("DELETE_SUBCATEGORY")
            }
            if ("proveedores" in selected) {
                add("UPSERT_SUPPLIER")
                add("DELETE_SUPPLIER")
            }
            if ("clientes" in selected) {
                add("UPSERT_CLIENT")
                add("DELETE_CLIENT")
            }
            if ("productos" in selected) add("DELETE_PRODUCT")
        }
        if (catalogOperations.isNotEmpty()) {
            processOutboxInternal(catalogOperations).getOrElse { return Result.failure(it) }
        }
        if ("categorias" in selected) reportProgress("categorias")
        if ("subcategorias" in selected) reportProgress("subcategorias")

        if ("proveedores" in selected) {
            supplierCatalog.listSuppliers()
            reportProgress("proveedores")
        }
        if ("clientes" in selected) reportProgress("clientes")

        if ("productos" in selected) {
            pendingProducts.syncPendingProducts().getOrElse { return Result.failure(it) }
            reportProgress("productos")
        }

        if (includeImageSync) {
            val completedBeforeImages = completedModules
            val imageResult = cacheProductImages { completed, total ->
                onProgress(
                    SyncProgress(
                        activeModuleKey = "imagenes_productos",
                        completedModules = completedBeforeImages,
                        totalModules = selectedInOrder.size,
                        completedItems = completed,
                        totalItems = total,
                    ),
                )
            }
            if (imageResult.failed > 0) {
                return Result.failure(
                    Exception(
                        "Se guardaron ${imageResult.downloaded} imágenes nuevas" +
                            (if (imageResult.missing > 0) " y ${imageResult.missing} productos no tienen archivo de imagen en la web" else "") +
                            ". ${imageResult.failed} descargas fallaron temporalmente; selecciona Imágenes de productos para reintentarlas." +
                            imageResult.failureSummary.takeIf { it.isNotBlank() }?.let { " Detalle: $it" }.orEmpty(),
                    ),
                )
            }
            reportProgress("imagenes_productos")
        }

        if ("caja" in selected) {
            listCashRegisters().getOrElse { return Result.failure(it) }
        }
        val transactionalOperations = buildSet {
            if ("caja" in selected) {
                add("OPEN_CASH")
                add("CLOSE_CASH")
            }
            if ("ventas" in selected) {
                add("CREATE_SALE")
                add("CANCEL_SALE")
            }
            if ("tickets" in selected) {
                add("SEND_RECEIPT_EMAIL")
                add("SEND_RECEIPT_WHATSAPP")
            }
        }
        if (transactionalOperations.isNotEmpty()) {
            processOutboxInternal(transactionalOperations).getOrElse { return Result.failure(it) }
        }
        if ("ventas" in selected) refreshProductStockAfterSales()
        if ("caja" in selected) reportProgress("caja")
        if ("ventas" in selected) reportProgress("ventas")
        if ("tickets" in selected) reportProgress("tickets")

        // Solo registrar una sincronización como terminada cuando todas sus
        // fases, incluidas las imágenes y la cola offline, finalizaron bien.
        realmWrite { realm ->
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

        return Result.success(Unit)
    }

    override fun processOutbox(): Result<Int> = processOutboxInternal()

    private fun processOutboxInternal(allowedOperations: Set<String>? = null): Result<Int> {
        val now = System.currentTimeMillis()
        val entries = realmQuery { realm ->
            realm.where(OutboxRealm::class.java)
                .lessThanOrEqualTo("nextAttemptAt", now)
                .findAll()
                .sortedBy { it.createdAt }
                .filter { allowedOperations == null || it.operation in allowedOperations }
                .map {
                    PendingOutbox(
                        id = it.id,
                        operation = it.operation,
                        aggregateLocalId = it.aggregateLocalId,
                        payloadJson = it.payloadJson,
                        attemptCount = it.attemptCount,
                    )
                }
        }
        var processed = 0
        entries.forEach { entry ->
            val result = when (entry.operation) {
                "UPSERT_CATEGORY" -> pushPendingCategory(entry)
                "DELETE_CATEGORY" -> pushPendingCategoryDelete(entry)
                "UPSERT_SUBCATEGORY" -> pushPendingSubcategory(entry)
                "DELETE_SUBCATEGORY" -> pushPendingSubcategoryDelete(entry)
                "UPSERT_SUPPLIER" -> pushPendingSupplier(entry)
                "DELETE_SUPPLIER" -> pushPendingSupplierDelete(entry)
                "UPSERT_CLIENT" -> pushPendingClient(entry)
                "DELETE_CLIENT" -> pushPendingClientDelete(entry)
                "DELETE_PRODUCT" -> pushPendingProductDelete(entry)
                "OPEN_CASH" -> pushPendingCashOpen(entry)
                "CLOSE_CASH" -> pushPendingCashClose(entry)
                "CREATE_SALE" -> pushPendingSale(entry)
                "CANCEL_SALE" -> pushPendingSaleCancellation(entry)
                "SEND_RECEIPT_EMAIL" -> pushPendingReceiptEmail(entry)
                "SEND_RECEIPT_WHATSAPP" -> pushPendingReceiptWhatsapp(entry)
                else -> Result.failure(Exception("Operacion outbox no soportada: ${entry.operation}"))
            }
            result.onSuccess {
                processed++
            }.onFailure { error ->
                val attempt = entry.attemptCount + 1
                val delayMillis = (5_000L * (1L shl attempt.coerceAtMost(9)))
                    .coerceAtMost(60 * 60 * 1000L)
                realmWrite { realm ->
                    realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.apply {
                        attemptCount = attempt
                        nextAttemptAt = System.currentTimeMillis() + delayMillis
                        updatedAt = System.currentTimeMillis()
                        state = if (attempt >= 5) "FAILED" else "PENDING"
                        lastError = error.message.orEmpty()
                    }
                }
                return Result.failure(error)
            }
        }
        return Result.success(processed)
    }

    private fun pushPendingSale(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val rawLines = payload.getJSONArray("lines")
        val lines = (0 until rawLines.length()).map { index ->
            val item = rawLines.getJSONObject(index)
            CartLine(
                productId = resolveRemoteId("product", item.getLong("product_id")),
                productName = item.getString("name"),
                unitPrice = item.getDouble("unit_price"),
                quantity = item.getInt("quantity"),
            )
        }
        val paymentJson = payload.getJSONObject("payment")
        val customerJson = payload.getJSONObject("customer")
        val localClientId = payload.getLong("client_id")
        val remoteClientId = resolveRemoteId("client", localClientId)
        val clientSnapshot = realmQuery { realm ->
            realm.where(ClientRealm::class.java)
                .equalTo("id", remoteClientId)
                .findFirst()
                ?.let { it.name to it.document }
        }
        val customerName = customerJson.optString("name").ifBlank { clientSnapshot?.first.orEmpty() }
        val customerDocument = customerJson.optString("document")
            .filter(Char::isDigit)
            .ifBlank { clientSnapshot?.second.orEmpty().filter(Char::isDigit) }
        val registered = remoteSale.registerSale(
            lines = lines,
            payment = SalePaymentInfo(
                tipoPago = paymentJson.getString("type"),
                montoRecibido = paymentJson.getDouble("received"),
                vuelto = paymentJson.getDouble("change"),
            ),
            clientId = remoteClientId,
            cashSessionId = resolveRemoteId("cash_session", payload.getLong("cash_session_id")),
            customerInfo = ReceiptCustomerInfo(
                id = remoteClientId,
                name = customerName,
                document = customerDocument,
            ),
            receiptType = TipoComprobanteEmision.valueOf(payload.getString("receipt_type")),
            idempotencyKey = entry.id,
        ).getOrThrow()

        realmWrite { realm ->
            val sale = realm.where(FinanzaVentaRealm::class.java)
                .equalTo("id", entry.aggregateLocalId)
                .findFirst()
            if (sale != null && sale.id != registered.id) {
                val replacement = realm.copyFromRealm(sale).apply {
                    id = registered.id
                    numeroComprobante = registered.number
                }
                sale.deleteFromRealm()
                realm.insertOrUpdate(replacement)
                realm.where(FinanzaVentaDetalleRealm::class.java)
                    .equalTo("idVenta", entry.aggregateLocalId)
                    .findAll()
                    .forEach { it.idVenta = registered.id }
                realm.where(FinanzaComprobanteRealm::class.java)
                    .equalTo("idVenta", entry.aggregateLocalId)
                    .findAll()
                    .forEach { it.idVenta = registered.id }
                realm.insertOrUpdate(
                    SyncIdMapRealm().apply {
                        key = "sale:${entry.aggregateLocalId}"
                        entityType = "sale"
                        localId = entry.aggregateLocalId
                        remoteId = registered.id
                        createdAt = System.currentTimeMillis()
                    },
                )
            }
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingSaleCancellation(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        cashApi.cancelSale(
            saleId = resolveRemoteId("sale", payload.getLong("sale_id")),
            comment = payload.optString("comment"),
            restoreStock = payload.optBoolean("restore_stock"),
        ).getOrThrow()
        realmWrite { realm ->
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingCategory(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val remoteId = categoryApi.saveCategory(
            CategoryAdminRow(
                id = entry.aggregateLocalId.takeIf { it > 0L } ?: 0L,
                name = payload.getString("name"),
                active = payload.optBoolean("active", true),
            ),
        ).getOrThrow()
        realmWrite { realm ->
            val local = realm.where(CategoryRealm::class.java)
                .equalTo("id", entry.aggregateLocalId)
                .findFirst()
            if (local != null && entry.aggregateLocalId != remoteId) {
                val name = local.name
                val active = local.active
                local.deleteFromRealm()
                realm.insertOrUpdate(CategoryRealm().apply {
                    id = remoteId
                    this.name = name
                    this.active = active
                })
                realm.where(SubcategoryRealm::class.java)
                    .equalTo("categoryId", entry.aggregateLocalId)
                    .findAll()
                    .forEach { it.categoryId = remoteId }
                realm.where(ProductRealm::class.java)
                    .equalTo("categoryId", entry.aggregateLocalId)
                    .findAll()
                    .forEach { it.categoryId = remoteId }
                realm.insertOrUpdate(SyncIdMapRealm().apply {
                    key = "category:${entry.aggregateLocalId}"
                    entityType = "category"
                    localId = entry.aggregateLocalId
                    this.remoteId = remoteId
                    createdAt = System.currentTimeMillis()
                })
            }
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingCategoryDelete(entry: PendingOutbox): Result<Unit> = runCatching {
        categoryApi.deleteCategory(resolveRemoteId("category", entry.aggregateLocalId)).getOrThrow()
        realmWrite { realm ->
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingSubcategory(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val categoryId = resolveRemoteId("category", payload.getLong("category_id"))
        val remoteId = categoryApi.saveSubcategory(
            SubcategoryAdminRow(
                id = entry.aggregateLocalId.takeIf { it > 0L } ?: 0L,
                categoryId = categoryId,
                name = payload.getString("name"),
                active = payload.optBoolean("active", true),
            ),
        ).getOrThrow()
        realmWrite { realm ->
            val local = realm.where(SubcategoryRealm::class.java)
                .equalTo("id", entry.aggregateLocalId)
                .findFirst()
            if (local != null && entry.aggregateLocalId != remoteId) {
                val name = local.name
                val active = local.active
                local.deleteFromRealm()
                realm.insertOrUpdate(SubcategoryRealm().apply {
                    id = remoteId
                    this.categoryId = categoryId
                    this.name = name
                    this.active = active
                })
                realm.where(ProductRealm::class.java)
                    .equalTo("subcategoryId", entry.aggregateLocalId)
                    .findAll()
                    .forEach {
                        it.subcategoryId = remoteId
                        it.categoryId = categoryId
                    }
                realm.insertOrUpdate(SyncIdMapRealm().apply {
                    key = "subcategory:${entry.aggregateLocalId}"
                    entityType = "subcategory"
                    localId = entry.aggregateLocalId
                    this.remoteId = remoteId
                    createdAt = System.currentTimeMillis()
                })
            }
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingSubcategoryDelete(entry: PendingOutbox): Result<Unit> = runCatching {
        categoryApi.deleteSubcategory(resolveRemoteId("subcategory", entry.aggregateLocalId)).getOrThrow()
        realmWrite { realm ->
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingClient(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val localId = entry.aggregateLocalId
        val row = ClientRow(
            id = localId.takeIf { it > 0L } ?: 0L,
            name = payload.getString("name"),
            document = payload.getString("document"),
            phone = payload.optString("phone"),
            active = payload.optBoolean("active", true),
            lastName = payload.optString("last_name"),
            email = payload.optString("email"),
            address = payload.optString("address"),
            businessName = payload.optString("business_name"),
            branchName = payload.optString("branch_name"),
            userId = payload.optLong("user_id"),
            personType = payload.optString("person_type", "Natural"),
            documentType = payload.optString("document_type", "DNI"),
            alias = payload.optString("alias"),
            gender = payload.optString("gender"),
            maritalStatus = payload.optString("marital_status"),
            discountPercentage = payload.optDouble("discount_percentage"),
            observations = payload.optString("observations"),
            webAccess = payload.optBoolean("web_access"),
        )
        val normalizedDocument = row.document.filter(Char::isLetterOrDigit).lowercase()
        val existingRemote = if (localId < 0L) {
            clientApi.list().getOrThrow().firstOrNull {
                it.document.filter(Char::isLetterOrDigit).lowercase() == normalizedDocument
            }
        } else {
            null
        }
        if (existingRemote == null) clientApi.save(row).getOrThrow()
        val remoteId = existingRemote?.id
            ?: localId.takeIf { it > 0L }
            ?: clientApi.list().getOrThrow().firstOrNull {
                it.document.filter(Char::isLetterOrDigit).lowercase() == normalizedDocument
            }?.id
            ?: error("El backend guardó el cliente, pero no devolvió un identificador verificable.")

        realmWrite { realm ->
            if (localId != remoteId) {
                val local = realm.where(ClientRealm::class.java).equalTo("id", localId).findFirst()
                local?.let {
                    val replacement = realm.copyFromRealm(it).apply { id = remoteId }
                    it.deleteFromRealm()
                    realm.insertOrUpdate(replacement)
                }
                realm.where(FinanzaVentaRealm::class.java)
                    .equalTo("idCliente", localId)
                    .findAll()
                    .forEach { it.idCliente = remoteId }
                realm.insertOrUpdate(SyncIdMapRealm().apply {
                    key = "client:$localId"
                    entityType = "client"
                    this.localId = localId
                    this.remoteId = remoteId
                    createdAt = System.currentTimeMillis()
                })
            }
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingClientDelete(entry: PendingOutbox): Result<Unit> = runCatching {
        clientApi.delete(resolveRemoteId("client", entry.aggregateLocalId)).getOrThrow()
        realmWrite { realm ->
            realm.where(ClientRealm::class.java)
                .equalTo("id", entry.aggregateLocalId)
                .findFirst()
                ?.deleteFromRealm()
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingSupplier(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val localId = entry.aggregateLocalId
        val row = SupplierRow(
            id = localId.takeIf { it > 0L } ?: 0L,
            codigoProveedor = payload.optString("codigo_proveedor"),
            businessName = payload.getString("business_name"),
            ruc = payload.getString("ruc"),
            correo = payload.optString("email"),
            phone = payload.optString("phone"),
            direccion = payload.optString("address"),
            personaContacto = payload.optString("contact_name"),
            cargoContacto = payload.optString("contact_role"),
            telefonoContacto = payload.optString("contact_phone"),
            correoContacto = payload.optString("contact_email"),
            calificacion = payload.optInt("rating"),
            estado = payload.optString("status", "Activo"),
            fechaRegistro = payload.optString("registration_date"),
            observaciones = payload.optString("observations"),
            banco = payload.optString("bank"),
            cuenta = payload.optString("account"),
            cci = payload.optString("cci"),
            active = payload.optBoolean("active", true),
        )
        val normalizedRuc = row.ruc.filter(Char::isDigit)
        val existingRemote = if (localId < 0L) {
            supplierCatalog.remoteRows().firstOrNull { it.ruc.filter(Char::isDigit) == normalizedRuc }
        } else {
            null
        }
        if (existingRemote == null) supplierCatalog.pushRemote(row).getOrThrow()
        val remoteId = existingRemote?.id
            ?: localId.takeIf { it > 0L }
            ?: supplierCatalog.remoteRows().firstOrNull {
                it.ruc.filter(Char::isDigit) == normalizedRuc
            }?.id
            ?: error("El backend guardó el proveedor, pero no devolvió un identificador verificable.")

        realmWrite { realm ->
            if (localId != remoteId) {
                val local = realm.where(SupplierRealm::class.java).equalTo("id", localId).findFirst()
                local?.let {
                    val replacement = realm.copyFromRealm(it).apply { id = remoteId }
                    it.deleteFromRealm()
                    realm.insertOrUpdate(replacement)
                }
                realm.insertOrUpdate(SyncIdMapRealm().apply {
                    key = "supplier:$localId"
                    entityType = "supplier"
                    this.localId = localId
                    this.remoteId = remoteId
                    createdAt = System.currentTimeMillis()
                })
            }
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingSupplierDelete(entry: PendingOutbox): Result<Unit> = runCatching {
        supplierCatalog.deleteRemote(resolveRemoteId("supplier", entry.aggregateLocalId)).getOrThrow()
        realmWrite { realm ->
            realm.where(SupplierRealm::class.java)
                .equalTo("id", entry.aggregateLocalId)
                .findFirst()
                ?.deleteFromRealm()
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingProductDelete(entry: PendingOutbox): Result<Unit> = runCatching {
        pendingProducts.deleteRemote(resolveRemoteId("product", entry.aggregateLocalId)).getOrThrow()
        realmWrite { realm ->
            realm.where(ProductRealm::class.java)
                .equalTo("id", entry.aggregateLocalId)
                .findFirst()
                ?.deleteFromRealm()
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingCashOpen(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val session = cashApi.openSession(
            cashRegisterId = payload.getLong("cash_register_id"),
            cashierId = payload.getLong("cashier_id"),
            openingAmount = payload.getDouble("opening_amount"),
        ).getOrThrow()
        realmWrite { realm ->
            realm.insertOrUpdate(
                SyncIdMapRealm().apply {
                    key = "cash_session:${entry.aggregateLocalId}"
                    entityType = "cash_session"
                    localId = entry.aggregateLocalId
                    remoteId = session.id
                    createdAt = System.currentTimeMillis()
                },
            )
            realm.where(FinanzaVentaRealm::class.java)
                .equalTo("idSesion", entry.aggregateLocalId)
                .findAll()
                .forEach { it.idSesion = session.id }
            realm.where(FinanzaSesionCajaRealm::class.java)
                .equalTo("id", entry.aggregateLocalId)
                .findFirst()
                ?.let { local ->
                    val replacement = realm.copyFromRealm(local).apply { id = session.id }
                    local.deleteFromRealm()
                    realm.insertOrUpdate(replacement)
                }
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
        if (prefs.getLong("pos_cash_session_id", 0L) == entry.aggregateLocalId) {
            cacheCashSession(session)
        }
    }

    private fun pushPendingCashClose(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        cashApi.closeSession(
            sessionId = resolveRemoteId("cash_session", payload.getLong("cash_session_id")),
            countedCash = payload.getDouble("counted_cash"),
            observations = payload.optString("observations"),
        ).getOrThrow()
        realmWrite { realm ->
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
    }

    private fun pushPendingReceiptEmail(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val pdf = File(payload.getString("pdf_path"))
        receiptDelivery.sendByEmail(
            email = payload.getString("email"),
            receiptNumber = payload.getString("receipt_number"),
            customerName = payload.optString("customer_name"),
            pdf = pdf,
        ).getOrThrow()
        realmWrite { realm ->
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
        pdf.delete()
    }

    private fun pushPendingReceiptWhatsapp(entry: PendingOutbox): Result<Unit> = runCatching {
        val payload = JSONObject(entry.payloadJson)
        val pdf = File(payload.getString("pdf_path"))
        receiptDelivery.requestWhatsappLink(
            receiptNumber = payload.getString("receipt_number"),
            customerName = payload.optString("customer_name"),
            pdf = pdf,
        ).getOrThrow()
        realmWrite { realm ->
            realm.where(OutboxRealm::class.java).equalTo("id", entry.id).findFirst()?.deleteFromRealm()
        }
        pdf.delete()
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
        if (emitter == null) return
        realmWrite { realm ->
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
        val editor = prefs.edit()
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
        if (!user.offlineSession) {
            editor.putString(
                "offline_user_profile",
                JSONObject()
                    .put("id", user.id)
                    .put("email", user.email)
                    .put("name", user.name)
                    .put("role", user.role)
                    .put("cashier_id", user.cashierId)
                    .put("cash_register_id", user.defaultCashRegisterId)
                    .put("cash_register_name", user.defaultCashRegisterName)
                    .put("document", user.document)
                    .put("phone", user.phone)
                    .put("address", user.address)
                    .put("branch_name", user.branchName)
                    .put("last_name", user.lastName)
                    .put("document_type", user.documentType)
                    .put("cashier_state", user.cashierState)
                    .put("avatar", user.avatar)
                    .put("avatar_base64", user.avatarBase64)
                    .toString(),
            )
        }
        editor.commit()
    }

    private fun getOfflineProfile(): UserSession? {
        val raw = prefs.getString("offline_user_profile", null) ?: return null
        return runCatching {
            val json = JSONObject(raw)
            UserSession(
                id = json.getLong("id"),
                email = json.getString("email"),
                name = json.getString("name"),
                role = json.optString("role", "admin"),
                offlineSession = true,
                cashierId = json.optLong("cashier_id"),
                defaultCashRegisterId = json.optLong("cash_register_id"),
                defaultCashRegisterName = json.optString("cash_register_name"),
                document = json.optString("document"),
                phone = json.optString("phone"),
                address = json.optString("address"),
                branchName = json.optString("branch_name"),
                lastName = json.optString("last_name"),
                documentType = json.optString("document_type", "DNI"),
                cashierState = json.optString("cashier_state", "Activo"),
                avatar = json.optString("avatar"),
                avatarBase64 = json.optString("avatar_base64"),
            )
        }.getOrNull()
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
        receptorDocumento: String,
        valorResumen: String = "",
    ): String {
        val buyerDocument = receptorDocumento.filter(Char::isDigit)
        val buyerDocumentType = when (buyerDocument.length) {
            11 -> "6" // RUC
            8 -> "1" // DNI
            else -> "" // No corresponde informar documento del adquirente
        }
        return listOf(
            ruc.filter(Char::isDigit),
            tipoDoc,
            serie,
            String.format(Locale.US, "%08d", correlativo),
            String.format(Locale.US, "%.2f", igv),
            String.format(Locale.US, "%.2f", total),
            fechaSunat,
            buyerDocumentType,
            buyerDocument,
            valorResumen,
        ).joinToString("|") + "|"
    }

    private fun nextId(realm: Realm, clazz: Class<out RealmObject>): Long {
        val max = realm.where(clazz).max("id") as Number?
        return (max?.toLong() ?: 0L) + 1L
    }

    private fun nextLocalId(realm: Realm, clazz: Class<out RealmObject>): Long {
        val min = (realm.where(clazz).min("id") as Number?)?.toLong() ?: 0L
        return if (min < 0L) min - 1L else -1L
    }

    private fun salePayloadJson(
        lines: List<CartLine>,
        payment: SalePaymentInfo,
        clientId: Long,
        cashSessionId: Long,
        customerInfo: ReceiptCustomerInfo,
        receiptType: TipoComprobanteEmision,
    ): String = JSONObject().apply {
        put("client_id", clientId)
        put("cash_session_id", cashSessionId)
        put("receipt_type", receiptType.name)
        put("payment", JSONObject().apply {
            put("type", payment.tipoPago)
            put("received", payment.montoRecibido)
            put("change", payment.vuelto)
        })
        put("customer", JSONObject().apply {
            put("id", customerInfo.id)
            put("name", customerInfo.name.trim())
            put("document", customerInfo.document.filter(Char::isDigit))
        })
        put("lines", JSONArray().apply {
            lines.forEach { line ->
                put(JSONObject().apply {
                    put("product_id", line.productId)
                    put("name", line.productName)
                    put("unit_price", line.unitPrice)
                    put("quantity", line.quantity)
                })
            }
        })
    }.toString()

    private fun resolveRemoteId(entityType: String, localId: Long): Long {
        if (localId >= 0L) return localId
        return realmQuery { realm ->
            realm.where(SyncIdMapRealm::class.java)
                .equalTo("key", "$entityType:$localId")
                .findFirst()
                ?.remoteId
        } ?: throw IllegalStateException(
            "La dependencia $entityType ($localId) aun no fue sincronizada.",
        )
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

        val preferredBase = ApiSessionStore(context).baseUrl

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
                "${ApiConfig.PRODUCTION_BASE_URL}${u.file}"
            } else {
                fullUrl
            }
        }.getOrDefault(fullUrl)
    }

    private data class ProductImageCacheResult(
        val total: Int,
        val downloaded: Int,
        val missing: Int,
        val failed: Int,
        val failureSummary: String = "",
    )

    private fun cacheProductImages(
        onProgress: (completed: Int, total: Int) -> Unit = { _, _ -> },
    ): ProductImageCacheResult {
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

        if (tasks.isEmpty()) return ProductImageCacheResult(0, 0, 0, 0)

        // Fase B: descargar imágenes (sin Realm abierto)
        val updates = java.util.concurrent.ConcurrentHashMap<Long, String>()
        val missingIds = java.util.concurrent.ConcurrentHashMap.newKeySet<Long>()
        val failures = java.util.concurrent.ConcurrentHashMap<String, java.util.concurrent.atomic.AtomicInteger>()
        val completed = java.util.concurrent.atomic.AtomicInteger(0)
        val pool = java.util.concurrent.Executors.newFixedThreadPool(minOf(8, tasks.size))
        onProgress(0, tasks.size)
        try {
            val futures = tasks.map { task ->
                pool.submit {
                    try {
                        val target = File(dir, "p_${task.id}.${task.ext}")
                        when (val result = productImagesApi.download(task.sourceUrl, target)) {
                            ProductImageDownloadResult.Downloaded -> {
                                updates[task.id] = "file://${target.absolutePath}"
                            }
                            is ProductImageDownloadResult.Missing -> missingIds += task.id
                            is ProductImageDownloadResult.Failed -> {
                                failures.computeIfAbsent(result.reason) { java.util.concurrent.atomic.AtomicInteger() }
                                    .incrementAndGet()
                            }
                        }
                    } finally {
                        onProgress(completed.incrementAndGet(), tasks.size)
                    }
                }
            }
            futures.forEach { it.get() }
        } finally {
            pool.shutdown()
        }

        // Fase C: guardar rutas file:// en Realm con transacción separada
        if (updates.isNotEmpty() || missingIds.isNotEmpty()) {
            realmWrite { realm ->
                updates.forEach { (id, filePath) ->
                    realm.where(ProductRealm::class.java).equalTo("id", id).findFirst()?.imageUrl = filePath
                }
                missingIds.forEach { id ->
                    realm.where(ProductRealm::class.java).equalTo("id", id).findFirst()?.imageUrl = ""
                }
            }
        }
        return ProductImageCacheResult(
            total = tasks.size,
            downloaded = updates.size,
            missing = missingIds.size,
            failed = failures.values.sumOf { it.get() },
            failureSummary = failures.entries
                .sortedByDescending { it.value.get() }
                .take(3)
                .joinToString { "${it.key}: ${it.value.get()}" },
        )
    }

    private fun localReceiptCustomerName(ventaId: Long): String = realmQuery { realm ->
        realm.where(FinanzaComprobanteRealm::class.java)
            .equalTo("idVenta", ventaId)
            .sort("id", io.realm.Sort.DESCENDING)
            .findFirst()
            ?.receptorRazonSocial
            .orEmpty()
            .takeUnless { it.isGenericCustomerName() }
            .orEmpty()
    }

    private fun String.isGenericCustomerName(): Boolean {
        val normalized = trim().uppercase(Locale.ROOT)
        return normalized.isBlank() || normalized in setOf(
            "GENERAL",
            "CLIENTE GENERAL",
            "CLIENTE GENERICO",
            "CLIENTE GENÉRICO",
            "CLIENTE VARIOS",
        )
    }

    private fun <T> realmQuery(block: (Realm) -> T): T = Realm.getDefaultInstance().use(block)
    private fun realmWrite(block: (Realm) -> Unit) = Realm.getDefaultInstance().use { realm ->
        realm.executeTransaction { block(it) }
    }

    private fun refreshProductStockAfterSales() {
        val products = remoteCatalog.fetchBestEffort().products
        if (products.isEmpty()) return
        realmWrite { realm ->
            products.forEach { remote ->
                realm.where(ProductRealm::class.java)
                    .equalTo("id", remote.id)
                    .notEqualTo("syncState", "PENDING")
                    .findFirst()
                    ?.let { local ->
                        local.stock = remote.stock
                        local.remoteUpdatedAt = remote.updatedAt
                    }
            }
        }
    }

    private fun Throwable?.isNetworkFailure(): Boolean {
        var current = this
        while (current != null) {
            if (current is IOException) return true
            current = current.cause
        }
        return false
    }

    private data class PendingOutbox(
        val id: String,
        val operation: String,
        val aggregateLocalId: Long,
        val payloadJson: String,
        val attemptCount: Int,
    )
}

