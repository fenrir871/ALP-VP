package com.example.alp_vp.ui.model

data class DailyActivityModels(
    val id: Int,
    val date: String,
    val steps: Int,
    val sleepHours: Float,
    val calories: Int,
    val waterGlasses: Int,
    val userId: Int
)
