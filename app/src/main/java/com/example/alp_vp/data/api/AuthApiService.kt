package com.example.alp_vp.data.api

import com.example.alp_vp.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<LoginResponse>>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<RegisterResponse>>
}

// Login Request/Response
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val token: String
)

// Register Request/Response
data class RegisterRequest(
    val name: String,
    val username: String,
    val phone: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val token: String
)