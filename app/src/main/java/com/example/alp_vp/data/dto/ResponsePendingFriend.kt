package com.example.alp_vp.data.dto

data class ResponsePendingFriends(
    val status: String,
    val message: String,
    val data: List<PendingFriend>
)

data class PendingFriend(
    val id: Int,
    val user_id: Int,
    val friend_id: Int,
    val status: String,
    val friend: FriendUserInfo
)

data class FriendUserInfo(
    val id: Int,
    val name: String
)