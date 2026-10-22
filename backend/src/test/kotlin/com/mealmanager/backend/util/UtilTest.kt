package com.mealmanager.backend.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CryptoUtilTest {

    @Test
    fun `sha256 produces deterministic hash`() {
        val h1 = CryptoUtil.sha256("hello")
        val h2 = CryptoUtil.sha256("hello")
        assertEquals(h1, h2)
        assertNotEquals(h1, CryptoUtil.sha256("world"))
    }

    @Test
    fun `sha256 is not empty`() {
        assertTrue(CryptoUtil.sha256("test").isNotEmpty())
    }
}

class DateUtilTest {

    @Test
    fun `isoNow returns non-empty string`() {
        assertTrue(DateUtil.isoNow().isNotEmpty())
    }
}

class PaginationUtilTest {

    @Test
    fun `offset computes correctly`() {
        assertEquals(0, PaginationUtil.offset(0, 20))
        assertEquals(20, PaginationUtil.offset(1, 20))
    }

    @Test
    fun `limit coerces to range`() {
        assertEquals(1, PaginationUtil.limit(0))
        assertEquals(100, PaginationUtil.limit(1000))
        assertEquals(50, PaginationUtil.limit(50))
    }
}
