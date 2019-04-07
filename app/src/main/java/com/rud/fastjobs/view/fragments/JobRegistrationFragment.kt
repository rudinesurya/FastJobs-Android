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
import com.google.firebase.firestore.GeoPoint
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Venue
import com.rud.fastjobs.viewmodel.JobRegistrationViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.job_registration_form.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.time.LocalDateTime
import java.util.*


class JobRegistrationFragment : Fragment(), KodeinAware, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
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
                        editText_title.setText(job!!.title)
                        editText_payout.setText(job.payout.toString())
                        editText_description.setText(job.description)
                        checkBox_urgency.isChecked = job.urgency
                        editText_venue.setText(job.venue?.name)

                        btn_save.text = "Update Job"
                    })
            }
        }

        editText_venue.setOnClickListener {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).build(this.context!!)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        editText_date.setOnClickListener {
            val dpd = DatePickerDialog.newInstance(this)
            dpd.version = DatePickerDialog.Version.VERSION_2
            dpd.show(fragmentManager, "date picker")
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
                    viewModel.currentSelectedVenue =
                        Venue(
                            name = place.name!!,
                            address = place.address!!,
                            geoPoint = GeoPoint(place.latLng?.latitude!!, place.latLng?.longitude!!)
                        )
                    editText_venue.setText(place.address)
                    Timber.d("Place: " + place.name + ", " + place.latLng)
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

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModel.currentSelectedDate =
            LocalDateTime.now().withYear(year).withMonth(monthOfYear + 1).withDayOfMonth(dayOfMonth)
//        Timber.d(viewModel.currentSelectedDate.toString())

        val tpd = TimePickerDialog.newInstance(this, false)
        tpd.version = TimePickerDialog.Version.VERSION_2
        tpd.show(fragmentManager, "time picker")
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        viewModel.currentSelectedDate = viewModel.currentSelectedDate!!.withHour(hourOfDay).withMinute(minute)
//        Timber.d(viewModel.currentSelectedDate.toString())
        editText_date.setText(viewModel.currentSelectedDate.toString())
    }
}
