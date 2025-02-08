package com.plavsic.taskly.domain.task.model

import java.time.LocalDateTime

data class AlarmTask(
    val taskId:String,
    val time:LocalDateTime?,
    val message:String
) {
    override fun hashCode(): Int {
        return taskId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmTask

        if (taskId != other.taskId) return false
        if (time != other.time) return false
        if (message != other.message) return false

        return true
    }


}
