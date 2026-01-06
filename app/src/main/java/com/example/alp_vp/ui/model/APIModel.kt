package com.example.alp_vp.data.api

import com.example.alp_vp.ui.model.UserModel

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val user: UserModel?,
    val message: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: Int?
)
