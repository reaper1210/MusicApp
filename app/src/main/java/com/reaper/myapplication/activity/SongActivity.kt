package com.reaper.myapplication.activity

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.reaper.myapplication.databinding.ActivitySongBinding
import java.time.Duration

class SongActivity : AppCompatActivity() {

    lateinit var favourites:ImageView
    lateinit var favourites_selected:ImageView
    lateinit var addToPlaylists:ImageView
    lateinit var addToPlaylistsSelected:ImageView
    lateinit var back:ImageView
    lateinit var play:ImageView
    lateinit var pause:ImageView
    lateinit var binding: ActivitySongBinding
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back= binding.btnBackSong

        favourites= binding.favourites
        favourites_selected=binding.favouritesSelected
        favourites_selected.visibility=View.GONE

        addToPlaylists=binding.addtoplaylists
        addToPlaylistsSelected=binding.addtoplaylistsselected
        addToPlaylistsSelected.visibility=View.GONE

        play=binding.play
        pause= binding.pause
        pause.visibility=View.GONE

        mediaPlayer= MediaPlayer()


        back.setOnClickListener {
//            val intent=Intent(this@SongActivity,MainActivity::class.java)
//            startActivity(intent)
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

        }

        pause.setOnClickListener {
            pause.visibility=View.INVISIBLE
            play.visibility=View.VISIBLE
        }


    }
}