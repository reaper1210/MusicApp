package com.reaper.myapplication.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.PlaylistSongsAdapter
import com.reaper.myapplication.database.PlayListById
import com.reaper.myapplication.database.RetrievePlaylists
import com.reaper.myapplication.databinding.ActivityMainBinding
import com.reaper.myapplication.databinding.ActivityPlaylistSongsBinding
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.PlaylistInfo
import java.io.File

class PlaylistSongs : AppCompatActivity() {
    lateinit var playlistRecyclerView: RecyclerView
    lateinit var binding: ActivityPlaylistSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_songs)

        val playlistId = intent.getIntExtra("playlist_id",-1)

        binding = ActivityPlaylistSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playlistRecyclerView = binding.playlistRecyclerView
        playlistRecyclerView.layoutManager = LinearLayoutManager(this@PlaylistSongs)

        val playlistInfo = PlayListById(this,playlistId).execute().get()
        val songUriList = playlistInfo.songList.split(",").map{it.trim()}

        val songInfoList = ArrayList<MySongInfo>()

        for(i in 0..songUriList.size){
            val songUri = Uri.parse(songUriList[i])
            val cursor = this.contentResolver.query(songUri,null,null,null,null,null)!!
            val name: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val mediaId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val s = MySongInfo(Integer.valueOf(mediaId),name,artist,"",songUri.toString())
            songInfoList.add(s)
        }

        val adapter = PlaylistSongsAdapter(songInfoList)
        playlistRecyclerView.adapter=adapter

    }
}
