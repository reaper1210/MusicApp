package com.reaper.myapplication.utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistInfo(
        @PrimaryKey val id: Int,
        @ColumnInfo (name = "playlist_name") val name: String,
        @ColumnInfo (name = "song_list") var songList: String
)