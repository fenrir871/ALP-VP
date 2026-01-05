package com.example.alp_vp.data.api

import com.example.alp_vp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface FriendApiService {

    @POST("friends/add")
    suspend fun addFriend(
        @Body request: AddFriendRequest
    ): Response<ApiResponse<FriendResponse>>

    @GET("friends/pending")
    suspend fun getPendingRequests(): Response<ApiResponse<List<PendingFriendRequest>>>

    @POST("friends/accept")
    suspend fun acceptFriendRequest(
        @Body request: AcceptFriendRequest
    ): Response<ApiResponse<FriendResponse>>

    @GET("friends/leaderboard")
    suspend fun getFriendLeaderboard(): Response<ApiResponse<List<LeaderboardItem>>>
}