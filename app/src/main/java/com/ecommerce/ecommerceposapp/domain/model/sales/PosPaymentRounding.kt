package com.ecommerce.ecommerceposapp.domain.model.sales

import java.math.BigDecimal
import java.math.RoundingMode

/** Reglas monetarias exclusivas de las ventas realizadas desde el POS. */
object PosPaymentRounding {
    private val cashStep = BigDecimal("0.50")

    fun isCash(method: String): Boolean = method.trim().lowercase() in setOf(
        "efe",
        "efectivo",
        "cash",
    )

    fun exactTotal(amount: Double): Double = amount.toMoney().toDouble()

    fun finalTotal(amount: Double, method: String, applyCashRounding: Boolean = false): Double {
        val exact = amount.toMoney()
        if (!isCash(method) || !applyCashRounding) return exact.toDouble()

        return exact.divide(cashStep, 0, RoundingMode.CEILING)
            .multiply(cashStep)
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }

    fun normalizedPayment(payment: SalePaymentInfo, exactAmount: Double): SalePaymentInfo {
        val total = finalTotal(exactAmount, payment.tipoPago, payment.aplicarRedondeo)
        if (!isCash(payment.tipoPago)) {
            return payment.copy(montoRecibido = total, vuelto = 0.0)
        }
        val received = exactTotal(payment.montoRecibido)
        val change = exactTotal((received - total).coerceAtLeast(0.0))
        return payment.copy(montoRecibido = received, vuelto = change)
    }

    private fun Double.toMoney(): BigDecimal = BigDecimal.valueOf(this)
        .setScale(2, RoundingMode.HALF_UP)
}
