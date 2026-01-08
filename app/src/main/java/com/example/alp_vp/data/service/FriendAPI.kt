package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface FriendAPI {
    @POST("friends/add")
    suspend fun addFriend(@Body request: RequestAddFriend): Response<ResponseFriend>

    @GET("friends/pending")
    suspend fun getPendingRequests(): Response<ResponsePendingFriends>

    @POST("friends/accept")
    suspend fun acceptFriendRequest(@Body request: RequestAcceptFriend): Response<ResponseFriend>

    @GET("friends/leaderboard")
    suspend fun getFriendLeaderboard(): Response<ResponseLeaderboard>
}