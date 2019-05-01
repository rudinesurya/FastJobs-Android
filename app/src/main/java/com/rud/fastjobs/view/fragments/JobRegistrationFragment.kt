package com.rud.fastjobs.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.firestore.GeoPoint
import com.rud.coffeemate.ui.fragments.ScopedFragment
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Venue
import com.rud.fastjobs.utils.toLocalDateTime
import com.rud.fastjobs.viewmodel.JobRegistrationViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_job_registration.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.Arrays

class JobRegistrationFragment : ScopedFragment(), KodeinAware, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobRegistrationViewModel
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val RC_SELECT_IMAGES = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobRegistrationViewModel::class.java)

        activity?.intent?.getStringExtra("id")?.let {
            viewModel.getJobById(it) { job ->
                job?.let {
                    viewModel.currentJob = job
                    input_title.setText(job.title)
                    input_payout.setText(job.payout.toString())
                    input_description.setText(job.description)
                    input_urgency.isChecked = job.urgency
                    input_venue.setText(job.venue?.name)
                    input_date.setText(job.date?.toLocalDateTime().toString())
                    btn_save.text = "Update Job"
                }
            }
        }

        input_venue.setOnClickListener {
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
            ).setTypeFilter(TypeFilter.ESTABLISHMENT).build(this.context!!)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        input_date.setOnClickListener {
            val dpd = DatePickerDialog.newInstance(this)
            dpd.version = DatePickerDialog.Version.VERSION_2
            dpd.show(fragmentManager, "date picker")
        }

        btn_attachImages.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGES)
        }

        btn_save.setOnClickListener {
            viewModel.handleSave(
                title = input_title.text.toString(),
                description = input_description.text.toString(),
                payout = input_payout.text.toString().toDouble(),
                urgency = input_urgency.isChecked
            )

            activity?.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Timber.d(status.statusMessage)
            }

            return
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            viewModel.currentSelectedVenue =
                Venue(
                    name = place.name!!,
                    address = place.address!!,
                    geoPoint = GeoPoint(place.latLng?.latitude!!, place.latLng?.longitude!!)
                )
            input_venue.setText(place.address)
            Timber.d("Place: " + place.name + ", " + place.latLng)
        } else if (requestCode == RC_SELECT_IMAGES) {
            val clipData = data?.clipData
            viewModel.selectedImageBytesArray.clear()

            if (clipData == null) {
                val selectedImagePath = data?.data
                // Timber.d(selectedImagePath.toString())
                if (selectedImagePath != null)
                    addToList(selectedImagePath)
            } else {
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    val selectedImagePath = item.uri
                    // Timber.d(selectedImagePath.toString())
                    addToList(selectedImagePath)
                }
            }
        }
    }

    fun addToList(selectedImagePath: Uri) {
        val selectedImageBitmap =
            MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

        val outputStream = ByteArrayOutputStream()
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val ba = outputStream.toByteArray()
        viewModel.selectedImageBytesArray.add(ba)
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
        input_date.setText(viewModel.currentSelectedDate.toString())
    }
}
