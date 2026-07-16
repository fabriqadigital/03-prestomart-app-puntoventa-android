package com.ecommerce.ecommerceposapp.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.usecase.categories.GetCategoriesUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.categories.GetSubcategoriesUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.products.DeactivateProductUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.products.GetProductsUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.products.SaveProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ProductsUiState(
    val products: List<ProductAdminRow> = emptyList(),
    val categories: List<CategoryAdminRow> = emptyList(),
    val subcategories: List<SubcategoryAdminRow> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class ProductsViewModel(
    private val getProducts: GetProductsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getSubcategories: GetSubcategoriesUseCase,
    private val saveProduct: SaveProductUseCase,
    private val deactivateProduct: DeactivateProductUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        runCatching {
            withContext(Dispatchers.IO) { Triple(getProducts(), getCategories(), getSubcategories()) }
        }.onSuccess { (products, categories, subcategories) ->
            _uiState.update { it.copy(products = products, categories = categories, subcategories = subcategories, isLoading = false) }
        }.onFailure(::showError)
    }
    fun save(row: ProductAdminRow) = action("Producto guardado.") { saveProduct(row) }
    fun remove(id: Long) = action("Producto desactivado.") { deactivateProduct(id) }
    fun clearMessages() = _uiState.update { it.copy(message = null, error = null) }
    private fun action(message: String, block: suspend () -> Result<Unit>) = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, message = null, error = null) }
        withContext(Dispatchers.IO) { block() }.onSuccess { _uiState.update { it.copy(isSaving = false, message = message) }; load() }.onFailure(::showError)
    }
    private fun showError(error: Throwable) { _uiState.update { it.copy(isLoading = false, isSaving = false, error = error.message ?: "Ocurrio un error") } }
}
