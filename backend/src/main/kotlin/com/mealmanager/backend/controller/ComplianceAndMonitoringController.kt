package com.mealmanager.backend.controller

import com.mealmanager.backend.model.dto.AuditLogDto
import com.mealmanager.backend.model.dto.GdprRequestDto
import com.mealmanager.backend.model.dto.FeatureFlagDto
import com.mealmanager.backend.model.dto.AbTestDto
import com.mealmanager.backend.model.dto.ApiResponse
import com.mealmanager.backend.service.AuditLogService
import com.mealmanager.backend.service.GdprService
import com.mealmanager.backend.service.FeatureFlagService
import com.mealmanager.backend.service.AbTestingService
import com.mealmanager.backend.service.MonitoringService
import com.mealmanager.backend.service.DistributedTracingService
import com.mealmanager.backend.service.I18nService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping("/api/v1/audit")
class AuditController(
    private val auditService: AuditLogService
) {

    @GetMapping
    fun listAuditLogs(
        @RequestParam(required = false) action: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int
    ): ResponseEntity<ApiResponse<Page<AuditLogDto>>> {
        val result = auditService.listAuditLogs(action, page, size)
        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/export")
    fun exportAuditLogs(): ResponseEntity<ApiResponse<String>> {
        val url = auditService.exportAuditLogs()
        return ResponseEntity.ok(ApiResponse.success(url))
    }
}

@RestController
@RequestMapping("/api/v1/gdpr")
class GdprController(
    private val gdprService: GdprService
) {

    @PostMapping("/export")
    fun requestDataExport(@Valid @RequestBody request: GdprRequestDto): ResponseEntity<ApiResponse<DataExportDto>> {
        val export = gdprService.requestDataExport(request)
        return ResponseEntity.ok(ApiResponse.success(export))
    }

    @DeleteMapping("/delete")
    fun requestDataDeletion(@Valid @RequestBody request: GdprRequestDto): ResponseEntity<ApiResponse<DeletionConfirmationDto>> {
        val confirmation = gdprService.requestDataDeletion(request)
        return ResponseEntity.ok(ApiResponse.success(confirmation))
    }

    @PostMapping("/consent")
    fun manageConsent(@Valid @RequestBody request: ConsentRequest): ResponseEntity<ApiResponse<Void>> {
        gdprService.manageConsent(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}

@RestController
@RequestMapping("/api/v1/feature-flags")
class FeatureFlagController(
    private val featureFlagService: FeatureFlagService
) {

    @GetMapping
    fun listFeatureFlags(): ResponseEntity<ApiResponse<List<FeatureFlagDto>>> {
        val flags = featureFlagService.listFeatureFlags()
        return ResponseEntity.ok(ApiResponse.success(flags))
    }

    @PostMapping
    fun setFeatureFlag(@Valid @RequestBody flag: FeatureFlagDto): ResponseEntity<ApiResponse<FeatureFlagDto>> {
        val updated = featureFlagService.setFeatureFlag(flag)
        return ResponseEntity.ok(ApiResponse.success(updated))
    }

    @GetMapping("/{key}")
    fun getFeatureFlag(@PathVariable key: String): ResponseEntity<ApiResponse<Boolean>> {
        val enabled = featureFlagService.isEnabled(key)
        return ResponseEntity.ok(ApiResponse.success(enabled))
    }
}

@RestController
@RequestMapping("/api/v1/ab-testing")
class AbTestingController(
    private val abTestingService: AbTestingService
) {

    @GetMapping("/experiments")
    fun listExperiments(): ResponseEntity<ApiResponse<List<AbTestDto>>> {
        val experiments = abTestingService.listExperiments()
        return ResponseEntity.ok(ApiResponse.success(experiments))
    }

    @PostMapping("/assign")
    fun assignVariant(@Valid @RequestBody request: AbTestAssignmentRequest): ResponseEntity<ApiResponse<AbTestAssignmentDto>> {
        val assignment = abTestingService.assignVariant(request)
        return ResponseEntity.ok(ApiResponse.success(assignment))
    }

    @PostMapping("/results")
    fun recordResult(@Valid @RequestBody request: AbTestResultRequest): ResponseEntity<ApiResponse<Void>> {
        abTestingService.recordResult(request)
        return ResponseEntity.ok(ApiResponse.success(null))
    }
}

@RestController
@RequestMapping("/api/v1/monitoring")
class MonitoringController(
    private val monitoringService: MonitoringService,
    private val tracingService: DistributedTracingService,
    private val i18nService: I18nService
) {

    @GetMapping("/metrics")
    fun getMetrics(): ResponseEntity<ApiResponse<MetricsDto>> {
        val metrics = monitoringService.getMetrics()
        return ResponseEntity.ok(ApiResponse.success(metrics))
    }

    @GetMapping("/health")
    fun getHealth(): ResponseEntity<ApiResponse<HealthDto>> {
        val health = monitoringService.getHealth()
        return ResponseEntity.ok(ApiResponse.success(health))
    }

    @GetMapping("/traces/{traceId}")
    fun getTrace(@PathVariable traceId: String): ResponseEntity<ApiResponse<TraceDto>> {
        val trace = tracingService.getTrace(traceId)
        return ResponseEntity.ok(ApiResponse.success(trace))
    }

    @GetMapping("/translations/{locale}")
    fun getTranslations(@PathVariable locale: String): ResponseEntity<ApiResponse<Map<String, String>>> {
        val translations = i18nService.getTranslations(locale)
        return ResponseEntity.ok(ApiResponse.success(translations))
    }
}

data class DataExportDto(
    val exportId: UUID,
    val downloadUrl: String,
    val expiresAt: String
)

data class DeletionConfirmationDto(
    val deletionId: UUID,
    val status: String,
    val estimatedCompletion: String
)

data class ConsentRequest(
    val userId: UUID,
    val consentType: String,
    val granted: Boolean
)

data class AbTestAssignmentRequest(
    val experimentId: UUID,
    val userId: UUID
)

data class AbTestAssignmentDto(
    val experimentId: UUID,
    val userId: UUID,
    val variant: String
)

data class AbTestResultRequest(
    val experimentId: UUID,
    val userId: UUID,
    val variant: String,
    val converted: Boolean
)

data class MetricsDto(
    val requestCount: Long,
    val errorRate: Double,
    val avgResponseTimeMs: Double
)

data class HealthDto(
    val status: String,
    val database: String,
    val timestamp: String
)

data class TraceDto(
    val traceId: String,
    val spans: List<SpanDto>
)

data class SpanDto(
    val spanId: String,
    val operation: String,
    val durationMs: Double
)
