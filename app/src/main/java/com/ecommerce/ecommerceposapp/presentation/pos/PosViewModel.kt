package com.ecommerce.ecommerceposapp.presentation.pos

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import com.ecommerce.ecommerceposapp.domain.model.sales.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.model.sales.ReceiptCustomerInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.model.sales.TipoComprobanteEmision
import com.ecommerce.ecommerceposapp.domain.model.catalog.CategoryItem
import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductItem
import com.ecommerce.ecommerceposapp.domain.model.catalog.ProductConversion
import com.ecommerce.ecommerceposapp.domain.model.catalog.SubcategoryItem
import com.ecommerce.ecommerceposapp.domain.model.cash.CashRegister
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSession
import com.ecommerce.ecommerceposapp.domain.model.cash.CashSummary
import kotlin.math.round
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class PosUiState(
    val categories: List<CategoryItem> = emptyList(),
    val subcategories: List<SubcategoryItem> = emptyList(),
    val products: List<ProductItem> = emptyList(),
    val selectedCategoryId: Long? = null,
    val selectedSubcategoryId: Long? = null,
    val search: String = "",
    val cart: List<CartLine> = emptyList(),
    val cashRegisters: List<CashRegister> = emptyList(),
    val cashSession: CashSession? = null,
    val cashSummary: CashSummary? = null,
    val cashLoading: Boolean = false,
    val cashError: String? = null,
    val message: String? = null,
    val descuentoPorcentaje: Double = 0.0,
    val descuentoLineKeys: Set<String> = emptySet(),
    val catalogReady: Boolean = false,
    val cashReady: Boolean = false,
    val offlineModeRequested: Boolean = false,
) {
    
    val initialLoading: Boolean get() = !catalogReady || !cashReady
    val totalAntesDescuento: Double = round(cart.sumOf { it.lineTotal } * 100) / 100
    val descuentoBase: Double =
        if (descuentoLineKeys.isEmpty()) totalAntesDescuento
        else round(cart.filter { it.lineKey in descuentoLineKeys }.sumOf { it.lineTotal } * 100) / 100
    val descuentoMonto: Double = round((descuentoBase * descuentoPorcentaje.coerceIn(0.0, 100.0) / 100.0) * 100) / 100
    val total: Double = (round((totalAntesDescuento - descuentoMonto) * 100) / 100).coerceAtLeast(0.0)
    val subtotal: Double = round((total / 1.18) * 100) / 100
    val igv: Double = round((total - subtotal) * 100) / 100
    val volumeDiscountMonto: Double = run {
        val total = cart.sumOf { line ->
            val product = products.firstOrNull { it.id == line.productId }
            if (product != null &&
                line.conversionId == null &&
                product.hasVolumePricing &&
                line.unitPrice == product.offerMaxQuantityPrice
            ) {
                (product.price - line.unitPrice) * line.quantity
            } else {
                0.0
            }
        }
        round(total * 100) / 100
    }
}

