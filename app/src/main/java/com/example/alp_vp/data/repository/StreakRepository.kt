package com.example.alp_vp.data.repository

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class StreakRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_ACTIVITY_DATE = "last_activity_date"
        private const val KEY_STREAK_COUNT = "streak_count"
    }

    fun updateStreak() {
        val today = getCurrentDate()
        val lastDate = prefs.getString(KEY_LAST_ACTIVITY_DATE, "")
        val currentStreak = prefs.getInt(KEY_STREAK_COUNT, 0)

        when {
            lastDate == today -> {
                // Already logged today, do nothing
            }
            lastDate == getYesterday() -> {
                // Consecutive day
                prefs.edit().apply {
                    putString(KEY_LAST_ACTIVITY_DATE, today)
                    putInt(KEY_STREAK_COUNT, currentStreak + 1)
                    apply()
                }
            }
            else -> {
                // Streak broken, reset
                prefs.edit().apply {
                    putString(KEY_LAST_ACTIVITY_DATE, today)
                    putInt(KEY_STREAK_COUNT, 1)
                    apply()
                }
            }
        }
    }

    fun getStreak(): Int = prefs.getInt(KEY_STREAK_COUNT, 0)

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getYesterday(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}