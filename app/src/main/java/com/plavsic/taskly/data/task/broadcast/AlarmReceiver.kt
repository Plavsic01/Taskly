package com.plavsic.taskly.data.task.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.plavsic.taskly.notification.TaskNotification

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("TITLE") ?: return
        context?.let {
            TaskNotification.showNotification(context = it, title = "Task: $title", description = "Reminder to do your task!")
        }
    }

}