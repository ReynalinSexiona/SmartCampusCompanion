package com.example.smartcampus.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.example.smartcampus.data.local.TaskDao
import com.example.smartcampus.data.local.TaskEntity
import com.example.smartcampus.util.AlarmReceiver

class TaskRepository(
    private val dao: TaskDao,
    private val context: Context
) {

    val tasks = dao.getAllTasks()

    suspend fun insert(task: TaskEntity) {
        val id = dao.insert(task)
        setTaskAlarm(task.copy(id = id.toInt()))
    }

    suspend fun update(task: TaskEntity) {
        dao.update(task)
        setTaskAlarm(task)
    }

    suspend fun delete(task: TaskEntity) {
        dao.delete(task)
        cancelAlarm(task)
    }

    private fun setTaskAlarm(task: TaskEntity) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Essential for Android 12+ to allow exact alarms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
                // We can't set it yet, but the user will be prompted
                return
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("task_title", task.title)
            putExtra("task_desc", task.description)
            putExtra("task_id", task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (task.date > System.currentTimeMillis()) {
            // Using setAlarmClock is the most reliable way to wake up the device exactly on time
            val alarmClockInfo = AlarmManager.AlarmClockInfo(task.date, pendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
        }
    }

    private fun cancelAlarm(task: TaskEntity) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
