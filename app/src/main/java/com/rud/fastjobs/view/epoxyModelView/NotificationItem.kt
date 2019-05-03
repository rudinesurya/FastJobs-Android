package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.Notification
import kotlinx.android.synthetic.main.notification_item.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NotificationItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.notification_item, this)
    }

    @ModelProp
    lateinit var notification: Notification

    @AfterPropsSet
    fun bindUI() {
        notification_title.text = notification.title
        notification_message.text = notification.message
    }
}