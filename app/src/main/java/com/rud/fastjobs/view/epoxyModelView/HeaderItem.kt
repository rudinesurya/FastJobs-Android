package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.widget.LinearLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.rud.fastjobs.R
import kotlinx.android.synthetic.main.header_view.view.*


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderItem @JvmOverloads constructor(
    context: Context
) : LinearLayout(context) {
    init {
        inflate(context, R.layout.header_view, this)
        orientation = VERTICAL
    }

    @ModelProp
    lateinit var headerTitle: String

    @AfterPropsSet
    fun bindUI() {
        title.text = headerTitle
    }
}