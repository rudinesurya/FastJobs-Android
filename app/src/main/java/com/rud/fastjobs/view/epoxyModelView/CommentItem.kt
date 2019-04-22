package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.data.repository.StorageUtil
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.view.glide.GlideApp
import kotlinx.android.synthetic.main.comment_item_card.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CommentItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KodeinAware {
    override val kodein: Kodein by closestKodein()
    val storageUtil: StorageUtil by instance()

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

            text_senderName.text = comment.userName
            text_postDate.text = lt.toString()
            text_comment.text = comment.text

            if (comment.userAvatarUrl.isNotBlank()) {
                GlideApp.with(context).load(storageUtil.pathToReference(comment.userAvatarUrl))
                    .transforms(CenterCrop(), RoundedCorners(100))
                    .into(image_senderAvatar)
            }
        }

        setOnClickListener(onClick)
    }
}