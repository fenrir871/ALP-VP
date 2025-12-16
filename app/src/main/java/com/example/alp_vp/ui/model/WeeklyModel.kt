package com.example.alp_vp.ui.model

import java.util.Date

data class WeeklyModel(
    val weeklyId: Int,
    val userId: Int,
    val weekStartDate: Date,
    val avgSteps: Double,
    val avgSleep: Double,
    val avgWater: Double,
    val avgCalories: Double,
    val scoreSteps: Int,
    val scoreSleep: Int,
    val scoreWater: Int,
    val scoreCalories: Int,
    val overallScore: Int? = null
)