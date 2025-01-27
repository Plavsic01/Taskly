package com.plavsic.taskly.utils.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.plavsic.taskly.ui.shared.task.TaskPriority
import java.time.LocalDate

object GsonInstance {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(TaskPriority::class.java,TaskPriorityTypeAdapter())
        .registerTypeAdapter(LocalDate::class.java,LocalDateAdapter())
        .create()
}