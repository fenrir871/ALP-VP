package com.example.alp_vp.data.repository

import android.content.Context

class GoalsRepository(context: Context) {

    fun getGoalsCompleted(
        sleepScore: Float,
        waterScore: Float,
        stepsScore: Float,
        caloriesScore: Float
    ): Int {
        var completed = 0
        // Goal is considered completed if score >= 15 (out of 20)
        if (sleepScore >= 15f) completed++
        if (waterScore >= 15f) completed++
        if (stepsScore >= 15f) completed++
        if (caloriesScore >= 15f) completed++
        return completed
    }

    fun getTotalGoals(): Int = 4 // Sleep, Water, Steps, Calories
}