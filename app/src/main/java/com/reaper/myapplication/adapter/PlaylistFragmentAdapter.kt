package com.reaper.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.R
import com.reaper.myapplication.utils.PlaylistInfo

class PlaylistFragmentAdapter(val context: Context,val dialog: AlertDialog?,private val items: ArrayList<PlaylistInfo>):RecyclerView.Adapter<PlaylistFragmentAdapter.PlaylistFragmentViewHolder>(){

    var onItemClickListener: PlaylistDialogOnItemClickListener? = null

    interface PlaylistDialogOnItemClickListener{
        fun onItemClick(dialog: AlertDialog?, context: Context, view: View, playlistInfo: PlaylistInfo, position: Int)
    }

    fun SetOnItemClickListener(onItemClickListener: PlaylistDialogOnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    class PlaylistFragmentViewHolder(View:View):RecyclerView.ViewHolder(View){
        val cardView: CardView = View.findViewById(R.id.PlaylistCardView)
        val txtPlaylistName: TextView = View.findViewById(R.id.txtPlaylistName)
        val txtNoOfSongs: TextView = View.findViewById(R.id.txtNoOfSongs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_fragment_single_row,parent,false)
        return PlaylistFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistFragmentViewHolder, position: Int) {
        val currentItem = items[position]
        holder.txtPlaylistName.text = currentItem.name
        val songList = currentItem.songList.split(",").map{it.trim()}
        holder.txtNoOfSongs.text = songList.size.toString()
        if(songList.contains("")){
            holder.txtNoOfSongs.text = "0"
        }
        holder.cardView.setOnClickListener {
            if(onItemClickListener!=null){
                onItemClickListener?.onItemClick(dialog,context,it,currentItem,position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}