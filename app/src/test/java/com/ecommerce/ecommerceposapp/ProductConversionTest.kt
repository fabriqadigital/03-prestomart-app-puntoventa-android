package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ProductConversionTest {
    @Test
    fun conversionLinesKeepIndependentIdentityPriceAndStockFactor() {
        val cold = CartLine(10L, "Gaseosa", 10.0, 2, 4L, "HELADA", 1.5)
        val offer = CartLine(10L, "Gaseosa", 8.0, 1, 5L, "OFERTA", 2.0)

        assertNotEquals(cold.lineKey, offer.lineKey)
        assertEquals(20.0, cold.lineTotal, 0.001)
        assertEquals(3.0, cold.quantity * cold.stockFactor, 0.001)
    }
}
