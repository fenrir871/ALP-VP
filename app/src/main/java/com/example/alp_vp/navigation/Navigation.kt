package com.example.alp_vp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alp_vp.data.local.TokenManager
import com.example.alp_vp.ui.view.Friend.Friend
import com.example.alp_vp.ui.view.HomeView
import com.example.alp_vp.ui.view.LoginRegister.Login
import com.example.alp_vp.ui.view.LoginRegister.Register
import com.example.alp_vp.ui.view.ProfileView

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Friend : Screen("friend")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation(tokenManager: TokenManager) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // ========== LOGIN SCREEN ==========
        composable(Screen.Login.route) {
            Login(
                tokenManager = tokenManager,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {  // Changed to Home
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // ========== REGISTER SCREEN ==========
        composable(Screen.Register.route) {
            Register(
                tokenManager = tokenManager,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {  // Changed to Home
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ========== HOME SCREEN ==========
        composable(Screen.Home.route) {
            val username by tokenManager.getUsername().collectAsState(initial = "User")

            HomeView()
        }

        composable(Screen.Friend.route) {
            Friend()
        }

        composable(Screen.Profile.route) {
            ProfileView()
        }
    }
}