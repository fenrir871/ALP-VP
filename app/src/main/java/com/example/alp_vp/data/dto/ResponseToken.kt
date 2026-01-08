package com.example.alp_vp.data.dto

data class ResponseToken(
    val success: Boolean,
    val data: TokenData
)

data class TokenData(
    val id: Int,
    val name: String,
    val username: String,
    val phone: String,
    val email: String,
    val highest_score: Int,
    val friends_count: Int,
    val token: String
)