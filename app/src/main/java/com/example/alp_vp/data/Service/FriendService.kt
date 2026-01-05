package com.example.alp_vp.data.service

import com.example.alp_vp.ui.model.Friend
import com.example.alp_vp.data.repository.FriendRepository

/**
 * Service layer for Friend feature
 * Handles business logic between repository and UI
 */
class FriendService(private val friendRepository: FriendRepository) {

    /**
     * Get ranked leaderboard with current user
     */
    fun getLeaderboardWithRanks(
        currentUserName: String,
        currentUserUsername: String,
        currentUserScore: Int
    ): List<RankedFriend> {
        val friends = friendRepository.getLeaderboard(
            currentUserName,
            currentUserUsername,
            currentUserScore
        )

        // Assign ranks based on sorted order
        return friends.mapIndexed { index, friend ->
            RankedFriend(
                rank = index + 1,
                friend = friend
            )
        }
    }

    /**
     * Get current user's rank in leaderboard
     */
    fun getCurrentUserRank(
        currentUserName: String,
        currentUserUsername: String,
        currentUserScore: Int
    ): Int? {
        val leaderboard = getLeaderboardWithRanks(
            currentUserName,
            currentUserUsername,
            currentUserScore
        )

        return leaderboard.find { it.friend.isCurrentUser }?.rank
    }

    /**
     * Search friends by query
     */
    fun searchFriends(query: String): List<Friend> {
        return friendRepository.searchFriends(query)
    }

    /**
     * Add a friend
     */
    fun addFriend(friend: Friend) {
        friendRepository.addFriend(friend)
    }

    /**
     * Remove a friend
     */
    fun removeFriend(friendId: Int) {
        friendRepository.removeFriend(friendId)
    }
}

/**
 * Data class representing a friend with their rank
 */
data class RankedFriend(
    val rank: Int,
    val friend: Friend
)

