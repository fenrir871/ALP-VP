package com.example.alp_vp.data.api

import android.content.Context
import com.example.alp_vp.data.local.TokenManager
import com.example.alp_vp.data.service.FriendAPI
import com.example.alp_vp.data.service.UserAPI
import com.example.alp_vp.data.service.WeeklyActivityAPI
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://10.73.208.129:3000/"

    private lateinit var tokenManager: TokenManager

    fun initialize(context: Context) {
        tokenManager = TokenManager(context)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Auth interceptor to add token to requests
    private val authInterceptor = Interceptor { chain ->
        val token = if (::tokenManager.isInitialized) {
            runBlocking {
                tokenManager.getToken().firstOrNull()
            }
        } else null

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi: UserAPI = retrofit.create(UserAPI::class.java)
    val weeklyApi: WeeklyActivityAPI = retrofit.create(WeeklyActivityAPI::class.java)
    val friendApi: FriendAPI = retrofit.create(FriendAPI::class.java)
}