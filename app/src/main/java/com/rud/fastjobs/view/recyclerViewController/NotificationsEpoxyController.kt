package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.TypedEpoxyController
import com.rud.fastjobs.data.model.Notification
import com.rud.fastjobs.view.epoxyModelView.notificationItem

class NotificationsEpoxyController(private val callbacks: AdapterCallbacks) :
    TypedEpoxyController<List<Notification>>() {
    interface AdapterCallbacks {
        fun onItemClick(id: String)
    }

    override fun buildModels(data: List<Notification>) {
        setFilterDuplicates(true)
        data.sortedBy { it.id }.forEach {
            notificationItem {
                id(it.id)
                notification(it)
            }
        }
    }
}