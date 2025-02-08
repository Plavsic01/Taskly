package com.plavsic.taskly.notification

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.plavsic.taskly.R
import com.plavsic.taskly.TasklyApplication


object TaskNotification {

    private const val PREFS_NAME = "app_preferences"
    private const val KEY_NOTIFICATION_DENIED ="notification_denied"


    fun hasNotificationPermission(context: Context) : Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }else{
            true
        }
    }

    fun isPermissionDeniedBefore(context: Context) : Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_NOTIFICATION_DENIED,false)
    }

    fun setPermissionDenied(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_NOTIFICATION_DENIED,true).apply()
    }


    fun showNotification(context: Context) {
        val notification = NotificationCompat.Builder(context, TasklyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hello world")
            .setContentText("This is a description")
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1,notification)
    }
}





