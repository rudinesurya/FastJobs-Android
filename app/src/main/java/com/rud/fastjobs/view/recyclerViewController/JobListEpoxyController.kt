package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.Typed2EpoxyController
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.epoxyModelView.headerItem
import com.rud.fastjobs.view.epoxyModelView.jobItem
import java.time.LocalDate

class JobListEpoxyController(private val callbacks: AdapterCallbacks) : Typed2EpoxyController<List<Job>, User>() {
    interface AdapterCallbacks {
        fun onItemClick(id: String)
        fun onFavChecked(id: String)
    }

    var showHeader: Boolean = true

    override fun buildModels(jobs: List<Job>, user: User?) {
        var dateState = LocalDate.MIN
        setFilterDuplicates(true)
        jobs.forEach {
            if (showHeader) {
                val ld = it.date?.toLocalDateTime()!!.toLocalDate()

                if (dateState != ld) // when the date is different, render a new header
                {
                    val dateStr = ld.dayOfWeek.toString() + ", " + ld.dayOfMonth + " " + ld.month

                    headerItem {
                        id("h${it.id}")
                        headerTitle(dateStr)
                    }

                    dateState = ld // set the current data state to this date
                }
            }

            jobItem {
                id(it.id)
                job(it)
                user(user)
                onFavChecked { model, modelview, _, _ -> callbacks.onFavChecked(modelview.job.id!!) }
                onClick { model, modelview, _, _ -> callbacks.onItemClick(modelview.job.id!!) }
            }
        }
    }
}