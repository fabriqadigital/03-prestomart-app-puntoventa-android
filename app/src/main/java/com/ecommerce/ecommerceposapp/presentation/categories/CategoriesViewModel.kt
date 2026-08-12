package com.ecommerce.ecommerceposapp.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.usecase.categories.DeleteCategoryUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.categories.DeleteSubcategoryUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.categories.GetCategoriesUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.categories.GetCategoriesPageUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.categories.GetSubcategoriesUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.categories.SaveCategoryUseCase
import com.ecommerce.ecommerceposapp.domain.usecase.categories.SaveSubcategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CategoriesUiState(
    val categories: List<CategoryAdminRow> = emptyList(),
    val allCategories: List<CategoryAdminRow> = emptyList(),
    val subcategories: List<SubcategoryAdminRow> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val perPage: Int = 20,
    val search: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class CategoriesViewModel(
    private val getCategories: GetCategoriesUseCase,
    private val getCategoriesPage: GetCategoriesPageUseCase,
    private val saveCategoryUseCase: SaveCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getSubcategories: GetSubcategoriesUseCase,
    private val saveSubcategoryUseCase: SaveSubcategoryUseCase,
    private val deleteSubcategoryUseCase: DeleteSubcategoryUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init { loadAll() }

    fun loadAll(page: Int = _uiState.value.page, perPage: Int = _uiState.value.perPage, search: String = _uiState.value.search) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        runCatching {
            withContext(Dispatchers.IO) { Triple(getCategoriesPage(page, perPage, search), getCategories(), getSubcategories()) }
        }.onSuccess { (categoryPage, allCategories, subcategories) ->
            _uiState.update { it.copy(
                categories = categoryPage.rows,
                allCategories = allCategories.ifEmpty { categoryPage.rows },
                subcategories = subcategories,
                total = categoryPage.total,
                page = categoryPage.page,
                perPage = categoryPage.perPage,
                search = search,
                isLoading = false,
            ) }
        }.onFailure { showError(it) }
    }

    fun saveCategory(row: CategoryAdminRow) = action("Categoria guardada.") { saveCategoryUseCase(row) }
    fun removeCategory(id: Long) = action("Categoria eliminada correctamente.") { deleteCategoryUseCase(id) }
    fun saveSubcategory(row: SubcategoryAdminRow) = action("Subcategoria guardada.") { saveSubcategoryUseCase(row) }
    fun removeSubcategory(id: Long) = action("Subcategoria eliminada correctamente.") { deleteSubcategoryUseCase(id) }
    fun clearMessages() = _uiState.update { it.copy(message = null, error = null) }

    private fun action(message: String, block: suspend () -> Result<Unit>) = viewModelScope.launch {
        _uiState.update { it.copy(isSaving = true, message = null, error = null) }
        withContext(Dispatchers.IO) { block() }
            .onSuccess { _uiState.update { it.copy(isSaving = false, message = message) }; loadAll() }
            .onFailure { showError(it) }
    }

    private fun showError(error: Throwable) {
        _uiState.update { it.copy(isLoading = false, isSaving = false, error = error.message ?: "Ocurrio un error") }
    }
}
