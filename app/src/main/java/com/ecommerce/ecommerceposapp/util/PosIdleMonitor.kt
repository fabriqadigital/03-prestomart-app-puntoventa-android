package com.ecommerce.ecommerceposapp.util

import android.os.SystemClock

object PosIdleMonitor {
    /** Brillo de la ventana en estado activo (interacción del usuario). */
    const val NORMAL_BRIGHTNESS = 1.0f

    /** Brillo de la ventana al atenuar (0f = apagado, 1f = máximo). */
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
