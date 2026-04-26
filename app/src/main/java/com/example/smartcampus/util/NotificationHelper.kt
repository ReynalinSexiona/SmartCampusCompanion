package com.example.smartcampus.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.smartcampus.MainActivity

object NotificationHelper {
    private const val CHANNEL_ID = "campus_notifications"
    private const val CHANNEL_NAME = "Campus Updates"
    const val ALARM_CHANNEL_ID = "task_alarms_v20" // Jumped version to ensure clean slate
    private const val ALARM_CHANNEL_NAME = "Task Alarms"

    fun showNotification(context: Context, title: String, message: String) {
        if (!SessionManager.areNotificationsEnabled(context)) {
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications for campus announcements"
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    fun createAlarmNotification(context: Context, title: String, message: String, taskId: Int): Notification {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val channel = NotificationChannel(
                ALARM_CHANNEL_ID,
                ALARM_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent task alarms"
                enableLights(true)
                enableVibration(true)
                setSound(alarmSound, audioAttributes)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setBypassDnd(true)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", "tasks")
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            taskId,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(context, AlarmDismissReceiver::class.java).apply {
            action = "ACTION_DISMISS_ALARM"
            putExtra("task_id", taskId)
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            taskId + 5000, // Very unique ID
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setSilent(false)
            .setVibrate(longArrayOf(0, 500, 500))
            .setSound(alarmSound)
            .setContentIntent(contentIntent)
            .setFullScreenIntent(contentIntent, true)
            .addAction(android.R.drawable.ic_delete, "TURN OFF ALARM", stopPendingIntent) // Label changed for clarity
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        return builder.build()
    }
}
