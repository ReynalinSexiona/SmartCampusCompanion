package com.example.smartcampus.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.AnnouncementRepository
import com.example.smartcampus.data.local.Announcement
import com.example.smartcampus.data.local.SmartCampusDatabase
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

    init {
        viewModelScope.launch {
            repository.deleteAll()
            addInitialAnnouncements()
        }
    }

    private suspend fun addInitialAnnouncements() {
        val calendar = Calendar.getInstance()

        // Announcement 1: Tomorrow at 8:00 AM
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        val announcement1Date = calendar.timeInMillis

        // Announcement 2: Today at 9:00 AM
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        val announcement2Date = calendar.timeInMillis

        // Announcement 3: Yesterday at 1:00 PM
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 13)
        calendar.set(Calendar.MINUTE, 0)
        val announcement3Date = calendar.timeInMillis

        val initialAnnouncements = listOf(
            Announcement(title = "University-Wide System Upgrade", message = "Please be advised that the university's online portal will be temporarily unavailable this Saturday from 2:00 AM to 6:00 AM for a scheduled system upgrade. We apologize for any inconvenience.", postDate = announcement1Date),
            Announcement(title = "Call for Volunteers: University Foundation Day", message = "Be part of our university's foundation day celebration! We are looking for student volunteers for various committees. Sign up at the student affairs office.", postDate = announcement2Date),
            Announcement(title = "Scholarship Application Period Now Open", message = "The application period for university scholarships for the next academic year is now open. The deadline for submissions is on October 30. Visit the scholarship office for more details.", postDate = announcement3Date, isRead = true)
        )
        repository.insertAll(initialAnnouncements)
    }

    private val refreshableAnnouncements = listOf(
        Announcement(
            title = "Attention: Final Examination Permit",
            message = "The final examination permits are now available for download from the student portal. Please secure your permit before the examination week.",
            postDate = System.currentTimeMillis()
        ),
        Announcement(
            title = "Intramurals 2024 Opening",
            message = "Join us for the opening ceremony of the University Intramurals 2024 this Monday, 8:00 AM, at the university grandstand.",
            postDate = System.currentTimeMillis()
        ),
        Announcement(
            title = "No Classes: Special Non-Working Holiday",
            message = "In observance of the upcoming national holiday, classes at all levels are suspended on Monday.",
            postDate = System.currentTimeMillis()
        ),
        Announcement(
            title = "Reminder: ID Validation",
            message = "All students are reminded to have their IDs validated for the current semester. Visit the registrar's office.",
            postDate = System.currentTimeMillis()
        )
    )

    fun refreshAnnouncements() {
        viewModelScope.launch {
            if (!hasRefreshed) {
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