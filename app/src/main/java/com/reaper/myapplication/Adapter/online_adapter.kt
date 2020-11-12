package com.reaper.myapplication.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R

class online_adapter(val items:ArrayList<String>): RecyclerView.Adapter<online_viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): online_viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return online_viewholder(view)
    }

    override fun getItemCount(): Int {
        return items.size+1
    }

    override fun onBindViewHolder(holder: online_viewholder, position: Int) {
        if(position<items.size){
            val currentSong=items[position]
            holder.TitleSongName.text=currentSong
        }
        else{
            println("Hello $position")
            holder.cardView.alpha= 0F
        }
    }

}

class online_viewholder(view:View):RecyclerView.ViewHolder(view){
    val TitleSongName:TextView=view.findViewById(R.id.TitleSongName)
    val cardView=view.findViewById<CardView>(R.id.onlineCardView)
}