package com.ecommerce.ecommerceposapp.data.repository.products

import android.content.Context
import com.ecommerce.ecommerceposapp.data.local.categories.CategoryRealm
import com.ecommerce.ecommerceposapp.data.local.categories.SubcategoryRealm
import com.ecommerce.ecommerceposapp.data.local.products.ProductRealm
import com.ecommerce.ecommerceposapp.data.remote.api.ProductApiDataSource
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository

class ProductRepositoryImpl(context: Context) : ProductRepository {
    private val db = RealmDataSource(context)
    private val remote = ProductApiDataSource(context)

    override fun listProductsAdmin() = db.query { realm ->
        realm.where(ProductRealm::class.java).equalTo("active", true).findAll().map { product ->
            ProductAdminRow(
                id = product.id,
                categoryId = product.categoryId,
                subcategoryId = product.subcategoryId,
                name = product.name,
                code = product.codigo,
                imageUrl = remote.normalizedImageUrl(product.imageUrl),
                price = product.price,
                stock = product.stock,
                active = product.active,
            )
        }
    }

    override fun upsertProduct(row: ProductAdminRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        val validPath = db.query { realm ->
            val categoryOk = realm.where(CategoryRealm::class.java).equalTo("id", row.categoryId).equalTo("active", true).findFirst() != null
            val subcategoryOk = row.subcategoryId <= 0L || realm.where(SubcategoryRealm::class.java)
                .equalTo("id", row.subcategoryId).equalTo("categoryId", row.categoryId).equalTo("active", true).findFirst() != null
            categoryOk && subcategoryOk
        }
        if (!validPath) return Result.failure(Exception("La categoria o subcategoria seleccionada no esta activa o no corresponde."))
        remote.save(row).getOrElse { return Result.failure(it) }
        db.write { realm ->
            val id = if (row.id == 0L) db.nextId(realm, ProductRealm::class.java) else row.id
            realm.insertOrUpdate(ProductRealm().apply {
                this.id = id
                categoryId = row.categoryId
                subcategoryId = row.subcategoryId
                name = row.name.trim()
                codigo = row.code.trim()
                imageUrl = remote.normalizedImageUrl(row.imageUrl)
                price = row.price
                stock = row.stock
                active = row.active
            })
        }
        return Result.success(Unit)
    }

    override fun deleteProduct(id: Long): Result<Unit> {
        db.write { realm -> realm.where(ProductRealm::class.java).equalTo("id", id).findFirst()?.active = false }
        return Result.success(Unit)
    }
}
