package com.mealmanager.backend.service

import com.mealmanager.backend.model.dto.MetricsSummaryDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class AnalyticsServiceTest {

    private lateinit var service: AnalyticsService

    @BeforeEach
    fun setUp() { service = AnalyticsService() }

    @Test
    fun `getSummary returns metrics`() {
        val summary = service.getSummary(LocalDate.now().minusDays(7), LocalDate.now())
        assertEquals(1000, summary.totalUsers)
        assertTrue(summary.revenue > 0)
    }

    @Test
    fun `listReports returns empty page`() {
        val page = service.listReports(0, 20)
        assertEquals(0, page.total)
    }

    @Test
    fun `generateReport creates report`() {
        val report = service.generateReport(com.mealmanager.backend.controller.GenerateReportRequest(reportType = "nutrition"))
        assertNotNull(report.id)
    }
}
