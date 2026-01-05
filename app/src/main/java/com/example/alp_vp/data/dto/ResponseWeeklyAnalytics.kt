package com.example.alp_vp.data.dto

data class ResponseWeeklyAnalytics(
    val weekStartDate: String,
    val metrics: WeeklyMetricsDto,
    val overallScore: Int,
    val grade: String,
    val improvementSuggestions: List<String>
)

data class WeeklyMetricsDto(
    val steps: MetricAnalysisDto,
    val sleep: MetricAnalysisDto,
    val water: MetricAnalysisDto,
    val calories: MetricAnalysisDto
)

data class MetricAnalysisDto(
    val average: Double,
    val score: Int,
    val status: String, // "excellent", "good", "fair", "needs_improvement"
    val target: Double,
    val percentage: Int
)

data class TodayActivityResponse(
    val id: Int,
    val date: String,
    val steps: Int,
    val sleep_hours: Int,
    val calories: Int,
    val user_id: Int
)