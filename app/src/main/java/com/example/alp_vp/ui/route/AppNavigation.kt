package com.example.alp_vp.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.alp_vp.ui.view.Home.HomeView
import com.example.alp_vp.ui.view.LoginRegister.Register
import com.example.alp_vp.ui.view.Profile.ProfileView
import com.example.alp_vp.ui.viewmodel.DailyActivityViewModel
import com.example.alp_vp.ui.view.LoginRegister.LoginScreen

enum class AppScreens(
    val title: String,
    val icon: ImageVector? = null,
    val selectedIcon: ImageVector? = null
) {
    LOGIN("Login"),
    REGISTER("Register"),
    HOME("Home", Icons.Outlined.Home, Icons.Filled.Home),
    FRIENDS("Friends", Icons.Outlined.People, Icons.Filled.People),
    PROFILE("Profile", Icons.Outlined.Person, Icons.Filled.Person)
}

@Composable
fun MainBottomNavBar(
    navController: NavController,
    currentDestination: NavDestination?,
    items: List<AppScreens>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            shadowElevation = 12.dp,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.title } == true

                    BottomNavItem(
                        icon = if (isSelected) item.selectedIcon ?: item.icon else item.icon,
                        label = item.title,
                        selected = isSelected,
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
    }
}

@Composable
fun RowScope.BottomNavItem(
    icon: ImageVector?,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val primaryColor = Color(0xFF2A7DE1)  // Blue color matching app theme
    val unselectedColor = Color(0xFF9AA7B8)

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = label,
                tint = if (selected) primaryColor else unselectedColor,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = if (selected) primaryColor else unselectedColor,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
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

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier.fillMaxSize(),
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
                HomeView(
                    navController = navController,
                    dailyActivityViewModel = dailyActivityViewModel
                )
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

        // Bottom navigation bar on top of content
        if (showBottomBar) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
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
    }
}

