package com.example.alp_vp.data.dto

data class ResponseWeeklySummary(
    val weeklyId: Int,
    val userId: Int,
    val weekStartDate: String,
    val avgSteps: Double,
    val avgSleep: Double,
    val avgWater: Double,
    val avgCalories: Double,
    val scoreSteps: Int,
    val scoreSleep: Int,
    val scoreWater: Int,
    val scoreCalories: Int,
    val overallScore: Int
)

data class CreateTodayActivityRequest(
    val date: String,
    val steps: Int,
    val sleep_hours: Int,
    val calories: Int,
    val user_id: Int
)

data class UpdateTodayActivityRequest(
    val date: String? = null,
    val steps: Int? = null,
    val sleep_hours: Int? = null,
    val calories: Int? = null
)