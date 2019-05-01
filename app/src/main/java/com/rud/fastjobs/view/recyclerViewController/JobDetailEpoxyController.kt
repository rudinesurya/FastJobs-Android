package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.CarouselModelBuilder
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.Typed3EpoxyController
import com.google.android.gms.maps.model.LatLng
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.view.epoxyModelView.PhotoItemModel_
import com.rud.fastjobs.view.epoxyModelView.jobBodyInfo
import com.rud.fastjobs.view.epoxyModelView.jobHeaderInfo
import com.rud.fastjobs.view.epoxyModelView.liteMapItem

class JobDetailEpoxyController(private val callbacks: AdapterCallbacks) : Typed3EpoxyController<Job, User?, Any?>() {
    interface AdapterCallbacks {
        fun onJoinBtnClick()
        fun onLeaveBtnClick()
        fun onCancelBtnClick()
        fun onResumeBtnClick()
        fun onFavChecked()
        fun onCarouselItemClick(id: String)
    }

    override fun buildModels(job: Job, user: User?, unused: Any?) {
        setFilterDuplicates(true)
        jobHeaderInfo {
            id("jobHeaderInfox")
            job(job)
            user(user)
            onFavChecked { _ -> callbacks.onFavChecked() }
            onJoinBtnClick { _ -> callbacks.onJoinBtnClick() }
            onLeaveBtnClick { _ -> callbacks.onLeaveBtnClick() }
            onCancelBtnClick { _ -> callbacks.onCancelBtnClick() }
            onResumeBtnClick { _ -> callbacks.onResumeBtnClick() }
        }

        liteMapItem {
            id("liteMapItem")
            val gp = job.venue?.geoPoint!!
            location(LatLng(gp.latitude, gp.longitude))
        }

        jobBodyInfo {
            id("jobBodyInfo")
            job(job)
        }

        carousel {
            id("carousel")
            numViewsToShowOnScreen(1.2f)

            val photoUrls = job.photoUrls
            withModelsFrom(photoUrls) {
                PhotoItemModel_()
                    .id(it)
                    .photoUrl(it)
            }
        }
    }
}

/** For use in the buildModels method of EpoxyController. A shortcut for creating a Carousel model, initializing it, and adding it to the controller.
 *
 */
inline fun EpoxyController.carousel(modelInitializer: CarouselModelBuilder.() -> Unit) {
    CarouselModel_().apply {
        modelInitializer()
    }.addTo(this)
}

/** Add models to a CarouselModel_ by transforming a list of items into EpoxyModels.
 *
 * @param items The items to transform to models
 * @param modelBuilder A function that take an item and returns a new EpoxyModel for that item.
 */
inline fun <T> CarouselModelBuilder.withModelsFrom(
    items: List<T>,
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map { modelBuilder(it) })
}