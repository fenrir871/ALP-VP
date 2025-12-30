package com.example.alp_vp.ui.route

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.alp_vp.data.container.DailyActivityContainer
import com.example.alp_vp.data.repository.UserRepository
import com.example.alp_vp.ui.view.Friend.Friend
import com.example.alp_vp.ui.view.HomeView
import com.example.alp_vp.ui.view.LoginRegister.Register
import com.example.alp_vp.ui.view.ProfileView
import com.example.alp_vp.ui.viewmodel.DailyActivityViewModel
import com.example.ui.LoginScreen

enum class AppScreens(val title: String, val icon: ImageVector? = null) {
    LOGIN("Login"),
    REGISTER("Register"),
    HOME("Home", Icons.Filled.Home),
    FRIENDS("Friends", Icons.Filled.People),
    PROFILE("Profile", Icons.Filled.Person)
}

@Composable
fun MainBottomNavBar(
    navController: NavController,
    currentDestination: NavDestination?,
    items: List<AppScreens>
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { item.icon?.let { Icon(it, contentDescription = item.title) } },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.title } == true,
                onClick = {
                    navController.navigate(item.title) {
                        popUpTo(AppScreens.HOME.title)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Initialize the container for DailyActivityViewModel
    val container = remember { DailyActivityContainer() }

    // Pass the repository to the ViewModel
    val dailyActivityViewModel: DailyActivityViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DailyActivityViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return DailyActivityViewModel(container.dailyActivityRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    // Determine start destination based on login status
    val startDestination = if (userRepository.isLoggedIn()) {
        AppScreens.HOME.title
    } else {
        AppScreens.LOGIN.title
    }

    // Show bottom bar only for authenticated screens
    val showBottomBar = currentDestination?.route in listOf(
        AppScreens.HOME.title,
        AppScreens.FRIENDS.title,
        AppScreens.PROFILE.title
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MainBottomNavBar(
                    navController = navController,
                    currentDestination = currentDestination,
                    items = listOf(
                        AppScreens.HOME,
                        AppScreens.FRIENDS,
                        AppScreens.PROFILE
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = startDestination
        ) {
            composable(AppScreens.LOGIN.title) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AppScreens.HOME.title) {
                            popUpTo(AppScreens.LOGIN.title) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(AppScreens.REGISTER.title)
                    }
                )
            }

            composable(AppScreens.REGISTER.title) {
                Register(
                    onRegisterSuccess = {
                        navController.navigate(AppScreens.HOME.title) {
                            popUpTo(AppScreens.LOGIN.title) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(AppScreens.HOME.title) {
                HomeView(dailyActivityViewModel = dailyActivityViewModel)
            }

            composable(AppScreens.FRIENDS.title) {
                Friend()
            }

            composable(AppScreens.PROFILE.title) {
                ProfileView(
                    onLogout = {
                        navController.navigate(AppScreens.LOGIN.title) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

