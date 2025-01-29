package com.plavsic.taskly.domain.task.model

import android.os.Parcelable
import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.ui.shared.task.TaskPriority
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

// Default values are for Firestore because if there are no documents he has to put default values
@Parcelize
data class Task(
    val taskId:String = "",
    val title:String = "",
    val description:String = "",
    val date: LocalDate? = null,
    val priority: TaskPriority? = null,
    val category: Category? = null,
    val isCompleted:Boolean = false,
) : Parcelable