package com.example.smartcampus.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.local.SmartCampusDatabase
import com.example.smartcampus.data.local.TaskEntity
import com.example.smartcampus.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SmartCampusDatabase
        .getDatabase(application)
        .taskDao()

    private val repository = TaskRepository(dao)

    val tasks = repository.tasks.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTask(title: String, description: String, date: Long) {
        viewModelScope.launch {
            repository.insert(
                TaskEntity(
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
