package com.reaper.myapplication.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.OnlineSongsAdapter
import com.reaper.myapplication.utils.OnlineSongsInfo


class OnlineSongs : Fragment() {
    private lateinit var onlinerecyclerView:RecyclerView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var progressbar: ProgressBar
    private lateinit var progressLayout: RelativeLayout
    private val songs= ArrayList<OnlineSongsInfo>()
    private val pontext = this.context

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)
        onlinerecyclerView= view.findViewById(R.id.onlineRecyclerView)
        onlinerecyclerView.layoutManager=LinearLayoutManager(this.context)

        progressLayout = view.findViewById(R.id.progressLayoutOnlineSongs)
        progressbar = view.findViewById(R.id.progressBarOnlineSongs)
        progressLayout.visibility = View.VISIBLE
        LoadSongs()

        return view
    }

    private fun LoadSongs(){

        val database = FirebaseDatabase.getInstance().reference

        val getData = object: ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                songs.clear()

                val song = snapshot.child("songs")
                for(i in song.children){

                    val songName = i.child("SongName").value.toString()
                    val songArtist = i.child("artist").value.toString()
                    val songUrl = i.child("url").value.toString()

                    val songInfo = OnlineSongsInfo(songName,songArtist,songUrl)

                    songs.add(songInfo)

                }
                val songAdapter=OnlineSongsAdapter(songs,pontext)
                onlinerecyclerView.adapter=songAdapter

                songAdapter.SetOnItemClickListener(object : OnlineSongsAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, songInfo: OnlineSongsInfo, position: Int) {
                        mediaPlayer=MediaPlayer()
                        mediaPlayer.setDataSource(songInfo.url)
                        mediaPlayer.prepareAsync()
                        mediaPlayer.setOnPreparedListener { mediaPlayer.start() }
                    }
                })
            }
        }

        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)
        progressLayout.visibility = View.GONE

    }

}
