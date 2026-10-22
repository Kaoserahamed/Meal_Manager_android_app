package com.mealmanager.backend.util

import java.security.MessageDigest

object ValidationUtil {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

object CryptoUtil {
    fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}

object DateUtil {
    fun isoNow(): String = java.time.Instant.now().toString()
    fun parseIso(value: String): java.time.Instant = java.time.Instant.parse(value)
}

object PaginationUtil {
    fun offset(page: Int, size: Int): Int = page * size
    fun limit(size: Int): Int = size.coerceIn(1, 100)
}
