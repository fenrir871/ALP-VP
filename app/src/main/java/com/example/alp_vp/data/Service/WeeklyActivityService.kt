package com.example.alp_vp.data.Service

import com.example.alp_vp.data.dto.ResponseWeeklySummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeeklyActivityService {
    @GET("weekly-summary/{userId}")
    suspend fun getWeeklySummaryByUserId(@Path("userId") userId: Int): Response<ResponseWeeklySummary>
}