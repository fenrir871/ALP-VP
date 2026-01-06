package com.example.alp_vp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.alp_vp.data.dto.ResponseWeeklySummary
import com.example.alp_vp.data.repository.WeeklyActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.text.toFloat
import kotlin.text.toInt

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

class WeeklyViewModel(private val repository: WeeklyActivityRepository) : ViewModel() {

    private val _weeklySummary = MutableLiveData<ResponseWeeklySummary?>()
    val weeklySummary: LiveData<ResponseWeeklySummary?> = _weeklySummary

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _weeklyStats = MutableStateFlow(WeeklyStats())
    val weeklyStats: StateFlow<WeeklyStats> = _weeklyStats.asStateFlow()

    fun fetchWeeklySummary(userId: Int) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                android.util.Log.d("WeeklyViewModel", "Fetching data for userId: $userId")
                val response = repository.getWeeklySummaryByUserId(userId)
                android.util.Log.d("WeeklyViewModel", "Response code: ${response.code()}")
                android.util.Log.d("WeeklyViewModel", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    _weeklySummary.value = response.body()
                    response.body()?.let { calculateWeeklyStatsFromResponse(it) }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                android.util.Log.e("WeeklyViewModel", "Error fetching data", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }



    private fun calculateWeeklyStatsFromResponse(summary: ResponseWeeklySummary) {
        _weeklyStats.value = WeeklyStats(
            sleepAvg = summary.avgSleep?.toFloat() ?: 0f,
            sleepScore = summary.scoreSleep?.toInt() ?: 0,
            waterAvg = summary.avgWater?.toInt() ?: 0,
            waterScore = summary.scoreWater?.toInt() ?: 0,
            stepsAvg = summary.avgSteps?.toInt() ?: 0,
            stepsScore = summary.scoreSteps?.toInt() ?: 0,
            caloriesAvg = summary.avgCalories?.toInt() ?: 0,
            caloriesScore = summary.scoreCalories?.toInt() ?: 0
        )
    }

    private fun calculateSleepScore(hours: Float): Int {
        return when {
            hours >= 8f -> 10
            hours >= 7f -> 8
            hours >= 6f -> 6
            hours >= 5f -> 4
            else -> 2
        }
    }

    private fun calculateWaterScore(glasses: Float): Int {
        return ((glasses / 8f) * 10).toInt().coerceIn(0, 10)
    }

    private fun calculateStepsScore(steps: Float): Int {
        return ((steps / 10000f) * 10).toInt().coerceIn(0, 10)
    }

    private fun calculateCaloriesScore(calories: Float): Int {
        return ((calories / 2000f) * 10).toInt().coerceIn(0, 10)
    }


    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WeeklyViewModel(WeeklyActivityRepository.getInstance(context)) as T
            }
        }
    }
}
