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

class MySongsAdapter(val items: ArrayList<String>, val context: Context?): RecyclerView.Adapter<MySongsViewHolder>() {

    val itemSize=items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySongsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return MySongsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MySongsViewHolder, position: Int) {
        if(position<items.size){
            val currentSong=items[position]
            holder.titleSongName.text=currentSong
            holder.cardView.setOnClickListener {
                Toast.makeText(context,"Theek hai bhai",Toast.LENGTH_LONG).show()
                val intent=Intent(context,SongActivity::class.java)
                context?.startActivity(intent)
            }
        }
    }
}

class MySongsViewHolder(view:View):RecyclerView.ViewHolder(view){
    val titleSongName:TextView=view.findViewById(R.id.txtSongName)
    val cardView=view.findViewById<CardView>(R.id.onlineCardView)
}