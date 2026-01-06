package com.example.alp_vp.ui.model

data class UserModel(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val id: Int? = null,
    val highestScore: Int? = null,
    val friendsCount: Int? = null,
    val token: String? = null
)

