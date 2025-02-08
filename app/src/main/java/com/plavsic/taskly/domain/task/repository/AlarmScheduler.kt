package com.plavsic.taskly.domain.task.repository

import com.plavsic.taskly.domain.task.model.AlarmTask

interface AlarmScheduler {
    fun schedule(task:AlarmTask)
    fun cancel(task:AlarmTask)
}