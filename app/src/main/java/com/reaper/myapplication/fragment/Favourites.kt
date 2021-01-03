package com.reaper.myapplication.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.activity.MainActivity
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.adapter.MySongsAdapter
import com.reaper.myapplication.database.RetrieveFavourites
import com.reaper.myapplication.utils.MySongInfo

class Favourites : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private val pontext = context
    private lateinit var applic: MusicApplication
    private lateinit var act: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        applic = activity?.application as MusicApplication
        act = activity as MainActivity

        recyclerView = view.findViewById(R.id.favouritesRecyclerView)
        progressLayout = view.findViewById(R.id.favProgressLayout)
        progressBar = view.findViewById(R.id.favProgressBar)

        applic.noFavLayout = view.findViewById(R.id.noFavLayout)
        applic.noFavLayout?.visibility = View.GONE

        checkPermission()


        return view
    }

    private fun checkPermission() {
        if(Build.VERSION.SDK_INT >= 23){
            if(pontext?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                this.requestPermissions(permissions, 116)
                loadSongs()
            }
            else{
                Toast.makeText(this.context,"Error bahenchod", Toast.LENGTH_LONG).show()
            }
        }
        else{
            loadSongs()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==116){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                progressLayout.visibility=View.GONE
                progressBar.visibility=View.GONE
                loadSongs()
            }
            else{
                Toast.makeText(this.context,"Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this.context,"Permission Leni chahiye na", Toast.LENGTH_LONG).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadSongs(){
        applic.favSongs.clear()

        applic.favSongs = RetrieveFavourites(this.requireContext()).execute().get() as ArrayList<MySongInfo>
        if(applic.favSongs.isEmpty()){
            applic.noFavLayout?.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        applic.favAdapter = MySongsAdapter(applic.favSongs,this.requireContext())
        layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = applic.favAdapter
        recyclerView.layoutManager = layoutManager

        applic.favAdapter?.SetOnItemClickListener(object : MySongsAdapter.OnItemClickListener {

            override fun onItemClick(view: MySongsAdapter, songInfo: MySongInfo, position: Int) {
                if(songInfo == applic.currentMySongInfo){
                    act.onlineEllipse.callOnClick()
                }
                else{
                    applic.mediaPlayer.stop()
                    applic.mediaPlayer.reset()
                    applic.mediaPlayer.setDataSource(activity!!, Uri.parse(songInfo.uri))
                    applic.mediaPlayer.prepareAsync()
                    applic.mediaPlayer.setOnPreparedListener {
                        it.start()
                        applic.musicIsPlaying = true
                    }
                    act.txtSongName.text = songInfo.name
                    act.txtSongArtist.text = songInfo.artist
                    applic.currentOnlineSongsInfo = null
                    applic.currentMySongInfo = songInfo
                    val intent= Intent(context, SongActivity::class.java)
                    intent.putExtra("isLoaded",false)
                    context?.startActivity(intent)
                }
            }
        })
    }

}