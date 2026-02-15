package com.example.smartcampus.ui.theme

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcampus.data.local.TaskEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskScreen(onBackClick: () -> Unit, viewModel: TaskViewModel = viewModel()) {

    val tasks by viewModel.tasks.collectAsState()
    val context = LocalContext.current

    var showEditDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskEntity?>(null) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Long?>(null) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task & Schedule Manager") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                DueDateSelector(dueDate = dueDate, onDueDateChange = { dueDate = it })

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                viewModel.addTask(title, description, dueDate ?: System.currentTimeMillis())
                                title = ""
                                description = ""
                                dueDate = null
                            }
                        },
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Add Task", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            title = ""
                            description = ""
                            dueDate = null
                        },
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Clear", fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onEdit = {
                                taskToEdit = task
                                showEditDialog = true
                            },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }
    }

    if (showEditDialog && taskToEdit != null) {
        EditTaskDialog(
            task = taskToEdit!!,
            onDismiss = { showEditDialog = false },
            onSave = {
                viewModel.updateTask(it)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun DueDateSelector(dueDate: Long?, onDueDateChange: (Long?) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    if (dueDate != null) {
        calendar.timeInMillis = dueDate
    }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            onDueDateChange(calendar.timeInMillis)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            timePickerDialog.show() // Show time picker after date is set
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.DateRange, contentDescription = "Due Date")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (dueDate != null) {
                "Due: ${dateFormat.format(Date(dueDate))} at ${timeFormat.format(Date(dueDate))}"
            } else {
                "Set Due Date"
            },
            style = TextStyle(color = Color.Black)
        )
    }
}

@Composable
fun EditTaskDialog(task: TaskEntity, onDismiss: () -> Unit, onSave: (TaskEntity) -> Unit) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var dueDate by remember { mutableStateOf(task.date) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task", color = Color.Black) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                DueDateSelector(dueDate = dueDate, onDueDateChange = { dueDate = it ?: task.date })
            }
        },
        confirmButton = {
            Button(onClick = { onSave(task.copy(title = title, description = description, date = dueDate)) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun TaskItem(task: TaskEntity, onEdit: () -> Unit, onDelete: () -> Unit) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium.copy(color = Color.Black), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = task.description, style = TextStyle(color = Color.Black))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Due: ${SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(Date(task.date))}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Black))
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onEdit) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showDeleteConfirmDialog = true }) {
                    Text("Delete")
                }
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirmDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}