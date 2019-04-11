package com.rud.fastjobs.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.libraries.places.api.Places
import com.google.android.material.navigation.NavigationView
import com.ptrbrynt.firestorelivedata.ResourceObserver
import com.rud.fastjobs.R
import com.rud.fastjobs.auth.Auth
import com.rud.fastjobs.data.model.User
import com.rud.fastjobs.data.repository.MyRepository
import com.rud.fastjobs.view.fragments.jobDashboard.*
import com.rud.fastjobs.view.glide.GlideApp
import kotlinx.android.synthetic.main.activity_job_dashboard.*
import kotlinx.android.synthetic.main.nav_drawer_header.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class JobDashboardActivity : AppCompatActivity(), KodeinAware, NavigationView.OnNavigationItemSelectedListener {
    override val kodein: Kodein by closestKodein()
    private val myRepository: MyRepository by instance()
    private val auth: Auth by instance()

    class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val titleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_dashboard)

        setSupportActionBar(dashboard_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupNavigation()

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(JobListFragment(), "All")
        adapter.addFragment(JoinedJobListFragment(), "Joined")
        adapter.addFragment(StarredJobListFragment(), "Starred")
        adapter.addFragment(PastJobListFragment(), "Past")
        adapter.addFragment(NotificationFragment(), "Notifications")

        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)

        // Initialize Places.
        val apiKey = getString(R.string.google_maps_key)
        Places.initialize(this, apiKey)
        Places.createClient(this)
    }

    private fun setupNavigation() {
        nav_view.setNavigationItemSelectedListener(this)

        nav_view.getHeaderView(0)?.apply {
            myRepository.getUserByIdLiveData(auth.currentUser!!.uid) { user ->
                user.observe(this@JobDashboardActivity, object : ResourceObserver<User> {
                    override fun onSuccess(user: User?) {
                        user?.let { user ->
                            user.avatarUrl?.let { avatarUrl ->
                                GlideApp.with(this@JobDashboardActivity).load(myRepository.pathToReference(avatarUrl))
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
                //                navController.navigate(R.id.accountFragment)
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