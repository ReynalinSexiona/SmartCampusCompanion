package com.example.smartcampus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcampus.ui.theme.CampusInfoScreen
import com.example.smartcampus.ui.theme.DashboardScreen
import com.example.smartcampus.ui.theme.LoginScreen

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
    }
}