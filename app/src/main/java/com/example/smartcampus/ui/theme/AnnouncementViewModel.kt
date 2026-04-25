package com.example.smartcampus.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.AnnouncementRepository
import com.example.smartcampus.data.local.Announcement
import com.example.smartcampus.data.local.SmartCampusDatabase
import com.example.smartcampus.util.SessionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class AnnouncementViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnnouncementRepository =
        AnnouncementRepository(SmartCampusDatabase.getDatabase(application).announcementDao())

    val announcements = repository.announcements.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    private var hasRefreshed = false

    fun isAdmin(): Boolean {
        return SessionManager.getUserRole(getApplication()) == "ADMIN"
    }

    fun addAnnouncement(title: String, message: String) {
        viewModelScope.launch {
            val newAnnouncement = Announcement(
                title = title,
                message = message,
                postDate = System.currentTimeMillis()
            )
            repository.insert(newAnnouncement)
            _toastMessage.emit("Announcement added successfully!")
        }
    }

    fun refreshAnnouncements() {
        viewModelScope.launch {
            if (!hasRefreshed) {
                val refreshableAnnouncements = listOf(
                    Announcement(
                        title = "Attention: Final Examination Permit",
                        message = "The final examination permits are now available for download from the student portal. Please secure your permit before the examination week.",
                        postDate = System.currentTimeMillis()
                    )
                )
                val currentTitles = announcements.value.map { it.title }
                val newAnnouncement = refreshableAnnouncements.firstOrNull { it.title !in currentTitles }

                if (newAnnouncement != null) {
                    repository.insert(newAnnouncement.copy(postDate = System.currentTimeMillis()))
                    hasRefreshed = true
                } else {
                    _toastMessage.emit("No more new announcements at this moment")
                }
            } else {
                _toastMessage.emit("No more new announcements at this moment")
            }
        }
    }

    fun markAsRead(announcement: Announcement) {
        viewModelScope.launch {
            repository.update(announcement.copy(isRead = true))
        }
    }

    fun markAsUnread(announcement: Announcement) {
        viewModelScope.launch {
            repository.update(announcement.copy(isRead = false))
        }
    }
}
