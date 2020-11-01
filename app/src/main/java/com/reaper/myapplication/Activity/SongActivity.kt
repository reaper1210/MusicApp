package com.reaper.myapplication.Activity

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.reaper.myapplication.R

class SongActivity : AppCompatActivity() {
    lateinit var favourites:ImageView
    lateinit var favourites_selected:ImageView
    lateinit var addToPlaylists:ImageView
    lateinit var addToPlaylistsSelected:ImageView
    lateinit var back:ImageView
    lateinit var play:ImageView
    lateinit var pause:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        back=findViewById(R.id.back)

        favourites=findViewById(R.id.favourites)
        favourites_selected=findViewById(R.id.favourites_selected)
        favourites_selected.visibility=View.GONE

        addToPlaylists=findViewById(R.id.addtoplaylists)
        addToPlaylistsSelected=findViewById(R.id.addtoplaylistsselected)
        addToPlaylistsSelected.visibility=View.GONE

        play=findViewById(R.id.play)
        pause=findViewById(R.id.pause)
        pause.visibility=View.GONE

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
            play.visibility=View.GONE
            pause.visibility=View.VISIBLE
        }

        pause.setOnClickListener {
            pause.visibility=View.GONE
            play.visibility=View.VISIBLE
        }


    }
}