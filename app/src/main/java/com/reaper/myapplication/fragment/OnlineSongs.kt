package com.reaper.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.adapter.online_adapter
import com.reaper.myapplication.R

class OnlineSongs : Fragment() {
    lateinit var recyclerView:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)
        recyclerView= view.findViewById(R.id.onlineRecyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this.context)

        val items= fetchData()
        val adapter:online_adapter= online_adapter(items,this.context)
        recyclerView.adapter=adapter

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