package com.ecommerce.ecommerceposapp.util

import android.os.SystemClock

/**
 * Mantiene la pantalla del POS encendida y permite atenuarla tras un periodo
 * sin interacción. Cualquier toque, tecla o lectura del escáner reinicia el
 * contador desde MainActivity.
 */
object PosIdleMonitor {
    const val POLL_INTERVAL_MILLIS = 1_000L
    const val DIMMED_BRIGHTNESS = 0.08f
    private const val DIM_AFTER_MILLIS = 2 * 60 * 1_000L

    @Volatile
    private var lastInteractionAt = SystemClock.elapsedRealtime()

    fun touch() {
        lastInteractionAt = SystemClock.elapsedRealtime()
    }

    fun reset() = touch()

    fun shouldDim(now: Long = SystemClock.elapsedRealtime()): Boolean =
        now - lastInteractionAt >= DIM_AFTER_MILLIS
}
