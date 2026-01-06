package com.example.alp_vp.data.repository

import com.example.alp_vp.data.Service.WeeklyActivityService
import com.example.alp_vp.data.dto.ResponseWeeklySummary
import retrofit2.Response

class WeeklyActivityRepository(private val service: WeeklyActivityService) {

    suspend fun getWeeklySummaryByUserId(userId: Int): Response<ResponseWeeklySummary> {
        return service.getWeeklySummaryByUserId(userId)
    }

    // Add more methods as needed, e.g., create/update weekly summary, etc.
}
