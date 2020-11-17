package com.reaper.myapplication.fragment

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.adapter.MySongsAdapter
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.OnlineSongsAdapter
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.OnlineSongsInfo

class OnlineSongs : Fragment() {
    lateinit var OnlinerecyclerView:RecyclerView
    lateinit var cardView: CardView
    lateinit var mediaPlayer: MediaPlayer
    val songs= ArrayList<OnlineSongsInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val songsInfo=OnlineSongsInfo("Cheap Thrills",50000,"Sia","","https://genius.com/Sia-chandelier-lyrics")
        songs.add(songsInfo)
        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)
        OnlinerecyclerView= view.findViewById(R.id.onlineRecyclerView)
        OnlinerecyclerView.layoutManager=LinearLayoutManager(this.context)
        val adapter: OnlineSongsAdapter = OnlineSongsAdapter(songs,this.context)
        OnlinerecyclerView.adapter=adapter

        adapter.SetOnItemClickListener(object:OnlineSongsAdapter.OnItemClickListener {
            override fun onItemClick(view: View, songsInfo: OnlineSongsInfo, position: Int) {
                mediaPlayer=MediaPlayer()
                mediaPlayer.setDataSource(songsInfo.url)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener(object :MediaPlayer.OnPreparedListener{
                    override fun onPrepared(p0: MediaPlayer?) {
                        mediaPlayer.start()
                    }

                })
            }

        })


        return view
    }
}

 fun fetchData():ArrayList<String>{
    val list=ArrayList<String>()
    for(i in 1 until 50){
        list.add("Song $i")
    }
    return list
}