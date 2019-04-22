package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.utils.toLocalDateTime
import kotlinx.android.synthetic.main.comment_item_card.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CommentItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.comment_item_card, this)
    }

    @ModelProp
    lateinit var comment: Comment

    var onClick: View.OnClickListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun bindUI() {
        comment.let {
            val lt = it.postDate?.toLocalDateTime()?.toLocalTime()!!

            // image_senderAvatar
            text_senderName.text = comment.userName
            text_postDate.text = lt.toString()
            text_comment.text = comment.text
        }

        setOnClickListener(onClick)
    }
}