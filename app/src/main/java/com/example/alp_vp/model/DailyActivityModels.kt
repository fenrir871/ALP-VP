package com.example.alp_vp.model

data class DailyActivityModels(
    val id: Int,
    val date: String,
    val steps: Int,
    val sleepHours: Float,
    val calories: Int,
    val userId: Int
)
