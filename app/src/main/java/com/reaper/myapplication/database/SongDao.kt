package com.reaper.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.reaper.myapplication.utils.MySongInfo

@Dao
interface SongDao {

    @Insert
    fun insertSong(songInfo: MySongInfo)

    @Delete
    fun deleteSong(songInfo: MySongInfo)

    @Query("SELECT * FROM songs")
    fun getAllSongs(): List<MySongInfo>

    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSongById(songId: Int): MySongInfo?

}