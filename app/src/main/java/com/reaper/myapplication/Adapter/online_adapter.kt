package com.reaper.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R

class online_adapter(val items:ArrayList<String>): RecyclerView.Adapter<online_viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): online_viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return online_viewholder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: online_viewholder, position: Int) {
        val currentSong=items[position]
        holder.TitleSongName.text=currentSong


    }

}

class online_viewholder(itemView:View):RecyclerView.ViewHolder(itemView){
    val TitleSongName:TextView=itemView.findViewById(R.id.TitleSongName)
}