package com.example.smartcampus.data

import com.example.smartcampus.data.local.Announcement
import com.example.smartcampus.data.local.AnnouncementDao
import kotlinx.coroutines.flow.Flow

class AnnouncementRepository(private val announcementDao: AnnouncementDao) {

    val announcements: Flow<List<Announcement>> = announcementDao.getAllAnnouncements()

    suspend fun getCount(): Int {
        return announcementDao.getCount()
    }

    suspend fun insert(announcement: Announcement) {
        announcementDao.insert(announcement)
    }

    suspend fun insertAll(announcements: List<Announcement>) {
        announcementDao.insertAll(announcements)
    }

    suspend fun update(announcement: Announcement) {
        announcementDao.update(announcement)
    }

    suspend fun deleteAll() {
        announcementDao.deleteAll()
    }
}
