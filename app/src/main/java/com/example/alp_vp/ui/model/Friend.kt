package com.example.alp_vp.ui.model

data class Friend(
    val rank: Int,
    val id: Int,
    val name: String,
    val username: String,
    val highestScore: Int,
    val isCurrentUser: Boolean = false
)

