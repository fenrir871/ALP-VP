package com.example.alp_vp.data.dto

data class CreateDailyActivityRequest(
    val date: String,
    val steps: Int,
    val sleepHours: Int,
    val calories: Int,
    val userId: Int,
    val waterGlasses: Int
)

data class UpdateDailyActivityRequest(
    val date: String? = null,
    val steps: Int? = null,
    val  waterGlasses: Int?,
    val sleepHours: Int? = null,
    val calories: Int? = null
)
