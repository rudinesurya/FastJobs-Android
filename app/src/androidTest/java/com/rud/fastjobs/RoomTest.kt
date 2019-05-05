package com.rud.fastjobs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.rud.fastjobs.data.db.NotificationDao
import com.rud.fastjobs.data.db.NotificationsDB
import com.rud.fastjobs.data.model.Notification
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RoomTest {
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()
    private lateinit var database: NotificationsDB
    private lateinit var dao: NotificationDao

    @Before
    fun initDb() {
        val context = InstrumentationRegistry.getContext()
        database = Room.inMemoryDatabaseBuilder(
            context, NotificationsDB::class.java
        ).allowMainThreadQueries().build()
        dao = database.notificationDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun createAndRead() {
        val notif = Notification(title = "title 123", message = "message 123")
        val id = runBlocking {
            dao.upsert(notif)
        }
        notif.id = id

        val returnedNotif = dao.getNotification(id).blockingObserve()
        assertEquals(notif, returnedNotif)
    }

    @Test
    fun createAndReadAll() {
        val notif = Notification(title = "title 123", message = "message 123")
        runBlocking {
            dao.upsert(notif)
            dao.upsert(notif)
        }

        val allNotifs = dao.getNotifications().blockingObserve()
        assertEquals(2, allNotifs?.count())
    }

    @Test
    fun createAndDelete() {
        val notif = Notification(title = "title 123", message = "message 123")
        val id = runBlocking {
            dao.upsert(notif)
        }

        var allNotifs = dao.getNotifications().blockingObserve()
        assertEquals(1, allNotifs?.count())

        runBlocking {
            dao.remove(id)
        }

        allNotifs = dao.getNotifications().blockingObserve()
        assertEquals(0, allNotifs?.count())
    }
}

fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)

    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }

    observeForever(observer)

    latch.await(2, TimeUnit.SECONDS)
    return value
}