package com.example.alp_vp.data.api

import com.example.alp_vp.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Change this to your backend URL
    private const val BASE_URL = "http://10.0.2.2:3000/api/" // Android emulator localhost
    // For physical device, use: "http://YOUR_IP_ADDRESS:3000/"

    private var tokenManager: TokenManager? = null

    fun initialize(tokenManager: TokenManager) {
        this.tokenManager = tokenManager
    }

    // Auth Interceptor to add JWT token to requests
    private val authInterceptor = Interceptor { chain ->
        val token = runBlocking {
            tokenManager?.getToken()?.first()
        }

        val request = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            request.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(request.build())
    }

    // Logging Interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with interceptors
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Friend API Service
    val friendApi: FriendApiService by lazy {
        retrofit.create(FriendApiService::class.java)
    }

    // Add more API services here as needed
    // val userApi: UserApiService by lazy { ... }
    // val activityApi: ActivityApiService by lazy { ... }
}