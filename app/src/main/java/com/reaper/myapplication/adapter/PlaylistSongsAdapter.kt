package com.reaper.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R

class PlaylistSongsAdapter(val items:ArrayList<String>): RecyclerView.Adapter<PlaylistSongsAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.onlineCardView)
        val txtPlaylistSongName: TextView = itemView.findViewById(R.id.txtSongName)
        val txtPlaylistSongDuration: TextView = itemView.findViewById(R.id.txtDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val currentItem = items[position]
        holder.txtPlaylistSongName.text = currentItem
        holder.txtPlaylistSongDuration.text = "0000"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}