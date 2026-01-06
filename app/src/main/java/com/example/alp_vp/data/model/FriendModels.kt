package com.example.alp_vp.data.model

data class FriendResponse(
    val id: Int,
    val message: String
)

data class AddFriendRequest(
    val friendId: Int
)

data class AcceptFriendRequest(
    val requestId: Int
)

data class PendingFriendRequest(
    val id: Int,
    val friend: FriendUser
)

data class FriendUser(
    val id: Int,
    val name: String,
    val username: String
)

data class LeaderboardItem(
    val rank: Int,
    val id: Int,
    val name: String,
    val username: String,
    val overall_score: Int
)