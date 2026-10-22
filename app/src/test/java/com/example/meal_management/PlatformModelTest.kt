package com.example.meal_management

import com.example.meal_management.data.model.*
import org.junit.Assert.*
import org.junit.Test

class PlatformModelTest {

    @Test
    fun `payment transaction tracks status`() {
        val tx = PaymentTransaction(userId = "user", amount = 99.99, currency = "USD", status = "pending")
        assertEquals(99.99, tx.amount, 0.001)
        assertEquals("pending", tx.status)
    }

    @Test
    fun `delivery order tracks address and status`() {
        val order = DeliveryOrder(userId = "user", address = "456 Oak Ave", status = "assigned")
        assertEquals("456 Oak Ave", order.address)
        assertEquals("assigned", order.status)
    }

    @Test
    fun `calendar event has dates`() {
        val event = CalendarEvent(userId = "user", title = "Dinner", startDate = System.currentTimeMillis())
        assertEquals("Dinner", event.title)
    }

    @Test
    fun `notification tracks read state`() {
        val n = NotificationItem(userId = "user", title = "Alert", body = "New message", isRead = false)
        assertFalse(n.isRead)
    }

    @Test
    fun `notification preference defaults`() {
        val pref = NotificationPreference(userId = "user")
        assertTrue(pref.emailEnabled)
        assertTrue(pref.pushEnabled)
    }

    @Test
    fun `loyalty account has tier`() {
        val acct = LoyaltyAccount(userId = "user", points = 1200, tier = "gold")
        assertEquals(1200, acct.points)
        assertEquals("gold", acct.tier)
    }

    @Test
    fun `subscription has plan`() {
        val sub = Subscription(userId = "user", plan = "premium", status = "active")
        assertEquals("premium", sub.plan)
        assertEquals("active", sub.status)
    }

    @Test
    fun `organization has plan`() {
        val org = Organization(name = "Acme Corp", plan = "enterprise")
        assertEquals("enterprise", org.plan)
    }

    @Test
    fun `membership has role`() {
        val mem = Membership(organizationId = "org", userId = "user", role = "admin")
        assertEquals("admin", mem.role)
    }

    @Test
    fun `webhook endpoint has url`() {
        val wh = WebhookEndpoint(url = "https://api.example.com/hooks")
        assertTrue(wh.url.startsWith("https"))
    }

    @Test
    fun `search history records query`() {
        val sh = SearchHistory(userId = "user", query = "pasta recipe")
        assertEquals("pasta recipe", sh.query)
    }
}
