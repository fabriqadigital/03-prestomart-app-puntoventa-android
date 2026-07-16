package com.ecommerce.ecommerceposapp.data.repository.categories

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.categories.CategoryRealm
import com.ecommerce.ecommerceposapp.data.local.categories.SubcategoryRealm
import com.ecommerce.ecommerceposapp.data.local.products.ProductRealm
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.repository.categories.CategoryRepository
import io.realm.Case

class CategoryRepositoryImpl(context: Context) : CategoryRepository {
    private val db = RealmDataSource(context)

    override fun listCategoriesAdmin() = db.query { realm -> realm.where(CategoryRealm::class.java).equalTo("active", true).findAll().map { CategoryAdminRow(it.id, it.name, it.active) } }

    override fun upsertCategory(row: CategoryAdminRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        db.write { realm ->
            val id = if (row.id == 0L) db.nextId(realm, CategoryRealm::class.java) else row.id
            realm.insertOrUpdate(CategoryRealm().apply { this.id = id; name = row.name.trim(); active = row.active })
        }
        return Result.success(Unit)
    }

    override fun deleteCategory(id: Long): Result<Unit> {
        db.write { realm ->
            realm.where(CategoryRealm::class.java).equalTo("id", id).findFirst()?.active = false
            realm.where(SubcategoryRealm::class.java).equalTo("categoryId", id).findAll().forEach { it.active = false }
            realm.where(ProductRealm::class.java).equalTo("categoryId", id).findAll().forEach { it.active = false }
        }
        return Result.success(Unit)
    }

    override fun listSubcategoriesAdmin() = db.query { realm -> realm.where(SubcategoryRealm::class.java).equalTo("active", true).findAll().map { SubcategoryAdminRow(it.id, it.categoryId, it.name, it.active) } }

    override fun upsertSubcategory(row: SubcategoryAdminRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        val categoryActive = db.query { realm -> realm.where(CategoryRealm::class.java).equalTo("id", row.categoryId).equalTo("active", true).findFirst() != null }
        if (!categoryActive) return Result.failure(Exception("Seleccione una categoria activa para la subcategoria."))
        val duplicate = db.query { realm ->
            val existing = realm.where(SubcategoryRealm::class.java).equalTo("categoryId", row.categoryId).equalTo("name", row.name.trim(), Case.INSENSITIVE).findFirst()
            existing != null && existing.id != row.id
        }
        if (duplicate) return Result.failure(Exception("Ya existe una subcategoria con ese nombre en la categoria."))
        db.write { realm ->
            val id = if (row.id == 0L) db.nextId(realm, SubcategoryRealm::class.java) else row.id
            realm.insertOrUpdate(SubcategoryRealm().apply { this.id = id; categoryId = row.categoryId; name = row.name.trim(); active = row.active })
        }
        return Result.success(Unit)
    }

    override fun deleteSubcategory(id: Long): Result<Unit> {
        db.write { realm ->
            realm.where(SubcategoryRealm::class.java).equalTo("id", id).findFirst()?.active = false
            realm.where(ProductRealm::class.java).equalTo("subcategoryId", id).findAll().forEach { it.active = false }
        }
        return Result.success(Unit)
    }
}
