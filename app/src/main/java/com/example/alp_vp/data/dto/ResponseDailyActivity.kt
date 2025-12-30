package com.example.alp_vp.dto

import com.google.gson.annotations.SerializedName

data class DailyActivityResponse(
    val id: Int,
    val date: String,
    val steps: Int,
   val sleepHours: Int,
    val calories: Int,
     val userId: Int
)
