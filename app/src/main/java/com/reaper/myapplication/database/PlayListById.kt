package com.reaper.myapplication.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.reaper.myapplication.utils.PlaylistInfo

class PlayListById(val context: Context,val id: Int):AsyncTask<Void,Void,PlaylistInfo>() {
    override fun doInBackground(vararg params: Void?): PlaylistInfo {
        val db = Room.databaseBuilder(context,PlaylistsDatabase::class.java,"playlists").build()
        val playlistInfo = db.PlaylistsDao().getPlaylistById(id)
        db.close()
        return playlistInfo!!
    }
}