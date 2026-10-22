package com.mealmanager.backend.controller

import com.mealmanager.backend.service.AnalyticsService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AnalyticsControllerTest {

    private lateinit var controller: AnalyticsController

    @BeforeEach
    fun setUp() { controller = AnalyticsController(AnalyticsService()) }

    @Test
    fun `getSummary returns 200`() {
        val response = controller.getSummary(LocalDate.now().minusDays(7), LocalDate.now())
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `listReports returns 200`() {
        val response = controller.listReports(0, 20)
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `generateReport returns 200`() {
        val response = controller.generateReport(com.mealmanager.backend.controller.GenerateReportRequest(reportType = "sales"))
        assertEquals(200, response.statusCode.value())
    }
}
