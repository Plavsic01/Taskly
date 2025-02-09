package com.plavsic.taskly.data.task.repository

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.plavsic.taskly.data.task.broadcast.AlarmReceiver
import com.plavsic.taskly.domain.task.model.AlarmTask
import com.plavsic.taskly.domain.task.repository.AlarmScheduler
import java.time.ZoneId

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    override fun schedule(task: AlarmTask) {
        val intent = Intent(context,AlarmReceiver::class.java).apply {
            putExtra("TITLE",task.title)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            // times 1000 because it requires milliseconds
            task.time?.atZone(ZoneId.systemDefault())?.toEpochSecond()!!.times(1000),
            PendingIntent.getBroadcast(
                context,
                task.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(task: AlarmTask) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                task.hashCode(),
                Intent(context,AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}