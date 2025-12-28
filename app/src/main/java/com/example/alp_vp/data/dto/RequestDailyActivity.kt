package com.example.alp_vp.dto

data class CreateDailyActivityRequest(
    val date: String,
    val steps: Int,
    val sleep_hours: Int,
    val calories: Int,
    val user_id: Int
)

data class UpdateDailyActivityRequest(
    val date: String? = null,
    val steps: Int? = null,
    val sleep_hours: Int? = null,
    val calories: Int? = null
)
