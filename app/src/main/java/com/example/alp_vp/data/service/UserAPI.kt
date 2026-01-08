package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.ResponseUserSearch
import com.example.alp_vp.ui.model.UserModel
import retrofit2.Response
import retrofit2.http.*

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val success: Boolean, val data: UserModel?, val message: String? = null)
data class RegisterResponse(val success: Boolean, val data: UserModel?, val message: String? = null)

interface UserAPI {
    @POST("api/register")
    suspend fun register(@Body user: UserModel): Response<RegisterResponse>

    @POST("api/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<UserModel>

    @GET("api/users/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<UserModel>

    @GET("api/users/search")
    suspend fun searchUsers(@Query("query") query: String): Response<ResponseUserSearch>
}