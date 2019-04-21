package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.rud.fastjobs.R
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

    var onJoinBtnClick: View.OnClickListener? = null
        @CallbackProp set
    var onLeaveBtnClick: View.OnClickListener? = null
        @CallbackProp set

    @AfterPropsSet
    fun bindUI() {
        btn_join.setOnClickListener(onJoinBtnClick)
        btn_leave.setOnClickListener(onLeaveBtnClick)
    }
}