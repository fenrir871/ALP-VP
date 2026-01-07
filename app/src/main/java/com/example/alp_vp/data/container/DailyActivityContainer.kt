package com.example.alp_vp.data.container

import com.example.alp_vp.data.Service.DailyActivityService
import com.example.alp_vp.data.repository.DailyActivityRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

class DailyActivityContainer {
    companion object {
        const val BASE_URL = "http://10.0.171.88/"
    }

    private val client = OkHttpClient.Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    private val retrofitService: DailyActivityService by lazy {
        retrofit.create(DailyActivityService::class.java)
    }

    val dailyActivityRepository: DailyActivityRepository by lazy {
        DailyActivityRepository(retrofitService)
    }
}
