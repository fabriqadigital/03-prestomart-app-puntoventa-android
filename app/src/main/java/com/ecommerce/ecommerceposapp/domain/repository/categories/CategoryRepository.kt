package com.ecommerce.ecommerceposapp.domain.repository.categories

import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.common.ServerPage

interface CategoryRepository {
    fun listCategoriesAdmin(): List<CategoryAdminRow>
    fun listCategoriesAdminPage(page: Int, perPage: Int, search: String): ServerPage<CategoryAdminRow>
    fun upsertCategory(row: CategoryAdminRow): Result<Unit>
    fun deleteCategory(id: Long): Result<Unit>
    fun listSubcategoriesAdmin(): List<SubcategoryAdminRow>
    fun upsertSubcategory(row: SubcategoryAdminRow): Result<Unit>
    fun deleteSubcategory(id: Long): Result<Unit>
}
