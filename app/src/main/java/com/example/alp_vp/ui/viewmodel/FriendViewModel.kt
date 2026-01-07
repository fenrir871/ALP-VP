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

    private val _allFriends = MutableStateFlow<List<Friend>>(emptyList())
    val allFriends: StateFlow<List<Friend>> = _allFriends.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun loadAllFriends() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getAllFriends()
                _allFriends.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun loadLeaderboard(userName: String, username: String, userScore: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getLeaderboard(userName, username, userScore)
                _leaderboard.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun searchFriends(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.searchFriends(query)
                _allFriends.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun addFriend(friend: Friend) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.addFriend(friend)
                _successMessage.value = "Friend added successfully!"
                loadAllFriends() // Reload the list
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.removeFriend(friendId)
                _successMessage.value = "Friend removed"
                loadAllFriends() // Reload the list
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}