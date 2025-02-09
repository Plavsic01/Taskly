package com.plavsic.taskly.domain.task.model

import java.time.LocalDateTime

data class AlarmTask(
    val taskId:String,
    val title:String,
    val time:LocalDateTime?
) {
    override fun hashCode(): Int {
        return taskId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmTask

        if (taskId != other.taskId) return false
        if (title != other.title) return false
        if (time != other.time) return false

        return true
    }


}
