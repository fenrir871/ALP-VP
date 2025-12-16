package com.example.alp_vp.data.dto

data class RequestWeeklyCreate(
    val userId: Int,
    val weekStartDate: String, // ISO 8601 format: "2024-01-01"
    val avgSteps: Double,
    val avgSleep: Double,
    val avgWater: Double,
    val avgCalories: Double
)

