package com.reaper.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reaper.myapplication.utils.MySongInfo

@Database(entities = [MySongInfo::class],version = 3)
abstract class FavouriteDatabase: RoomDatabase() {

    abstract fun songDao(): SongDao

}