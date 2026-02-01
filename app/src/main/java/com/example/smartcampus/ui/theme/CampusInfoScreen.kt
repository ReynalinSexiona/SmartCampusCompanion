package com.example.smartcampus.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcampus.data.CampusRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusInfoScreen(
    onBackClick: () -> Unit
) {
    val departments = CampusRepository.getDepartments()

    // Gradient background
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Campus Departments",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF388E3C)
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(departments) { dept ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xCCFFFFFF)
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.85f) // centered & smaller
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Department header
                            Text(
                                text = "Department: ${dept.name}",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )

                            // Department info
                            Column(
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                Text("Contact: ${dept.contact}", style = MaterialTheme.typography.bodyMedium)
                                Text("Email: ${dept.email ?: "Not Provided"}", style = MaterialTheme.typography.bodyMedium)
                                Text("Phone: ${dept.phone ?: "(123) 456-7890"}", style = MaterialTheme.typography.bodyMedium)

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    "Office Hours: ${dept.officeHours ?: "Mon–Fri, 8:00 AM – 5:00 PM"}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                if (!dept.faculty.isNullOrEmpty()) {
                                    Text(
                                        "Faculty:",
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Column(modifier = Modifier.padding(start = 12.dp, top = 4.dp)) {
                                        dept.faculty.forEach { prof ->
                                            Text("- $prof", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                if (!dept.courses.isNullOrEmpty()) {
                                    Text(
                                        "Courses Offered:",
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Column(modifier = Modifier.padding(start = 12.dp, top = 4.dp)) {
                                        dept.courses.forEach { course ->
                                            Text("- $course", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}