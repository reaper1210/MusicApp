package com.reaper.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.adapter.MySongs
import com.reaper.myapplication.R
import com.reaper.myapplication.utils.SongInfo

class OnlineSongs : Fragment() {
    lateinit var OnlinerecyclerView:RecyclerView
    val songs= ArrayList<SongInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)
        OnlinerecyclerView= view.findViewById(R.id.onlineRecyclerView)
        OnlinerecyclerView.layoutManager=LinearLayoutManager(this.context)
        val items= fetchData()
        val adapter: MySongs = MySongs(items,this.context)
        OnlinerecyclerView.adapter=adapter

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