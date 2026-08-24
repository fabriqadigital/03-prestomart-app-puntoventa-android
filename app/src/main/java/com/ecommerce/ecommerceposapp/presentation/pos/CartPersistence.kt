package com.ecommerce.ecommerceposapp.presentation.pos

import android.content.Context
import android.content.SharedPreferences
import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import org.json.JSONArray
import org.json.JSONObject

/**
 * Guarda y restaura el carrito en SharedPreferences con un TTL de 15 minutos.
 */
object CartPersistence {

    private const val PREFS_NAME   = "pos_cart_draft"
    private const val KEY_CART     = "cart_json"
    private const val KEY_SAVED_AT = "cart_saved_at"
    private const val KEY_DISCOUNT_PCT  = "cart_discount_pct"
    private const val KEY_DISCOUNT_KEYS = "cart_discount_keys"

    /** 15 minutos en milisegundos */
    private const val TTL_MS = 15L * 60 * 1000

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

   

    fun save(
        ctx: Context,
        cart: List<CartLine>,
        descuentoPorcentaje: Double = 0.0,
        descuentoLineKeys: Set<String> = emptySet(),
    ) {
        val array = JSONArray()
        cart.forEach { line ->
            array.put(JSONObject().apply {
                put("productId",      line.productId)
                put("productName",    line.productName)
                put("unitPrice",      line.unitPrice)
                put("quantity",       line.quantity)
                put("saleType",       line.saleType)
                put("conversionId",   line.conversionId ?: -1L)
                put("conversionName", line.conversionName)
                put("stockFactor",    line.stockFactor)
            })
        }
        prefs(ctx).edit()
            .putString(KEY_CART,     array.toString())
            .putLong(KEY_SAVED_AT,   System.currentTimeMillis())
            .putFloat(KEY_DISCOUNT_PCT, descuentoPorcentaje.toFloat())
            .putStringSet(KEY_DISCOUNT_KEYS, descuentoLineKeys)
            .apply()
    }

   
    data class SavedCart(
        val cart: List<CartLine>,
        val descuentoPorcentaje: Double,
        val descuentoLineKeys: Set<String>,
        val savedAtMillis: Long,
    ) {
        /** Minutos restantes de vigencia (puede ser negativo si ya expiró) */
        val minutesLeft: Long
            get() = ((savedAtMillis + TTL_MS) - System.currentTimeMillis()) / 60_000
    }

    fun load(ctx: Context): SavedCart? {
        val p = prefs(ctx)
        val savedAt = p.getLong(KEY_SAVED_AT, 0L)
        if (savedAt == 0L) return null
        // Expirado
        if (System.currentTimeMillis() - savedAt > TTL_MS) {
            clear(ctx)
            return null
        }
        val json = p.getString(KEY_CART, null) ?: return null
        return runCatching {
            val array = JSONArray(json)
            val cart = (0 until array.length()).mapNotNull { i ->
                val obj = array.optJSONObject(i) ?: return@mapNotNull null
                CartLine(
                    productId      = obj.getLong("productId"),
                    productName    = obj.getString("productName"),
                    unitPrice      = obj.getDouble("unitPrice"),
                    quantity       = obj.getDouble("quantity"),
                    saleType       = obj.optString("saleType", "UNIDAD"),
                    conversionId   = obj.optLong("conversionId").takeIf { it > 0L },
                    conversionName = obj.optString("conversionName", ""),
                    stockFactor    = obj.optDouble("stockFactor", 0.0),
                )
            }
            SavedCart(
                cart                = cart,
                descuentoPorcentaje = p.getFloat(KEY_DISCOUNT_PCT, 0f).toDouble(),
                descuentoLineKeys   = p.getStringSet(KEY_DISCOUNT_KEYS, emptySet()).orEmpty(),
                savedAtMillis       = savedAt,
            )
        }.getOrNull()
    }

   
    fun clear(ctx: Context) {
        prefs(ctx).edit()
            .remove(KEY_CART)
            .remove(KEY_SAVED_AT)
            .remove(KEY_DISCOUNT_PCT)
            .remove(KEY_DISCOUNT_KEYS)
            .apply()
    }
}
