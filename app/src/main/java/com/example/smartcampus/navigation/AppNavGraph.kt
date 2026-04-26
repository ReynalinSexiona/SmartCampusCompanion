package com.example.smartcampus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcampus.ui.theme.AnnouncementScreen
import com.example.smartcampus.ui.theme.CampusInfoScreen
import com.example.smartcampus.ui.theme.DashboardScreen
import com.example.smartcampus.ui.theme.LoginScreen
import com.example.smartcampus.ui.theme.SettingsScreen
import com.example.smartcampus.ui.theme.TaskScreen
import com.example.smartcampus.util.SessionManager

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    startDestination: String? = null
) {
    val context = LocalContext.current
    val effectiveStartDestination = startDestination ?: if (SessionManager.isLoggedIn(context)) "dashboard" else "login"

    NavHost(
        navController = navController,
        startDestination = effectiveStartDestination
    ) {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("campus") { CampusInfoScreen(onBackClick = { navController.popBackStack() }) }
        composable("tasks") { TaskScreen(onBackClick = { navController.popBackStack() }) }
        composable("announcements") { AnnouncementScreen(onBackClick = { navController.popBackStack() }) }
        composable("settings") {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    SessionManager.clearSession(context)
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
