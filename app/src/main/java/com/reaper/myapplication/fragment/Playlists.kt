package com.reaper.myapplication.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.activity.PlaylistSongs
import com.reaper.myapplication.adapter.PlaylistFragmentAdapter
import com.reaper.myapplication.database.RetrievePlaylists
import com.reaper.myapplication.utils.PlaylistInfo

class Playlists : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_playlists, container, false)

        val applic = activity?.application as MusicApplication
        applic.playlistFragment = this

        recyclerView = view.findViewById(R.id.playlistFragmentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        applic.playlistInfo = ArrayList()
        applic.playlistInfo?.addAll(RetrievePlaylists(requireContext()).execute().get())

        applic.playlistFragPlaylistAdapter = PlaylistFragmentAdapter(this.requireContext(),null,applic.playlistInfo!!,applic)

        applic.playlistFragPlaylistAdapter?.SetOnItemClickListener(object: PlaylistFragmentAdapter.PlaylistDialogOnItemClickListener{
                override fun onItemClick(dialog: AlertDialog?, context: Context, view: View, playlistInfo: PlaylistInfo, position: Int) {
                    val intent = Intent(context, PlaylistSongs::class.java)
                    intent.putExtra("playlist_id",playlistInfo.id)
                    context.startActivity(intent)
                }
            }
        )
        recyclerView.adapter = applic.playlistFragPlaylistAdapter

        return view
    }

    fun playlistReload(){

        val currentFragment = activity?.supportFragmentManager?.findFragmentById(R.id.mainViewPager)
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()

        fragmentTransaction?.detach(currentFragment!!)?.attach(currentFragment)?.commitAllowingStateLoss()

    }

}