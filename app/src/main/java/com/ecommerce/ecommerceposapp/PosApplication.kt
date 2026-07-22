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

        // Detecta si SharedPreferences fueron restauradas desde un backup de Android.
        // noBackupFilesDir nunca se respalda, por lo que si el marker no existe
        // es una instalación nueva (o reinstalación) y hay que limpiar la sesión guardada.
        clearSessionIfRestoredFromBackup()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("ecommerce_pos.realm")
            .schemaVersion(14)
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
                if (oldVersion < 13L) {
                    realm.schema.get("SupplierRealm")?.apply {
                        val requiredTextFields = listOf(
                            "codigoProveedor",
                            "correo",
                            "direccion",
                            "personaContacto",
                            "cargoContacto",
                            "telefonoContacto",
                            "correoContacto",
                            "fechaRegistro",
                            "observaciones",
                            "banco",
                            "cuenta",
                            "cci",
                        )
                        requiredTextFields.forEach { field ->
                            addField(field, String::class.java)
                            transform { supplier -> supplier.setString(field, "") }
                            setRequired(field, true)
                        }
                        addField("calificacion", Int::class.javaPrimitiveType!!)
                        addField("estado", String::class.java)
                        transform { supplier -> supplier.setString("estado", "Activo") }
                        setRequired("estado", true)
                    }
                }
                if (oldVersion < 14L) {
                    realm.schema.get("ClientRealm")?.apply {
                        addField("userId", Long::class.javaPrimitiveType!!)
                        listOf("personType", "documentType", "alias", "gender", "maritalStatus", "observations").forEach { field ->
                            addField(field, String::class.java)
                            transform { client -> client.setString(field, if (field == "personType") "Natural" else if (field == "documentType") "DNI" else "") }
                            setRequired(field, true)
                        }
                        addField("discountPercentage", Double::class.javaPrimitiveType!!)
                        addField("webAccess", Boolean::class.javaPrimitiveType!!)
                    }
                }
            })
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

    /**
     * Android Auto-Backup puede restaurar SharedPreferences al reinstalar la app,
     * lo que hace que el usuario aterrice en el POS con credenciales antiguas en vez
     * de ver la pantalla de Login.
     *
     * Para detectarlo usamos noBackupFilesDir (excluido explícitamente del backup):
     * si el marker no existe → primera ejecución tras instalación → limpiamos sesión.
     */
    private fun clearSessionIfRestoredFromBackup() {
        val markerFile = java.io.File(noBackupFilesDir, "install.marker")
        if (!markerFile.exists()) {
            // Instalación nueva o reinstalación: borrar cualquier sesión restaurada del backup
            getSharedPreferences("pos_prefs", MODE_PRIVATE).edit()
                .remove("session_user_id")
                .remove("session_email")
                .remove("session_name")
                .remove("session_role")
                .remove("session_offline")
                .remove("session_cashier_id")
                .remove("session_default_cash_register_id")
                .remove("session_default_cash_register_name")
                .remove("api_token")
                .remove("api_refresh_token")
                .remove("pos_cash_session_id")
                .remove("offline_auth_email")
                .remove("offline_auth_salt")
                .remove("offline_auth_verifier")
                .remove("offline_auth_verified_at")
                .apply()
            // Crear el marker para futuras ejecuciones normales
            noBackupFilesDir.mkdirs()
            markerFile.createNewFile()
        }
    }
}
