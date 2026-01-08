package com.example.alp_vp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.ui.model.Friend
import com.example.alp_vp.data.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendViewModel(context: Context) : ViewModel() {

    private val repository = FriendRepository(context)

    private val _leaderboard = MutableStateFlow<List<Friend>>(emptyList())
    val leaderboard: StateFlow<List<Friend>> = _leaderboard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    /**
     * Load friend leaderboard from API
     */
    fun loadLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.getFriendLeaderboard()
            result.onSuccess { friends ->
                _leaderboard.value = friends
                _isLoading.value = false
            }.onFailure { error ->
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        }
    }

    /**
     * Add a friend (send friend request)
     */
    fun addFriend(friendId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.addFriend(friendId)
            result.onSuccess { message ->
                _successMessage.value = message
                _isLoading.value = false
            }.onFailure { error ->
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        }
    }

    /**
     * Accept a friend request
     */
    fun acceptFriendRequest(requestId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.acceptFriendRequest(requestId)
            result.onSuccess { message ->
                _successMessage.value = message
                loadLeaderboard() // Reload leaderboard after accepting
                _isLoading.value = false
            }.onFailure { error ->
                _errorMessage.value = error.message
                _isLoading.value = false
            }
        }
    }

    /**
     * Clear success and error messages
     */
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}