package com.example.smartcampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.smartcampus.ui.theme.SmartCampusTheme
import com.example.smartcampus.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCampusTheme {
                AppNavGraph()
            }
        }
    }
}
