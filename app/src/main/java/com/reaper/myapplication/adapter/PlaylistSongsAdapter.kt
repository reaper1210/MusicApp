package com.reaper.myapplication.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.PlaylistInfo

class PlaylistSongsAdapter(val items:ArrayList<MySongInfo>): RecyclerView.Adapter<PlaylistSongsAdapter.PlaylistViewHolder>() {

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
        holder.txtPlaylistSongName.text = currentItem.name
        holder.txtPlaylistSongDuration.text = "00"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}