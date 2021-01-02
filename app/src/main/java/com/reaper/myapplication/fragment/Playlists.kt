package com.reaper.myapplication.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.PlaylistFragmentAdapter
import com.reaper.myapplication.database.RetrievePlaylists
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.PlaylistInfo

class Playlists : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaylistFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_playlists, container, false)
        recyclerView = view.findViewById(R.id.playlistFragmentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val items = ArrayList<PlaylistInfo>()
        items.addAll(RetrievePlaylists(requireContext()).execute().get())

        adapter = PlaylistFragmentAdapter(this.requireContext(),null,items)
        recyclerView.adapter = adapter

        return view
    }

}

private fun fetchData(): ArrayList<PlaylistInfo> {
    val list = ArrayList<PlaylistInfo>()
    for(i in 0 until 10){
        val songList = ArrayList<String>()
        val playlistInfo = PlaylistInfo(i,"playlist $i", songList.joinToString(","))
        list.add(playlistInfo)
    }
    return list
}