package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.repository.DailyActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// In UiState (change types to String)
data class UiState(
    val username: String = "User",
    val dateLabel: String = "Today",
    val streakDays: Int = 0,
    val avgScore: Int = 0,
    val goalsDone: Int = 0,
    val goalsTotal: Int = 0,
    val steps: String = "",
    val calories: String = "",
    val waterGlasses: String = "",
    val sleepHours: String = ""
)


class DailyActivityViewModel(
    private val repository: DailyActivityRepository
) : ViewModel() {
    // UI State
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // Score and message states
    val _sleepScore = MutableStateFlow(0f)
    val _sleepMessage = MutableStateFlow("")
    val _waterScore = MutableStateFlow(0f)
    val _waterMessage = MutableStateFlow("")
    val _stepsScore = MutableStateFlow(0f)
    val _stepsMessage = MutableStateFlow("")
    val _caloriesScore = MutableStateFlow(0f)
    val _caloriesMessage = MutableStateFlow("")

    // Loading and error states
    val _isLoading = MutableStateFlow(false)
    val _error = MutableStateFlow<String?>(null)

    // For demo, use local state for activity values
    val _sleepHours = MutableStateFlow(0f)
    val _waterGlasses = MutableStateFlow(0)
    val _steps = MutableStateFlow(0)
    val _calories = MutableStateFlow(0)
    val _avgScore = MutableStateFlow(0)

    fun onSleepChange(value: String) {
        val hours = value.toFloatOrNull() ?: 0f
        _sleepHours.value = hours
        _uiState.value = _uiState.value.copy(sleepHours = value)
        calculateSleepScore(hours)
    }
    fun onWaterChange(value: String) {
        val glasses = value.toIntOrNull() ?: 0
        _waterGlasses.value = glasses
        _uiState.value = _uiState.value.copy(waterGlasses = value)
        calculateWaterScore(glasses)
    }
    fun onStepsChange(value: String) {
        val steps = value.toIntOrNull() ?: 0
        _steps.value = steps
        _uiState.value = _uiState.value.copy(steps = value)
        calculateStepsScore(steps.toFloat())
    }
    fun onCaloriesChange(value: String) {
        val calories = value.toIntOrNull() ?: 0
        _calories.value = calories
        _uiState.value = _uiState.value.copy(calories = value)
        calculateCaloriesScore(calories.toFloat())
    }
    fun calculateSleepScore(hours: Float) {
        val score: Float
        val message: String

        when {
            hours in 7f..8f -> {
                score = 20f
                message = "Jam tidur anda telah mencukupi (20/20)"
            }
            hours < 7 -> {
                score = (hours / 7) * 20
                message = "Anda kurang tidur (${String.format("%.2f", score)}/20)"
            }
            else -> {
                score = ((8 - (hours - 8)) / 8) * 20
                val finalScore = if (score < 0) 0f else score
                message = "Anda terlalu banyak tidur (${String.format("%.2f", finalScore)}/20)"
                _sleepScore.value = finalScore
                _sleepMessage.value = message
                return
            }
        }

        _sleepScore.value = score
        _sleepMessage.value = message
    }

    fun calculateWaterScore(glasses: Int) {
        val score = (glasses.toFloat() / 8) * 20
        val finalScore = if (score >= 20) 20f else score

        val message = when {
            finalScore >= 20 -> "Jumlah air yang anda minum sudah mencukupi (20/20)"
            finalScore >= 10 -> "Terus pertahankan konsumsi air anda (${String.format("%.2f", finalScore)}/20)"
            else -> "Lebih rajin lagi minum air (${String.format("%.2f", finalScore)}/20)"
        }

        _waterScore.value = finalScore
        _waterMessage.value = message
    }

    fun calculateStepsScore(steps: Float) {
        val score = (steps / 10000) * 20
        val finalScore = if (score >= 20) 20f else score

        val message = when {
            finalScore >= 20 -> "Langkah kaki anda telah mencukupi (20/20)"
            finalScore >= 10 -> "Terus pertahankan langkah kaki anda (${String.format("%.2f", finalScore)}/20)"
            else -> "Lebih rajin lagi berjalan kaki (${String.format("%.2f", finalScore)}/20)"
        }

        _stepsScore.value = finalScore
        _stepsMessage.value = message
    }

    fun calculateCaloriesScore(calories: Float) {
        val score = (calories / 2000) * 20
        val finalScore = if (score >= 20) 20f else score

        val message = when {
            finalScore >= 20 -> "Asupan kalori anda telah mencukupi (20/20)"
            finalScore >= 10 -> "Terus pertahankan asupan kalori anda (${String.format("%.2f", finalScore)}/20)"
            else -> "Tingkatkan asupan kalori anda (${String.format("%.2f", finalScore)}/20)"
        }

        _caloriesScore.value = finalScore
        _caloriesMessage.value = message
    }

    fun calculateTotalScore(): Float {
        return _sleepScore.value + _waterScore.value + _stepsScore.value + _caloriesScore.value
    }

    fun loadTodayData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val activities = repository.getAllTodayActivities()
                calculateAllScores()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateAllScores() {
        calculateSleepScore(_sleepHours.value)
        calculateWaterScore(_waterGlasses.value)
        calculateStepsScore(_steps.value.toFloat())
        calculateCaloriesScore(_calories.value.toFloat())
        _avgScore.value = calculateTotalScore().toInt()
    }
}
