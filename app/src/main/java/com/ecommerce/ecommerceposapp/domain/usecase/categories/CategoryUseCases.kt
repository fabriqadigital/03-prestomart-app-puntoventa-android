package com.ecommerce.ecommerceposapp.domain.usecase.categories

import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.repository.categories.CategoryRepository

class GetCategoriesUseCase(private val repository: CategoryRepository) { operator fun invoke() = repository.listCategoriesAdmin() }
class SaveCategoryUseCase(private val repository: CategoryRepository) { operator fun invoke(row: CategoryAdminRow) = repository.upsertCategory(row) }
class DeleteCategoryUseCase(private val repository: CategoryRepository) { operator fun invoke(id: Long) = repository.deleteCategory(id) }
class GetSubcategoriesUseCase(private val repository: CategoryRepository) { operator fun invoke() = repository.listSubcategoriesAdmin() }
class SaveSubcategoryUseCase(private val repository: CategoryRepository) { operator fun invoke(row: SubcategoryAdminRow) = repository.upsertSubcategory(row) }
class DeleteSubcategoryUseCase(private val repository: CategoryRepository) { operator fun invoke(id: Long) = repository.deleteSubcategory(id) }
