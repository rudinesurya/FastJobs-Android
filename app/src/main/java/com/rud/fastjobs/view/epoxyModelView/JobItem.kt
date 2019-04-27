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
import com.rud.fastjobs.utils.toLocalDateTime
import kotlinx.android.synthetic.main.job_item_card.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class JobItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.job_item_card, this)
    }

    @ModelProp
    lateinit var job: Job

    var onClick: View.OnClickListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun bindUI() {
        job.let {
            val lt = it.date?.toLocalDateTime()?.toLocalTime()!!

            title.text = it.title
            time.text = "${lt.hour}:${lt.minute}"
            venue.text = it.venue?.name
            shortDescription.text = it.description
            payout.text = "$ ${it.payout}"

            if (it.urgency)
                chip_urgency.visibility = View.VISIBLE
            else
                chip_urgency.visibility = View.INVISIBLE
        }

        setOnClickListener(onClick)
    }
}