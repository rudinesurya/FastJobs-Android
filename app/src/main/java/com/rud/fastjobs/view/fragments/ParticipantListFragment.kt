package com.rud.fastjobs.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ptrbrynt.firestorelivedata.ResourceObserver

import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Participant
import com.rud.fastjobs.view.recyclerViewController.ParticipantListEpoxyController
import com.rud.fastjobs.viewmodel.ParticipantListViewModel
import kotlinx.android.synthetic.main.fragment_user_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class ParticipantListFragment : Fragment(), KodeinAware, ParticipantListEpoxyController.AdapterCallbacks {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: ParticipantListViewModel
    private val controller = ParticipantListEpoxyController(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ParticipantListViewModel::class.java)
        Timber.d("onViewCreated")

        activity?.intent?.getStringExtra("id")?.let {
            viewModel.jobId = it
        }

        initRecyclerView(view)

        viewModel.getAllParticipantsLiveData { participants ->
            participants.observe(this, object : ResourceObserver<List<Participant>> {
                override fun onSuccess(participants: List<Participant>?) {
                    Timber.d("participants changes observed")
                    Timber.d(participants?.toString())

                    controller.setData(participants)
                }

                override fun onLoading() {
                    Timber.d("loading participants observer")
                }

                override fun onError(throwable: Throwable?, errorMessage: String?) {
                    Timber.e(errorMessage)
                }
            })
        }
    }

    private fun initRecyclerView(view: View) {
        participants_recyclerView.setController(controller)
    }

    override fun onItemClick(id: String) {
        Timber.d("item [%s] clicked!", id)
    }
}
