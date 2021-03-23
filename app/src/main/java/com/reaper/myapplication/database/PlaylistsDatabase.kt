package com.reaper.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reaper.myapplication.utils.PlaylistInfo

@Database(entities = [PlaylistInfo::class],version = 1)
 abstract class PlaylistsDatabase: RoomDatabase() {

     abstract fun PlaylistsDao():PlaylistsDao

}