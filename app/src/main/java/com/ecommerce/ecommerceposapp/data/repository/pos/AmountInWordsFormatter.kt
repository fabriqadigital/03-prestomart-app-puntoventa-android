package com.ecommerce.ecommerceposapp.data.repository.pos

import java.util.Locale
import kotlin.math.roundToInt

object AmountInWordsFormatter {
    fun soles(amount: Double): String {
        val entero = amount.toLong().coerceAtLeast(0L)
        val centavos = ((amount - entero) * 100).roundToInt().coerceIn(0, 99)
        val letras = integerToSpanishWords(entero).uppercase(Locale("es", "PE"))
        return "Son: $letras CON ${String.format(Locale.US, "%02d", centavos)}/100 SOLES"
    }

    private fun integerToSpanishWords(value: Long): String {
        if (value == 0L) return "cero"
        if (value < 0L) return "menos ${integerToSpanishWords(-value)}"
        if (value >= 1_000_000) return value.toString()
        val parts = mutableListOf<String>()
        var rest = value
        if (rest >= 1000) {
            val miles = rest / 1000
            rest %= 1000
            parts += if (miles == 1L) "mil" else "${wordsBelowThousand(miles)} mil"
        }
        if (rest > 0L) parts += wordsBelowThousand(rest)
        return parts.joinToString(" ").trim()
    }

    private fun wordsBelowThousand(value: Long): String {
        require(value in 0..999)
        if (value == 0L) return ""
        if (value < 16) return shortUnits(value.toInt())
        if (value < 20) return when (value.toInt()) {
            16 -> "dieciséis"
            17 -> "diecisiete"
            18 -> "dieciocho"
            19 -> "diecinueve"
            else -> ""
        }
        if (value < 100) {
            val unit = (value % 10).toInt()
            val tens = (value / 10).toInt()
            if (tens == 2 && unit > 0) return "veinti${shortUnits(unit)}"
            val tensWord = decenas.getValue(tens)
            return if (unit == 0) tensWord else "$tensWord y ${shortUnits(unit)}"
        }
        val hundreds = (value / 100).toInt()
        val rest = value % 100
        val hundredWord = when {
            hundreds == 1 && rest == 0L -> "cien"
            hundreds == 1 -> "ciento"
            else -> centenas.getValue(hundreds)
        }
        if (rest == 0L) return hundredWord
        return "$hundredWord ${wordsBelowThousand(rest)}".trim()
    }

    private fun shortUnits(value: Int): String = when (value) {
        0 -> "cero"
        1 -> "uno"
        2 -> "dos"
        3 -> "tres"
        4 -> "cuatro"
        5 -> "cinco"
        6 -> "seis"
        7 -> "siete"
        8 -> "ocho"
        9 -> "nueve"
        10 -> "diez"
        11 -> "once"
        12 -> "doce"
        13 -> "trece"
        14 -> "catorce"
        15 -> "quince"
        else -> ""
    }

    private val decenas = mapOf(
        2 to "veinte",
        3 to "treinta",
        4 to "cuarenta",
        5 to "cincuenta",
        6 to "sesenta",
        7 to "setenta",
        8 to "ochenta",
        9 to "noventa",
    )

    private val centenas = mapOf(
        1 to "ciento",
        2 to "doscientos",
        3 to "trescientos",
        4 to "cuatrocientos",
        5 to "quinientos",
        6 to "seiscientos",
        7 to "setecientos",
        8 to "ochocientos",
        9 to "novecientos",
    )
}
