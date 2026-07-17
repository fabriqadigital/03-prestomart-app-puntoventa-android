package com.ecommerce.ecommerceposapp.data.security

import android.content.SharedPreferences
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class OfflineCredentialVerifier(private val prefs: SharedPreferences) {
    fun remember(email: String, password: CharArray) {
        val salt = ByteArray(16).also(SecureRandom()::nextBytes)
        val verifier = derive(password, salt)
        password.fill('\u0000')
        prefs.edit()
            .putString(KEY_EMAIL, email.trim().lowercase())
            .putString(KEY_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
            .putString(KEY_VERIFIER, Base64.encodeToString(verifier, Base64.NO_WRAP))
            .putLong(KEY_VERIFIED_AT, System.currentTimeMillis())
            .apply()
    }

    fun verify(email: String, password: CharArray): Boolean {
        if (!isAvailableFor(email)) return false
        val salt = Base64.decode(prefs.getString(KEY_SALT, ""), Base64.NO_WRAP)
        val expected = Base64.decode(prefs.getString(KEY_VERIFIER, ""), Base64.NO_WRAP)
        val actual = derive(password, salt)
        password.fill('\u0000')
        return MessageDigest.isEqual(expected, actual)
    }

    fun isAvailableFor(email: String): Boolean {
        val verifiedAt = prefs.getLong(KEY_VERIFIED_AT, 0L)
        return prefs.getString(KEY_EMAIL, "") == email.trim().lowercase() &&
            verifiedAt > 0L && System.currentTimeMillis() - verifiedAt <= OFFLINE_VALIDITY_MS
    }

    fun clear() {
        prefs.edit().remove(KEY_EMAIL).remove(KEY_SALT).remove(KEY_VERIFIER).remove(KEY_VERIFIED_AT).apply()
    }

    private fun derive(password: CharArray, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password, salt, ITERATIONS, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private companion object {
        const val ITERATIONS = 210_000
        const val OFFLINE_VALIDITY_MS = 7L * 24 * 60 * 60 * 1000
        const val KEY_EMAIL = "offline_auth_email"
        const val KEY_SALT = "offline_auth_salt"
        const val KEY_VERIFIER = "offline_auth_verifier"
        const val KEY_VERIFIED_AT = "offline_auth_verified_at"
    }
}
