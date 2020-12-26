package com.reaper.myapplication.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.reaper.myapplication.utils.MySongInfo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DbAsyncTask (val context: Context, private val songInfo: MySongInfo, private val mode: Int): AsyncTask<Void, Void, Boolean>() {

    private val db = Room.databaseBuilder(context,FavouriteDatabase::class.java,"fav-songs").fallbackToDestructiveMigration().build()

    override fun doInBackground(vararg params: Void?): Boolean {
        when(mode){

            1->{

                val song: MySongInfo? = db.songDao().getSongById(songInfo.id)
                db.close()
                return song!=null

            }
            2->{

                db.songDao().insertSong(songInfo)
                db.close()
                return true

            }
            3->{

                db.songDao().deleteSong(songInfo)
                db.close()
                return true

            }

            else -> return false

        }
    }

}