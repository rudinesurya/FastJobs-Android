package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.navigation.NavigationView
import com.rud.fastjobs.R
import com.rud.fastjobs.ViewModelFactory
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.utils.MyViewPagerAdapter
import com.rud.fastjobs.view.fragments.jobDashboard.DefaultJobListFragment
import com.rud.fastjobs.view.fragments.jobDashboard.JoinedJobListFragment
import com.rud.fastjobs.view.fragments.jobDashboard.NotificationFragment
import com.rud.fastjobs.view.fragments.jobDashboard.PastJobListFragment
import com.rud.fastjobs.view.fragments.jobDashboard.StarredJobListFragment
import com.rud.fastjobs.view.glide.GlideApp
import com.rud.fastjobs.viewmodel.jobDashboard.JobDashboardActivityViewModel
import kotlinx.android.synthetic.main.activity_job_dashboard.*
import kotlinx.android.synthetic.main.nav_drawer_header.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class JobDashboardActivity : AppCompatActivity(), KodeinAware, NavigationView.OnNavigationItemSelectedListener {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: JobDashboardActivityViewModel
    private val auth: Auth by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_dashboard)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JobDashboardActivityViewModel::class.java)

        setSupportActionBar(dashboard_toolbar)

        val mToggle =
            ActionBarDrawerToggle(this, drawer_layout, dashboard_toolbar, R.string.app_name, R.string.app_name)
        drawer_layout.addDrawerListener(mToggle)
        mToggle.syncState()

        setupNavigation()

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DefaultJobListFragment(), "All")
        adapter.addFragment(JoinedJobListFragment(), "Joined")
        adapter.addFragment(StarredJobListFragment(), "Starred")
        adapter.addFragment(PastJobListFragment(), "Past")
        adapter.addFragment(NotificationFragment(), "Notifications")

        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)

        viewModel.initGooglePlaces()
    }

    override fun onResume() {
        super.onResume()
        nav_view.checkedItem?.isChecked = false
    }

    private fun setupNavigation() {
        nav_view.setNavigationItemSelectedListener(this)

        nav_view.getHeaderView(0)?.apply {
            viewModel.getUserByIdLiveData(auth.currentUser?.uid!!) { user ->
                user.observe(this@JobDashboardActivity, Observer {
                    it.data?.let { user ->
                        if (user.avatarUrl.isNotBlank()) {
                            GlideApp.with(this@JobDashboardActivity).load(viewModel.pathToReference(user.avatarUrl))
                                .transforms(CenterCrop(), RoundedCorners(100))
                                .into(imageView_avatar)
                        }

                        displayName.text = user.name
                        email.text = user.email
                    }
                })
            }

            setOnClickListener {
                val intent = Intent(this@JobDashboardActivity, UserActivity::class.java)
                startActivity(intent)
                drawer_layout.closeDrawers()
            }
        }
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
            R.id.jobRegistrationActivity -> {
                val intent = Intent(this@JobDashboardActivity, JobRegistrationActivity::class.java)
                startActivity(intent)
            }

            R.id.userActivity -> {
                val intent = Intent(this@JobDashboardActivity, UserActivity::class.java)
                startActivity(intent)
            }

            R.id.mapActivity -> {
                val intent = Intent(this@JobDashboardActivity, MapsActivity::class.java)
                startActivity(intent)
            }

            R.id.signOut -> {
                auth.signOut()

                val intent = Intent(this@JobDashboardActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                Toast.makeText(this@JobDashboardActivity, "Signed out!", Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawers()
        return true
    }
}
