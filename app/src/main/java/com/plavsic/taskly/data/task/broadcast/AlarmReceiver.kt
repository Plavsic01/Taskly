package com.plavsic.taskly.data.task.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.plavsic.taskly.notification.TaskNotification

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MESSAGE") ?: return
        Log.i("TRIGGERED_ALARM",message)
        context?.let {
            TaskNotification.showNotification(it)
        }
    }

}