package com.example.alp_vp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.repository.DailyActivityRepository
import com.example.alp_vp.data.repository.UserRepository
import com.example.alp_vp.data.container.DailyActivityContainer
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
    private val repository: DailyActivityRepository,
    private val userRepository: UserRepository
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

    // In DailyActivityViewModel.kt
    fun onWaterChange(input: String) {
        val cleanInput = input.trimStart('0').ifEmpty { "0" }
        val parsed = cleanInput.toIntOrNull() ?: 0

        when {
            parsed < 0 -> {
                _waterMessage.value = "Invalid: Water glasses cannot be negative"
                _waterScore.value = 0f
            }
            parsed > 30 -> {
                _waterMessage.value = "Invalid: That's too much water (max: 30 glasses)"
                _waterScore.value = 0f
            }
            else -> {
                _waterGlasses.value = parsed
                calculateWaterScore(parsed)
            }
        }
    }

    fun onStepsChange(input: String) {
        val cleanInput = input.trimStart('0').ifEmpty { "0" }
        val parsed = cleanInput.toIntOrNull() ?: 0

        when {
            parsed < 0 -> {
                _stepsMessage.value = "Invalid: Steps cannot be negative"
                _stepsScore.value = 0f
            }
            parsed > 50000 -> {
                _stepsMessage.value = "Invalid: That's too many steps (max: 50,000)"
                _stepsScore.value = 0f
            }
            else -> {
                _steps.value = parsed
                calculateStepsScore(parsed.toFloat())
            }
        }
    }

    fun onCaloriesChange(input: String) {
        val cleanInput = input.trimStart('0').ifEmpty { "0" }
        val parsed = cleanInput.toIntOrNull() ?: 0

        when {
            parsed < 0 -> {
                _caloriesMessage.value = "Invalid: Calories cannot be negative"
                _caloriesScore.value = 0f
            }
            parsed > 10000 -> {
                _caloriesMessage.value = "Invalid: That's too many calories (max: 10,000)"
                _caloriesScore.value = 0f
            }
            else -> {
                _calories.value = parsed
                calculateCaloriesScore(parsed.toFloat())
            }
        }
    }

    fun onSleepChange(input: String) {
        val cleanInput = input.trimStart('0').ifEmpty { "0" }
        val parsed = cleanInput.toFloatOrNull() ?: 0f

        when {
            parsed < 0 -> {
                _sleepMessage.value = "Invalid: Sleep hours cannot be negative"
                _sleepScore.value = 0f
            }
            parsed > 24 -> {
                _sleepMessage.value = "Invalid: Sleep cannot exceed 24 hours"
                _sleepScore.value = 0f
            }
            else -> {
                _sleepHours.value = parsed
                calculateSleepScore(parsed)
            }
        }
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
        // Each category is 0-20, but we need 0-25 for total of 100
        // So multiply by 2.5 (20 * 2.5 = 50, but we want 25, so 10 * 2.5 = 25)
        // Since display shows 0-10 (which is score/2), actual conversion is score * 1.25
        val sleepPoints = _sleepScore.value * 1.25f  // 20 * 1.25 = 25
        val waterPoints = _waterScore.value * 1.25f  // 20 * 1.25 = 25
        val stepsPoints = _stepsScore.value * 1.25f  // 20 * 1.25 = 25
        val caloriesPoints = _caloriesScore.value * 1.25f  // 20 * 1.25 = 25
        return sleepPoints + waterPoints + stepsPoints + caloriesPoints  // Max: 100
    }

    fun loadTodayData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val activities = repository.getAllDailyActivities()
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

    private fun updateHighestScoreIfNeeded() {
        val totalScore = calculateTotalScore().toInt()
        userRepository.updateHighestScore(totalScore)
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val container = DailyActivityContainer()
                val userRepository = UserRepository(context)
                return DailyActivityViewModel(container.dailyActivityRepository, userRepository) as T
            }
        }
    }
}
