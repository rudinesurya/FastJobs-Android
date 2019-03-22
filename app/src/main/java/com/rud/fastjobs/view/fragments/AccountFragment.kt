package com.rud.fastjobs.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.rud.fastjobs.R
import com.rud.fastjobs.data.repository.UserRepository
import com.rud.fastjobs.view.activities.SignInActivity
import com.rud.fastjobs.view.glide.GlideApp
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream


class AccountFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val userRepository: UserRepository by instance()
    private val RC_SELECT_IMAGE = 2
    private var selectedImageBytes: ByteArray? = null
    private var pictureJustChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        view.apply {
            imageView_avatar.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }

            btn_save.setOnClickListener {
                if (selectedImageBytes != null) {
                    userRepository.uploadAvatar(selectedImageBytes!!) { imagePath ->
                        userRepository.updateCurrentUser(
                            editText_displayName.text.toString(),
                            editText_bio.text.toString(),
                            imagePath
                        )
                    }
                } else {
                    userRepository.updateCurrentUser(
                        editText_displayName.text.toString(),
                        editText_bio.text.toString()
                    )
                }
                Toast.makeText(this@AccountFragment.context!!, "Saved!", Toast.LENGTH_SHORT).show()
            }

            btn_sign_out.setOnClickListener {
                AuthUI.getInstance().signOut(this@AccountFragment.context!!)
                    .addOnCompleteListener {
                        val intent = Intent(this@AccountFragment.context!!, SignInActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        Toast.makeText(this@AccountFragment.context!!, "Signed out!", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data
            val selectedImageBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            GlideApp.with(this).load(selectedImageBytes)
                .into(imageView_avatar)

            pictureJustChanged = true
        }
    }

    override fun onStart() {
        super.onStart()
        userRepository.getCurrentUser { user ->
            if (this.isVisible) {
                editText_displayName.setText(user.name)
                editText_bio.setText(user.bio)
                if (!pictureJustChanged && user.avatarUrl != null) {
                    GlideApp.with(this).load(userRepository.pathToReference(user.avatarUrl))
                        .into(imageView_avatar)
                }
            }
        }
    }
}