class PosViewModel(
    private val catalogRepository: CatalogRepository,
    private val context: Context,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PosUiState())
    val uiState: StateFlow<PosUiState> = _uiState
    private val catalogRefreshMutex = Mutex()

    private fun switchToOffline(message: String) {
        authRepository.enterOfflineMode()
        _uiState.update { it.copy(message = message, offlineModeRequested = true) }
    }

   
    private fun saveCartDraft() {
        val s = _uiState.value
        if (s.cart.isEmpty()) {
            CartPersistence.clear(context)
        } else {
            CartPersistence.save(context, s.cart, s.descuentoPorcentaje, s.descuentoLineKeys)
        }
    }

   
    private fun clearCartDraft() = CartPersistence.clear(context)

    
    fun loadImmediate(cashierId: Long) {
        viewModelScope.launch {
            
            val catalogDeferred = async(Dispatchers.IO) {
                Triple(
                    catalogRepository.categories(),
                    catalogRepository.subcategories(),
                    catalogRepository.products(),
                )
            }
            val cashDeferred = async(Dispatchers.IO) {
                runCatching {
                    val registers = catalogRepository.listCashRegisters().getOrThrow()
                    val current   = catalogRepository.findOpenCashSession(cashierId).getOrThrow()
                    registers to current
                }
            }

          
            val (cats, subs, prods) = catalogDeferred.await()
            val cashResult = cashDeferred.await()

            
            val savedCart = CartPersistence.load(context)

            cashResult.onSuccess { (registers, current) ->
                _uiState.update {
                    it.copy(
                        categories    = cats,
                        subcategories = subs,
                        products      = prods,
                        cart          = savedCart?.cart ?: emptyList(),
                        descuentoPorcentaje = savedCart?.descuentoPorcentaje ?: 0.0,
                        descuentoLineKeys   = savedCart?.descuentoLineKeys ?: emptySet(),
                        cashRegisters = registers,
                        cashSession   = current,
                        cashLoading   = false,
                        cashError     = null,
                        catalogReady  = true,
                        cashReady     = true,
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        categories    = cats,
                        subcategories = subs,
                        products      = prods,
                        cart          = savedCart?.cart ?: emptyList(),
                        descuentoPorcentaje = savedCart?.descuentoPorcentaje ?: 0.0,
                        descuentoLineKeys   = savedCart?.descuentoLineKeys ?: emptySet(),
                        cashLoading   = false,
                        cashError     = error.message ?: "No se pudo consultar la caja.",
                        catalogReady  = true,
                        cashReady     = true,
                    )
                }
                switchToOffline("No se pudo consultar el servidor. El POS continuará en modo offline.")
            }

            
            launch {
                try { refreshCatalog() }
                catch (e: Exception) {
                    android.util.Log.e("PosViewModel", "background refresh failed", e)
                }
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            try {
                refreshCatalog()
            } catch (e: Exception) {
                android.util.Log.e("PosViewModel", "refreshCatalog failed on load", e)
            } finally {
                _uiState.update { it.copy(catalogReady = true) }
            }
        }
    }

    fun loadCashSession(cashierId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(cashLoading = true, cashError = null) }
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    val registers = catalogRepository.listCashRegisters().getOrThrow()
                    val current = catalogRepository.findOpenCashSession(cashierId).getOrThrow()
                    registers to current
                }
            }
            result.onSuccess { (registers, current) ->
                _uiState.update { it.copy(cashRegisters = registers, cashSession = current, cashLoading = false, cashReady = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(cashLoading = false, cashError = error.message ?: "No se pudo consultar la caja.", cashReady = true) }
                switchToOffline("No se pudo consultar la caja online. El POS continuará en modo offline.")
            }
        }
    }

    suspend fun openCashSession(cashRegisterId: Long, cashierId: Long, amount: Double): Result<Unit> {
        _uiState.update { it.copy(cashLoading = true, cashError = null) }
        return withContext(Dispatchers.IO) { catalogRepository.openCashSession(cashRegisterId, cashierId, amount) }
            .map { opened -> _uiState.update { it.copy(cashSession = opened, cashLoading = false) } }
            .onFailure { error ->
                _uiState.update { it.copy(cashLoading = false, cashError = error.message) }
                switchToOffline("No se pudo abrir la caja online. El POS continuará en modo offline.")
            }
    }

    suspend fun loadCashSummary(): Result<CashSummary> {
        val id = _uiState.value.cashSession?.id ?: return Result.failure(Exception("No hay una caja abierta."))
        return withContext(Dispatchers.IO) { catalogRepository.cashSummary(id) }
            .onSuccess { value -> _uiState.update { it.copy(cashSummary = value) } }
    }

    suspend fun closeCashSession(countedCash: Double, observations: String): Result<Unit> {
        val id = _uiState.value.cashSession?.id ?: return Result.failure(Exception("No hay una caja abierta."))
        return withContext(Dispatchers.IO) { catalogRepository.closeCashSession(id, countedCash, observations) }
            .onSuccess { _uiState.update { it.copy(cashSession = null, cashSummary = null) } }
    }

    suspend fun refreshCatalog(isOnline: Boolean = true) = catalogRefreshMutex.withLock {
        val catalog = withContext(Dispatchers.IO) {
            if (isOnline) {
              
                try {
                    withTimeout(8_000L) { catalogRepository.refreshCatalog() }
                } catch (_: Exception) {
                   
                }
            }
            // Siempre leer desde Realm local (resultado fresco o caché)
            Triple(catalogRepository.categories(), catalogRepository.subcategories(), catalogRepository.products())
        }
        _uiState.update { it.copy(categories = catalog.first, subcategories = catalog.second, products = catalog.third) }
    }

    fun setSearch(search: String) = _uiState.update { it.copy(search = search) }
    fun setCategory(categoryId: Long?) = _uiState.update { it.copy(selectedCategoryId = categoryId, selectedSubcategoryId = null) }
    fun setSubcategory(subcategoryId: Long?) = _uiState.update { it.copy(selectedSubcategoryId = subcategoryId) }

    fun clearMessage() = _uiState.update { it.copy(message = null) }
    fun toggleFeatured(product: ProductItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                catalogRepository.setProductFeatured(product.id, !product.featuredInPos)
            }
            val products = withContext(Dispatchers.IO) { catalogRepository.products() }
            _uiState.update { it.copy(products = products) }
        }
    }
    fun addToCart(product: ProductItem, conversion: ProductConversion? = null) {
        val current = _uiState.value.cart.toMutableList()
        val conversionId = conversion?.id
        val index = current.indexOfFirst { it.productId == product.id && it.conversionId == conversionId }
        val step = if (product.isBulk) 0.1 else 1.0
        val totalProductQuantity = current.filter { it.productId == product.id }.sumOf { it.quantity }
        if (totalProductQuantity + step > product.stock + 0.000001) {
            _uiState.update { it.copy(message = "Stock insuficiente. Máximo ${formatStock(product.stock, product.isBulk)} de ${product.name}.") }
            return
        }
        val nextQuantity: Double
        if (index >= 0) {
            val row = current[index]
            val candidate = normalizeQuantity(row.quantity + step, row.isBulk)
            if (conversion != null && candidate > conversion.stockFactor + 0.000001) {
                _uiState.update { it.copy(message = "Stock insuficiente para ${conversion.name}.") }
                return
            }
            nextQuantity = candidate
            current[index] = row.copy(quantity = nextQuantity)
        } else {
            if (product.stock <= 0.0) return
            nextQuantity = normalizeQuantity(minOf(1.0, product.stock), product.isBulk)
            if (!product.isBulk && nextQuantity < 1.0) return
            if (conversion != null && conversion.stockFactor + 0.000001 < nextQuantity) {
                _uiState.update { it.copy(message = "Stock insuficiente para ${conversion.name}.") }
                return
            }
            current.add(CartLine(
                productId = product.id,
                productName = product.name,
                unitPrice = conversion?.finalPrice ?: product.price,
                quantity = nextQuantity,
                saleType = product.saleType,
                conversionId = conversionId,
                conversionName = conversion?.name.orEmpty(),
                stockFactor = conversion?.stockFactor ?: 0.0,
            ))
        }
        _uiState.update {
            it.copy(
                cart = applyVolumePricing(current, product),
                message = "Producto agregado al carrito",
            )
        }
        saveCartDraft()
    }

    fun increase(line: CartLine) {
        _uiState.update {
            val product = it.products.firstOrNull { p -> p.id == line.productId }
            val productStock = product?.stock ?: Double.MAX_VALUE
            val totalProductQuantity = it.cart.filter { row -> row.productId == line.productId }.sumOf { row -> row.quantity }
            val conversionStock = line.conversionId?.let { id -> product?.conversions?.firstOrNull { conversion -> conversion.id == id }?.stockFactor }
            val step = if (line.isBulk) 0.1 else 1.0
            val nextQuantity = normalizeQuantity(line.quantity + step, line.isBulk)
            if (totalProductQuantity + step > productStock + 0.000001) {
                it.copy(message = "Stock insuficiente. Máximo ${formatStock(productStock, line.isBulk)} de ${line.productName}.")
            } else if (conversionStock != null && nextQuantity > conversionStock + 0.000001) {
                it.copy(message = "Stock insuficiente para ${line.conversionName}.")
            } else {
                val recalculated = applyVolumePricing(
                    it.cart.map { row -> if (row.lineKey == line.lineKey) row.copy(quantity = nextQuantity) else row },
                    product ?: return@update it,
                )
                it.copy(cart = recalculated, message = "${line.productName}: ${formatStock(nextQuantity, line.isBulk)}")
            }
        }
        saveCartDraft()
    }

    fun decrease(line: CartLine) {
        _uiState.update {
            val next = it.cart.mapNotNull { row ->
                if (row.lineKey != line.lineKey) return@mapNotNull row
                val step = if (row.isBulk) 0.1 else 1.0
                val quantity = normalizeQuantity(row.quantity - step, row.isBulk)
                if (quantity <= 0) null else row.copy(quantity = quantity)
            }
            val product = it.products.firstOrNull { p -> p.id == line.productId }
            val recalculated = product?.let { prod -> applyVolumePricing(next, prod) } ?: next
            it.copy(
                cart = recalculated,
                descuentoPorcentaje = if (recalculated.isEmpty()) 0.0 else it.descuentoPorcentaje,
                descuentoLineKeys = if (recalculated.isEmpty()) emptySet() else it.descuentoLineKeys,
            )
        }
        saveCartDraft()
    }

    fun applyGlobalDiscount(percent: Double, lineKeys: Set<String>) {
        _uiState.update { state ->
            when {
                state.cart.isEmpty() -> state.copy(message = "No hay productos en el carrito para aplicar el descuento.")
                lineKeys.isEmpty() -> state.copy(message = "Selecciona al menos un producto para aplicar el descuento.")
                percent < 0.0 || percent > 100.0 -> state.copy(message = "El porcentaje de descuento debe estar entre 0 y 100.")
                else -> state.copy(
                    descuentoPorcentaje = percent,
                    descuentoLineKeys = lineKeys,
                    message = if (percent > 0.0) "Descuento de $percent% aplicado a ${lineKeys.size} producto(s)." else null,
                )
            }
        }
        saveCartDraft()
    }

    fun clearGlobalDiscount() {
        _uiState.update { it.copy(descuentoPorcentaje = 0.0, descuentoLineKeys = emptySet()) }
        saveCartDraft()
    }

    fun selectConversion(line: CartLine, conversion: ProductConversion?) {
        _uiState.update { state ->
            val product = state.products.firstOrNull { it.id == line.productId } ?: return@update state
            val totalProductQuantity = state.cart.filter { it.productId == line.productId }.sumOf { it.quantity }
            if (totalProductQuantity > product.stock) {
                return@update state.copy(message = "Stock insuficiente para ${conversion?.name ?: product.name}.")
            }
            val existing = state.cart.firstOrNull {
                it.productId == line.productId && it.conversionId == conversion?.id && it.lineKey != line.lineKey
            }
            val resultingConversionQuantity = line.quantity + (existing?.quantity ?: 0.0)
            if (conversion != null && resultingConversionQuantity > conversion.stockFactor) {
                return@update state.copy(message = "Stock insuficiente para ${conversion.name}.")
            }
            val replacement = line.copy(
                unitPrice = conversion?.finalPrice ?: product.price,
                conversionId = conversion?.id,
                conversionName = conversion?.name.orEmpty(),
                stockFactor = conversion?.stockFactor ?: 0.0,
            )
            val cart = if (existing == null) {
                state.cart.map { if (it.lineKey == line.lineKey) replacement else it }
            } else {
                state.cart.filterNot { it.lineKey == line.lineKey }.map {
                    if (it.lineKey == existing.lineKey) it.copy(quantity = it.quantity + line.quantity) else it
                }
            }
            state.copy(cart = cart, message = "Conversión actualizada")
        }
        saveCartDraft()
    }

    fun updateQuantity(line: CartLine, requestedQuantity: Double) {
        _uiState.update { state ->
            val product = state.products.firstOrNull { it.id == line.productId }
                ?: return@update state.copy(message = "El producto ${line.productName} ya no está disponible.")
            val productStock = product.stock
            val normalized = normalizeQuantity(requestedQuantity, line.isBulk)
            val validMinimum = if (line.isBulk) normalized >= 0.001 else normalized >= 1.0
            val isWholeUnit = line.isBulk || kotlin.math.abs(requestedQuantity - requestedQuantity.roundToInt()) < 0.000001
            val otherProductQuantity = state.cart
                .filter { it.productId == line.productId && it.lineKey != line.lineKey }
                .sumOf { it.quantity }
            val conversionStock = line.conversionId?.let { id ->
                product.conversions.firstOrNull { it.id == id }?.stockFactor
            }
            when {
                !validMinimum || !isWholeUnit -> state.copy(
                    message = if (line.isBulk) "Ingrese un peso mayor o igual a 0.001 kg."
                    else "Los productos por unidad solo aceptan cantidades enteras.",
                )
                otherProductQuantity + normalized > productStock + 0.000001 -> state.copy(
                    message = "Stock insuficiente. Máximo ${formatStock(productStock, line.isBulk)} de ${line.productName}.",
                )
                conversionStock != null && normalized > conversionStock + 0.000001 -> state.copy(
                    message = "Stock insuficiente para ${line.conversionName}.",
                )
                else -> {
                    val updatedCart = state.cart.map { row -> if (row.lineKey == line.lineKey) row.copy(quantity = normalized) else row }
                    state.copy(cart = applyVolumePricing(updatedCart, product), message = null)
                }
            }
        }
        saveCartDraft()
    }

    private fun normalizeQuantity(quantity: Double, isBulk: Boolean): Double =
        if (isBulk) round(quantity * 1000.0) / 1000.0 else quantity.roundToInt().toDouble()

    private fun formatStock(quantity: Double, isBulk: Boolean): String =
        if (isBulk) "${java.math.BigDecimal.valueOf(quantity).stripTrailingZeros().toPlainString()} kg"
        else "${quantity.roundToInt()} unidad(es)"

    private fun applyVolumePricing(cart: List<CartLine>, product: ProductItem): List<CartLine> {
        if (!product.hasVolumePricing) return cart
        val totalQty = cart.filter { it.productId == product.id }.sumOf { it.quantity }
        val applies = totalQty + 1e-9 >= product.offerMaxQuantity
        val targetPrice = if (applies) product.offerMaxQuantityPrice else product.price
        return cart.map { line ->
            if (line.productId != product.id || line.conversionId != null) line
            else if (line.unitPrice == targetPrice) line
            else line.copy(unitPrice = targetPrice)
        }
    }

    suspend fun pay(
        payment: SalePaymentInfo,
        idCliente: Long = 0L,
        customerInfo: ReceiptCustomerInfo = ReceiptCustomerInfo(),
        receiptType: TipoComprobanteEmision = TipoComprobanteEmision.SOLO_TICKET,
    ): Result<CompletedSaleReceipt> {
        if (_uiState.value.cashSession == null) return Result.failure(Exception("Abre una caja antes de registrar ventas."))
        val lines = _uiState.value.cart
        val previousProducts = _uiState.value.products
        
        _uiState.update { state ->
            state.copy(products = state.products.map { product ->
                val soldLines = lines.filter { it.productId == product.id }
                if (soldLines.isEmpty()) return@map product
                product.copy(
                    stock = (product.stock - soldLines.sumOf { it.quantity }).coerceAtLeast(0.0),
                    conversions = product.conversions.map { conversion ->
                        val sold = soldLines.filter { it.conversionId == conversion.id }.sumOf { it.quantity }
                        if (sold == 0.0) conversion else conversion.copy(
                            stockFactor = (conversion.stockFactor - sold).coerceAtLeast(0.0),
                        )
                    },
                )
            })
        }
        val descuentoPorcentaje = _uiState.value.descuentoPorcentaje
        val descuentoLineKeys = _uiState.value.descuentoLineKeys
        val result = withContext(Dispatchers.IO) {
            catalogRepository.registerSale(lines, payment, idCliente, customerInfo, receiptType, descuentoPorcentaje, descuentoLineKeys)
        }
        if (result.isSuccess) {
            val products = withContext(Dispatchers.IO) { catalogRepository.products() }
          
            clearCartDraft()
            _uiState.update {
                it.copy(cart = emptyList(), products = products, descuentoPorcentaje = 0.0, descuentoLineKeys = emptySet())
            }
            if (authRepository.getSession()?.offlineSession == true) {
                _uiState.update {
                    it.copy(
                        message = "La venta se guardó localmente. El POS continuará en modo offline y reintentará la sincronización.",
                        offlineModeRequested = true,
                    )
                }
            }
        } else {
            _uiState.update { it.copy(products = previousProducts) }
            switchToOffline("No se pudo completar la operación online. El POS continuará en modo offline y reintentará la sincronización.")
        }
        return result
    }

    fun removeLine(line: CartLine) {
        _uiState.update { state ->
            val next = state.cart.filterNot { it.lineKey == line.lineKey }
            state.copy(
                cart = next,
                descuentoPorcentaje = if (next.isEmpty()) 0.0 else state.descuentoPorcentaje,
                descuentoLineKeys = if (next.isEmpty()) emptySet() else state.descuentoLineKeys,
                message = "Producto eliminado del carrito",
            )
        }
        saveCartDraft()
    }
}
