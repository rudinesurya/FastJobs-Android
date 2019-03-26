package com.rud.fastjobs.view.recyclerView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.Job
import kotlinx.android.synthetic.main.job_item_card.view.*


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class JobItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.job_item_card, this)
        orientation = VERTICAL
    }

    @ModelProp
    lateinit var job: Job

    var onClick: View.OnClickListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun bindUI() {
        job.let {
            title.text = it.title
            payout.text = it.payout.toString()
            hostName.text = it.hostName
        }

        setOnClickListener(onClick)
    }
}