package com.example.smartcampus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcampus.ui.theme.AnnouncementScreen
import com.example.smartcampus.ui.theme.CampusInfoScreen
import com.example.smartcampus.ui.theme.DashboardScreen
import com.example.smartcampus.ui.theme.LoginScreen
import com.example.smartcampus.ui.theme.SettingsScreen
import com.example.smartcampus.ui.theme.TaskScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("campus") { CampusInfoScreen(onBackClick = { navController.popBackStack() }) }
        composable("tasks") { TaskScreen(onBackClick = { navController.popBackStack() }) }
        composable("announcements") { AnnouncementScreen(onBackClick = { navController.popBackStack() }) }
        composable("settings") {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}
