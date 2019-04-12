package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.*
import com.google.android.gms.maps.model.LatLng
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.epoxyModelView.RecItemModel_
import com.rud.fastjobs.view.epoxyModelView.jobBodyInfo
import com.rud.fastjobs.view.epoxyModelView.jobHeaderInfo
import com.rud.fastjobs.view.epoxyModelView.liteMapItem


class JobDetailEpoxyController(private val callbacks: AdapterCallbacks) : Typed2EpoxyController<Job, List<String>>() {
    interface AdapterCallbacks {
        fun onCarouselItemClick(id: String)
    }

    override fun buildModels(job: Job, recommendations: List<String>) {

        jobHeaderInfo {
            id("jobHeaderInfo")
        }

        liteMapItem {
            id("liteMapItem")
            location(LatLng(-33.8670522, 151.1957362))
        }

        jobBodyInfo {
            id("jobBodyInfo")
        }

        carousel {
            id("carousel")
            numViewsToShowOnScreen(1.2f)

            withModelsFrom(recommendations) {
                RecItemModel_()
                    .id(it)
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