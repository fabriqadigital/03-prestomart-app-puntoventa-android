package com.ecommerce.ecommerceposapp.data.remote.api

import android.content.Context
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject

data class RemoteProductSeed(
    val id: Long,
    val categoryId: Long,
    val subcategoryId: Long,
    val categoryName: String?,
    val subcategoryName: String?,
    val name: String,
    val code: String,
    val imageUrl: String,
    val price: Double,
    val stock: Double,
    val active: Boolean,
)

data class RemoteCategorySeed(
    val id: Long,
    val name: String,
    val active: Boolean,
)

data class RemoteSubcategorySeed(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val active: Boolean,
)

data class RemoteCatalogSeed(
    val categories: List<RemoteCategorySeed> = emptyList(),
    val subcategories: List<RemoteSubcategorySeed> = emptyList(),
    val products: List<RemoteProductSeed> = emptyList(),
)

class RemoteCatalogDataSource(context: Context) {
    private val prefs = context.getSharedPreferences(ApiConfig.PREFS_NAME, Context.MODE_PRIVATE)
    private val candidates = listOf(ApiBaseCandidate(ApiConfig.PRODUCTION_BASE_URL))

    fun fetchBestEffort(): RemoteCatalogSeed {
        val preferredBase = prefs.getString("api_base_url", null)
        val preferredHost = prefs.getString("api_host_header", null)?.takeIf { it.isNotBlank() }
        val preferred = preferredBase?.let { ApiBaseCandidate(it, preferredHost) }
        val bases = listOfNotNull(preferred) + candidates.filter { it.baseUrl != preferredBase }
        for (base in bases) {
            val catalog = runCatching {
                fetchCatalogFromEndpoint("${base.baseUrl}/api${ApiConfig.SYNC_CATALOG}", base)
            }.getOrNull()
            if (catalog != null && (catalog.categories.isNotEmpty() || catalog.products.isNotEmpty())) {
                val categories = catalog.categories.ifEmpty {
                    runCatching { fetchCategoriesFromEndpoint("${base.baseUrl}/api${ApiConfig.CATEGORY_LIST}", base) }.getOrDefault(emptyList())
                }
                val subcategories = catalog.subcategories.ifEmpty {
                    runCatching { fetchSubcategoriesFromEndpoint("${base.baseUrl}/api${ApiConfig.SUBCATEGORY_LIST}", base) }.getOrDefault(emptyList())
                }
                return catalog.copy(categories = categories, subcategories = subcategories)
            }
            val categories = runCatching {
                fetchCategoriesFromEndpoint("${base.baseUrl}/api${ApiConfig.CATEGORY_LIST}", base)
            }.getOrDefault(emptyList())
            if (categories.isNotEmpty()) {
                val subcategories = runCatching {
                    fetchSubcategoriesFromEndpoint("${base.baseUrl}/api${ApiConfig.SUBCATEGORY_LIST}", base)
                }.getOrDefault(emptyList())
                return RemoteCatalogSeed(categories = categories, subcategories = subcategories)
            }
        }
        return RemoteCatalogSeed()
    }

    private fun fetchCatalogFromEndpoint(url: String, base: ApiBaseCandidate): RemoteCatalogSeed {
        val payload = openJson(url, base)
        return parseRemoteCatalog(payload, base.baseUrl)
    }

    private fun fetchCategoriesFromEndpoint(url: String, base: ApiBaseCandidate): List<RemoteCategorySeed> {
        return parseRemoteCategories(openJson(url, base))
    }

    private fun fetchSubcategoriesFromEndpoint(url: String, base: ApiBaseCandidate): List<RemoteSubcategorySeed> {
        return parseRemoteSubcategories(openJson(url, base))
    }

