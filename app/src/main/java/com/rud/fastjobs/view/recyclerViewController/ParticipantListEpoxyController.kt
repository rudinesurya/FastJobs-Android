package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.TypedEpoxyController
import com.rud.fastjobs.data.model.Participant
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.epoxyModelView.participantItem

class ParticipantListEpoxyController(private val callbacks: AdapterCallbacks) :
    TypedEpoxyController<List<Participant>>() {
    interface AdapterCallbacks {
        fun onItemClick(id: String)
    }

    override fun buildModels(data: List<Participant>) {
        setFilterDuplicates(true)
        data.sortedBy { it.joinDate }.forEach {
            val ld = it.joinDate?.toLocalDateTime()!!.toLocalDate()

            participantItem {
                id(it.id)
                participant(it)
                onClick { _, modelview, _, _ -> callbacks.onItemClick(modelview.participant.id!!) }
            }
        }
    }
}