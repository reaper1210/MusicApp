package com.reaper.myapplication.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.Activity.SongActivity
import com.reaper.myapplication.Fragment.OnlineSongs
import com.reaper.myapplication.R

class online_adapter(val items:ArrayList<String>): RecyclerView.Adapter<online_viewholder>() {
    val itemSize=items.size
    lateinit var cardView:CardView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): online_viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return online_viewholder(view)
    }

    override fun getItemCount(): Int {
        return items.size+1
    }

    override fun onBindViewHolder(holder: online_viewholder, position: Int) {

        if(position<itemSize){
            val currentSong=items[position]
            holder.TitleSongName.text=currentSong
            holder.cardView.setOnClickListener {
                Toast.makeText(this as Context, "theek hai bhai", Toast.LENGTH_SHORT).show()
//                val intent= Intent(,SongActivity::class.java)
//                startActivity(intent)
            }
        }
         else
           {
            println("Hello $position")
            holder.cardView.alpha= 0F
        }
    }

}

class online_viewholder(view:View):RecyclerView.ViewHolder(view){
    val TitleSongName:TextView=view.findViewById(R.id.TitleSongName)
    val cardView=view.findViewById<CardView>(R.id.onlineCardView)
}