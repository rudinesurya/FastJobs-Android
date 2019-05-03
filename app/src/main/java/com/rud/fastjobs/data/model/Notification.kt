package com.rud.fastjobs.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(tableName = "notifications")
@Parcelize
data class Notification(
    val title: String,
    val message: String,
    // val postDate: java.sql.Timestamp,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
) : Serializable, Parcelable