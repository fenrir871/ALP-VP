package com.example.alp_vp.data.repository

import com.example.alp_vp.data.api.RetrofitClient
import com.example.alp_vp.data.model.*

class FriendRepository {

    private val api = RetrofitClient.friendApi

    // Add friend
    suspend fun addFriend(friendId: Int): Result<FriendResponse> {
        return try {
            val response = api.addFriend(AddFriendRequest(friendId))

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to add friend"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get pending friend requests
    suspend fun getPendingRequests(): Result<List<PendingFriendRequest>> {
        return try {
            val response = api.getPendingRequests()

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get pending requests"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Accept friend request
    suspend fun acceptFriendRequest(requestId: Int): Result<FriendResponse> {
        return try {
            val response = api.acceptFriendRequest(AcceptFriendRequest(requestId))

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to accept friend request"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get friend leaderboard
    suspend fun getFriendLeaderboard(): Result<List<LeaderboardItem>> {
        return try {
            val response = api.getFriendLeaderboard()

            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get leaderboard"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}