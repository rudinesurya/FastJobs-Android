package com.rud.fastjobs.view.activities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.epoxy.EpoxyTouchHelper
import com.google.android.material.math.MathUtils.lerp
import com.google.android.material.snackbar.Snackbar
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Notification
import com.rud.fastjobs.utils.ScopedActivity
import com.rud.fastjobs.view.epoxyModelView.NotificationItemModel_
import com.rud.fastjobs.view.recyclerViewController.NotificationsEpoxyController
import com.rud.fastjobs.viewmodel.NotificationsActivityViewModel
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class NotificationsActivity : ScopedActivity(), KodeinAware, NotificationsEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: NotificationsActivityViewModel
    private val controller = NotificationsEpoxyController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NotificationsActivityViewModel::class.java)

        setSupportActionBar(notification_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initRecyclerView()

        launch {
            // seed the db for testing
            for (i in 1..3) {
                val n = Notification(
                    title = "Swipe me",
                    message = "Hello World $i",
                    id = i.toLong()
                )
                viewModel.addNotification(n)
            }

            val notif = viewModel.notifications.await()
            notif.observe(this@NotificationsActivity, Observer {
                it?.let { notifications ->
                    controller.setData(notifications)
                }
            })
        }
    }

    private fun initRecyclerView() {
        notifications_rv.setController(controller)
        EpoxyTouchHelper.initSwiping(notifications_rv)
            .leftAndRight()
            .withTarget(NotificationItemModel_::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<NotificationItemModel_?>() {
                private val icon: Drawable =
                    ContextCompat.getDrawable(this@NotificationsActivity, R.drawable.ic_baseline_delete_sweep_24px)!!
                private val padding = 40

                private val rect = RectF()
                private val iconBounds = Rect()

                private val iconHeight = icon.intrinsicHeight
                private val iconWidth = icon.intrinsicWidth

                private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.rgb(255, 0, 0)
                }

                private val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.rgb(255, 128, 128)
                }

                override fun onSwipeProgressChanged(
                    model: NotificationItemModel_?,
                    itemView: View,
                    swipeProgress: Float,
                    canvas: Canvas
                ) {
                    rect.set(
                        itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    )
                    rect.offset(itemView.translationX, itemView.translationY)

                    if (rect.left == 0f || rect.right == canvas.width.toFloat()) {
                        return
                    }

                    var radius = 0f

                    val save = canvas.save()

                    if (rect.left > 0) {
                        // Swiping left-to-right
                        rect.right = rect.left
                        rect.left = 0f

                        canvas.clipRect(rect)

                        val startValue = iconBounds.right + padding / 2
                        val endValue = rect.left + itemView.width / 2
                        if (rect.right >= startValue) {
                            val fraction = ((rect.right - startValue) / (endValue - startValue)).coerceIn(0f, 1f)
                            val maxRadius = Math.hypot(
                                rect.right.toDouble() - iconBounds.centerX(),
                                iconBounds.centerY().toDouble()
                            )
                            radius = lerp(0f, maxRadius.toFloat(), fraction)
                        }

                        val left = rect.left.toInt() + padding
                        val top = (rect.top + (rect.height() - iconHeight) / 2).toInt()
                        iconBounds.set(left, top, left + iconWidth, top + iconHeight)
                        icon.bounds = iconBounds
                    } else if (rect.right < canvas.width) {
                        // Swiping right-to-left
                        rect.left = rect.right
                        rect.right = canvas.width.toFloat()

                        canvas.clipRect(rect)

                        val startValue = iconBounds.left - padding / 2
                        val endValue = rect.right - itemView.width / 2
                        if (rect.left <= startValue) {
                            val fraction = ((rect.left - startValue) / (endValue - startValue)).coerceIn(0f, 1f)
                            val maxRadius =
                                Math.hypot(iconBounds.centerX() - rect.left.toDouble(), iconBounds.centerY().toDouble())
                            radius = lerp(0f, maxRadius.toFloat(), fraction)
                        }

                        val right = rect.right.toInt() - padding
                        val top = (rect.top + (rect.height() - iconHeight) / 2).toInt()
                        iconBounds.set(right - iconWidth, top, right, top + iconHeight)
                        icon.bounds = iconBounds
                    }

                    // Draw the background color
                    canvas.drawRect(rect, backgroundPaint)

                    if (radius > 0) {
                        canvas.drawCircle(
                            iconBounds.centerX().toFloat(),
                            iconBounds.centerY().toFloat(),
                            radius,
                            foregroundPaint
                        )
                    }

                    // Now draw the icon
                    icon.draw(canvas)

                    canvas.restoreToCount(save)
                }

                override fun onSwipeCompleted(
                    model: NotificationItemModel_?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val deletedItem = model!!.notification() // copy of the deleted item for future restoring
                    launch {
                        viewModel.removeNotification(deletedItem)
                    }

                    Snackbar.make(itemView!!, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo") {
                        launch {
                            viewModel.addNotification(deletedItem)
                        }
                    }.show()
                }
            })
    }

    override fun onItemClick(id: String) {
        // Timber.d("item [%s] clicked!", id)
    }
}
