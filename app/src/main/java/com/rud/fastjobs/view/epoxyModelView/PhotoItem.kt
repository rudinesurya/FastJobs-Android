package com.rud.fastjobs.view.epoxyModelView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.rud.fastjobs.R
import com.rud.fastjobs.data.repository.StorageUtil
import com.rud.fastjobs.view.glide.GlideApp
import kotlinx.android.synthetic.main.photo_item_card.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PhotoItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KodeinAware {
    override val kodein: Kodein by closestKodein()
    val storageUtil: StorageUtil by instance()

    init {
        inflate(context, R.layout.photo_item_card, this)
    }

    @ModelProp
    lateinit var photoUrl: String

    @AfterPropsSet
    fun bindUI() {
        if (photoUrl.isNotBlank()) {
            GlideApp.with(context).load(storageUtil.pathToReference(photoUrl))
                .into(image_photo)
        }
    }
}