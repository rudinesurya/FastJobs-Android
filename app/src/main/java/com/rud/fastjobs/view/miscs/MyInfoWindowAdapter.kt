package com.rud.fastjobs.view.miscs

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.rud.fastjobs.R
import com.rud.fastjobs.data.model.InfoWindowData
import com.rud.fastjobs.utils.toLocalDateTime
import kotlinx.android.synthetic.main.info_window.view.*

class MyInfoWindowAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

    override fun getInfoContents(marker: Marker?): View {
        val v = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)
        val infoWindowData: InfoWindowData? = marker?.tag as InfoWindowData?

        if (infoWindowData?.job != null) {
            val job = infoWindowData.job
            v.text_title.text = job.title
            v.text_date.text = job.date?.toLocalDateTime().toString()
        } else {
            v.text_title.text = infoWindowData?.locationName
            v.text_date.text = infoWindowData?.locationHours
        }

        return v
    }
}