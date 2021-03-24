package com.reaper.myapplication.activity

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.MySongsAdapter
import com.reaper.myapplication.database.PlayListById
import com.reaper.myapplication.databinding.ActivityPlaylistSongsBinding
import com.reaper.myapplication.utils.MySongInfo
import java.time.Duration

class PlaylistSongs : AppCompatActivity() {
    lateinit var playlistRecyclerView: RecyclerView
    lateinit var playlistName: TextView
    lateinit var totalSongs: TextView
    lateinit var totalDurationHours: TextView
    lateinit var totalDurationMinutes: TextView
    lateinit var totalDurationSeconds: TextView
    lateinit var binding: ActivityPlaylistSongsBinding
    lateinit var applic: MusicApplication
    lateinit var act: MainActivity
    var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_songs)

        flag = 0

        applic = this.application as MusicApplication
        act = applic.mainActivity!!
        val playlistId = intent.getIntExtra("playlist_id",-1)

        binding = ActivityPlaylistSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var duration: Long = 0

        playlistRecyclerView = binding.playlistRecyclerView
        playlistRecyclerView.layoutManager = LinearLayoutManager(this@PlaylistSongs)

        val playlistInfo = PlayListById(this,playlistId).execute().get()
        val songUriList = playlistInfo.songList.split(",").map{it.trim()}

        val songList = ArrayList<MySongInfo>()

        if(songUriList[0]!=""){
            for(i in 0..songUriList.size-1){
                val songUri = Uri.parse(songUriList[i])
                val cursor = this.contentResolver.query(songUri,null,null,null,null,null)!!
                cursor.moveToFirst()
                val name: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val mediaId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val s = MySongInfo(Integer.valueOf(mediaId),name,artist,"",songUri.toString())
                songList.add(s)

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(applicationContext,songUri)
                val stringDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val intDuration = Integer.parseInt(stringDuration) / 1000
                duration += intDuration
            }
        }
        else{
            Toast.makeText(this,"Playlist Empty", Toast.LENGTH_SHORT).show()
        }

        val adapter = MySongsAdapter(songList,this)
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

        playlistName = findViewById(R.id.txtPlaylistName)
        playlistName.text=playlistInfo.name

        totalSongs = findViewById(R.id.txtTotalSongs)
        totalSongs.text= songList.size.toString()

        var seconds = duration
        var minutes = duration/60
        val hours = duration/3600

        minutes -= hours*60
        seconds -= minutes*60

        totalDurationHours = findViewById(R.id.txtTotalDurationHours)
        totalDurationHours.text = hours.toString()

        totalDurationMinutes = findViewById(R.id.txtTotalDurationMinutes)
        totalDurationMinutes.text = minutes.toString()

        totalDurationSeconds = findViewById(R.id.txtTotalDurationSeconds)
        totalDurationSeconds.text = seconds.toString()

    }

    override fun onResume() {
        flag++
        if(flag>1){
            this.recreate()
        }
        super.onResume()
    }

}
