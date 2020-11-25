package com.reaper.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reaper.myapplication.R
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.utils.OnlineSongsInfo

class OnlineSongsAdapter(private val itemList: ArrayList<OnlineSongsInfo>, val context: Context):RecyclerView.Adapter<OnlineSongsAdapter.OnlineSongsViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    class OnlineSongsViewHolder(view:View): RecyclerView.ViewHolder(view){
        val songName: TextView = view.findViewById(R.id.txtSongName)
        val artist: TextView = view.findViewById(R.id.txtDuration)
        val cardView: CardView = view.findViewById(R.id.onlineCardView)
        var  image:ImageView=view.findViewById(R.id.imgSongOnline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.online_single_row,parent,false)
        return OnlineSongsViewHolder(view)
    }

    public interface OnItemClickListener{
        fun onItemClick(view: View, songsInfo: OnlineSongsInfo, position: Int)
    }

    public fun SetOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    override fun onBindViewHolder(holder: OnlineSongsViewHolder, position: Int) {
        val songInfo=itemList[position]
        holder.songName.text = songInfo.name
        holder.artist.text=songInfo.artist
        holder.image.setImageResource(R.drawable.music_image)
        Glide.with(context).load(songInfo.image).into(holder.image)
        holder.cardView.setOnClickListener {
            if(onItemClickListener!=null){
                onItemClickListener?.onItemClick(it,songInfo,position)
            }
            val intent= Intent(context, SongActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}