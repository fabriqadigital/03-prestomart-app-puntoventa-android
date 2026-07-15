package com.ecommerce.ecommerceposapp

import android.app.Application
import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ecommerce.ecommerceposapp.di.appModules
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("ecommerce_pos.realm")
            .schemaVersion(6)
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)

        val prefs = getSharedPreferences("pos_prefs", Context.MODE_PRIVATE)

        // Interceptor: agrega Host header a peticiones de imagen hacia IPs del emulador
        // (Genymotion: 10.0.3.2, AVD: 10.0.2.2) para que Apache resuelva el vhost correcto.
        val hostInterceptor = Interceptor { chain ->
            val req = chain.request()
            val urlHost = req.url.host
            val isEmulatorHost = urlHost == "10.0.3.2" || urlHost == "10.0.2.2"
            if (isEmulatorHost) {
                val savedHost = prefs.getString("api_host_header", "")?.trim().orEmpty()
                val port = req.url.port.takeIf { it > 0 }
                val hostHeader = savedHost.ifBlank {
                    if (port != null && port != 80 && port != 443) "prestomart.localhost:$port"
                    else "prestomart.localhost"
                }
                chain.proceed(req.newBuilder().header("Host", hostHeader).build())
            } else {
                chain.proceed(req)
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(hostInterceptor)
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
