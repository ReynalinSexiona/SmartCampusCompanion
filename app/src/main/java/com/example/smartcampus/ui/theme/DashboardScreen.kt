package com.example.smartcampus.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartcampus.R

@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = viewModel()) {

    val context = LocalContext.current
    val weatherData by viewModel.weatherState.collectAsState()
    val isWeatherLoading by viewModel.isWeatherLoading.collectAsState()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 🔰 LOGO
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Campus Logo",
                modifier = Modifier.size(100.dp)
            )

            // HEADER
            Text(
                text = "Smart Campus",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🌦️ WEATHER CARD (RETROFIT IMPLEMENTATION)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x99FFFFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Cloud, contentDescription = null, tint = Color(0xFF1976D2))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Manila Weather", style = MaterialTheme.typography.labelMedium)
                            if (isWeatherLoading) {
                                LinearProgressIndicator(modifier = Modifier.width(50.dp))
                            } else {
                                Text(
                                    text = weatherData?.let { "${it.current.temperature}°C" } ?: "Tap to refresh",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    IconButton(onClick = { viewModel.fetchWeather() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Weather")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DESCRIPTION CARD
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Your central hub for campus activities. Stay connected!",
                    color = Color(0xFF2E7D32),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // MENU BUTTONS
            val buttonModifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            
            Button(onClick = { navController.navigate("campus") }, shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                Text("Campus Departments", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { navController.navigate("tasks") }, shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                Text("Task Manager", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { navController.navigate("announcements") }, shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                Text("Announcements", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { navController.navigate("settings") }, shape = RoundedCornerShape(12.dp), modifier = buttonModifier) {
                Text("Settings", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
