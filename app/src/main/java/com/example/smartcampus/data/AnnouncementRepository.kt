package com.example.smartcampus.data

import android.content.Context
import com.example.smartcampus.data.local.Announcement
import com.example.smartcampus.data.local.AnnouncementDao
import com.example.smartcampus.util.NotificationHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AnnouncementRepository(private val announcementDao: AnnouncementDao, private val context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val announcementsCollection = firestore.collection("announcements")

    val announcements: Flow<List<Announcement>> = announcementDao.getAllAnnouncements()
    val visibleAnnouncements: Flow<List<Announcement>> = announcementDao.getAllVisibleAnnouncements()

    // Real-time listener: Ito ang magti-trigger ng banner/notification sa ibang device
    fun startRealtimeSync(scope: CoroutineScope) {
        announcementsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            
            snapshot?.let { querySnapshot ->
                scope.launch(Dispatchers.IO) {
                    val locallyDeletedIds = announcementDao.getLocallyDeletedIds()
                    
                    for (change in querySnapshot.documentChanges) {
                        val doc = change.document
                        val firestoreId = doc.id
                        
                        if (change.type == DocumentChange.Type.ADDED) {
                            // 🔔 MAGPAPALABAS NG BANNER/NOTIFICATION PAG MAY BAGO
                            val title = doc.getString("title") ?: "New Announcement"
                            val message = doc.getString("message") ?: ""
                            
                            // I-check kung wala pa ito sa local para hindi mag-duplicate ang notification
                            val currentCount = announcementDao.getCount() // Optional check
                            
                            NotificationHelper.showNotification(context, title, message)
                        }
                    }

                    // I-sync ang buong listahan sa local database
                    val remoteAnnouncements = querySnapshot.documents.mapNotNull { doc ->
                        val firestoreId = doc.id
                        Announcement(
                            firestoreId = firestoreId,
                            title = doc.getString("title") ?: "",
                            message = doc.getString("message") ?: "",
                            postDate = doc.getLong("postDate") ?: System.currentTimeMillis(),
                            isRead = false,
                            isDeletedLocally = locallyDeletedIds.contains(firestoreId)
                        )
                    }
                    
                    announcementDao.deleteAll()
                    announcementDao.insertAll(remoteAnnouncements)
                }
            }
        }
    }

    suspend fun insert(announcement: Announcement) {
        try {
            val data = mapOf(
                "title" to announcement.title,
                "message" to announcement.message,
                "postDate" to announcement.postDate
            )
            announcementsCollection.add(data).await()
        } catch (e: Exception) {
            e.printStackTrace()
            announcementDao.insert(announcement)
        }
    }

    suspend fun update(announcement: Announcement) {
        announcementDao.update(announcement)
    }

    suspend fun deleteAnnouncement(announcement: Announcement, isAdmin: Boolean) {
        if (isAdmin) {
            try {
                announcement.firestoreId?.let { id ->
                    announcementsCollection.document(id).delete().await()
                }
                announcementDao.delete(announcement)
            } catch (e: Exception) {
                e.printStackTrace()
                announcementDao.delete(announcement)
            }
        } else {
            announcementDao.update(announcement.copy(isDeletedLocally = true))
        }
    }

    // Para sa manual sync kung kinakailangan
    suspend fun syncWithFirebase() {
        try {
            val snapshot = announcementsCollection.get().await()
            val locallyDeletedIds = announcementDao.getLocallyDeletedIds()
            val remoteAnnouncements = snapshot.documents.mapNotNull { doc ->
                val firestoreId = doc.id
                Announcement(
                    firestoreId = firestoreId,
                    title = doc.getString("title") ?: "",
                    message = doc.getString("message") ?: "",
                    postDate = doc.getLong("postDate") ?: System.currentTimeMillis(),
                    isRead = false,
                    isDeletedLocally = locallyDeletedIds.contains(firestoreId)
                )
            }
            announcementDao.deleteAll()
            announcementDao.insertAll(remoteAnnouncements)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
