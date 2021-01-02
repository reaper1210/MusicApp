package com.reaper.myapplication.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.reaper.myapplication.utils.MySongInfo

class RetrieveFavourites(val context: Context): AsyncTask<Void, Void, List<MySongInfo>>() {
    override fun doInBackground(vararg params: Void?): List<MySongInfo> {
        val db = Room.databaseBuilder(context,FavouriteDatabase::class.java,"fav-songs").build()
        val list = db.songDao().getAllSongs()
        db.close()
        return list
    }
}