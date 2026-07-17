package com.ecommerce.ecommerceposapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ecommerce.ecommerceposapp.data.remote.api.ApiSessionStore
import com.ecommerce.ecommerceposapp.data.remote.api.HostHeaderInterceptor
import com.ecommerce.ecommerceposapp.di.appModules
import com.ecommerce.ecommerceposapp.data.sync.NetworkSyncCoordinator
import com.ecommerce.ecommerceposapp.domain.repository.catalog.CatalogRepository
import com.ecommerce.ecommerceposapp.domain.repository.products.ProductRepository
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("ecommerce_pos.realm")
            .schemaVersion(12)
            .migration(RealmMigration { realm, oldVersion, _ ->
                if (oldVersion < 9L) {
                    realm.schema.get("ProductRealm")?.apply {
                        val requiredTextFields = listOf(
                            "barcode",
                            "slug",
                            "description",
                            "location",
                            "packageMeasures",
                            "packageDimension",
                            "promoCutoffTime",
                            "saturdayCutoffTime",
                            "metaTitle",
                            "metaDescription",
                        )
                        requiredTextFields.forEach { field ->
                            addField(field, String::class.java)
                            transform { product -> product.setString(field, "") }
                            setRequired(field, true)
                        }
                        addField("oldPrice", Double::class.javaPrimitiveType!!)
                        addField("costPrice", Double::class.javaPrimitiveType!!)
                        addField("wholesalePrice", Double::class.javaPrimitiveType!!)
                        addField("wholesaleOldPrice", Double::class.javaPrimitiveType!!)
                        addField("yapePrice", Double::class.javaPrimitiveType!!)
                        addField("minimumStock", Double::class.javaPrimitiveType!!)
                        addField("productTypeId", Long::class.javaPrimitiveType!!)
                        addField("ratingsEnabled", Boolean::class.javaPrimitiveType!!)
                        addField("adminRating", Double::class.javaPrimitiveType!!)
                        addField("weightKg", Double::class.javaPrimitiveType!!)
                        addField("offerMaxQuantity", Double::class.javaPrimitiveType!!)
                        addField("offerMaxQuantityPrice", Double::class.javaPrimitiveType!!)
                    }
                }
                if (oldVersion < 10L) {
                    realm.schema.get("ClientRealm")?.apply {
                        listOf("lastName", "email", "address", "businessName").forEach { field ->
                            addField(field, String::class.java)
                            transform { client -> client.setString(field, "") }
                            setRequired(field, true)
                        }
                    }
                }
                if (oldVersion < 11L) {
                    realm.schema.get("ProductRealm")?.apply {
                        addField("localCreatedAt", Long::class.javaPrimitiveType!!)
                        addField("remoteCreatedAt", Long::class.javaPrimitiveType!!)
                        addField("remoteUpdatedAt", Long::class.javaPrimitiveType!!)
                        addField("syncState", String::class.java)
                        addField("syncError", String::class.java)
                        transform { product ->
                            product.setString("syncState", "SYNCED")
                            product.setString("syncError", "")
                        }
                        setRequired("syncState", true)
                        setRequired("syncError", true)
                    }
                }
                if (oldVersion < 12L) {
                    realm.schema.get("ClientRealm")?.apply {
                        addField("branchName", String::class.java)
                        transform { client -> client.setString("branchName", "") }
                        setRequired("branchName", true)
                    }
                }
            })
            .compactOnLaunch()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HostHeaderInterceptor(ApiSessionStore(this)))
            .build()

        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .okHttpClient(okHttpClient)
                .diskCache {
                    DiskCache.Builder()
                        .directory(cacheDir.resolve("coil_images"))
                        .maxSizeBytes(150 * 1024 * 1024L)
                        .build()
                }
                .memoryCache {
                    MemoryCache.Builder(this)
                        .maxSizePercent(0.25)
                        .build()
                }
                .respectCacheHeaders(false)
                .build(),
        )

        val koin = startKoin {
            androidContext(this@PosApplication)
            modules(appModules)
        }.koin
        NetworkSyncCoordinator(
            this,
            koin.get<ProductRepository>(),
            koin.get<CatalogRepository>(),
        ).start()
    }
}
