package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.presentation.navigation.isAuthenticationError
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CashAuthenticationErrorTest {
    @Test
    fun `backend unauthenticated message requests login`() {
        assertTrue("No autenticado. Inicie sesión nuevamente.".isAuthenticationError())
    }

    @Test
    fun `expired token requests login`() {
        assertTrue("El token ha expirado".isAuthenticationError())
    }

    @Test
    fun `network failure does not discard session`() {
        assertFalse("Unable to resolve host prestomartperu.com".isAuthenticationError())
    }
}
