package com.rud.fastjobs.view.recyclerView

import com.airbnb.epoxy.TypedEpoxyController
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.utils.toLocalDateTime
import java.time.LocalDate


class JobListController(private val callbacks: AdapterCallbacks) : TypedEpoxyController<List<Job>>() {
    interface AdapterCallbacks {
        fun onItemClick(id: String)
    }


    override fun buildModels(data: List<Job>?) {
        var dateState = LocalDate.MIN
        data?.sortedBy { it.date }?.forEach {
            val ld = it.date?.toLocalDateTime()!!.toLocalDate()

            if (dateState != ld) //when the date is different, render a new header
            {
                val dateStr = ld.dayOfWeek.toString() + ", " + ld.dayOfMonth + " " + ld.month

                headerItem {
                    id(it.id)
                    headerTitle(dateStr)
                }

                dateState = ld //set the current data state to this date
            }

            jobItem {
                id(it.id)
                job(it)
                onClick { model, modelview, _, _ -> callbacks.onItemClick(modelview.job.id!!) }
            }
        }
    }
}