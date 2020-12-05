package com.reaper.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.PlaylistSongsAdapter
import com.reaper.myapplication.databinding.ActivityMainBinding
import com.reaper.myapplication.databinding.ActivityPlaylistSongsBinding

class PlaylistSongs : AppCompatActivity() {
    lateinit var playlistRecyclerView: RecyclerView
    lateinit var binding: ActivityPlaylistSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_songs)

        binding = ActivityPlaylistSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playlistRecyclerView = binding.playlistRecyclerView
        playlistRecyclerView.layoutManager = LinearLayoutManager(this@PlaylistSongs)

        val items = fetchData()
        val adapter:PlaylistSongsAdapter = PlaylistSongsAdapter(items)
        playlistRecyclerView.adapter=adapter

    }
}

private fun fetchData(): ArrayList<String> {
    val list = ArrayList<String>()
    for(i in 0 until 10){
        list.add("Playlist $i")
    }
    return list
}