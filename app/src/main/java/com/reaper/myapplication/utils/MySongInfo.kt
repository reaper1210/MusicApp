package com.reaper.myapplication.utils

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="songs")
data class MySongInfo(

    @PrimaryKey val id: Int,
    @ColumnInfo(name = "song_name") val name:String,
    @ColumnInfo(name = "song_artist") val artist:String,
    @ColumnInfo(name = "song_image") val image:String,
    @ColumnInfo(name = "song_uri") val  uri: String

)