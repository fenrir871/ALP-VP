package com.example.alp_vp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.alp_vp.data.api.RetrofitClient
import com.example.alp_vp.data.local.TokenManager
import com.example.alp_vp.navigation.AppNavigation
import com.example.alp_vp.ui.theme.ALPVPTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val tokenManager = TokenManager(applicationContext)
        RetrofitClient.initialize(tokenManager)

        setContent {
            ALPVPTheme {
                AppNavigation(tokenManager = tokenManager)
            }
        }
    }
}