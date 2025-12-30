package com.example.alp_vp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.alp_vp.ui.model.Friend

class FriendRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("friend_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FRIEND_COUNT = "friend_count"
        private const val KEY_FRIEND_PREFIX = "friend_"
    }

    /**
     * Get all friends from local storage
     * In a real app, this would fetch from a remote API
     */
    fun getAllFriends(): List<Friend> {
        // For now, return mock data
        // In production, this would query a database or API
        return listOf(
            Friend(0, 1, "John Doe", "@johndoe", 95, false),
            Friend(0, 2, "Jane Smith", "@janesmith", 88, false),
            Friend(0, 3, "Mike Johnson", "@mikej", 92, false),
            Friend(0, 4, "Sarah Williams", "@sarahw", 87, false),
            Friend(0, 5, "David Brown", "@davidb", 90, false),
            Friend(0, 6, "Emily Davis", "@emilyd", 85, false),
            Friend(0, 7, "Chris Wilson", "@chrisw", 89, false)
        )
    }

    /**
     * Add a friend to local storage
     */
    fun addFriend(friend: Friend) {
        val count = prefs.getInt(KEY_FRIEND_COUNT, 0)
        prefs.edit().apply {
            putString("${KEY_FRIEND_PREFIX}${count}_name", friend.name)
            putString("${KEY_FRIEND_PREFIX}${count}_username", friend.username)
            putInt("${KEY_FRIEND_PREFIX}${count}_score", friend.highestScore)
            putInt(KEY_FRIEND_COUNT, count + 1)
            apply()
        }
    }

    /**
     * Remove a friend from local storage
     */
    fun removeFriend(friendId: Int) {
        // Implementation for removing a friend
        // In production, this would call an API
    }

    /**
     * Search friends by name or username
     */
    fun searchFriends(query: String): List<Friend> {
        return getAllFriends().filter {
            it.name.contains(query, ignoreCase = true) ||
            it.username.contains(query, ignoreCase = true)
        }
    }

    /**
     * Get leaderboard with current user included
     */
    fun getLeaderboard(currentUserName: String, currentUserUsername: String, currentUserScore: Int): List<Friend> {
        val friends = getAllFriends().toMutableList()

        // Add current user to leaderboard
        friends.add(
            Friend(
                rank = 0,
                id = 999,
                name = currentUserName,
                username = currentUserUsername,
                highestScore = currentUserScore,
                isCurrentUser = true
            )
        )

        // Sort by score descending
        return friends.sortedByDescending { it.highestScore }
    }
}

