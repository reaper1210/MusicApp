package com.reaper.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.utils.OnlineSongsInfo

class OnlineSongsAdapter(private val itemList: ArrayList<OnlineSongsInfo>, val context: Context?):RecyclerView.Adapter<OnlineSongsAdapter.OnlineSongsViewHolder>() {

    class OnlineSongsViewHolder(view:View): RecyclerView.ViewHolder(view){
        val songName: TextView = view.findViewById(R.id.txtSongName)
        val duration: TextView = view.findViewById(R.id.txtDuration)
        val cardView: CardView = view.findViewById(R.id.onlineCardView)
        val image: ImageView = view.findViewById(R.id.imgSongOnline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return OnlineSongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnlineSongsViewHolder, position: Int) {
        holder.songName.text = itemList[position].name
        holder.duration.text = itemList[position].duration.toString()
        holder.image.setImageResource(R.drawable.music_image)
        holder.cardView.setOnClickListener {
            Toast.makeText(context,"Theek hai bhai", Toast.LENGTH_LONG).show()
            val intent= Intent(context, SongActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}