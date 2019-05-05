package com.rud.fastjobs.view.fragments.jobDetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.utils.FragmentLifecycle
import com.rud.fastjobs.view.glide.GlideApp
import com.rud.fastjobs.view.recyclerViewController.ParticipantListEpoxyController
import com.rud.fastjobs.viewmodel.jobDetail.ParticipantListViewModel
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.fragment_user_list.*
import kotlinx.android.synthetic.main.profile_modal.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class ParticipantListFragment : Fragment(), KodeinAware, FragmentLifecycle,
    ParticipantListEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: ParticipantListViewModel
    private val controller = ParticipantListEpoxyController(this)

    override fun onPauseFragment() {
    }

    override fun onResumeFragment() {
        activity?.app_bar_layout?.setExpanded(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ParticipantListViewModel::class.java)

        activity?.intent?.getStringExtra("id")?.let {
            viewModel.jobId = it
        }

        initRecyclerView(view)

        viewModel.getAllParticipantsLiveData { participants ->
            participants.observe(this, Observer {
                it.data?.let { participants ->
                    controller.setData(participants)
                }
            })
        }
    }

    private fun initRecyclerView(view: View) {
        participants_recyclerView.setController(controller)
    }

    override fun onItemClick(id: String) {
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.profile_modal, null)

        viewModel.getUserById(id) { user ->
            user?.let {
                // set the image avatar
                if (user.avatarUrl.isNotBlank()) {
                    GlideApp.with(this).load(viewModel.pathToReference(user.avatarUrl))
                        .transforms(CenterCrop(), RoundedCorners(1000))
                        .into(view.image_avatar)
                }
                //set the bio
                view.text_name.text = user.name
                view.text_bio.text = user.bio
                view.text_location.text = user.location?.address
            }
        }

        with(builder)
        {
            setView(view)
            setMessage("")
            setPositiveButton("Ok", null)
            show()
        }
    }
}
