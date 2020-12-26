package com.reaper.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.utils.PlaylistInfo

class PlaylistFragmentAdapter(val items: ArrayList<PlaylistInfo>):RecyclerView.Adapter<PlaylistFragmentAdapter.PlaylistFragmentViewHolder>(){

    class PlaylistFragmentViewHolder(View:View):RecyclerView.ViewHolder(View){
        val txtPlaylistName: TextView = View.findViewById(R.id.txtPlaylistName)
        val txtNoOfSongs: TextView = View.findViewById(R.id.txtNoOfSongs)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_fragment_single_row,parent,false)
        return PlaylistFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistFragmentViewHolder, position: Int) {
        val currentItem = items[position]
        holder.txtPlaylistName.text = currentItem.name
        holder.txtNoOfSongs.text = "10 Songs"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}