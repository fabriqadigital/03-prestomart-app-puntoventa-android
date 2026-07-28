package com.ecommerce.ecommerceposapp.data.repository.categories

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.categories.CategoryRealm
import com.ecommerce.ecommerceposapp.data.local.categories.SubcategoryRealm
import com.ecommerce.ecommerceposapp.data.local.products.ProductRealm
import com.ecommerce.ecommerceposapp.data.local.sync.OutboxRealm
import com.ecommerce.ecommerceposapp.data.remote.api.CategoryApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.categories.CategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.model.categories.SubcategoryAdminRow
import com.ecommerce.ecommerceposapp.domain.repository.categories.CategoryRepository
import io.realm.Case
import java.io.IOException
import java.util.UUID
import org.json.JSONObject

class CategoryRepositoryImpl(context: Context) : CategoryRepository {
    private val db = RealmDataSource(context)
    private val api = CategoryApiDataSource(context)

    override fun listCategoriesAdmin() = db.query { realm ->
        realm.where(CategoryRealm::class.java).findAll()
            .map { CategoryAdminRow(it.id, it.name, it.active) }
            .sortedWith(compareByDescending<CategoryAdminRow> { it.id < 0L }.thenByDescending { it.id })
    }

    override fun upsertCategory(row: CategoryAdminRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        val remote = api.saveCategory(row)
        if (remote.isSuccess) {
            val savedId = remote.getOrThrow()
            db.write { realm ->
                realm.insertOrUpdate(CategoryRealm().apply { id = savedId; name = row.name.trim(); active = row.active })
            }
            return Result.success(Unit)
        }
        val error = remote.exceptionOrNull()
        if (!error.canQueueOffline()) return Result.failure(error ?: Exception("No se pudo guardar la categoría."))
        val now = System.currentTimeMillis()
        val localId = row.id.takeIf { it != 0L } ?: -(now + (0..999).random())
        db.write { realm ->
            realm.insertOrUpdate(CategoryRealm().apply {
                id = localId
                name = row.name.trim()
                active = row.active
            })
            realm.insert(OutboxRealm().apply {
                id = UUID.randomUUID().toString()
                moduleKey = "categorias"
                operation = "UPSERT_CATEGORY"
                aggregateType = "category"
                aggregateLocalId = localId
                payloadJson = JSONObject()
                    .put("name", row.name.trim())
                    .put("active", row.active)
                    .toString()
                createdAt = now
                updatedAt = now
                state = "PENDING"
            })
        }
        return Result.success(Unit)
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
            .sortedWith(compareByDescending<SubcategoryAdminRow> { it.id < 0L }.thenByDescending { it.id })
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
        val remote = api.saveSubcategory(row)
        if (remote.isSuccess) {
            val savedId = remote.getOrThrow()
            db.write { realm ->
                realm.insertOrUpdate(SubcategoryRealm().apply { id = savedId; categoryId = row.categoryId; name = row.name.trim(); active = row.active })
            }
            return Result.success(Unit)
        }
        val error = remote.exceptionOrNull()
        if (!error.canQueueOffline()) return Result.failure(error ?: Exception("No se pudo guardar la subcategoría."))
        val now = System.currentTimeMillis()
        val localId = row.id.takeIf { it != 0L } ?: -(now + (0..999).random())
        db.write { realm ->
            realm.insertOrUpdate(SubcategoryRealm().apply {
                id = localId
                categoryId = row.categoryId
                name = row.name.trim()
                active = row.active
            })
            realm.insert(OutboxRealm().apply {
                id = UUID.randomUUID().toString()
                moduleKey = "subcategorias"
                operation = "UPSERT_SUBCATEGORY"
                aggregateType = "subcategory"
                aggregateLocalId = localId
                payloadJson = JSONObject()
                    .put("category_id", row.categoryId)
                    .put("name", row.name.trim())
                    .put("active", row.active)
                    .toString()
                createdAt = now
                updatedAt = now
                state = "PENDING"
            })
        }
        return Result.success(Unit)
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

    private fun Throwable?.canQueueOffline(): Boolean {
        var current = this
        while (current != null) {
            if (current is IOException) return true
            current = current.cause
        }
        return this?.message?.contains("sesion en linea", ignoreCase = true) == true ||
            this?.message?.contains("sesión en línea", ignoreCase = true) == true
    }
}
