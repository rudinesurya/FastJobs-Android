package com.rud.fastjobs.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rud.fastjobs.R
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
    }
}
