package com.example.alp_vp.data.dto

data class ResponseFriend(
    val status: String,
    val message: String,
    val data: FriendData?
)

data class FriendData(
    val id: Int,
    val user_id: Int,
    val friend_id: Int,
    val status: String
)
