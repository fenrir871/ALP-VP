package com.example.alp_vp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.alp_vp.data.api.RetrofitInstance
import com.example.alp_vp.data.dto.*
import com.example.alp_vp.ui.model.Friend
import com.example.alp_vp.ui.model.UserSearchModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("friend_prefs", Context.MODE_PRIVATE)

    private val friendApi = RetrofitInstance.friendApi
    private val userApi = RetrofitInstance.userApi

    /**
     * Search for users to add as friends
     */
    suspend fun searchUsers(query: String): Result<List<UserSearchModel>> = withContext(Dispatchers.IO) {
        try {
            if (query.length < 2) {
                return@withContext Result.failure(Exception("Search query must be at least 2 characters"))
            }

            val response = userApi.searchUsers(query)
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    when {
                        body?.success == true && body.data.isNotEmpty() -> {
                            val users = body.data.map { item ->
                                UserSearchModel(
                                    id = item.id,
                                    name = item.name,
                                    username = "@${item.username}",
                                    highestScore = item.highest_score,
                                    friendshipStatus = item.friendship_status
                                )
                            }
                            Result.success(users)
                        }
                        body?.success == true && body.data.isEmpty() -> {
                            Result.success(emptyList())
                        }
                        else -> Result.failure(Exception(body?.message ?: "Failed to search users"))
                    }
                }
                else -> Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    /**
     * Get friend leaderboard from API
     */
    suspend fun getFriendLeaderboard(): Result<List<Friend>> = withContext(Dispatchers.IO) {
        try {
            val response = friendApi.getFriendLeaderboard()
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    when {
                        body?.status == "success" && body.data.isNotEmpty() -> {
                            val friends = body.data.map { item ->
                                Friend(
                                    rank = item.rank,
                                    id = item.friend_id,
                                    name = item.name,
                                    username = "@${item.username}",
                                    highestScore = item.overall_score,
                                    isCurrentUser = false
                                )
                            }
                            Result.success(friends)
                        }
                        body?.status == "success" && body.data.isEmpty() -> {
                            Result.success(emptyList())
                        }
                        else -> Result.failure(Exception(body?.message ?: "Failed to load leaderboard"))
                    }
                }
                else -> Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    /**
     * Add a friend (send friend request)
     */
    suspend fun addFriend(friendId: Int): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = RequestAddFriend(friend_id = friendId)
            val response = friendApi.addFriend(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to add friend"))
                }
            } else {
                Result.failure(Exception("Failed to add friend"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    /**
     * Get pending friend requests
     */
    suspend fun getPendingRequests(): Result<List<PendingFriend>> = withContext(Dispatchers.IO) {
        try {
            val response = friendApi.getPendingRequests()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == "success") {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to load pending requests"))
                }
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    /**
     * Accept a friend request
     */
    suspend fun acceptFriendRequest(requestId: Int): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = RequestAcceptFriend(friend_request_id = requestId)
            val response = friendApi.acceptFriendRequest(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == "success") {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to accept request"))
                }
            } else {
                Result.failure(Exception("Failed to accept request"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }
}