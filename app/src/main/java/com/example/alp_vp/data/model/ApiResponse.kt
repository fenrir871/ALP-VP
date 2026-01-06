package com.example.alp_vp.data.model

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)