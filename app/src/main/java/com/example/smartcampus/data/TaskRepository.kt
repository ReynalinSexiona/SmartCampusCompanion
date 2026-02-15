package com.example.smartcampus.data

import com.example.smartcampus.data.local.TaskDao
import com.example.smartcampus.data.local.TaskEntity

class TaskRepository(private val dao: TaskDao) {

    val tasks = dao.getAllTasks()

    suspend fun insert(task: TaskEntity) = dao.insert(task)

    suspend fun update(task: TaskEntity) = dao.update(task)

    suspend fun delete(task: TaskEntity) = dao.delete(task)
}
