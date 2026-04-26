package com.example.smartcampus.util

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Pag may dumating na message galing sa Firebase Console
        remoteMessage.notification?.let {
            NotificationHelper.showNotification(
                applicationContext,
                it.title ?: "New Announcement",
                it.body ?: "Check the app for updates!"
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Dito nase-save ang device token kung kailangan nating magpadala sa specific device
    }
}
