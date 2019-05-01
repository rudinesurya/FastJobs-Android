package com.rud.fastjobs.view.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.rud.fastjobs.R
import com.rud.fastjobs.utils.FragmentLifecycle
import com.rud.fastjobs.utils.MyViewPagerAdapter
import com.rud.fastjobs.view.fragments.jobDetail.ChatRoomFragment
import com.rud.fastjobs.view.fragments.jobDetail.JobDetailFragment
import com.rud.fastjobs.view.fragments.jobDetail.ParticipantListFragment
import kotlinx.android.synthetic.main.activity_job_detail.*

class JobDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        setSupportActionBar(jobDetail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(JobDetailFragment(), "Job")
        adapter.addFragment(ChatRoomFragment(), "Chat")
        adapter.addFragment(ParticipantListFragment(), "Participants")

        viewpager.adapter = adapter
        tabs.setupWithViewPager(viewpager)

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var currentPosition = -1

            override fun onPageSelected(position: Int) {
                val newFrag = adapter.getItem(position) as FragmentLifecycle
                newFrag.onResumeFragment()

                if (currentPosition >= 0) {
                    val oldFrag = adapter.getItem(currentPosition) as FragmentLifecycle
                    oldFrag.onPauseFragment()
                }

                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.job_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_share -> {
            Toast.makeText(this, "Share", Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
