package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.model.sales.CurrencyFormatter
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyFormatterTest {
    @Test
    fun `formats usd and pen consistently`() {
        assertEquals("S/ 46.25", CurrencyFormatter.formatAmount(46.25, "PEN"))
        assertEquals("$ 12.50", CurrencyFormatter.formatAmount(12.5, "USD"))
    }

    @Test
    fun `converts using explicit exchange rate`() {
        assertEquals(12.5, CurrencyFormatter.convertToCurrency(46.25, "USD", 3.7), 0.0001)
        assertEquals(46.25, CurrencyFormatter.convertToCurrency(46.25, "PEN", 3.7), 0.0001)
    }

    @Test
    fun `converts received usd amount back to base currency`() {
        assertEquals(3.08, CurrencyFormatter.convertToBaseCurrency(0.79, "USD", 3.90), 0.0001)
        assertEquals(3.00, CurrencyFormatter.convertToBaseCurrency(3.00, "PEN", 3.90), 0.0001)
    }
}
