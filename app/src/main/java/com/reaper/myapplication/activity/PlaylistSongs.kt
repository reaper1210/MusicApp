package com.reaper.myapplication.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.MySongsAdapter
import com.reaper.myapplication.database.PlayListById
import com.reaper.myapplication.databinding.ActivityPlaylistSongsBinding
import com.reaper.myapplication.utils.MySongInfo

class PlaylistSongs : AppCompatActivity() {
    lateinit var playlistRecyclerView: RecyclerView
    lateinit var binding: ActivityPlaylistSongsBinding
    lateinit var applic: MusicApplication
    lateinit var act: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_songs)

        applic = this.application as MusicApplication
        act = applic.mainActivity!!
        val playlistId = intent.getIntExtra("playlist_id",-1)

        binding = ActivityPlaylistSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playlistRecyclerView = binding.playlistRecyclerView
        playlistRecyclerView.layoutManager = LinearLayoutManager(this@PlaylistSongs)

        val playlistInfo = PlayListById(this,playlistId).execute().get()
        val songUriList = playlistInfo.songList.split(",").map{it.trim()}

        val songInfoList = ArrayList<MySongInfo>()

        for(i in 0..songUriList.size-1){
            val songUri = Uri.parse(songUriList[i])
            val cursor = this.contentResolver.query(songUri,null,null,null,null,null)!!
            cursor.moveToFirst()
            val name: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val mediaId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val s = MySongInfo(Integer.valueOf(mediaId),name,artist,"",songUri.toString())
            songInfoList.add(s)
        }

        val adapter = MySongsAdapter(songInfoList,this)
        adapter.SetOnItemClickListener(object : MySongsAdapter.OnItemClickListener {

            override fun onItemClick(view: MySongsAdapter, songInfo: MySongInfo, position: Int) {
                if(songInfo==applic.currentMySongInfo){
                    act.onlineEllipse.callOnClick()
                }
                else{
                    applic.mediaPlayer.stop()
                    applic.mediaPlayer.reset()
                    applic.mediaPlayer.setDataSource(this@PlaylistSongs, Uri.parse(songInfo.uri))
                    applic.mediaPlayer.prepareAsync()
                    applic.mediaPlayer.setOnPreparedListener {
                        it.start()
                        applic.musicIsPlaying = true
                    }
                    act.txtSongName.text = songInfo.name
                    act.txtSongArtist.text = songInfo.artist
                    applic.currentOnlineSongsInfo = null
                    applic.currentMySongInfo = songInfo
                    val intent= Intent(this@PlaylistSongs, SongActivity::class.java)
                    intent.putExtra("isLoaded",false)
                    this@PlaylistSongs.startActivity(intent)
                }
            }
        })
        playlistRecyclerView.adapter=adapter

    }
}
