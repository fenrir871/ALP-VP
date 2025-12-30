package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.model.LeaderboardItem
import com.example.alp_vp.data.model.PendingFriendRequest
import com.example.alp_vp.data.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendViewModel : ViewModel() {

    private val repository = FriendRepository()

    // UI State for leaderboard
    private val _leaderboard = MutableStateFlow<List<LeaderboardItem>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardItem>> = _leaderboard.asStateFlow()

    // UI State for pending requests
    private val _pendingRequests = MutableStateFlow<List<PendingFriendRequest>>(emptyList())
    val pendingRequests: StateFlow<List<PendingFriendRequest>> = _pendingRequests.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error message
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Success message
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // Load friend leaderboard
    fun loadLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getFriendLeaderboard()

            result.onSuccess { data ->
                _leaderboard.value = data
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to load leaderboard"
            }

            _isLoading.value = false
        }
    }

    // Load pending friend requests
    fun loadPendingRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getPendingRequests()

            result.onSuccess { data ->
                _pendingRequests.value = data
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to load pending requests"
            }

            _isLoading.value = false
        }
    }

    // Add friend
    fun addFriend(friendId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            val result = repository.addFriend(friendId)

            result.onSuccess {
                _successMessage.value = "Friend request sent!"
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to send friend request"
            }

            _isLoading.value = false
        }
    }

    // Accept friend request
    fun acceptFriendRequest(requestId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            val result = repository.acceptFriendRequest(requestId)

            result.onSuccess {
                _successMessage.value = "Friend request accepted!"
                // Reload pending requests and leaderboard
                loadPendingRequests()
                loadLeaderboard()
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Failed to accept friend request"
            }

            _isLoading.value = false
        }
    }

    // Clear messages
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}