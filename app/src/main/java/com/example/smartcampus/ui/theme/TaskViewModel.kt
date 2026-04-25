package com.example.smartcampus.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.local.SmartCampusDatabase
import com.example.smartcampus.data.local.TaskEntity
import com.example.smartcampus.data.TaskRepository
import com.example.smartcampus.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SmartCampusDatabase
        .getDatabase(application)
        .taskDao()

    private val repository = TaskRepository(dao)

    private val currentUserId = SessionManager.getUsername(application) ?: ""

    val tasks: StateFlow<List<TaskEntity>> = dao.getTasksByUser(currentUserId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addTask(title: String, description: String, date: Long) {
        viewModelScope.launch {
            repository.insert(
                TaskEntity(
                    userId = currentUserId,
                    title = title,
                    description = description,
                    date = date
                )
            )
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}
