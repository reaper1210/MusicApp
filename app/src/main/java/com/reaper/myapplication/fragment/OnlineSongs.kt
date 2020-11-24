package com.reaper.myapplication.fragment

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    lateinit var noInternetImage:ImageView
    lateinit var noInternetLayout:RelativeLayout
    private val songs= ArrayList<OnlineSongsInfo>()
    private val pontext = this.context

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val connectivityManager:ConnectivityManager
        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)
        onlinerecyclerView= view.findViewById(R.id.onlineRecyclerView)
        onlinerecyclerView.layoutManager=LinearLayoutManager(this.context)

        progressLayout = view.findViewById(R.id.progressLayoutOnlineSongs)
        progressbar = view.findViewById(R.id.progressBarOnlineSongs)
        noInternetLayout=view.findViewById(R.id.noInternetLayout)
        noInternetImage=view.findViewById(R.id.imgNoInternetImage)
        if(checkConnectivity(activity as Context)){

            noInternetImage.visibility=View.GONE
            noInternetLayout.visibility=View.GONE
            progressLayout.visibility=View.VISIBLE
            progressbar.visibility=View.VISIBLE

            LoadSongs()

        }

        else{

            progressLayout.visibility=View.GONE
            progressbar.visibility=View.GONE
            noInternetImage.visibility=View.VISIBLE
            noInternetImage.visibility=View.VISIBLE

        }

        return view
    }

    private fun LoadSongs(){

        val database = FirebaseDatabase.getInstance().reference

        val getData = object: ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                progressLayout.visibility=View.VISIBLE
                progressbar.visibility=View.VISIBLE
//                noInternetLayout.visibility=View.GONE
//                noInternetImage.visibility=View.GONE

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                progressLayout.visibility=View.GONE
                progressbar.visibility=View.GONE

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

    }
    fun checkConnectivity(context: Context?):Boolean{
        val connectivityManager= context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo?=connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected!=null){
            progressLayout.visibility=View.GONE
            progressbar.visibility=View.GONE
            return activeNetwork.isConnected
        }
        else{
            return false
        }
    }
}
