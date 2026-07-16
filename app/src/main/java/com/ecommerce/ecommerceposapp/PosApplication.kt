package com.ecommerce.ecommerceposapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ecommerce.ecommerceposapp.data.remote.api.ApiSessionStore
import com.ecommerce.ecommerceposapp.data.remote.api.HostHeaderInterceptor
import com.ecommerce.ecommerceposapp.di.appModules
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("ecommerce_pos.realm")
            .schemaVersion(7)
            .deleteRealmIfMigrationNeeded()
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

        startKoin {
            androidContext(this@PosApplication)
            modules(appModules)
        }
    }
}
