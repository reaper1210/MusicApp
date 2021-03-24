package com.reaper.myapplication.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.PlaylistInfo

class PlaylistDbAsyncTask(val context: Context, val songUri: String, val playlistInfo:PlaylistInfo, val mode: Int):AsyncTask<Void,Void,Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {

        val db = Room.databaseBuilder(context,PlaylistsDatabase::class.java,"playlists").fallbackToDestructiveMigration().build()

        fun getPlaylistOfSong(uri: String): PlaylistInfo? {
            val allPlaylists = db.PlaylistsDao().getPlaylists()
            if(allPlaylists.isNotEmpty()){
                for(element in allPlaylists){
                    val songList = element.songList.split(",").map{it.trim()}
                    if(!songList.isNullOrEmpty()){
                        for(j in songList.indices){
                            if(uri == songList[j]){
                                db.close()
                                return element
                            }
                        }
                    }
                }
            }
            return null
        }

        when(mode){

            1->{

                if(getPlaylistOfSong(songUri)!=null){
                    return true
                }
                return false

            }
            2-> {

                val songList = ArrayList<String>()
                songList.addAll(playlistInfo.songList.split(",").map{it.trim()})
                songList.remove("")
                if(!songList.contains(songUri)){
                    songList.add(songUri)
                    playlistInfo.songList = songList.joinToString(",")
                    db.PlaylistsDao().updatePlaylist(playlistInfo)
                }
                db.close()
                return true

            }
            3->{

                val playlist = getPlaylistOfSong(songUri)
                if(playlist!=null){
                    val songList = ArrayList<String>()
                    songList.addAll(playlist.songList.split(",").map{it.trim()})
                    songList.remove(songUri)
                    playlist.songList = songList.joinToString(",")
                    db.PlaylistsDao().updatePlaylist(playlist)
                    db.close()
                    return true
                }
                return false

            }
            4->{
                db.PlaylistsDao().insertPlaylist(playlistInfo)
                db.close()
                return true
            }
            5->{
                db.PlaylistsDao().deletePlaylist(playlistInfo)
                db.close()
                return true
            }
            else->{
                db.close()
                return false
            }

        }

    }



}