package com.example.alp_vp.ui.model

data class Friend(
    val rank: Int = 0,
    val id: Int,
    val name: String,
    val username: String,
    val highestScore: Int = 0,
    val isCurrentUser: Boolean = false
)