    private fun openJson(url: String, base: ApiBaseCandidate): String {
        val conn = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 6000
            readTimeout = 6000
            setRequestProperty("Accept", "application/json")
            base.hostHeader?.let { setRequestProperty("Host", it) }
            addAuthHeaderIfAvailable(this)
        }
        return conn.inputStream.bufferedReader().use { it.readText().trim() }
    }

    private fun parseRemoteCatalog(payload: String, base: String): RemoteCatalogSeed {
        val root: Any = if (payload.trim().startsWith("[")) JSONArray(payload) else JSONObject(payload)
        val rootObj = root as? JSONObject
        val categoriesArray = rootObj?.optArrayAny("categories", "categorias", "categorias_producto", "producto_categoria")
        val subcategoriesArray = rootObj?.optArrayAny("subcategories", "subcategorias", "producto_categoria_sub", "categoria_sub")
        val productsArray = rootObj?.optArrayAny("products", "productos", "items", "articulos", "result")
            ?: (root as? JSONArray)
        return RemoteCatalogSeed(
            categories = categoriesArray?.let { parseRemoteCategories(it) }.orEmpty(),
            subcategories = subcategoriesArray?.let { parseRemoteSubcategories(it) }.orEmpty(),
            products = productsArray?.let { parseRemoteProducts(it, base) }.orEmpty(),
        )
    }

    private fun parseRemoteCategories(payload: String): List<RemoteCategorySeed> {
        val root: Any = if (payload.trim().startsWith("[")) JSONArray(payload) else JSONObject(payload)
        val array = (root as? JSONObject)?.optArrayAny("result", "categories", "categorias", "data")
            ?: (root as? JSONArray)
            ?: return emptyList()
        return parseRemoteCategories(array)
    }

    private fun parseRemoteCategories(array: JSONArray): List<RemoteCategorySeed> {
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            val id = obj.optAnyLong("id_producto_categoria", "category_id", "id_categoria", "id")
            val name = obj.optAnyString("nombre", "name", "category_name", "categoria")
            if (id <= 0L || name.isBlank()) null else RemoteCategorySeed(id, name, obj.optActive())
        }.distinctBy { it.id }
    }

    private fun parseRemoteSubcategories(payload: String): List<RemoteSubcategorySeed> {
        val root: Any = if (payload.trim().startsWith("[")) JSONArray(payload) else JSONObject(payload)
        val array = (root as? JSONObject)?.optArrayAny("result", "subcategories", "subcategorias", "data")
            ?: (root as? JSONArray)
            ?: return emptyList()
        return parseRemoteSubcategories(array)
    }

    private fun parseRemoteSubcategories(array: JSONArray): List<RemoteSubcategorySeed> {
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            val id = obj.optAnyLong("id_producto_categoria_sub", "subcategory_id", "id_subcategoria", "subcategoria_id", "id")
            val categoryId = obj.optAnyLong("id_producto_categoria", "category_id", "id_categoria", "categoria_id")
            val name = obj.optAnyString("nombre", "name", "subcategory_name", "subcategoria", "nombre_subcategoria")
            if (id <= 0L || categoryId <= 0L || name.isBlank()) null else RemoteSubcategorySeed(id, categoryId, name, obj.optActive())
        }.distinctBy { it.id }
    }

    private fun parseRemoteProducts(root: Any, base: String): List<RemoteProductSeed> {
        val imageFields = listOf("url_imagen", "image_url", "image", "imagen", "foto", "thumbnail", "thumb", "imagen_url", "img", "photo")
        val nameFields = listOf("nombre", "name", "title", "producto", "descripcion")
        val priceFields = listOf("precio", "price", "pvp", "sale_price", "precio_venta")
        val stockFields = listOf("stock", "cantidad", "existencia", "stock_actual")
        val codeFields = listOf("codigo_producto", "code", "codigo", "sku", "barcode", "codigo_barras")
        val categoryIdFields = listOf("id_producto_categoria", "category_id", "id_categoria", "categoria_id", "id_categoria_producto")
        val subcategoryIdFields = listOf("id_producto_categoria_sub", "subcategory_id", "id_subcategoria", "subcategoria_id")
        val categoryNameFields = listOf("category_name", "categoria", "category", "nombre_categoria")
        val subcategoryNameFields = listOf("subcategory_name", "subcategoria", "subcategory", "nombre_subcategoria", "nombre_sub_categoria")

        fun optAnyString(obj: JSONObject, keys: List<String>): String {
            keys.forEach { key ->
                val value = obj.optString(key, "").trim()
                if (value.isNotBlank() && !value.equals("null", ignoreCase = true)) return value
            }
            return ""
        }

        fun optAnyDouble(obj: JSONObject, keys: List<String>): Double {
            keys.forEach { key ->
                when (val raw = obj.opt(key)) {
                    is Number -> return raw.toDouble()
                    is String -> raw.replace(",", ".").toDoubleOrNull()?.let { return it }
                }
            }
            return 0.0
        }

        fun optAnyLong(obj: JSONObject, keys: List<String>): Long {
            keys.forEach { key ->
                when (val raw = obj.opt(key)) {
                    is Number -> return raw.toLong()
                    is String -> raw.toLongOrNull()?.let { return it }
                }
            }
            return 0L
        }

        fun normalizeImage(rawImage: String): String {
            val raw = rawImage.trim()
            return when {
                raw.isBlank() -> ""
                raw.startsWith("http://") || raw.startsWith("https://") -> raw
                raw.startsWith("/") -> "$base$raw"
                else -> "$base/$raw"
            }
        }

        fun mapObj(obj: JSONObject): RemoteProductSeed? {
            val name = optAnyString(obj, nameFields)
            val price = optAnyDouble(obj, priceFields)
            if (name.isBlank() || price <= 0.0) return null
            return RemoteProductSeed(
                id = obj.optLong("id_producto", obj.optLong("id", obj.optLong("product_id", 0L))),
                categoryId = optAnyLong(obj, categoryIdFields).takeIf { it > 0L } ?: 1L,
                subcategoryId = optAnyLong(obj, subcategoryIdFields),
                categoryName = optAnyString(obj, categoryNameFields).ifBlank { null },
                subcategoryName = optAnyString(obj, subcategoryNameFields).ifBlank { null },
                name = name,
                code = optAnyString(obj, codeFields),
                imageUrl = normalizeImage(optAnyString(obj, imageFields)),
                price = price,
                stock = optAnyDouble(obj, stockFields),
                active = obj.optActive(),
            )
        }

        fun collectObjects(node: Any?, out: MutableList<JSONObject>) {
            when (node) {
                is JSONObject -> {
                    out += node
                    val keys = node.keys()
                    while (keys.hasNext()) collectObjects(node.opt(keys.next()), out)
                }
                is JSONArray -> for (i in 0 until node.length()) collectObjects(node.opt(i), out)
            }
        }

        val allObjects = mutableListOf<JSONObject>()
        collectObjects(root, allObjects)
        return allObjects.mapNotNull { mapObj(it) }.distinctBy { "${it.id}-${it.code}-${it.name}" }
    }

    private fun JSONObject.optArrayAny(vararg keys: String): JSONArray? {
        keys.forEach { key ->
            val value = opt(key)
            if (value is JSONArray) return value
        }
        return null
    }

    private fun JSONObject.optAnyString(vararg keys: String): String {
        keys.forEach { key ->
            val raw = opt(key)
            val value = when (raw) {
                is String -> raw
                is Number -> raw.toString()
                is Boolean -> raw.toString()
                else -> ""
            }.trim()
            if (value.isNotBlank() && !value.equals("null", ignoreCase = true)) return value
        }
        return ""
    }

    private fun JSONObject.optAnyLong(vararg keys: String): Long {
        keys.forEach { key ->
            when (val raw = opt(key)) {
                is Number -> return raw.toLong()
                is String -> raw.trim().toLongOrNull()?.let { return it }
            }
        }
        return 0L
    }

    private fun JSONObject.optActive(default: Boolean = true): Boolean {
        val raw = opt("Activo").takeUnless { it == null }
            ?: opt("Active").takeUnless { it == null }
            ?: opt("active").takeUnless { it == null }
            ?: opt("activo").takeUnless { it == null }
            ?: return default
        return when (raw) {
            is Boolean -> raw
            is Number -> raw.toInt() != 0
            is String -> raw.trim().let { value ->
                value.equals("S", true) ||
                    value.equals("SI", true) ||
                    value.equals("TRUE", true) ||
                    value == "1" ||
                    value.equals("A", true)
            }
            else -> default
        }
    }

    private fun addAuthHeaderIfAvailable(conn: HttpURLConnection) {
        val token = prefs.getString("api_token", "")?.trim().orEmpty()
        if (token.isNotBlank()) conn.setRequestProperty("Authorization", "Bearer $token")
    }

    private data class ApiBaseCandidate(val baseUrl: String, val hostHeader: String? = null)
}
