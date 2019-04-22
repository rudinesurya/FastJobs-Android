package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.TypedEpoxyController
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.epoxyModelView.commentItem

class ChatRoomEpoxyController(private val callbacks: AdapterCallbacks) : TypedEpoxyController<List<Comment>>() {
    interface AdapterCallbacks {
        fun onItemClick(id: String)
    }

    override fun buildModels(data: List<Comment>) {
        data.sortedBy { it.postDate }.forEach {
            val ld = it.postDate?.toLocalDateTime()!!.toLocalDate()

            commentItem {
                id(it.id)
                comment(it)
                onClick { _, modelview, _, _ -> callbacks.onItemClick(modelview.comment.id!!) }
            }
        }
    }
}