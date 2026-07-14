package com.ecommerce.ecommerceposapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.data.repository.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.CartLine
import com.ecommerce.ecommerceposapp.domain.CompletedSaleReceipt
import com.ecommerce.ecommerceposapp.domain.SalePaymentInfo
import com.ecommerce.ecommerceposapp.domain.CategoryItem
import com.ecommerce.ecommerceposapp.domain.ProductItem
import kotlin.math.round
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PosUiState(
    val categories: List<CategoryItem> = emptyList(),
    val products: List<ProductItem> = emptyList(),
    val selectedCategoryId: Long? = null,
    val search: String = "",
    val cart: List<CartLine> = emptyList(),
) {
    val subtotal: Double = round(cart.sumOf { it.lineTotal } * 100) / 100
    val igv: Double = round(subtotal * 0.18 * 100) / 100
    val total: Double = round((subtotal + igv) * 100) / 100
}

class PosViewModel(
    private val catalogRepository: CatalogRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PosUiState())
    val uiState: StateFlow<PosUiState> = _uiState

    fun load() {
        viewModelScope.launch {
            val categories = withContext(Dispatchers.IO) { catalogRepository.categories() }
            val products = withContext(Dispatchers.IO) { catalogRepository.products() }
            _uiState.update { it.copy(categories = categories, products = products) }
        }
    }

    fun setSearch(search: String) = _uiState.update { it.copy(search = search) }
    fun setCategory(categoryId: Long?) = _uiState.update { it.copy(selectedCategoryId = categoryId) }

    fun addToCart(product: ProductItem) {
        val current = _uiState.value.cart.toMutableList()
        val index = current.indexOfFirst { it.productId == product.id }
        if (index >= 0) {
            val row = current[index]
            current[index] = row.copy(quantity = row.quantity + 1)
        } else {
            current.add(CartLine(product.id, product.name, product.price, 1))
        }
        _uiState.update { it.copy(cart = current) }
    }

    fun increase(line: CartLine) {
        _uiState.update {
            it.copy(cart = it.cart.map { row -> if (row.productId == line.productId) row.copy(quantity = row.quantity + 1) else row })
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

    suspend fun pay(payment: SalePaymentInfo, idCliente: Long = 0L): Result<CompletedSaleReceipt> {
        val lines = _uiState.value.cart
        val result = withContext(Dispatchers.IO) { catalogRepository.registerSale(lines, payment, idCliente) }
        if (result.isSuccess) _uiState.update { it.copy(cart = emptyList()) }
        return result
    }
}
