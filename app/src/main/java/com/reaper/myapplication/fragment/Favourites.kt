package com.reaper.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.MySongsAdapter

class Favourites : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MySongsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        val itemsList= fetchData()

        recyclerView = view.findViewById(R.id.favouritesRecyclerView)
        adapter = MySongsAdapter(itemsList,context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        return view
    }

}