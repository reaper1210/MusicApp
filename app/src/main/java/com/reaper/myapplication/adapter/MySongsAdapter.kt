package com.reaper.myapplication.adapter

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.R
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.OnlineSongsInfo

class MySongsAdapter(val items: ArrayList<MySongInfo>, val context: Context?): RecyclerView.Adapter<MySongsViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySongsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return MySongsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: MySongsAdapter, songInfo: MySongInfo, position: Int)
    }

    fun SetOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    override fun onBindViewHolder(holder: MySongsViewHolder, position: Int) {
        if(position<items.size){
            val currentSong=items[position]
            holder.titleSongName.text=currentSong.name
            holder.titleSongName.isSelected = true
            holder.artist.text=currentSong.artist
            holder.cardView.setOnClickListener {
                if(onItemClickListener!=null){
                    this.onItemClickListener?.onItemClick(this,currentSong,position)
                }
            }
        }
    }

}

class MySongsViewHolder(view:View):RecyclerView.ViewHolder(view){
    val titleSongName:TextView=view.findViewById(R.id.txtSongName)
    val artist:TextView=view.findViewById(R.id.txtDuration)
    val cardView:CardView=view.findViewById(R.id.onlineCardView)
}
