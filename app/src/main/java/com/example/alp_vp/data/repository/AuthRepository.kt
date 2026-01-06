package com.example.alp_vp.data.repository

import com.example.alp_vp.data.api.LoginRequest
import com.example.alp_vp.data.api.LoginResponse
import com.example.alp_vp.data.api.RegisterRequest
import com.example.alp_vp.data.api.RegisterResponse
import com.example.alp_vp.data.api.RetrofitClient
import com.example.alp_vp.data.local.TokenManager

class AuthRepository(private val tokenManager: TokenManager) {

    private val api = RetrofitClient.authApi

    // Login
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body()?.data != null) {
                val loginData = response.body()!!.data!!

                // Save token, user ID, and username
                tokenManager.saveToken(loginData.token)
                tokenManager.saveUserId(loginData.id)
                tokenManager.saveUsername(loginData.username)

                Result.success(loginData)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Register
    suspend fun register(
        name: String,
        username: String,
        phone: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return try {
            val response = api.register(
                RegisterRequest(name, username, phone, email, password)
            )

            if (response.isSuccessful && response.body()?.data != null) {
                val registerData = response.body()!!.data!!

                // Save token, user ID, and username
                tokenManager.saveToken(registerData.token)
                tokenManager.saveUserId(registerData.id)
                tokenManager.saveUsername(registerData.username)  // ADD THIS LINE

                Result.success(registerData)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}