package com.example.alp_vp.data.dto

data class ResponseUserSearch(
    val success: Boolean,
    val message: String,
    val data: List<UserSearchItem>
)

data class UserSearchItem(
    val id: Int,
    val name: String,
    val username: String,
    val highest_score: Int,
    val friendship_status: String // 'none', 'pending', or 'accepted'
)