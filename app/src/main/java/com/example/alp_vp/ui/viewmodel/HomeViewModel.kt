package com.example.alp_vp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.repository.GoalsRepository
import com.example.alp_vp.data.repository.StreakRepository
import com.example.alp_vp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class HomeUiState(
    val username: String = "",
    val currentDate: String = getCurrentFormattedDate(),  // Not currentDateLabel
    val streakDays: Int = 0,
    val avgScore: Int = 0,
    val goalsCompleted: Int = 0,
    val goalsTotal: Int = 4,
    val isLoading: Boolean = true,
    val error: String? = null
)
private fun getCurrentFormattedDate(): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}

class HomeViewModel(
    private val userRepository: UserRepository,
    private val streakRepository: StreakRepository,
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
        loadStreakData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val user = userRepository.getCurrentUser()
                _uiState.value = _uiState.value.copy(
                    username = user?.fullName ?: "Guest",
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    private fun loadStreakData() {
        viewModelScope.launch {
            val streak = streakRepository.getStreak()
            _uiState.value = _uiState.value.copy(
                streakDays = streak
            )
        }
    }

    fun updateStats(
        sleepScore: Float,
        waterScore: Float,
        stepsScore: Float,
        caloriesScore: Float
    ) {
        viewModelScope.launch {
            val avgScore = ((sleepScore + waterScore + stepsScore + caloriesScore) / 8).toInt()

            val goalsCompleted = listOf(sleepScore, waterScore, stepsScore, caloriesScore)
                .count { it >= 15f }

            _uiState.value = _uiState.value.copy(
                avgScore = avgScore,
                goalsCompleted = goalsCompleted
            )
        }
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    UserRepository(context),
                    StreakRepository(context),
                    GoalsRepository(context)
                ) as T
            }
        }
    }
}