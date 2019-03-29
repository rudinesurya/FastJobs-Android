package com.rud.fastjobs.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.viewmodel.JobRegistrationViewModel
import kotlinx.android.synthetic.main.job_registration_form.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*


class JobRegistrationFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobRegistrationViewModel
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobRegistrationViewModel::class.java)

        arguments?.let {
            val safeArgs = JobRegistrationFragmentArgs.fromBundle(it)
            safeArgs.jobId?.let { id ->
                viewModel.getJobById(id,
                    onSuccess = { job ->
                        editText_title.setText(job.title)
                        editText_payout.setText(job.payout.toString())
                        editText_description.setText(job.description)
                        checkBox_urgency.isChecked = job.urgency

                        btn_save.text = "Update Job"
                    })
            }
        }

        editText_venue.setOnClickListener {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            ).build(this.context!!)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        btn_save.setOnClickListener {
            viewModel.handleSave(
                title = editText_title.text.toString(),
                description = editText_description.text.toString(),
                payout = editText_payout.text.toString().toDouble(),
                urgency = checkBox_urgency.isChecked
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                AutocompleteActivity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Timber.d("Place: " + place.name + ", " + place.id)
                    editText_venue.setText(place.name)
                }

                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Timber.d(status.statusMessage)
                }

                AutocompleteActivity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
    }
}
