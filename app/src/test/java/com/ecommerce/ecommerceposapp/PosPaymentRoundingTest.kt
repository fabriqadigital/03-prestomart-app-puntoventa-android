package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.model.sales.PosPaymentRounding
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PosPaymentRoundingTest {
    @Test
    fun cashRoundsFinalTotalUpToHalfSolSteps() {
        assertEquals(3.00, PosPaymentRounding.finalTotal(2.90, "EFE", true), 0.001)
        assertEquals(1.50, PosPaymentRounding.finalTotal(1.40, "efectivo", true), 0.001)
        assertEquals(3.00, PosPaymentRounding.finalTotal(2.60, "cash", true), 0.001)
        assertEquals(1.00, PosPaymentRounding.finalTotal(0.90, "EFE", true), 0.001)
        assertEquals(0.50, PosPaymentRounding.finalTotal(0.40, "EFE", true), 0.001)
        assertEquals(0.50, PosPaymentRounding.finalTotal(0.10, "EFE", true), 0.001)
        assertEquals(2.50, PosPaymentRounding.finalTotal(2.49, "EFE", true), 0.001)
        assertEquals(3.00, PosPaymentRounding.finalTotal(2.51, "EFE", true), 0.001)
        assertEquals(2.00, PosPaymentRounding.finalTotal(2.00, "EFE", true), 0.001)
        assertEquals(2.50, PosPaymentRounding.finalTotal(2.50, "EFE", true), 0.001)
    }

    @Test
    fun electronicMethodsKeepExactAmount() {
        listOf("TAR", "YAP", "PLN", "TRANSFERENCIA").forEach { method ->
            assertEquals(2.90, PosPaymentRounding.finalTotal(2.90, method, true), 0.001)
            assertEquals(1.40, PosPaymentRounding.finalTotal(1.40, method, true), 0.001)
        }
    }

    @Test
    fun cashKeepsExactAmountWhenRoundingIsNotSelected() {
        assertEquals(0.10, PosPaymentRounding.finalTotal(0.10, "EFE", false), 0.001)
        assertEquals(2.90, PosPaymentRounding.finalTotal(2.90, "EFE", false), 0.001)
    }

    @Test
    fun identifiesOnlyCashAliases() {
        assertTrue(PosPaymentRounding.isCash(" EFE "))
        assertTrue(PosPaymentRounding.isCash("Efectivo"))
        assertFalse(PosPaymentRounding.isCash("YAP"))
    }
}
