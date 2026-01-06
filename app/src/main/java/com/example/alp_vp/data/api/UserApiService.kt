package com.example.alp_vp.data.api

import com.example.alp_vp.data.dto.ResponseUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {

    @GET("users/me") // Get current authenticated user
    suspend fun getCurrentUser(): Response<ResponseUser>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<ResponseUser>

    // Add more user-related endpoints as needed
}