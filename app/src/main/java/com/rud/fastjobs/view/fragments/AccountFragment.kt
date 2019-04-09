package com.rud.fastjobs.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.ptrbrynt.firestorelivedata.ResourceObserver
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.view.glide.GlideApp
import com.rud.fastjobs.viewmodel.AccountViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.io.ByteArrayOutputStream


class AccountFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: AccountViewModel
    private val RC_SELECT_IMAGE = 2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(AccountViewModel::class.java)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        }

        viewModel.getCurrentUserLiveData { user ->
            user.observe(this@AccountFragment, object : ResourceObserver<User> {
                override fun onSuccess(user: User?) {
                    // Handle successful result here
                    Timber.d("currentUser changes observed")
                    viewModel.currentUser = user!!
                    input_name.setText(user.name)
                    input_bio.setText(user.bio)

                    if (!viewModel.pictureJustChanged && user.avatarUrl != null) {
                        GlideApp.with(this@AccountFragment).load(viewModel.pathToReference(user.avatarUrl))
                            .into(input_avatar)
                    }
                }

                override fun onLoading() {
                    // Handle loading state e.g. display a loading animation
                    Timber.d("loading current user observer")
                }

                override fun onError(throwable: Throwable?, errorMessage: String?) {
                    // Handle errors here
                    Timber.e(errorMessage)
                }
            })
        }

        view.apply {
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
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data
            val selectedImageBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            viewModel.selectedImageBytes = outputStream.toByteArray()

            GlideApp.with(this).load(viewModel.selectedImageBytes)
                .into(input_avatar)

            viewModel.pictureJustChanged = true
        }
    }
}
