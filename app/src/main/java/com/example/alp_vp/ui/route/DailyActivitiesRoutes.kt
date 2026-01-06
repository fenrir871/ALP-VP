package com.example.alp_vp.ui.route

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.alp_vp.ui.view.Home.HomeView
import com.example.alp_vp.ui.viewmodel.DailyActivityViewModel

enum class DailyScreens(val title: String, val icon: ImageVector? = null) {
    HOME("Home", Icons.Filled.Home),
    WATER("Water", Icons.Filled.LocalDrink),
    FOOD("Food", Icons.Filled.Fastfood),
    STEPS("Steps", Icons.Filled.DirectionsRun)
}

@Composable
fun BottomNavBar(
    navController: NavController,
    currentDestination: NavDestination?,
    items: List<DailyScreens>
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { item.icon?.let { Icon(it, contentDescription = item.title) } ?: Spacer(Modifier) },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.title } == true,
                onClick = {
                    navController.navigate(item.title) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
fun DailyActivitiesRoutes() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Initialize the container
    val container = DailyActivityContainer()

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

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentDestination = currentDestination,
                items = listOf(
                    DailyScreens.HOME,
                    DailyScreens.WATER,
                    DailyScreens.FOOD,
                    DailyScreens.STEPS
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = DailyScreens.HOME.title
        ) {
            composable(DailyScreens.HOME.title) {
                HomeView(
                    navController = navController,
                    dailyActivityViewModel = dailyActivityViewModel
                )
            }
            composable(DailyScreens.WATER.title) {
                Text("Water Screen")
            }
            composable(DailyScreens.FOOD.title) {
                Text("Food Screen")
            }
            composable(DailyScreens.STEPS.title) {
                Text("Steps Screen")
            }
        }
    }
}
