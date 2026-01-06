package com.example.alp_vp.data.repository

import android.content.Context
import com.example.alp_vp.data.api.RetrofitInstance
import com.example.alp_vp.data.dto.ResponseWeeklySummary
import com.example.alp_vp.data.service.WeeklyActivityAPI
import retrofit2.Response

class WeeklyActivityRepository(context: Context) {
    private val api: WeeklyActivityAPI = RetrofitInstance.weeklyApi

    suspend fun getWeeklySummaryByUserId(userId: Int): Response<ResponseWeeklySummary> {
        return api.getWeeklySummaryByUserId(userId)
    }

    companion object {
        @Volatile
        private var INSTANCE: WeeklyActivityRepository? = null

        fun getInstance(context: Context): WeeklyActivityRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WeeklyActivityRepository(context).also { INSTANCE = it }
            }
        }
    }
}
