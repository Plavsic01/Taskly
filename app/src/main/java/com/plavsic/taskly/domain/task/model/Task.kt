package com.plavsic.taskly.domain.task.model

import android.os.Parcelable
import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.ui.shared.task.TaskPriority
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

// Default values are for Firestore because if there are no documents he has to put default values
@Parcelize
data class Task(
    val taskId:String = "",
    val title:String = "",
    val description:String = "",
    val date: LocalDateTime? = null,
    val alert:LocalDateTime? = null,
    val priority: TaskPriority? = null,
    val category: Category? = null,
    val isCompleted:Boolean = false,
) : Parcelable {

    override fun hashCode(): Int {
        return taskId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (taskId != other.taskId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (date != other.date) return false
        if (alert != other.alert) return false
        if (priority != other.priority) return false
        if (category != other.category) return false
        if (isCompleted != other.isCompleted) return false

        return true
    }


}