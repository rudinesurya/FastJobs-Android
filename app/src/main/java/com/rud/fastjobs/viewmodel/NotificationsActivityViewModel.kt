package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rud.fastjobs.data.model.Notification
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.utils.lazyDeferred

class NotificationsActivityViewModel(
    private val myRepository: MyRepository,
    app: Application
) : AndroidViewModel(app) {
    val notifications by lazyDeferred {
        myRepository.getAllNotificationsLiveData()
    }

    suspend fun removeNotification(item: Notification) {
        myRepository.removeNotification(item.id)
    }

    suspend fun addNotification(item: Notification) {
        myRepository.addNotification(item)
    }
}