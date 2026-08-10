package com.ecommerce.ecommerceposapp.util

import android.os.SystemClock

object PosIdleMonitor {
    const val DIM_AFTER_MILLIS = 60_000L

    /** Brillo de la ventana en estado activo (interacción del usuario). */
    const val NORMAL_BRIGHTNESS = 1.0f

    /** Brillo de la ventana al atenuar (0f = apagado, 1f = máximo). */
    const val DIMMED_BRIGHTNESS = 0.3f
    const val POLL_INTERVAL_MILLIS = 1_000L

    @Volatile
    private var lastInteractionAt: Long = SystemClock.elapsedRealtime()
    fun touch() {
        lastInteractionAt = SystemClock.elapsedRealtime()
    }

    fun idleMillis(): Long = SystemClock.elapsedRealtime() - lastInteractionAt
    fun reset() {
        lastInteractionAt = SystemClock.elapsedRealtime()
    }
    fun shouldDim(): Boolean = idleMillis() >= DIM_AFTER_MILLIS
}
