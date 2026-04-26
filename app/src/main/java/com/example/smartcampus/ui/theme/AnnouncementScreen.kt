package com.example.smartcampus.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcampus.data.local.Announcement
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementScreen(onBackClick: () -> Unit, viewModel: AnnouncementViewModel = viewModel()) {
    val announcements by viewModel.announcements.collectAsState()
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    )
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Announcements", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshAnnouncements() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(announcements) { announcement ->
                    AnnouncementItem(
                        announcement = announcement,
                        onMarkAsRead = { viewModel.markAsRead(announcement) },
                        onMarkAsUnread = { viewModel.markAsUnread(announcement) }
                    )
                }
            }
        }
    }
}

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onMarkAsRead: () -> Unit,
    onMarkAsUnread: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (announcement.isRead) Color.White else Color(0xFFD3FFD5)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = announcement.title, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = announcement.message, color = Color.Black)
            Text(
                text = "Posted on: ${SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(Date(announcement.postDate))}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
            if (announcement.isRead) {
                Button(onClick = onMarkAsUnread) {
                    Text("Mark as Unread")
                }
            } else {
                Button(onClick = onMarkAsRead) {
                    Text("Mark as Read")
                }
            }
        }
    }
}