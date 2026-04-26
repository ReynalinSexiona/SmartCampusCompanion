package com.example.smartcampus.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "SmartCampus:AlarmWakeLock"
        )
        
        // Gisingin ang CPU nang pansamantala (max 10 seconds)
        wakeLock.acquire(10 * 1000L)

        val title = intent.getStringExtra("task_title") ?: "Task Reminder"
        val desc = intent.getStringExtra("task_desc") ?: "You have a task to do!"
        val taskId = intent.getIntExtra("task_id", 0)

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("task_title", title)
            putExtra("task_desc", desc)
            putExtra("task_id", taskId)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}
