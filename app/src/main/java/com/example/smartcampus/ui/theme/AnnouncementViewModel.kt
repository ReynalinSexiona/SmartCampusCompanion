package com.example.smartcampus.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.local.Announcement
import com.example.smartcampus.data.local.SmartCampusDatabase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnnouncementViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = SmartCampusDatabase
        .getDatabase(application)
        .announcementDao()

    val announcements = dao.getAllAnnouncements().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    fun refreshAnnouncements() {
        viewModelScope.launch {
            // Simulated refresh - in a real app, this would fetch from a network API
            _toastMessage.emit("Announcements refreshed")
        }
    }

    fun markAsRead(announcement: Announcement) {
        viewModelScope.launch {
            dao.update(announcement.copy(isRead = true))
        }
    }

    fun markAsUnread(announcement: Announcement) {
        viewModelScope.launch {
            dao.update(announcement.copy(isRead = false))
        }
    }
}
