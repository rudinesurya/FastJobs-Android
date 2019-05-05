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
import com.rud.fastjobs.utils.toLocalDateTime
import kotlinx.android.synthetic.main.job_header_info.view.*
import java.time.format.DateTimeFormatter

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class JobHeaderInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.job_header_info, this)
    }

    var _user: User? = null

    @ModelProp
    lateinit var job: Job

    @ModelProp
    fun setUser(user: User?) {
        _user = user
    }

    var onFavChecked: View.OnClickListener? = null
        @CallbackProp set
    var onCancelBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onResumeBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onJoinBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onLeaveBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onDateBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onLocationBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onHostBtnClick: View.OnClickListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun bindUI() {
        _user?.let { user ->
            val userIsHost = user.id == job.hostUid
            val userCanJoin = !userIsHost && !user.joinedList.contains(job.id)

            if (userCanJoin) {
                btn_joinLeave.text = "Join"
                btn_joinLeave.setOnClickListener(onJoinBtnClick)
            } else if (userIsHost)
                btn_joinLeave.isVisible = false
            else {
                btn_joinLeave.text = "Leave"
                btn_joinLeave.setOnClickListener(onLeaveBtnClick)
            }

            btn_cancelResume.isVisible = userIsHost

            if (userIsHost) {
                if (job.status) {
                    btn_cancelResume.text = "Cancel"
                    btn_cancelResume.setOnClickListener(onCancelBtnClick)
                } else {
                    btn_cancelResume.text = "Resume"
                    btn_cancelResume.setOnClickListener(onResumeBtnClick)
                }
            }

            val ld = job.date?.toLocalDateTime()?.toLocalDate()!!
            val lt = job.date?.toLocalDateTime()?.toLocalTime()!!
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            val dateStr = ld.dayOfWeek.toString() + ", " + ld.dayOfMonth + " " + ld.month
            val timeStr = lt.format(formatter)

            text_host.text = "Hosted by: ${job.hostName}"
            text_date.text = "$dateStr $timeStr"
            text_location.text = job.venue?.address.toString()

            checkbox_fav.isChecked = user.favList.contains(job.id)

            checkbox_fav.setOnClickListener(onFavChecked)

            layout_date.setOnClickListener(onDateBtnClick)
            layout_location.setOnClickListener(onLocationBtnClick)
            layout_host.setOnClickListener(onHostBtnClick)
        }
    }
}