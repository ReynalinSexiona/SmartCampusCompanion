package com.example.smartcampus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firestoreId: String? = null,
    val title: String,
    val message: String,
    val postDate: Long,
    val isRead: Boolean = false,
    val isDeletedLocally: Boolean = false // Para hindi bumalik pag nag-sync ang student
)
