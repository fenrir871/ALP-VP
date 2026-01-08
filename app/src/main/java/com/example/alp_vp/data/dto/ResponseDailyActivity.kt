package com.example.alp_vp.data.dto

data class DailyActivityResponse(
    val id: Int,
    val date: String,
    val steps: Int,
    val sleepHours: Int,
    val calories: Int,
    val userId: Int
)
