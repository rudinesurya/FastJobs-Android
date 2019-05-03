package com.rud.fastjobs.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rud.fastjobs.data.model.Notification

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(coffee: Notification): Long

    @Query("SELECT * FROM notifications")
    fun getNotifications(): LiveData<List<Notification>>

    @Query("SELECT * FROM notifications where id = :id")
    fun getCoffee(id: Long): LiveData<Notification>

    @Query("DELETE FROM notifications WHERE id = :id")
    fun remove(id: Long)
}