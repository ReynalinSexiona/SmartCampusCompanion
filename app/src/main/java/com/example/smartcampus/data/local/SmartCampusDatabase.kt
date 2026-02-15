package com.example.smartcampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class, Announcement::class], version = 14, exportSchema = false)
abstract class SmartCampusDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun announcementDao(): AnnouncementDao

    companion object {
        @Volatile
        private var INSTANCE: SmartCampusDatabase? = null

        fun getDatabase(context: Context): SmartCampusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartCampusDatabase::class.java,
                    "smart_campus_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
