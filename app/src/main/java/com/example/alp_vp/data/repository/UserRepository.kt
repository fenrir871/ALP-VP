package com.example.alp_vp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.alp_vp.data.api.RetrofitClient
import com.example.alp_vp.data.dto.ResponseUser

class UserRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Use the correct API service from RetrofitClient
    private val authApi = RetrofitClient.authApi
    private val userApi = RetrofitClient.userApi

    // Save user data after login
    fun saveUserData(user: ResponseUser) {
        sharedPreferences.edit().apply {
            putString("username", user.username)
            putString("user_id", user.id.toString())
            putString("email", user.email)
            putString("phone", user.phone)
            putString("createdAt", user.createdAt)
            apply()
        }
    }

    // Get current logged-in user from local storage
    suspend fun getCurrentUser(): ResponseUser? {
        val username = sharedPreferences.getString("username", null)
        val userId = sharedPreferences.getString("user_id", null)
        val email = sharedPreferences.getString("email", null)
        val phone = sharedPreferences.getString("phone", null)
        val createdAt = sharedPreferences.getString("createdAt", null)

        return if (username != null && userId != null) {
            ResponseUser(
                id = userId.toIntOrNull() ?: 0,
                username = username,
                email = email ?: "",
                phone = phone ?: "",
                createdAt = createdAt ?: ""
            )
        } else {
            null
        }
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("username")
    }

    // Logout user
    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    // Get username only (quick access)
    fun getUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    // Get user ID only (quick access)
    fun getUserId(): Int? {
        return sharedPreferences.getString("user_id", null)?.toIntOrNull()
    }

    // Fetch user from API
    suspend fun fetchUserFromApi(): ResponseUser? {
        return try {
            val response = userApi.getCurrentUser()
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                saveUserData(user) // Save to local storage
                user
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Sync user data (fetch from API and save locally)
    suspend fun syncUserData() {
        fetchUserFromApi()
    }
}