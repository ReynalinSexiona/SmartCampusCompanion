package com.example.smartcampus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcements ORDER BY postDate DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(announcement: Announcement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(announcements: List<Announcement>)

    @Update
    suspend fun update(announcement: Announcement)

    @Query("SELECT COUNT(*) FROM announcements")
    suspend fun getCount(): Int

    @Query("DELETE FROM announcements")
    suspend fun deleteAll()
}
