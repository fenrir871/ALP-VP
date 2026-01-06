// Kotlin
package com.example.alp_vp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.alp_vp.data.service.LoginRequest
import com.example.alp_vp.data.api.RetrofitInstance
import com.example.alp_vp.ui.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UserRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val userApi = RetrofitInstance.userApi

    companion object {
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Validate and register user
    suspend fun registerUser(user: UserModel): Result<String> = withContext(Dispatchers.IO) {
        // Local validation: prevent empty payloads
        if (user.fullName.isNullOrBlank() ||
            user.username.isNullOrBlank() ||
            user.email.isNullOrBlank() ||
            user.password.isNullOrBlank()
        ) {
            return@withContext Result.failure(Exception("Please fill in all required fields."))
        }

        try {
            val response = userApi.register(user)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    // Save the registered user data locally
                    saveUserLocally(body.data)
                    Result.success("Registration successful.")
                } else {
                    Result.failure(Exception(body?.message ?: "Registration failed."))
                }
            } else {
                val msg = parseError(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Registration failed."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or server error: ${e.message}"))
        }
    }

    // Validate and login
    suspend fun loginUser(email: String, password: String): Result<UserModel> = withContext(Dispatchers.IO) {
        if (email.isBlank() || password.isBlank()) {
            return@withContext Result.failure(Exception("Email and password are required."))
        }

        try {
            val response = userApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true && body.data != null) {
                    saveUserLocally(body.data)
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Invalid credentials."))
                }
            } else {
                val msg = parseError(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Login failed."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or server error: ${e.message}"))
        }
    }

    // Fetch by email with clear errors
    suspend fun fetchUserByEmail(email: String): Result<UserModel> = withContext(Dispatchers.IO) {
        if (email.isBlank()) {
            return@withContext Result.failure(Exception("Email is required."))
        }

        try {
            val response = userApi.getUserByEmail(email)
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("User not found."))
                }
            } else {
                val msg = parseError(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Failed to fetch user."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or server error: ${e.message}"))
        }
    }

    private fun saveUserLocally(user: UserModel) {
        prefs.edit().apply {
            putString(KEY_FULL_NAME, user.fullName)
            putString(KEY_USERNAME, user.username)
            putString(KEY_EMAIL, user.email)
            putString(KEY_PHONE, user.phone)
            putString(KEY_PASSWORD, user.password) // avoid storing raw passwords in production
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getCurrentUser(): UserModel? {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        if (!isLoggedIn) return null
        return getUserData()
    }

    private fun getUserData(): UserModel? {
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        if (email.isEmpty()) return null
        return UserModel(
            fullName = prefs.getString(KEY_FULL_NAME, "") ?: "",
            username = prefs.getString(KEY_USERNAME, "") ?: "",
            email = email,
            phone = prefs.getString(KEY_PHONE, "") ?: "",
            password = prefs.getString(KEY_PASSWORD, "") ?: ""
        )
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    private fun parseError(errorBody: String?): String? {
        return try {
            errorBody?.let { JSONObject(it).optString("message", null) }
        } catch (_: Exception) {
            null
        }
    }
}
