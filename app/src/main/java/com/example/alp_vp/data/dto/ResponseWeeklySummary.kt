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
