package com.example.alp_vp.dto

data class TodayActivityResponse(
    val id: Int,
    val date: String,
    val steps: Int,
    val sleep_hours: Int,
    val calories: Int,
    val user_id: Int
)
