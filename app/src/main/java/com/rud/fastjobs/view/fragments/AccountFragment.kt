package com.rud.fastjobs.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.firestore.GeoPoint
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.Venue
import com.rud.fastjobs.utils.ScopedFragment
import com.rud.fastjobs.view.glide.GlideApp
import com.rud.fastjobs.viewmodel.AccountViewModel
import kotlinx.android.synthetic.main.fragment_account.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.util.Arrays

class AccountFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: AccountViewModel
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val RC_SELECT_IMAGE = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AccountViewModel::class.java)

        viewModel.currentUser.observe(this, Observer { user ->
            user?.let { user ->
                // Timber.d("currentUser changes observed")
                input_name.setText(user.name)
                input_bio.setText(user.bio)
                input_location.setText(user.location?.address)

                if (!viewModel.pictureJustChanged && user.avatarUrl.isNotBlank()) {
                    GlideApp.with(this@AccountFragment).load(viewModel.pathToReference(user.avatarUrl))
                        .transforms(CenterCrop(), RoundedCorners(1000))
                        .into(input_avatar)
                }
            }
        })

        input_avatar.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
        }

        btn_save.setOnClickListener {
            viewModel.handleSave(
                displayName = input_name.text.toString(),
                bio = input_bio.text.toString()
            )

            activity?.finish()
        }

        input_location.setOnClickListener {
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
            ).setTypeFilter(TypeFilter.CITIES).build(this.context!!)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
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
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            viewModel.currentSelectedVenue =
                Venue(
                    name = place.name!!,
                    address = place.address!!,
                    geoPoint = GeoPoint(place.latLng?.latitude!!, place.latLng?.longitude!!)
                )
            input_location.setText(place.address)
            Timber.d("Place: " + place.name + ", " + place.latLng)
        }

        if (requestCode == RC_SELECT_IMAGE) {
            if (data != null && data.data != null) {
                val selectedImagePath = data.data
                val selectedImageBitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

                val outputStream = ByteArrayOutputStream()
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                viewModel.selectedImageBytes = outputStream.toByteArray()

                GlideApp.with(this).load(viewModel.selectedImageBytes)
                    .into(input_avatar)

                viewModel.pictureJustChanged = true
            }
        }
    }
}
