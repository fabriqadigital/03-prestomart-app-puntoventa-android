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
import io.realm.RealmMigration
import io.realm.FieldAttribute
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
            .schemaVersion(22)
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
                if (oldVersion < 15L) {
                    realm.schema.create("OutboxRealm")
                        .addField("id", String::class.java, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
                        .addField("moduleKey", String::class.java, FieldAttribute.REQUIRED)
                        .addField("operation", String::class.java, FieldAttribute.REQUIRED)
                        .addField("aggregateType", String::class.java, FieldAttribute.REQUIRED)
                        .addField("aggregateLocalId", Long::class.javaPrimitiveType!!)
                        .addField("payloadJson", String::class.java, FieldAttribute.REQUIRED)
                        .addField("createdAt", Long::class.javaPrimitiveType!!)
                        .addField("updatedAt", Long::class.javaPrimitiveType!!)
                        .addField("attemptCount", Int::class.javaPrimitiveType!!)
                        .addField("nextAttemptAt", Long::class.javaPrimitiveType!!)
                        .addField("state", String::class.java, FieldAttribute.REQUIRED)
                        .addField("lastError", String::class.java, FieldAttribute.REQUIRED)
                    realm.schema.create("SyncIdMapRealm")
                        .addField("key", String::class.java, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
                        .addField("entityType", String::class.java, FieldAttribute.REQUIRED)
                        .addField("localId", Long::class.javaPrimitiveType!!)
                        .addField("remoteId", Long::class.javaPrimitiveType!!)
                        .addField("createdAt", Long::class.javaPrimitiveType!!)
                }
                if (oldVersion < 17L) {
                    realm.schema.get("OutboxRealm")?.let { schema ->
                        if (!schema.isRequired("id")) schema.setRequired("id", true)
                    }
                    realm.schema.get("SyncIdMapRealm")?.let { schema ->
                        if (!schema.isRequired("key")) schema.setRequired("key", true)
                    }
                }
                if (oldVersion < 18L) {
                    realm.schema.get("ProductRealm")?.apply {
                        addField("conversionsJson", String::class.java)
                        transform { product -> product.setString("conversionsJson", "[]") }
                        setRequired("conversionsJson", true)
                    }
                }
                if (oldVersion < 19L) {
                    realm.schema.get("FinanzaVentaRealm")?.apply {
                        addField("descuentoPorcentaje", Double::class.javaPrimitiveType!!)
                    }
                }
                if (oldVersion < 20L) {
                    realm.schema.get("ProductRealm")?.apply {
                        addField("saleType", String::class.java)
                        transform { product -> product.setString("saleType", "UNIDAD") }
                        setRequired("saleType", true)
                    }
                    realm.schema.get("FinanzaVentaDetalleRealm")?.apply {
                        addField("saleType", String::class.java)
                        transform { detail -> detail.setString("saleType", "UNIDAD") }
                        setRequired("saleType", true)
                    }
                }
                if (oldVersion < 21L) {
                    realm.schema.get("ProductRealm")?.apply {
                        addField("stockWeb", Double::class.javaPrimitiveType!!)
                    }
                }
                if (oldVersion < 22L) {
                    realm.schema.get("FinanzaVentaRealm")?.apply {
                        addField("currencyCode", String::class.java)
                        transform { venta -> venta.setString("currencyCode", "PEN") }
                        setRequired("currencyCode", true)
                        addField("exchangeRate", Double::class.javaPrimitiveType!!)
                        transform { venta -> venta.setDouble("exchangeRate", 1.0) }
                        addField("totalAmountInCurrency", Double::class.javaPrimitiveType!!)
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

        startKoin {
            androidContext(this@PosApplication)
            modules(appModules)
        }
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
            getSharedPreferences("pos_prefs", MODE_PRIVATE)
                .edit()
                .clear()
                .commit()
            // Crear el marker para futuras ejecuciones normales
            noBackupFilesDir.mkdirs()
            markerFile.createNewFile()
        }
    }
}
