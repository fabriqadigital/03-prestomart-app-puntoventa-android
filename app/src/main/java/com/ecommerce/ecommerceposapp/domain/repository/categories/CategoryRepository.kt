package com.ecommerce.ecommerceposapp.domain.repository.categories

import com.ecommerce.ecommerceposapp.domain.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.SubcategoryAdminRow

interface CategoryRepository {
    fun listCategoriesAdmin(): List<CategoryAdminRow>
    fun upsertCategory(row: CategoryAdminRow): Result<Unit>
    fun deleteCategory(id: Long): Result<Unit>
    fun listSubcategoriesAdmin(): List<SubcategoryAdminRow>
    fun upsertSubcategory(row: SubcategoryAdminRow): Result<Unit>
    fun deleteSubcategory(id: Long): Result<Unit>
}
