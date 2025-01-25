package com.plavsic.taskly.domain.task.model

import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.ui.shared.task.TaskPriority
import java.time.LocalDate

// Default values are for Firestore because if there are no documents he has to put default values
data class Task(
    val title:String = "",
    val description:String = "",
    val date: LocalDate? = null,
    val priority: TaskPriority? = null,
    val category: Category? = null,
    val isCompleted:Boolean = false
)