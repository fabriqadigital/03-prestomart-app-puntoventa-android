package com.ecommerce.ecommerceposapp.data.repository.products

import android.content.Context
import com.ecommerce.ecommerceposapp.BuildConfig
import com.ecommerce.ecommerceposapp.data.local.categories.CategoryRealm
import com.ecommerce.ecommerceposapp.data.local.categories.SubcategoryRealm
import com.ecommerce.ecommerceposapp.data.local.products.ProductRealm
import com.ecommerce.ecommerceposapp.data.remote.api.ProductApiDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.ApiConfig
import com.ecommerce.ecommerceposapp.data.remote.api.ApiSessionStore
import com.ecommerce.ecommerceposapp.data.remote.api.RemoteCatalogDataSource
import com.ecommerce.ecommerceposapp.data.remote.api.RemoteProductSeed
import com.ecommerce.ecommerceposapp.data.repository.common.RealmDataSource
import com.ecommerce.ecommerceposapp.domain.model.products.ProductAdminRow
import com.ecommerce.ecommerceposapp.domain.model.products.ProductTypeRow
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository
import java.io.IOException

class ProductRepositoryImpl(context: Context) : ProductRepository {
    private val db = RealmDataSource(context)
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)
    private val remote = ProductApiDataSource(context)
    private val remoteCatalog = RemoteCatalogDataSource(context)

    init {
        val savedBase = prefs.getString("api_base_url", null)?.trim().orEmpty()
        val activeBase = ApiSessionStore(context).baseUrl
        if (BuildConfig.DEBUG && savedBase.isNotBlank() && ApiConfig.normalizeBaseUrl(savedBase) != activeBase) {
            db.write { realm ->
                realm.where(ProductRealm::class.java).findAll().deleteAllFromRealm()
                realm.where(SubcategoryRealm::class.java).findAll().deleteAllFromRealm()
                realm.where(CategoryRealm::class.java).findAll().deleteAllFromRealm()
            }
        }
        if (BuildConfig.DEBUG) {
            prefs.edit()
                .putString("api_base_url", activeBase)
                .putString("api_host_header", ApiConfig.configuredHostHeader(null))
                .apply()
        }
    }

    override fun listProductsAdmin(): List<ProductAdminRow> = db.query { realm ->
        realm.where(ProductRealm::class.java).findAll().map(::toRow).sortedByDescending { it.id }
    }

    override fun listProductTypes(): Result<List<ProductTypeRow>> = remote.listTypes()

    override fun upsertProduct(row: ProductAdminRow): Result<Unit> {
        validate(row).getOrElse { return Result.failure(it) }
        val now = System.currentTimeMillis()
        val remoteResult = remote.save(row)
        if (remoteResult.isSuccess) {
            saveLocal(row, remoteResult.getOrThrow(), "SYNCED", now, "")
            return Result.success(Unit)
        }
        val error = remoteResult.exceptionOrNull() ?: Exception("No se pudo guardar el producto.")
        if (!error.hasNetworkCause()) return Result.failure(error)

        val temporaryId = row.id.takeIf { it != 0L } ?: -(now + (0..999).random())
        saveLocal(row, temporaryId, "PENDING", now, "Sin conexión. Se sincronizará automáticamente.")
        return Result.success(Unit)
    }

    override fun deleteProduct(id: Long): Result<Unit> {
        if (id <= 0L) return Result.failure(Exception("El producto aun no esta sincronizado y no puede eliminarse del servidor."))
        return remote.delete(id).onSuccess {
            db.write { realm ->
                realm.where(ProductRealm::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
            }
        }
    }

    override fun syncPendingProducts(): Result<Int> = runCatching {
        val pending = db.query { realm ->
            realm.where(ProductRealm::class.java).equalTo("syncState", "PENDING").findAll().map { product ->
                PendingProduct(toRow(product), product.localCreatedAt)
            }
        }
        if (pending.isEmpty()) return@runCatching 0

        val serverProducts = remoteCatalog.fetchBestEffort().products
        if (serverProducts.isEmpty()) throw IOException("Todavía no hay conexión para sincronizar productos.")
        var synchronized = 0
        pending.sortedBy { it.createdAt }.forEach { item ->
            val server = serverProducts.firstOrNull { it.sameIdentity(item.row) }
            val serverWins = server != null && server.createdAt > item.createdAt
            if (serverWins) {
                replaceLocal(item.row.id, server!!.toAdminRow(), server.id, server.createdAt, server.updatedAt)
                synchronized++
                return@forEach
            }

            val target = item.row.copy(id = server?.id ?: 0L, syncState = "SYNCED")
            val serverId = remote.save(target).getOrElse { error ->
                markError(item.row.id, error.message ?: "No se pudo sincronizar.")
                throw error
            }
            replaceLocal(item.row.id, target, serverId, item.createdAt, System.currentTimeMillis())
            synchronized++
        }
        synchronized
    }

    private fun validate(row: ProductAdminRow): Result<Unit> {
        if (row.name.isBlank()) return Result.failure(Exception("Nombre obligatorio."))
        val validPath = db.query { realm ->
            val categoryOk = realm.where(CategoryRealm::class.java).equalTo("id", row.categoryId).equalTo("active", true).findFirst() != null
            val subcategoryOk = row.subcategoryId <= 0L || realm.where(SubcategoryRealm::class.java)
                .equalTo("id", row.subcategoryId).equalTo("categoryId", row.categoryId).equalTo("active", true).findFirst() != null
            categoryOk && subcategoryOk
        }
        return if (validPath) Result.success(Unit) else Result.failure(Exception("La categoría o subcategoría no está activa o no corresponde."))
    }

    private fun saveLocal(row: ProductAdminRow, id: Long, state: String, createdAt: Long, error: String) {
        db.write { realm ->
            val existing = realm.where(ProductRealm::class.java).equalTo("id", row.id).findFirst()
            val originalCreatedAt = existing?.localCreatedAt?.takeIf { it > 0L } ?: createdAt
            if (row.id != 0L && row.id != id) existing?.deleteFromRealm()
            realm.insertOrUpdate(toRealm(row, id, state, originalCreatedAt, createdAt, error))
        }
    }

    private fun replaceLocal(oldId: Long, row: ProductAdminRow, newId: Long, createdAt: Long, updatedAt: Long) {
        db.write { realm ->
            if (oldId != newId) realm.where(ProductRealm::class.java).equalTo("id", oldId).findFirst()?.deleteFromRealm()
            realm.insertOrUpdate(toRealm(row, newId, "SYNCED", createdAt, updatedAt, ""))
        }
    }

    private fun markError(id: Long, message: String) = db.write { realm ->
        realm.where(ProductRealm::class.java).equalTo("id", id).findFirst()?.apply {
            syncState = "PENDING"
            syncError = message
        }
    }

    private fun toRealm(row: ProductAdminRow, id: Long, state: String, createdAt: Long, updatedAt: Long, error: String) = ProductRealm().apply {
        this.id = id
        categoryId = row.categoryId; subcategoryId = row.subcategoryId; name = row.name.trim()
        codigo = row.code.ifBlank { row.barcode }.trim(); barcode = row.barcode.trim(); slug = row.slug
        description = row.description; location = row.location; imageUrl = remote.normalizedImageUrl(row.imageUrl)
        price = row.price; stock = row.stock; oldPrice = row.oldPrice; costPrice = row.costPrice
        wholesalePrice = row.wholesalePrice; wholesaleOldPrice = row.wholesaleOldPrice; yapePrice = row.yapePrice
        minimumStock = row.minimumStock; productTypeId = row.productTypeId; canalVenta = row.salesChannel
        ratingsEnabled = row.ratingsEnabled; adminRating = row.adminRating; packageMeasures = row.packageMeasures
        packageDimension = row.packageDimension; weightKg = row.weightKg; promoCutoffTime = row.promoCutoffTime
        saturdayCutoffTime = row.saturdayCutoffTime; offerMaxQuantity = row.offerMaxQuantity
        offerMaxQuantityPrice = row.offerMaxQuantityPrice; metaTitle = row.metaTitle; metaDescription = row.metaDescription
        active = row.active; localCreatedAt = createdAt; remoteCreatedAt = createdAt; remoteUpdatedAt = updatedAt
        syncState = state; syncError = error
    }

    private fun toRow(product: ProductRealm) = ProductAdminRow(
        id = product.id, categoryId = product.categoryId, subcategoryId = product.subcategoryId,
        name = product.name, code = product.codigo, barcode = product.barcode, slug = product.slug,
        description = product.description, location = product.location, imageUrl = remote.normalizedImageUrl(product.imageUrl),
        price = product.price, oldPrice = product.oldPrice, costPrice = product.costPrice,
        wholesalePrice = product.wholesalePrice, wholesaleOldPrice = product.wholesaleOldPrice, yapePrice = product.yapePrice,
        stock = product.stock, minimumStock = product.minimumStock, productTypeId = product.productTypeId,
        salesChannel = product.canalVenta.ifBlank { "ambos" }, ratingsEnabled = product.ratingsEnabled,
        adminRating = product.adminRating, packageMeasures = product.packageMeasures, packageDimension = product.packageDimension,
        weightKg = product.weightKg, promoCutoffTime = product.promoCutoffTime, saturdayCutoffTime = product.saturdayCutoffTime,
        offerMaxQuantity = product.offerMaxQuantity, offerMaxQuantityPrice = product.offerMaxQuantityPrice,
        metaTitle = product.metaTitle, metaDescription = product.metaDescription, active = product.active, syncState = product.syncState,
    )

    private fun RemoteProductSeed.sameIdentity(row: ProductAdminRow): Boolean {
        val remoteBarcode = barcode.trim().lowercase()
        val localBarcode = row.barcode.trim().lowercase()
        if (remoteBarcode.isNotBlank() && localBarcode.isNotBlank()) return remoteBarcode == localBarcode
        val remoteCode = code.trim().lowercase()
        val localCode = row.code.trim().lowercase()
        return remoteCode.isNotBlank() && localCode.isNotBlank() && remoteCode == localCode
    }

    private fun RemoteProductSeed.toAdminRow() = ProductAdminRow(
        id = id, categoryId = categoryId, subcategoryId = subcategoryId, name = name, code = code, barcode = barcode,
        slug = slug, description = description, location = location, imageUrl = imageUrl, price = price, oldPrice = oldPrice,
        costPrice = costPrice, wholesalePrice = wholesalePrice, wholesaleOldPrice = wholesaleOldPrice, yapePrice = yapePrice,
        stock = stock, minimumStock = minimumStock, productTypeId = productTypeId, salesChannel = salesChannel,
        ratingsEnabled = ratingsEnabled, adminRating = adminRating, packageMeasures = packageMeasures,
        packageDimension = packageDimension, weightKg = weightKg, promoCutoffTime = promoCutoffTime,
        saturdayCutoffTime = saturdayCutoffTime, offerMaxQuantity = offerMaxQuantity,
        offerMaxQuantityPrice = offerMaxQuantityPrice, metaTitle = metaTitle, metaDescription = metaDescription,
        active = active, syncState = "SYNCED",
    )

    private fun Throwable.hasNetworkCause(): Boolean {
        var current: Throwable? = this
        while (current != null) {
            if (current is IOException) return true
            current = current.cause
        }
        return false
    }

    private data class PendingProduct(val row: ProductAdminRow, val createdAt: Long)
}
