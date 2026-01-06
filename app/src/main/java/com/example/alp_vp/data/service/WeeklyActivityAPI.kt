package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.ResponseWeeklySummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeeklyActivityAPI {
    @GET("api/weekly/{userId}")
    suspend fun getWeeklySummaryByUserId(@Path("userId") userId: Int): Response<ResponseWeeklySummary>
}