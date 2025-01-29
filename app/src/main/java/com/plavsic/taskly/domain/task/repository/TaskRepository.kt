package com.plavsic.taskly.domain.task.repository

import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.task.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks() : Flow<Response<List<Task>>>
    suspend fun addTask(task:Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
}