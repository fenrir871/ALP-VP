package com.example.alp_vp.data.model

// Request to add a friend
data class AddFriendRequest(
    val friend_id: Int
)

// Request to accept a friend request
data class AcceptFriendRequest(
    val friend_request_id: Int
)

// Response after adding/accepting friend
data class FriendResponse(
    val id: Int,
    val user_id: Int,
    val friend_id: Int,
    val status: String
)

// Pending friend request with user info
data class PendingFriendRequest(
    val id: Int,
    val user_id: Int,
    val friend_id: Int,
    val status: String,
    val friend: UserInfo
)

// Basic user info
data class UserInfo(
    val id: Int,
    val name: String,
    val username: String
)

// Leaderboard item
data class LeaderboardItem(
    val friend_id: Int,
    val name: String,
    val username: String,
    val overall_score: Int,
    val rank: Int
)

// API Response wrapper
data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T?
)