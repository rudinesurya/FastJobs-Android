package com.rud.fastjobs.view.recyclerView

import com.airbnb.epoxy.TypedEpoxyController
import com.rud.fastjobs.data.model.Job


class JobListController(private val callbacks: AdapterCallbacks) : TypedEpoxyController<List<Job>>() {
    interface AdapterCallbacks {
        fun onItemClick(id: String)
    }

    var filter: String = ""

    override fun buildModels(data: List<Job>?) {
        data?.filter { it.title.toUpperCase().contains(filter.toUpperCase()) }?.forEach {
            jobItem {
                id(it.id)
                job(it)
                onClick { model, modelview, _, _ -> callbacks.onItemClick(modelview.job.id!!) }
            }
        }
    }
}