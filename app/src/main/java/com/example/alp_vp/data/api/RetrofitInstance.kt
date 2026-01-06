
package com.example.alp_vp.data.api

import com.example.alp_vp.data.service.UserAPI
import com.example.alp_vp.data.service.WeeklyActivityAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // FOR EMULATOR: Use 10.0.2.2 (this is the alias to your computer's localhost)
    // FOR PHYSICAL DEVICE: Use your computer's WiFi IP address (10.0.163.28)
    // Make sure your phone and computer are on the same WiFi network
    private const val BASE_URL = "http://10.73.208.129:3000/" // For physical device - MUST include port :3000

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
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

}