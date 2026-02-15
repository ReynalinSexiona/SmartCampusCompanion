package com.example.smartcampus.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.smartcampus.R

@Composable
fun DashboardScreen(navController: NavController) {

    val context = LocalContext.current

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔰 LOGO
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Campus Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 20.dp)
            )

            // HEADER
            Text(
                text = "Welcome to Smart Campus!",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // DESCRIPTION CARD
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xCCFFFFFF)
                ),
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your central hub for campus activities, classes, events, and notifications. Stay connected and informed!",
                        color = Color(0xFF2E7D32),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ✅ Campus Departments
            Button(
                onClick = { navController.navigate("campus") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Campus Departments",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Task & Schedule Manager
            Button(
                onClick = { navController.navigate("tasks") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Task and Schedule Manager",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ Campus Announcements
            Button(
                onClick = { navController.navigate("announcements") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Campus Announcements",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ⚙️ Settings
            Button(
                onClick = { navController.navigate("settings") },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Settings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
