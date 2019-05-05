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
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.utils.toLocalDateTime
import kotlinx.android.synthetic.main.job_item_card.view.*
import java.time.format.DateTimeFormatter

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class JobItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.job_item_card, this)
    }

    var _user: User? = null

    @ModelProp
    lateinit var job: Job

    var onFavChecked: View.OnClickListener? = null
        @CallbackProp set

    @ModelProp
    fun setUser(user: User?) {
        _user = user
    }

    var onClick: View.OnClickListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun bindUI() {
        job.let {
            val lt = it.date?.toLocalDateTime()?.toLocalTime()!!
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")

            text_title.text = it.title
            text_time.text = lt.format(formatter)
            text_venue.text = it.venue?.name
            text_shortDescription.text = it.description
            text_payout.text = "$ ${it.payout}"

            checkbox_fav.isChecked = _user?.favList?.contains(job.id) ?: false
            checkbox_fav.setOnClickListener(onFavChecked)

            if (it.urgency)
                chip_urgency.visibility = View.VISIBLE
            else
                chip_urgency.visibility = View.INVISIBLE
        }

        setOnClickListener(onClick)
    }
}