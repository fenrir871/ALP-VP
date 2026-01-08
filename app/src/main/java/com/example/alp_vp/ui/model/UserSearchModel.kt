package com.example.alp_vp.ui.model

data class UserSearchModel(
    val id: Int,
    val name: String,
    val username: String,
    val highestScore: Int = 0,
    val friendshipStatus: String = "none"
)