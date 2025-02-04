package com.plavsic.taskly.utils.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.plavsic.taskly.ui.shared.task.TaskPriority
import java.time.LocalDateTime

object GsonInstance {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(TaskPriority::class.java,TaskPriorityTypeAdapter())
        .registerTypeAdapter(LocalDateTime::class.java,LocalDateTimeAdapter())
        .create()
}