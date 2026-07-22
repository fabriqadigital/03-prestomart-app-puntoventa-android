package com.ecommerce.ecommerceposapp.presentation.pos

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
) {
    val total: Double = round(cart.sumOf { it.lineTotal } * 100) / 100
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
            catalogRepository.refreshCatalog()
            Triple(catalogRepository.categories(), catalogRepository.subcategories(), catalogRepository.products())
        }
        _uiState.update { it.copy(categories = catalog.first, subcategories = catalog.second, products = catalog.third) }
    }

    fun setSearch(search: String) = _uiState.update { it.copy(search = search) }
    fun setCategory(categoryId: Long?) = _uiState.update { it.copy(selectedCategoryId = categoryId, selectedSubcategoryId = null) }
    fun setSubcategory(subcategoryId: Long?) = _uiState.update { it.copy(selectedSubcategoryId = subcategoryId) }

    fun toggleFeatured(product: ProductItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                catalogRepository.setProductFeatured(product.id, !product.featuredInPos)
            }
            val products = withContext(Dispatchers.IO) { catalogRepository.products() }
            _uiState.update { it.copy(products = products) }
        }
    }

    fun addToCart(product: ProductItem) {
        val current = _uiState.value.cart.toMutableList()
        val index = current.indexOfFirst { it.productId == product.id }
        if (index >= 0) {
            val row = current[index]
            if (row.quantity >= product.stock.toInt()) return
            current[index] = row.copy(quantity = row.quantity + 1)
        } else {
            if (product.stock <= 0.0) return
            current.add(CartLine(product.id, product.name, product.price, 1))
        }
        _uiState.update { it.copy(cart = current) }
    }

    fun increase(line: CartLine) {
        _uiState.update {
            val productStock = it.products.firstOrNull { p -> p.id == line.productId }?.stock?.toInt() ?: Int.MAX_VALUE
            it.copy(cart = it.cart.map { row -> if (row.productId == line.productId) row.copy(quantity = row.quantity + 1) else row })
                .let { next ->
                    next.copy(cart = next.cart.map { row ->
                        if (row.productId == line.productId && row.quantity > productStock) row.copy(quantity = productStock) else row
                    })
                }
        }
    }

    fun decrease(line: CartLine) {
        _uiState.update {
            val next = it.cart.mapNotNull { row ->
                if (row.productId != line.productId) return@mapNotNull row
                val quantity = row.quantity - 1
                if (quantity <= 0) null else row.copy(quantity = quantity)
            }
            it.copy(cart = next)
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
        val result = withContext(Dispatchers.IO) {
            catalogRepository.registerSale(lines, payment, idCliente, customerInfo, receiptType)
        }
        if (result.isSuccess) {
            val products = withContext(Dispatchers.IO) { catalogRepository.products() }
            _uiState.update { it.copy(cart = emptyList(), products = products) }
        }
        return result
    }
}
