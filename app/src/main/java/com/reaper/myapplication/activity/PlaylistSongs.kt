 package com.reaper.myapplication.activity

import android.content.Intent
import android.media.Image
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.MySongsAdapter
import com.reaper.myapplication.database.PlayListById
import com.reaper.myapplication.databinding.ActivityPlaylistSongsBinding
import com.reaper.myapplication.utils.MySongInfo
import kotlinx.android.synthetic.main.activity_playlist_songs.*
import java.time.Duration

class PlaylistSongs : AppCompatActivity() {
    lateinit var playlistRecyclerView: RecyclerView
    lateinit var playlistName: TextView
    lateinit var totalSongs: TextView
    lateinit var totalDurationHours: TextView
    lateinit var totalDurationMinutes: TextView
    lateinit var totalDurationSeconds: TextView
    lateinit var dragUpButtonPlaylist: ImageView
    lateinit var dragDownbuttonPlaylist: ImageView
    lateinit var ellipsePlaylist: ImageView
    lateinit var playPlaylistBtn: ImageView
    lateinit var pausePlaylistBtn: ImageView
    lateinit var txtSongNamePlaylist: TextView
    lateinit var txtArtistPlaylist: TextView
    lateinit var relativeGroup:RelativeLayout
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

        dragUpButtonPlaylist = binding.dragUpButtonPlaylist
        dragUpButtonPlaylist.visibility = View.INVISIBLE

        dragDownbuttonPlaylist = binding.dragDownButtonPlaylist
        dragDownbuttonPlaylist.visibility = View.INVISIBLE

        ellipsePlaylist = binding.ellipsePlaylist
        ellipsePlaylist.visibility = View.GONE

        playPlaylistBtn = binding.playPlaylist
        playPlaylistBtn.visibility = View.INVISIBLE

        pausePlaylistBtn = binding.pausePlaylist
        pausePlaylistBtn.visibility = View.INVISIBLE

        txtSongNamePlaylist = binding.txtSongNamePlaylist
        txtSongNamePlaylist.visibility = View.INVISIBLE

        txtArtistPlaylist = binding.txtArtistPlaylist
        txtArtistPlaylist.visibility = View.INVISIBLE

        relativeGroup = binding.relativeGroup

        when{
            applic.currentMySongInfo!=null -> {
                txtSongNamePlaylist.text = applic.currentMySongInfo?.name
                txtSongNamePlaylist.isSelected = true
                val artist = applic.currentMySongInfo?.artist
                txtArtistPlaylist.text = artist
                dragUpButtonPlaylist.visibility = View.VISIBLE
            }
            else -> {
                dragUpButtonPlaylist.visibility= View.GONE
                pausePlaylistBtn.visibility = View.GONE
            }
        }

        dragUpButtonPlaylist.setOnClickListener {
            dragUpButtonPlaylist.animate().apply {
                duration = 300
                rotationXBy(360f)
            }.withEndAction {
                val transition: Transition = Slide(Gravity.BOTTOM)
                transition.duration = 300
                transition.addTarget(R.id.onlineEllipse)
                transition.addTarget(R.id.onlinePlay)
                transition.addTarget(R.id.txtSongName)
                transition.addTarget(R.id.txtDuration)
                transition.addTarget(R.id.dragDownButton)
                TransitionManager.beginDelayedTransition(relativeGroup, transition)
                dragUpButtonPlaylist.visibility=View.GONE
                txtSongNamePlaylist.visibility = View.VISIBLE
                txtSongNamePlaylist.isSelected = true
                txtArtistPlaylist.visibility = View.VISIBLE
                ellipsePlaylist.visibility = View.VISIBLE
                if(applic.musicIsPlaying){
                    playPlaylistBtn.visibility=View.VISIBLE
                    pausePlaylistBtn.visibility=View.GONE
                }
                else{
                    playPlaylistBtn.visibility=View.GONE
                    pausePlaylistBtn.visibility=View.VISIBLE
                }
                dragDownButtonPlaylist.visibility = View.VISIBLE
            }
        }

        dragDownbuttonPlaylist.setOnClickListener {
            dragDownbuttonPlaylist.animate().apply {
                duration=300
                rotationXBy(360f)
            }.withEndAction {
                val transition: Transition = Slide(Gravity.BOTTOM)
                transition.duration = 300
                transition.addTarget(R.id.onlineEllipse)
                transition.addTarget(R.id.onlinePlay)
                transition.addTarget(R.id.txtSongName)
                transition.addTarget(R.id.txtDuration)
                transition.addTarget(R.id.dragDownButton)
                TransitionManager.beginDelayedTransition(relativeGroup, transition)
                txtSongNamePlaylist.visibility=View.GONE
                txtArtistPlaylist.visibility=View.GONE
                dragUpButtonPlaylist.visibility=View.VISIBLE
                ellipsePlaylist.visibility=View.GONE
                playPlaylistBtn.visibility=View.GONE
                pausePlaylistBtn.visibility= View.GONE
                dragDownbuttonPlaylist.visibility=View.GONE
            }
        }

        playPlaylistBtn.setOnClickListener {
            playPlaylistBtn.visibility=View.GONE
            pausePlaylistBtn.visibility=View.VISIBLE
            applic.mediaPlayer.pause()
            applic.musicIsPlaying = false
        }

        pausePlaylistBtn.setOnClickListener {
            pausePlaylistBtn.visibility=View.GONE
            playPlaylistBtn.visibility= View.VISIBLE
            applic.mediaPlayer.start()
            applic.musicIsPlaying = true
        }

        ellipsePlaylist.setOnClickListener {
            dragDownbuttonPlaylist.callOnClick()
            val intent = Intent(this@PlaylistSongs, SongActivity::class.java)
            intent.putExtra("isLoaded",true)
            startActivity(intent)
        }

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

    override fun onBackPressed() {
        applic.mainActivity?.dragDownButton?.callOnClick()
        applic.mainActivity?.dragUpButton?.callOnClick()
        super.onBackPressed()
    }
}
