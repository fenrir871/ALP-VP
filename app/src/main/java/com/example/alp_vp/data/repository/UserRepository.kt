package com.example.alp_vp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.alp_vp.data.api.RetrofitInstance
import com.example.alp_vp.data.dto.RequestLogin
import com.example.alp_vp.data.dto.RequestRegister
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
        private const val KEY_TOKEN = "token"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Register user
    suspend fun registerUser(
        name: String,
        username: String,
        phone: String,
        email: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        // Local validation
        if (name.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
            return@withContext Result.failure(Exception("Please fill in all required fields."))
        }

        try {
            val request = RequestRegister(
                name = name,
                username = username,
                phone = phone,
                email = email,
                password = password
            )

            val response = userApi.register(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data?.token != null) {
                    // Save user data locally
                    saveUserLocally(name, username, email, phone, body.data.token)
                    Result.success("Registration successful.")
                } else {
                    Result.failure(Exception("Registration failed - no token received."))
                }
            } else {
                val msg = parseError(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Registration failed."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or server error: ${e.message}"))
        }
    }

    // Login user
    suspend fun loginUser(email: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        if (email.isBlank() || password.isBlank()) {
            return@withContext Result.failure(Exception("Email and password are required."))
        }

        try {
            val request = RequestLogin(email = email, password = password)
            val response = userApi.login(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.data?.token != null) {
                    // Use the data from the login response directly
                    saveUserLocally(
                        body.data.name,
                        body.data.username,
                        body.data.email,
                        body.data.phone,
                        body.data.token
                    )
                    Result.success("Login successful.")
                } else {
                    Result.failure(Exception("Invalid credentials."))
                }
            } else {
                val msg = parseError(response.errorBody()?.string())
                Result.failure(Exception(msg ?: "Login failed."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network or server error: ${e.message}"))
        }
    }

    // Fetch user by email
    suspend fun fetchUserByEmail(email: String): Result<UserModel> = withContext(Dispatchers.IO) {
        if (email.isBlank()) {
            return@withContext Result.failure(Exception("Email is required."))
        }

        try {
            val response = userApi.getUserByEmail(email)
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    val userModel = UserModel(
                        fullName = user.name,
                        username = user.username,
                        email = user.email,
                        phone = user.phone,
                        password = "" // Don't store password
                    )
                    Result.success(userModel)
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

    private fun saveUserLocally(
        fullName: String,
        username: String,
        email: String,
        phone: String,
        token: String
    ) {
        prefs.edit().apply {
            putString(KEY_FULL_NAME, fullName)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            putString(KEY_PHONE, phone)
            putString(KEY_TOKEN, token)
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
            password = "" // Don't return password
        )
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_TOKEN)
            apply()
        }
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