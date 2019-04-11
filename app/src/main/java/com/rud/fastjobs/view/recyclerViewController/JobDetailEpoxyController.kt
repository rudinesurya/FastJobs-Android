package com.rud.fastjobs.view.recyclerViewController

import com.airbnb.epoxy.*
import com.rud.fastjobs.data.model.Job
import com.rud.fastjobs.view.epoxyModelView.headerItem


class JobDetailEpoxyController(private val callbacks: AdapterCallbacks) : TypedEpoxyController<List<Job>>() {
    interface AdapterCallbacks {
        fun onCarouselItemClick(id: String)
    }

    override fun buildModels(data: List<Job>) {

        headerItem {
            id("header1")
            headerTitle("TEST")
        }

//        carousel {
//            id("carousel")
//            numViewsToShowOnScreen(1.2f)
//
//            withModelsFrom(data) {
//                JobItemModel_()
//                    .id(it.id)
//                    .job(it)
//                    .onClick { model, modelview, _, _ -> callbacks.onCarouselItemClick(modelview.job.id!!) }
//            }
//        }
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