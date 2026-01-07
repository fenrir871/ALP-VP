package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.RequestLogin
import com.example.alp_vp.data.dto.RequestRegister
import com.example.alp_vp.data.dto.ResponseToken
import com.example.alp_vp.data.dto.ResponseUser
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @POST("auth/register")
    suspend fun register(@Body request: RequestRegister): Response<ResponseToken>

    @POST("auth/login")
    suspend fun login(@Body credentials: RequestLogin): Response<ResponseToken>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<ResponseUser>

    @GET("api/users/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<ResponseUser>
}