package com.plavsic.taskly.domain.task.repository

import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.task.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TaskRepository {
    fun getTasks() : Flow<Response<List<Task>>>
    suspend fun addTask(task:Task)
    suspend fun updateTask(task: Task)
    suspend fun updateAlertSchedule(taskId:String,alert:LocalDateTime)
    suspend fun deleteTask(taskId: String)
}