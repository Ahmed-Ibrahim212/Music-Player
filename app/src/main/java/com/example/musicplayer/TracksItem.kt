package com.example.musicplayer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TracksItem(
    val album: String,
    val artist: String,
    val artist_name: String,
    val artwork: String,
    val duration: String,
    val genre_name: String,
    val id: String,
    val name: String,
    val preview: String,
    val releasedate: Long,
    val sharelink: String,
    val source: String,
    val status: String
):Parcelable