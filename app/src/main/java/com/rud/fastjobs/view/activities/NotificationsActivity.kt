package com.rud.fastjobs.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.epoxy.EpoxyTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.view.epoxyModelView.NotificationItemModel_
import com.rud.fastjobs.view.recyclerViewController.NotificationsEpoxyController
import com.rud.fastjobs.viewmodel.NotificationsActivityViewModel
import kotlinx.android.synthetic.main.activity_notifications.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class NotificationsActivity : AppCompatActivity(), KodeinAware, NotificationsEpoxyController.AdapterCallbacks {
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

        viewModel.notifications.observe(this, Observer {
            it?.let { notifications ->
                controller.setData(notifications)
            }
        })
    }

    // override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    //     val inflater = mode?.menuInflater
    //     inflater?.inflate(R.menu.overlay_ondelete_menu, menu)
    //     return true
    // }
    //
    // override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    //     return false
    // }
    //
    // override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
    //     if (item?.itemId == R.id.action_delete) {
    //         deleteAllSelected(mode)
    //         return true
    //     }
    //     return false
    // }

    // fun deleteAllSelected(mode: ActionMode?) {
    //     for (i in adapter.count - 1 downTo 0) {
    //         if (listView.isItemChecked(i)) {
    //             Timber.d("removing index $i")
    //             // adapter.remove(i)
    //             notifications.removeAt(i)
    //             adapter.setData(notifications)
    //         }
    //     }
    //     mode?.finish()
    // }

    // override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int, id: Long, checked: Boolean) {
    // }
    //
    // override fun onDestroyActionMode(mode: ActionMode?) {
    // }

    private fun initRecyclerView() {
        notifications_rv.setController(controller)

        EpoxyTouchHelper.initSwiping(notifications_rv)
            .leftAndRight()
            .withTarget(NotificationItemModel_::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<NotificationItemModel_?>() {
                override fun onSwipeCompleted(
                    model: NotificationItemModel_?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    Timber.d("removing [%s]", model!!.id())
                    val deletedItem = model.notification() // copy of the deleted item for future restoring
                    viewModel.removeNotification(deletedItem)

                    Snackbar.make(itemView!!, "Item Deleted", Snackbar.LENGTH_LONG).setAction("Undo") {
                        viewModel.addNotification(deletedItem)
                    }.show()
                }
            })
    }

    override fun onItemClick(id: String) {
        // Timber.d("item [%s] clicked!", id)
    }
}
