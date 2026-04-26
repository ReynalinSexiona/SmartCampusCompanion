package com.example.smartcampus.ui.theme

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcampus.data.local.Announcement
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementScreen(onBackClick: () -> Unit, viewModel: AnnouncementViewModel = viewModel()) {
    val announcements by viewModel.announcements.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    )
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var announcementToDelete by remember { mutableStateOf<Announcement?>(null) }

    val isAdmin = remember { viewModel.isAdmin() }

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
                    if (isAdmin) {
                        IconButton(onClick = { showAddDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Announcement", tint = Color.Black)
                        }
                    }
                    IconButton(onClick = { viewModel.refreshAnnouncements() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Loading updates...", color = Color.White)
                }
            }

            if (!isLoading) {
                if (announcements.isEmpty()) {
                    EmptyAnnouncementState()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(announcements) { announcement ->
                            AnnouncementItem(
                                announcement = announcement,
                                onMarkAsRead = { viewModel.markAsRead(announcement) },
                                onMarkAsUnread = { viewModel.markAsUnread(announcement) },
                                onDelete = { announcementToDelete = announcement }
                            )
                        }
                    }
                }
            }
        }

        // Add Confirmation Dialog
        if (showAddDialog) {
            AddAnnouncementDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { title, message ->
                    viewModel.addAnnouncement(title, message)
                    showAddDialog = false
                }
            )
        }

        // Delete Confirmation Dialog
        announcementToDelete?.let { announcement ->
            AlertDialog(
                onDismissRequest = { announcementToDelete = null },
                icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                title = { Text("Delete Announcement") },
                text = { Text("Are you sure you want to delete this announcement? This action cannot be undone.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteAnnouncement(announcement)
                            announcementToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { announcementToDelete = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun EmptyAnnouncementState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Announcements Yet",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Stay tuned for campus updates!",
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun AddAnnouncementDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showConfirmAdd by remember { mutableStateOf(false) }

    if (!showConfirmAdd) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("New Announcement") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { if (title.isNotBlank() && message.isNotBlank()) showConfirmAdd = true },
                    enabled = title.isNotBlank() && message.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = { showConfirmAdd = false },
            title = { Text("Confirm Post") },
            text = { Text("Do you want to post this announcement globally to all students?") },
            confirmButton = {
                Button(onClick = { onAdd(title, message) }) {
                    Text("Post Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmAdd = false }) {
                    Text("Back")
                }
            }
        )
    }
}

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onMarkAsRead: () -> Unit,
    onMarkAsUnread: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (announcement.isRead) Color.White else Color(0xFFE8F5E9)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = announcement.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(Date(announcement.postDate)),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFD32F2F))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = announcement.message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = if (announcement.isRead) onMarkAsUnread else onMarkAsRead
                ) {
                    Text(if (announcement.isRead) "Mark as Unread" else "Mark as Read")
                }
            }
        }
    }
}