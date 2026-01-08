package com.example.alp_vp.data.repository

import com.example.alp_vp.data.service.DailyActivityService
import com.example.alp_vp.data.dto.CreateDailyActivityRequest
import com.example.alp_vp.data.dto.DailyActivityResponse
import com.example.alp_vp.data.dto.UpdateDailyActivityRequest
import retrofit2.Response

class DailyActivityRepository(private val service: DailyActivityService) {

    suspend fun getAllDailyActivities(): Response<List<DailyActivityResponse>> {
        return service.getAllDailyActivities()
    }

    suspend fun getDailyActivityById(id: Int): Response<DailyActivityResponse> {
        return service.getDailyActivityById(id)
    }

    suspend fun getDailyActivitiesByUserId(userId: Int): Response<List<DailyActivityResponse>> {
        return service.getDailyActivitiesByUserId(userId)
    }

    suspend fun createDailyActivity(request: CreateDailyActivityRequest): Response<DailyActivityResponse> {
        return service.createDailyActivity(request)
    }

    suspend fun updateDailyActivity(id: Int, request: UpdateDailyActivityRequest): Response<DailyActivityResponse> {
        return service.updateDailyActivity(id, request)
    }

    suspend fun deleteDailyActivity(id: Int): Response<Unit> {
        return service.deleteDailyActivity(id)
    }
}
