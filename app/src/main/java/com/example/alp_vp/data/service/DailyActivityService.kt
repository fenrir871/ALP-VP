package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.CreateTodayActivityRequest
import com.example.alp_vp.data.dto.TodayActivityResponse
import com.example.alp_vp.data.dto.UpdateTodayActivityRequest
import retrofit2.Response
import retrofit2.http.*

interface DailyActivityService{

    @GET("today-activities")
    suspend fun getAllTodayActivities(): Response<List<TodayActivityResponse>>

    @GET("today-activities/{id}")
    suspend fun getTodayActivityById(@Path("id") id: Int): Response<TodayActivityResponse>

    @GET("today-activities/user/{userId}")
    suspend fun getTodayActivitiesByUserId(@Path("userId") userId: Int): Response<List<TodayActivityResponse>>

    @POST("today-activities")
    suspend fun createTodayActivity(@Body request: CreateTodayActivityRequest): Response<TodayActivityResponse>

    @PUT("today-activities/{id}")
    suspend fun updateTodayActivity(
        @Path("id") id: Int,
        @Body request: UpdateTodayActivityRequest
    ): Response<TodayActivityResponse>

    @DELETE("today-activities/{id}")
    suspend fun deleteTodayActivity(@Path("id") id: Int): Response<Unit>
}
