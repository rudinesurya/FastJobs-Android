package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.TypedEpoxyController
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.epoxyModelView.headerItem
import com.rud.fastjobs.view.epoxyModelView.jobItem
import java.time.LocalDate

class JobListEpoxyController(private val callbacks: AdapterCallbacks) : TypedEpoxyController<List<Job>>() {
    interface AdapterCallbacks {
        fun onItemClick(id: String)
    }

    var showHeader: Boolean = true

    override fun buildModels(data: List<Job>) {
        var dateState = LocalDate.MIN
        data.forEach {
            if (showHeader) {
                val ld = it.date?.toLocalDateTime()!!.toLocalDate()

                if (dateState != ld) // when the date is different, render a new header
                {
                    val dateStr = ld.dayOfWeek.toString() + ", " + ld.dayOfMonth + " " + ld.month

                    headerItem {
                        id(it.id)
                        headerTitle(dateStr)
                    }

                    dateState = ld // set the current data state to this date
                }
            }

            jobItem {
                id(it.id)
                job(it)
                onClick { model, modelview, _, _ -> callbacks.onItemClick(modelview.job.id!!) }
            }
        }
    }
}