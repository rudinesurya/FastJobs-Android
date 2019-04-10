package com.rud.fastjobs.view.recyclerView

import com.airbnb.epoxy.*
import com.rud.fastjobs.data.model.Job


class RecommendationCarouselController : TypedEpoxyController<List<Job>>() {
    override fun buildModels(data: List<Job>) {
        carousel {
            id("carousel")
            numViewsToShowOnScreen(1.2f)
            withModelsFrom(data) {
                JobItemModel_()
                    .id(it.id)
                    .job(it)
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