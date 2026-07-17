package com.ecommerce.ecommerceposapp.data.repository.categories

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.categories.CategoryRealm
import com.ecommerce.ecommerceposapp.data.local.categories.SubcategoryRealm
import com.ecommerce.ecommerceposapp.data.local.products.ProductRealm
import com.ecommerce.ecommerceposapp.data.remote.api.CategoryApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.repository.categories.CategoryRepository
import io.realm.Case

class CategoryRepositoryImpl(context: Context) : CategoryRepository {
    private val db = RealmDataSource(context)
    private val api = CategoryApiDataSource(context)

    override fun listCategoriesAdmin() = db.query { realm ->
        realm.where(CategoryRealm::class.java).findAll()
            .map { CategoryAdminRow(it.id, it.name, it.active) }
            .sortedByDescending { it.id }
    }

    override fun upsertCategory(row: CategoryAdminRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        return api.saveCategory(row).map { savedId ->
            db.write { realm ->
                realm.insertOrUpdate(CategoryRealm().apply { id = savedId; name = row.name.trim(); active = row.active })
            }
        }
    }

    override fun deleteCategory(id: Long): Result<Unit> {
        val hasProducts = db.query { realm ->
            realm.where(ProductRealm::class.java).equalTo("categoryId", id).count() > 0L
        }
        if (hasProducts) return Result.failure(Exception("No se puede eliminar la categoria porque tiene productos asociados."))
        return api.deleteCategory(id).onSuccess {
            db.write { realm ->
                realm.where(SubcategoryRealm::class.java).equalTo("categoryId", id).findAll().deleteAllFromRealm()
                realm.where(CategoryRealm::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
            }
        }
    }

    override fun listSubcategoriesAdmin() = db.query { realm ->
        realm.where(SubcategoryRealm::class.java).findAll()
            .map { SubcategoryAdminRow(it.id, it.categoryId, it.name, it.active) }
            .sortedByDescending { it.id }
    }

    override fun upsertSubcategory(row: SubcategoryAdminRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        val categoryActive = db.query { realm -> realm.where(CategoryRealm::class.java).equalTo("id", row.categoryId).equalTo("active", true).findFirst() != null }
        if (!categoryActive) return Result.failure(Exception("Seleccione una categoria activa para la subcategoria."))
        val duplicate = db.query { realm ->
            val existing = realm.where(SubcategoryRealm::class.java).equalTo("categoryId", row.categoryId).equalTo("name", row.name.trim(), Case.INSENSITIVE).findFirst()
            existing != null && existing.id != row.id
        }
        if (duplicate) return Result.failure(Exception("Ya existe una subcategoria con ese nombre en la categoria."))
        return api.saveSubcategory(row).map { savedId ->
            db.write { realm ->
                realm.insertOrUpdate(SubcategoryRealm().apply { id = savedId; categoryId = row.categoryId; name = row.name.trim(); active = row.active })
            }
        }
    }

    override fun deleteSubcategory(id: Long): Result<Unit> {
        val hasProducts = db.query { realm ->
            realm.where(ProductRealm::class.java).equalTo("subcategoryId", id).count() > 0L
        }
        if (hasProducts) return Result.failure(Exception("No se puede eliminar la subcategoria porque tiene productos asociados."))
        return api.deleteSubcategory(id).onSuccess {
            db.write { realm ->
                realm.where(SubcategoryRealm::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
            }
        }
    }
}
