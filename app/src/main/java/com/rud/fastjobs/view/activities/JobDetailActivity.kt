package com.rud.fastjobs.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rud.fastjobs.R
import kotlinx.android.synthetic.main.activity_job_detail.*


class JobDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        setSupportActionBar(jobDetail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}
