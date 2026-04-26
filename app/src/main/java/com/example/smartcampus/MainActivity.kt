package com.example.smartcampus

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import com.example.smartcampus.ui.theme.SmartCampusTheme
import com.example.smartcampus.navigation.AppNavGraph
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.compose.rememberNavController
import com.example.smartcampus.util.SessionManager

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            val systemInDarkTheme = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }
            val navController = rememberNavController()
            
            // Check for navigation from notification
            LaunchedEffect(intent) {
                handleIntent(intent, { destination ->
                    navController.navigate(destination)
                })
            }

            SmartCampusTheme(darkTheme = isDarkTheme) {
                AppNavGraph(
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun handleIntent(intent: Intent?, navigate: (String) -> Unit) {
        intent?.getStringExtra("navigate_to")?.let { destination ->
            if (SessionManager.isLoggedIn(this)) {
                navigate(destination)
            }
        }
    }
}
