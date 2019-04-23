package com.rud.fastjobs.view.fragments.jobDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.auth.Auth
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
    private val auth: Auth by instance()
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

        viewModel.getUserById(auth.currentUser?.uid!!)

        activity?.intent?.getStringExtra("id")?.let {
            viewModel.jobId = it
        }

        initRecyclerView(view)

        viewModel.getAllCommentsLiveData { comments ->
            comments.observe(this, Observer {
                it.data?.let { comments ->
                    Timber.d("comments changes observed")
                    Timber.d(comments.toString())

                    controller.setData(comments)
                }
            })
        }

        btn_post.setOnClickListener {
            val text = input_comment.text.toString()
            viewModel.postComment(viewModel.jobId, text, onSuccess = {
                Timber.d("post success")
                input_comment.setText("")
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
