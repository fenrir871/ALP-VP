package com.example.alp_vp.data.dto

data class ResponseLeaderboard(
    val status: String,
    val message: String,
    val data: List<LeaderboardItem>
)

data class LeaderboardItem(
    val friend_id: Int,
    val name: String,
    val username: String,
    val overall_score: Int,
    val rank: Int
)