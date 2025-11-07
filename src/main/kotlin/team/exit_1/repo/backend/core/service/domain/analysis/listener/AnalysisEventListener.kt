package team.exit_1.repo.backend.core.service.domain.analysis.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import team.exit_1.repo.backend.core.service.domain.analysis.data.entity.Analysis
import team.exit_1.repo.backend.core.service.domain.analysis.data.repository.AnalysisJpaRepository
import team.exit_1.repo.backend.core.service.domain.analysis.event.AnalysisGeneratedEvent
import team.exit_1.repo.backend.core.service.global.config.logger
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
open class AnalysisEventListener(
    private val analysisJpaRepository: AnalysisJpaRepository,
    private val objectMapper: ObjectMapper,
) {
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    open fun handleAnalysisGeneratedEvent(event: AnalysisGeneratedEvent) {
        try {
            val analysis =
                Analysis().apply {
                    this.userId = event.analysisResult.userId
                    this.domains = objectMapper.writeValueAsString(event.analysisResult.domains)
                    this.report = event.analysisResult.report
                    this.analyzedAt =
                        try {
                            ZonedDateTime.parse(event.analysisResult.analyzedAt).toLocalDateTime()
                        } catch (e: Exception) {
                            LocalDateTime.parse(event.analysisResult.analyzedAt, DateTimeFormatter.ISO_DATE_TIME)
                        }
                    this.createdAt = LocalDateTime.now()
                }

            val savedAnalysis = analysisJpaRepository.save(analysis)
            logger().info("분석 결과 저장 완료 - analysisId: ${savedAnalysis.id}, userId: ${event.userId}")
        } catch (e: Exception) {
            logger().error("분석 결과 저장 중 오류 발생 - userId: ${event.userId}", e)
        }
    }
}
