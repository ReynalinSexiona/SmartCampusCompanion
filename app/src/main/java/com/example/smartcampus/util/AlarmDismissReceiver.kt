package com.example.smartcampus.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ACTION_DISMISS_ALARM") {
            val taskId = intent.getIntExtra("task_id", -1)

            val serviceIntent = Intent(context, AlarmService::class.java)
            context.stopService(serviceIntent)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (taskId != -1) {
                notificationManager.cancel(taskId)
            }
        }
    }
}
