package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import kotlinx.android.synthetic.main.job_header_info.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class JobHeaderInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.job_header_info, this)
    }

    @ModelProp
    lateinit var job: Job
    @ModelProp
    lateinit var user: User

    var onJoinBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onLeaveBtnClick: View.OnClickListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun bindUI() {
        val userIsHost = user.id == job.hostUid
        val userCanJoin = !userIsHost && !user.joinedList.contains(job.id)

        btn_join.isVisible = userCanJoin
        btn_leave.isVisible = !userCanJoin

        text_hostName.text = job.hostName
        text_updatedAt.text = "to be notified.."
        text_urgency.text = job.urgency.toString()

        btn_join.setOnClickListener(onJoinBtnClick)
        btn_leave.setOnClickListener(onLeaveBtnClick)
    }
}