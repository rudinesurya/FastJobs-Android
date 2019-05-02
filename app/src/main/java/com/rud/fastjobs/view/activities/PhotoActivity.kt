package com.rud.fastjobs.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.view.glide.GlideApp
import com.rud.fastjobs.viewmodel.PhotoActivityViewModel
import kotlinx.android.synthetic.main.activity_photo.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class PhotoActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: PhotoActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(PhotoActivityViewModel::class.java)

        intent?.getStringExtra("url")?.let {
            GlideApp.with(this).load(viewModel.pathToReference(it))
                .into(image_photo)
        }
    }
}