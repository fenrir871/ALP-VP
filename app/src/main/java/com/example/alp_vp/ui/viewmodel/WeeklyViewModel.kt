package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alp_vp.data.dto.ResponseWeeklySummary
import com.example.alp_vp.data.repository.WeeklyActivityRepository
import kotlinx.coroutines.launch

class WeeklyViewModel(private val repository: WeeklyActivityRepository) : ViewModel() {

    private val _weeklySummary = MutableLiveData<ResponseWeeklySummary?>()
    val weeklySummary: LiveData<ResponseWeeklySummary?> = _weeklySummary

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchWeeklySummary(userId: Int) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val response = repository.getWeeklySummaryByUserId(userId)
                if (response.isSuccessful) {
                    _weeklySummary.value = response.body()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}