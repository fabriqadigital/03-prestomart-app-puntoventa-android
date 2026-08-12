package com.ecommerce.ecommerceposapp.presentation.pos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    /** Líneas del carrito a las que se aplica el descuento (por lineKey). */
    val descuentoLineKeys: Set<String> = emptySet(),
) {
    /** Total bruto del carrito (antes de cualquier descuento). */
    val totalAntesDescuento: Double = round(cart.sumOf { it.lineTotal } * 100) / 100
    /** Base del descuento: solo los productos seleccionados (todo el carrito si no hay selección). */
    val descuentoBase: Double =
        if (descuentoLineKeys.isEmpty()) totalAntesDescuento
        else round(cart.filter { it.lineKey in descuentoLineKeys }.sumOf { it.lineTotal } * 100) / 100
    /** Monto descontado: round(descuentoBase * porcentaje / 100, 2). */
    val descuentoMonto: Double = round((descuentoBase * descuentoPorcentaje.coerceIn(0.0, 100.0) / 100.0) * 100) / 100
    /** Total final de la venta (nunca negativo). */
    val total: Double = (round((totalAntesDescuento - descuentoMonto) * 100) / 100).coerceAtLeast(0.0)
    val subtotal: Double = round((total / 1.18) * 100) / 100
    val igv: Double = round((total - subtotal) * 100) / 100
}

class PosViewModel(
    private val catalogRepository: CatalogRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PosUiState())
    val uiState: StateFlow<PosUiState> = _uiState

    fun load() {
        viewModelScope.launch {
            refreshCatalog()
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
                _uiState.update { it.copy(cashRegisters = registers, cashSession = current, cashLoading = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(cashLoading = false, cashError = error.message ?: "No se pudo consultar la caja.") }
            }
        }
    }

    suspend fun openCashSession(cashRegisterId: Long, cashierId: Long, amount: Double): Result<Unit> {
        _uiState.update { it.copy(cashLoading = true, cashError = null) }
        return withContext(Dispatchers.IO) { catalogRepository.openCashSession(cashRegisterId, cashierId, amount) }
            .map { opened -> _uiState.update { it.copy(cashSession = opened, cashLoading = false) } }
            .onFailure { error -> _uiState.update { it.copy(cashLoading = false, cashError = error.message) } }
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

    suspend fun refreshCatalog() {
        val catalog = withContext(Dispatchers.IO) {
            // Actualiza Realm desde el backend antes de construir el catálogo visible.
            // Si no hay conexión, se conserva y muestra el catálogo local existente.
            catalogRepository.refreshCatalog()
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
        val totalProductQuantity = current.filter { it.productId == product.id }.sumOf { it.quantity }
        if (totalProductQuantity + 1 > product.stock) {
            _uiState.update { it.copy(message = "Stock insuficiente para ${product.name}.") }
            return
        }
        val nextQuantity: Int
        if (index >= 0) {
            val row = current[index]
            if (conversion != null && row.quantity + 1 > conversion.stockFactor) {
                _uiState.update { it.copy(message = "Stock insuficiente para ${conversion.name}.") }
                return
            }
            nextQuantity = row.quantity + 1
            current[index] = row.copy(quantity = nextQuantity)
        } else {
            if (conversion != null && conversion.stockFactor < 1.0) {
                _uiState.update { it.copy(message = "Stock insuficiente para ${conversion.name}.") }
                return
            }
            nextQuantity = 1
            current.add(CartLine(
                productId = product.id,
                productName = product.name,
                unitPrice = conversion?.finalPrice ?: product.price,
                quantity = 1,
                conversionId = conversionId,
                conversionName = conversion?.name.orEmpty(),
                stockFactor = conversion?.stockFactor ?: 0.0,
            ))
        }
        _uiState.update {
            it.copy(
                cart = current,
                message = "Producto agregado",
            )
        }
    }

    fun increase(line: CartLine) {
        _uiState.update {
            val product = it.products.firstOrNull { p -> p.id == line.productId }
            val productStock = product?.stock ?: Double.MAX_VALUE
            val totalProductQuantity = it.cart.filter { row -> row.productId == line.productId }.sumOf { row -> row.quantity }
            val conversionStock = line.conversionId?.let { id -> product?.conversions?.firstOrNull { conversion -> conversion.id == id }?.stockFactor }
            if (totalProductQuantity + 1 > productStock) {
                it.copy(message = "Stock insuficiente para ${line.productName}.")
            } else if (conversionStock != null && line.quantity + 1 > conversionStock) {
                it.copy(message = "Stock insuficiente para ${line.conversionName}.")
            } else {
                val nextQuantity = line.quantity + 1
                it.copy(
                    cart = it.cart.map { row ->
                        if (row.lineKey == line.lineKey) row.copy(quantity = nextQuantity) else row
                    },
                    message = "Producto agregado",
                )
            }
        }
    }

    fun decrease(line: CartLine) {
        _uiState.update {
            val next = it.cart.mapNotNull { row ->
                if (row.lineKey != line.lineKey) return@mapNotNull row
                val quantity = row.quantity - 1
                if (quantity <= 0) null else row.copy(quantity = quantity)
            }
            // Si el carrito queda vacío, el descuento ya no aplica.
            it.copy(
                cart = next,
                descuentoPorcentaje = if (next.isEmpty()) 0.0 else it.descuentoPorcentaje,
                descuentoLineKeys = if (next.isEmpty()) emptySet() else it.descuentoLineKeys,
            )
        }
    }

    /**
     * Aplica el descuento a la venta actual. El porcentaje lo ingresa
     * el cajero (0-100) y se calcula sobre los productos seleccionados.
     * Solo se permite un descuento por venta: volver a llamar esta
     * función reemplaza el porcentaje existente, nunca acumula.
     */
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
    }

    /** Quita el descuento de la venta actual. */
    fun clearGlobalDiscount() {
        _uiState.update { it.copy(descuentoPorcentaje = 0.0, descuentoLineKeys = emptySet()) }
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
            val resultingConversionQuantity = line.quantity + (existing?.quantity ?: 0)
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
    }

    suspend fun pay(
        payment: SalePaymentInfo,
        idCliente: Long = 0L,
        customerInfo: ReceiptCustomerInfo = ReceiptCustomerInfo(),
        receiptType: TipoComprobanteEmision = TipoComprobanteEmision.SOLO_TICKET,
    ): Result<CompletedSaleReceipt> {
        if (_uiState.value.cashSession == null) return Result.failure(Exception("Abre una caja antes de registrar ventas."))
        val lines = _uiState.value.cart
        val descuentoPorcentaje = _uiState.value.descuentoPorcentaje
        val descuentoLineKeys = _uiState.value.descuentoLineKeys
        val result = withContext(Dispatchers.IO) {
            catalogRepository.registerSale(lines, payment, idCliente, customerInfo, receiptType, descuentoPorcentaje, descuentoLineKeys)
        }
        if (result.isSuccess) {
            val products = withContext(Dispatchers.IO) { catalogRepository.products() }
            // La venta terminó: se vacía el carrito y se reinicia el descuento.
            _uiState.update {
                it.copy(cart = emptyList(), products = products, descuentoPorcentaje = 0.0, descuentoLineKeys = emptySet())
            }
        }
        return result
    }
}
