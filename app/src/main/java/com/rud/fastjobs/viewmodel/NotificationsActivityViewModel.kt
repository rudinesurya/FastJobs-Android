package com.rud.fastjobs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rud.fastjobs.data.model.Notification
import com.rud.fastjobs.data.repository.MyRepository

class NotificationsActivityViewModel(
    private val myRepository: MyRepository, app: Application
) : AndroidViewModel(app) {
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>?>
        get() = _notifications

    val datasrc = mutableListOf<Notification>()

    init {
        for (i in 0 until 10) {
            datasrc.add(
                Notification(
                    title = "Hello",
                    message = "Hello World $i",
                    // postDate = Timestamp.now(),
                    id = i.toLong()
                )
            )
        }
        _notifications.postValue(datasrc)
    }

    fun removeNotification(item: Notification) {
        datasrc.remove(item)
        _notifications.postValue(datasrc)
    }

    fun addNotification(item: Notification) {
        datasrc.add(item)
        _notifications.postValue(datasrc)
    }
}