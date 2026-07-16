package com.ecommerce.ecommerceposapp.data.remote.api

object ApiConfig {
    const val PREFS_NAME = "pos_prefs"
    const val PRODUCTION_BASE_URL = "https://prestomartperu.com"

    // En el backend el bloque "Punto de Venta" vive hoy bajo /post.
    // Si lo renombran a /pos, solo cambia esta constante.
    const val POS_PREFIX = "/post"

    const val LOGIN = "$POS_PREFIX/login"
    const val PRODUCT_CREATE = "$POS_PREFIX/producto/crear"
    const val PRODUCT_UPDATE = "$POS_PREFIX/producto/actualizar"
    const val PRODUCT_LIST = "$POS_PREFIX/producto/listar"
    const val CATEGORY_LIST = "$POS_PREFIX/producto_categoria/listar"
    const val SUBCATEGORY_LIST = "$POS_PREFIX/producto_categoria_sub/listar"
    const val SYNC_CATALOG = "$POS_PREFIX/finanza/sync/catalog"
}
