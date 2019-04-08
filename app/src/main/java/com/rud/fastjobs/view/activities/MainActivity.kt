package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.libraries.places.api.Places
import com.google.android.material.navigation.NavigationView
import com.ptrbrynt.firestorelivedata.ResourceObserver
import com.rud.fastjobs.R
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.view.glide.GlideApp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_drawer_header.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber


class MainActivity : AppCompatActivity(), KodeinAware, NavigationView.OnNavigationItemSelectedListener {
    override val kodein: Kodein by closestKodein()
    private val myRepository: MyRepository by instance()
    private val auth: Auth by instance()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d("onCreate")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupNavigation()

        // Initialize Places.
        val apiKey = getString(R.string.google_api_key)
        Places.initialize(this, apiKey)
        Places.createClient(this)
    }

    private fun setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        NavigationUI.setupWithNavController(nav_view, navController)
        nav_view.setNavigationItemSelectedListener(this)

        nav_view.getHeaderView(0)?.apply {
            myRepository.getUserByIdLiveData(auth.currentUser!!.uid) { user ->
                user.observe(this@MainActivity, object : ResourceObserver<User> {
                    override fun onSuccess(user: User?) {
                        user?.let { user ->
                            user.avatarUrl?.let { avatarUrl ->
                                GlideApp.with(this@MainActivity).load(myRepository.pathToReference(avatarUrl))
                                    .transforms(CenterCrop(), RoundedCorners(100))
                                    .into(imageView_avatar)
                            }

                            displayName.text = user.name
                            email.text = user.email
                        }
                    }

                    override fun onLoading() {
                    }

                    override fun onError(throwable: Throwable?, errorMessage: String?) {
                    }
                })
            }

            setOnClickListener {
                navController.navigate(R.id.accountFragment)
                drawer_layout.closeDrawers()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true

        when (menuItem.itemId) {
            R.id.jobDashboardFragment,
            R.id.accountFragment,
            R.id.jobRegistrationFragment
            -> navController.navigate(menuItem.itemId)

            R.id.signOut -> {
                auth.signOut()

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                Toast.makeText(this@MainActivity, "Signed out!", Toast.LENGTH_SHORT).show()
            }
        }

        drawer_layout.closeDrawers()
        return true
    }
}
