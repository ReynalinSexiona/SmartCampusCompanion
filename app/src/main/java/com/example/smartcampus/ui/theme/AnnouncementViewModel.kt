package com.example.smartcampus.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.AnnouncementRepository
import com.example.smartcampus.data.local.Announcement
import com.example.smartcampus.data.local.SmartCampusDatabase
import com.example.smartcampus.util.NotificationHelper
import com.example.smartcampus.util.SessionManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnnouncementViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnnouncementRepository =
        AnnouncementRepository(
            SmartCampusDatabase.getDatabase(application).announcementDao(),
            application
        )

    val announcements = if (isAdmin()) {
        repository.announcements
    } else {
        repository.visibleAnnouncements
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    init {
        // Simulan ang real-time sync para lumabas ang banner pag may bagong announcement
        repository.startRealtimeSync(viewModelScope)
    }

    fun isAdmin(): Boolean {
        return SessionManager.getUserRole(getApplication()) == "ADMIN"
    }

    fun addAnnouncement(title: String, message: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val newAnnouncement = Announcement(
                title = title,
                message = message,
                postDate = System.currentTimeMillis()
            )
            repository.insert(newAnnouncement)
            _isLoading.value = false
            _toastMessage.emit("Announcement added successfully!")
        }
    }

    fun refreshAnnouncements() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.syncWithFirebase()
            _isLoading.value = false
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

    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            val adminStatus = isAdmin()
            repository.deleteAnnouncement(announcement, adminStatus)

            if (adminStatus) {
                _toastMessage.emit("Deleted globally by Admin")
            } else {
                _toastMessage.emit("Hidden locally for you")
            }
        }
    }
}