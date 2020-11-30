package com.reaper.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.databinding.ActivitySongBinding
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.OnlineSongsInfo

class SongActivity : AppCompatActivity() {

    private lateinit var favourites:ImageView
    private lateinit var favourites_selected:ImageView
    private lateinit var addToPlaylists:ImageView
    private lateinit var addToPlaylistsSelected:ImageView
    private lateinit var back:ImageView
    private lateinit var play:ImageView
    private lateinit var pause:ImageView
    private lateinit var songName: TextView
    private lateinit var songArtist: TextView
    private lateinit var songImage: ImageView
    private lateinit var progressbarSongLoading: ProgressBar
    private lateinit var binding: ActivitySongBinding
    private lateinit var applic: MusicApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applic = application as MusicApplication
        back= binding.btnBackSong

        songName = binding.txtSongName
        songArtist = binding.txtSingerName
        songImage = binding.imgSongImage

        favourites= binding.favourites
        favourites_selected=binding.favouritesSelected
        favourites_selected.visibility=View.GONE

        addToPlaylists=binding.addtoplaylists
        addToPlaylistsSelected=binding.addtoplaylistsselected
        addToPlaylistsSelected.visibility=View.GONE

        play=binding.play
        pause= binding.pause
        pause.visibility=View.GONE
        progressbarSongLoading = binding.progressBarSongLoading
        if(intent.getBooleanExtra("isLoaded",false)){
            progressbarSongLoading.visibility = View.GONE
            play.visibility = View.INVISIBLE
            pause.visibility = View.VISIBLE
        }

        when {
            applic.currentOnlineSongsInfo != null -> {
                songName.text = applic.currentOnlineSongsInfo?.name
                songName.isSelected = true
                songArtist.text = applic.currentOnlineSongsInfo?.artist
                val imageUrl = applic.currentOnlineSongsInfo?.image
                Glide.with(this@SongActivity).load(imageUrl).error(R.drawable.music_image).into(songImage)
            }
            applic.currentMySongInfo != null -> {
                songName.text = applic.currentMySongInfo?.name
                songName.isSelected = true
                songArtist.text = applic.currentMySongInfo?.artist
            }
            else -> {
                Toast.makeText(this@SongActivity,"No Song Playing",Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }

        if(applic.mediaPlayer.isPlaying){
            progressbarSongLoading.visibility = View.GONE
        }

        applic.mediaPlayer.setOnPreparedListener {
            it.start()
            applic.musicIsPlaying = true
            progressbarSongLoading.visibility = View.GONE
        }

        back.setOnClickListener {
            onBackPressed()
        }

        favourites.setOnClickListener {
            favourites.visibility=View.GONE
            favourites_selected.visibility=View.VISIBLE
            Toast.makeText(this@SongActivity, "Added To Favourites", Toast.LENGTH_SHORT).show()
        }

        favourites_selected.setOnClickListener {
            favourites_selected.visibility=View.GONE
            favourites.visibility=View.VISIBLE
            Toast.makeText(this@SongActivity, "Removed From Favourites", Toast.LENGTH_SHORT).show()
        }

        addToPlaylists.setOnClickListener {
            addToPlaylists.visibility=View.GONE
            addToPlaylistsSelected.visibility=View.VISIBLE
            Toast.makeText(this@SongActivity, "Added To playlist", Toast.LENGTH_SHORT).show()
            val intent=Intent(this@SongActivity,PlaylistSongs::class.java)
            startActivity(intent)
        }

        play.setOnClickListener {
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
            applic.mediaPlayer.pause()
            applic.musicIsPlaying = false
        }

        pause.setOnClickListener {
            pause.visibility=View.INVISIBLE
            play.visibility=View.VISIBLE
            applic.mediaPlayer.start()
            applic.musicIsPlaying = true
        }
    }
}