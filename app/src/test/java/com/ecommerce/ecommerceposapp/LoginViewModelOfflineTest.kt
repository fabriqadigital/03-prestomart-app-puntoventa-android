package com.ecommerce.ecommerceposapp

import com.ecommerce.ecommerceposapp.domain.model.auth.UserSession
import com.ecommerce.ecommerceposapp.domain.repository.auth.AuthRepository
import com.ecommerce.ecommerceposapp.presentation.auth.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginViewModelOfflineTest {
    @Test
    fun `remembered online user can enable offline mode after logout`() {
        val email = "cajero@prestomart.pe"
        val viewModel = LoginViewModel(FakeAuthRepository(email))

        assertEquals(email, viewModel.uiState.value.email)
        assertTrue(viewModel.uiState.value.offlineAvailable)

        viewModel.setOfflineMode(true)

        assertTrue(viewModel.uiState.value.offlineMode)
    }

    private class FakeAuthRepository(
        private val rememberedEmail: String,
    ) : AuthRepository {
        override fun login(email: String, password: String) =
            Result.failure<UserSession>(UnsupportedOperationException())

        override fun loginOffline(email: String, password: String) =
            Result.failure<UserSession>(UnsupportedOperationException())

        override fun canLoginOffline(email: String): Boolean =
            email.equals(rememberedEmail, ignoreCase = true)

        override fun offlineLoginEmail(): String = rememberedEmail

        override fun getSession(): UserSession? = null

        override fun hasStoredToken(): Boolean = false

        override fun logout() = Unit
    }
}
