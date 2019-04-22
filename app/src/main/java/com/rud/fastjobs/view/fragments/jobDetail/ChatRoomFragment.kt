package com.rud.fastjobs.view.fragments.jobDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ptrbrynt.firestorelivedata.ResourceObserver
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Comment
import com.rud.fastjobs.view.recyclerViewController.ChatRoomEpoxyController
import com.rud.fastjobs.viewmodel.jobDetail.ChatRoomViewModel
import kotlinx.android.synthetic.main.fragment_chat_room.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class ChatRoomFragment : Fragment(), KodeinAware, ChatRoomEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: ChatRoomViewModel
    private val controller = ChatRoomEpoxyController(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ChatRoomViewModel::class.java)
        Timber.d("onViewCreated")

        activity?.intent?.getStringExtra("id")?.let {
            viewModel.jobId = it
        }

        initRecyclerView(view)

        viewModel.getAllCommentsLiveData { comments ->
            comments.observe(this, object : ResourceObserver<List<Comment>> {
                override fun onSuccess(comments: List<Comment>?) {
                    Timber.d("comments changes observed")
                    Timber.d(comments?.toString())

                    controller.setData(comments)
                }

                override fun onLoading() {
                    Timber.d("loading comments observer")
                }

                override fun onError(throwable: Throwable?, errorMessage: String?) {
                    Timber.e(errorMessage)
                }
            })
        }
    }

    private fun initRecyclerView(view: View) {
        comments_recyclerView.setController(controller)
    }

    override fun onItemClick(id: String) {
        Timber.d("item [%s] clicked!", id)
    }
}
