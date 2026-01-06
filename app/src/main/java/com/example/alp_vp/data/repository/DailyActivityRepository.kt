package com.example.alp_vp.data.repository

import com.example.alp_vp.data.service.DailyActivityService
import com.example.alp_vp.data.dto.CreateTodayActivityRequest
import com.example.alp_vp.data.dto.TodayActivityResponse
import com.example.alp_vp.data.dto.UpdateTodayActivityRequest
import retrofit2.Response

class DailyActivityRepository(private val service: DailyActivityService) {

    suspend fun getAllTodayActivities(): Response<List<TodayActivityResponse>> {
        return service.getAllTodayActivities()
    }

    suspend fun getTodayActivityById(id: Int): Response<TodayActivityResponse> {
        return service.getTodayActivityById(id)
    }

    suspend fun getTodayActivitiesByUserId(userId: Int): Response<List<TodayActivityResponse>> {
        return service.getTodayActivitiesByUserId(userId)
    }

    suspend fun createTodayActivity(request: CreateTodayActivityRequest): Response<TodayActivityResponse> {
        return service.createTodayActivity(request)
    }

    suspend fun updateTodayActivity(id: Int, request: UpdateTodayActivityRequest): Response<TodayActivityResponse> {
        return service.updateTodayActivity(id, request)
    }

    suspend fun deleteTodayActivity(id: Int): Response<Unit> {
        return service.deleteTodayActivity(id)
    }
}
