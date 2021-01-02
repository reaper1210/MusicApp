package com.reaper.myapplication.database

import androidx.room.*
import com.reaper.myapplication.utils.PlaylistInfo

@Dao
interface PlaylistsDao {

    @Insert
    fun insertPlaylist(playlistInfo: PlaylistInfo)

    @Delete
    fun deletePlaylist(playlistInfo: PlaylistInfo)

    @Query("SELECT * FROM playlists")
    fun getPlaylists(): List<PlaylistInfo>

    @Query("SELECT * FROM playlists WHERE id= :playlistId")
    fun getPlaylistById(playlistId:Int): PlaylistInfo?

    @Update
    fun updatePlaylist(playlistInfo: PlaylistInfo)

}