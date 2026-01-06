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

    private val _leaderboard = MutableStateFlow<List<LeaderboardItem>>(emptyList())
    val leaderboard: StateFlow<List<LeaderboardItem>> = _leaderboard.asStateFlow()

    private val _pendingRequests = MutableStateFlow<List<PendingFriendRequest>>(emptyList())
    val pendingRequests: StateFlow<List<PendingFriendRequest>> = _pendingRequests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun loadLeaderboard() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getFriendLeaderboard()
            result.onSuccess { _leaderboard.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun loadPendingRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getPendingRequests()
            result.onSuccess { _pendingRequests.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun acceptFriendRequest(requestId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.acceptFriendRequest(requestId)
            result.onSuccess {
                _successMessage.value = "Friend request accepted!"
                loadPendingRequests()
                loadLeaderboard()
            }.onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}