package com.example.smartcampus.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build

class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    companion object {
        private const val CHANNEL_ID = "alarm_service_channel_v2"
        const val ACTION_STOP_ALARM = "com.example.smartcampus.ACTION_STOP_ALARM"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_ALARM) {
            stopForeground(true)
            stopSelf()
            return START_NOT_STICKY
        }

        val title = intent?.getStringExtra("task_title") ?: "Task Reminder"
        val desc = intent?.getStringExtra("task_desc") ?: "You have a task to do!"
        val taskId = intent?.getIntExtra("task_id", 1001) ?: 1001

        val notification = NotificationHelper.createAlarmNotification(this, title, desc, taskId)

        // Match the foregroundServiceType with the manifest (mediaPlayback)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(taskId, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(taskId, notification)
        }

        startAlarm()

        return START_STICKY
    }

    private fun startAlarm() {
        if (mediaPlayer == null) {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, alarmUri)
                isLooping = true
                prepare()
                start()
            }
        }

        if (vibrator == null) {
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 1000, 1000), 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 1000, 1000), 0)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator?.cancel()
        vibrator = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
