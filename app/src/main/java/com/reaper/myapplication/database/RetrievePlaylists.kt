package com.reaper.myapplication.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.reaper.myapplication.utils.PlaylistInfo

class RetrievePlaylists(val context: Context): AsyncTask<Void,Void,List<PlaylistInfo>>() {
    override fun doInBackground(vararg params: Void?): List<PlaylistInfo> {
        val db = Room.databaseBuilder(context,PlaylistsDatabase::class.java,"playlists").build()
        val playlists = db.PlaylistsDao().getPlaylists()
        db.close()
        println("Hello ${playlists.size}")
        return playlists
    }
}