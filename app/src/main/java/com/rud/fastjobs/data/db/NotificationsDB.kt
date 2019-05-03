package com.rud.fastjobs.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rud.fastjobs.data.model.Notification

@Database(
    entities = [Notification::class],
    version = 1
)
abstract class NotificationsDB : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var instance: NotificationsDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NotificationsDB::class.java, "fjnotifications.db"
        ).build()
    }
}