package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.CreateDailyActivityRequest
import com.example.alp_vp.data.dto.DailyActivityResponse
import com.example.alp_vp.data.dto.UpdateDailyActivityRequest
import retrofit2.Response
import retrofit2.http.*

interface DailyActivityService{

    @GET("daily-activities")
    suspend fun getAllDailyActivities(): Response<List<DailyActivityResponse>>

    @GET("daily-activities/{id}")
    suspend fun getDailyActivityById(@Path("id") id: Int): Response<DailyActivityResponse>

    @GET("daily-activities/user/{userId}")
    suspend fun getDailyActivitiesByUserId(@Path("userId") userId: Int): Response<List<DailyActivityResponse>>

    @POST("daily-activities")
    suspend fun createDailyActivity(@Body request: CreateDailyActivityRequest): Response<DailyActivityResponse>

    @PUT("daily-activities/{id}")
    suspend fun updateDailyActivity(
        @Path("id") id: Int,
        @Body request: UpdateDailyActivityRequest
    ): Response<DailyActivityResponse>

    @DELETE("daily-activities/{id}")
    suspend fun deleteDailyActivity(@Path("id") id: Int): Response<Unit>
}
