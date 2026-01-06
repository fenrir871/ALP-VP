// kotlin
package com.example.alp_vp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



data class HomeUiState(
    val username: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(
    private val userRepository: UserRepository? = null // Add default null or provide default instance
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val user = userRepository?.getCurrentUser()
                _uiState.value = _uiState.value.copy(
                    username = user?.username ?: "User",
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }


    data class WeeklyStats(
        val sleepAvg: Float = 0f,
        val sleepScore: Int = 0,
        val waterAvg: Int = 0,
        val waterScore: Int = 0,
        val stepsAvg: Int = 0,
        val stepsScore: Int = 0,
        val caloriesAvg: Int = 0,
        val caloriesScore: Int = 0
    )

    private val _weeklyStats = MutableStateFlow(WeeklyStats())
    val weeklyStats: StateFlow<WeeklyStats> = _weeklyStats.asStateFlow()

    init {
        calculateWeeklyStats()
    }

    private fun calculateWeeklyStats() {
        viewModelScope.launch {
            try {
                // Get last 7 days of data from your repository
                val last7Days = repository.getLastSevenDaysActivities()

                if (last7Days.isEmpty()) {
                    _weeklyStats.value = WeeklyStats() // Keep zeros if no data
                    return@launch
                }

                val avgSleep = last7Days.map { it.sleepHours }.average().toFloat()
                val avgWater = last7Days.map { it.waterGlasses }.average().toInt()
                val avgSteps = last7Days.map { it.steps }.average().toInt()
                val avgCalories = last7Days.map { it.calories }.average().toInt()

                _weeklyStats.value = WeeklyStats(
                    sleepAvg = avgSleep,
                    sleepScore = calculateSleepScore(avgSleep),
                    waterAvg = avgWater,
                    waterScore = calculateWaterScore(avgWater.toFloat()),
                    stepsAvg = avgSteps,
                    stepsScore = calculateStepsScore(avgSteps.toFloat()),
                    caloriesAvg = avgCalories,
                    caloriesScore = calculateCaloriesScore(avgCalories.toFloat())
                )
            } catch (e: Exception) {
                // Handle error - keep zeros
                _weeklyStats.value = WeeklyStats()
            }
        }
    }



    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(UserRepository(context)) as T
            }
        }
    }
}