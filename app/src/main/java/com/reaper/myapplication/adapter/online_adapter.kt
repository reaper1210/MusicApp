package com.reaper.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.R

class online_adapter(private val items:ArrayList<String>, val context: Context?): RecyclerView.Adapter<online_viewholder>() {
    val itemSize=items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): online_viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return online_viewholder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: online_viewholder, position: Int) {
        if(position<items.size){
            val currentSong=items[position]
            holder.TitleSongName.text=currentSong
            holder.cardView.setOnClickListener {
                Toast.makeText(context,"Theek hai bhai",Toast.LENGTH_LONG).show()
                val intent=Intent(context,SongActivity::class.java)
                context?.startActivity(intent)
            }
        }
    }
}

class online_viewholder(view:View):RecyclerView.ViewHolder(view){
    val TitleSongName:TextView=view.findViewById(R.id.TitleSongName)
    val cardView=view.findViewById<CardView>(R.id.onlineCardView)
}