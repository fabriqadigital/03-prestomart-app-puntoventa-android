package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.model.sales.CartLine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ProductConversionTest {
    @Test
    fun receiptDisplayNameIncludesConversionWithoutDuplicatingIt() {
        val converted = CartLine(
            productId = 1265,
            productName = "Gaseosa Inka",
            unitPrice = 15.0,
            quantity = 1,
            conversionId = 1,
            conversionName = "HELADA",
            stockFactor = 2.0,
        )
        assertEquals("Gaseosa Inka HELADA", converted.displayName)
        assertEquals("Gaseosa Inka HELADA", converted.copy(productName = "Gaseosa Inka HELADA").displayName)
        assertEquals("Gaseosa Inka", converted.copy(conversionId = null, conversionName = "").displayName)
    }

    @Test
    fun conversionLinesKeepIndependentIdentityPriceAndAvailableStock() {
        val cold = CartLine(10L, "Gaseosa", 10.0, 2, 4L, "HELADA", 3.0)
        val offer = CartLine(10L, "Gaseosa", 8.0, 1, 5L, "OFERTA", 2.0)

        assertNotEquals(cold.lineKey, offer.lineKey)
        assertEquals(20.0, cold.lineTotal, 0.001)
        assertEquals(1.0, cold.stockFactor - cold.quantity, 0.001)
    }
}
