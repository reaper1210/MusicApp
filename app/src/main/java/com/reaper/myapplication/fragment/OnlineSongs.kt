package com.reaper.myapplication.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.firebase.database.*
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.activity.MainActivity
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.adapter.OnlineSongsAdapter
import com.reaper.myapplication.utils.OnlineSongsInfo

class OnlineSongs : Fragment() {
    private lateinit var onlinerecyclerView:RecyclerView
    private lateinit var progressbar: ProgressBar
    private lateinit var progressLayout: RelativeLayout
    private lateinit var noInternetImage:ImageView
    private lateinit var noInternetLayout:RelativeLayout
    private val songs= ArrayList<OnlineSongsInfo>()
    private lateinit var applic: MusicApplication
    private lateinit var act: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)
        onlinerecyclerView= view.findViewById(R.id.onlineRecyclerView)
        onlinerecyclerView.layoutManager=LinearLayoutManager(this.context)

        act = activity as MainActivity
        applic = activity?.application as MusicApplication
        progressLayout = view.findViewById(R.id.progressLayoutOnlineSongs)
        progressbar = view.findViewById(R.id.progressBarOnlineSongs)
        noInternetLayout=view.findViewById(R.id.noInternetLayout)
        noInternetImage=view.findViewById(R.id.imgNoInternetImage)

        if(checkConnectivity(activity as Context)){
            noInternetImage.visibility=View.GONE
            noInternetLayout.visibility=View.GONE
            progressLayout.visibility=View.VISIBLE
            progressbar.visibility=View.VISIBLE

            LoadSongs()
        }

        else{
            progressLayout.visibility=View.GONE
            progressbar.visibility=View.GONE
            noInternetImage.visibility=View.VISIBLE
            noInternetImage.visibility=View.VISIBLE
        }

        return view
    }

    private fun LoadSongs(){

        val database = FirebaseDatabase.getInstance().reference

        val getData = object: ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                progressLayout.visibility=View.VISIBLE
                progressbar.visibility=View.VISIBLE
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                progressLayout.visibility=View.GONE
                progressbar.visibility=View.GONE

                songs.clear()

                val song = snapshot.child("songs")
                for(i in song.children){

                    val songId = Integer.valueOf(i.child("id").value.toString())
                    val songName = i.child("SongName").value.toString()
                    val songArtist = i.child("artist").value.toString()
                    val songImage=i.child("image").value.toString()
                    val songUrl = i.child("url").value.toString()
                    val songInfo = OnlineSongsInfo(songId,songName,songArtist, songImage,songUrl)

                    songs.add(songInfo)

                }
                val songAdapter=OnlineSongsAdapter(songs, context!!)
                onlinerecyclerView.adapter=songAdapter

                songAdapter.SetOnItemClickListener(object : OnlineSongsAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, songsInfo: OnlineSongsInfo, position: Int) {
                        val transition: Transition = Slide(Gravity.BOTTOM)
                        transition.duration = 300
                        transition.addTarget(R.id.onlineEllipse)
                        transition.addTarget(R.id.onlinePlay)
                        transition.addTarget(R.id.txtSongName)
                        transition.addTarget(R.id.txtDuration)
                        transition.addTarget(R.id.dragDownButton)
                        TransitionManager.beginDelayedTransition(act.relativeGroup, transition)
                        act.txtSongName.visibility=View.VISIBLE
                        act.txtDuration.visibility=View.VISIBLE
                        act.dragUpButton.visibility=View.VISIBLE
                        act.onlineEllipse.visibility=View.VISIBLE
                        act.onlinePlay.visibility=View.VISIBLE
                        act.onlinePause.visibility= View.VISIBLE
                        act.dragDownButton.visibility=View.VISIBLE
                        act.dragUpButton.visibility=View.GONE

                        if (applic.mediaPlayer == null) {
                            act.onlinePlay.visibility = View.GONE
                            if(act.dragDownButton.isActivated){
                                act.onlinePause.visibility = View.GONE
                            }
                            else{
                                act.onlinePause.visibility=View.VISIBLE
                            }

                        } else {
                            if(act.dragDownButton.isActivated){
                                act.onlinePlay.visibility = View.GONE
                            }
                            else{
                                act.onlinePlay.visibility=View.VISIBLE
                            }
                            act.onlinePause.visibility = View.GONE
                        }
                        if (applic.mediaPlayer.isPlaying) {
                            if(act.dragDownButton.isActivated){
                                act.onlinePlay.visibility = View.GONE
                            }
                            else{
                                act.onlinePlay.visibility=View.VISIBLE
                            }
                            act.onlinePause.visibility = View.GONE
                        }
                        applic.mediaPlayer.stop()
                        applic.mediaPlayer.reset()
                        applic.mediaPlayer.setDataSource(songsInfo.url)
                        applic.mediaPlayer.prepareAsync()
                        applic.mediaPlayer.setOnCompletionListener {
                            act.onlinePlay.visibility=View.GONE
                            act.onlinePause.visibility=View.VISIBLE
                        }
                        applic.currentOnlineSongsInfo = songsInfo
                        val intent= Intent(context, SongActivity::class.java)
                        intent.putExtra("SongName",songsInfo.name)
                        context?.startActivity(intent)
                    }

                })
            }
        }

        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)

    }
    private fun checkConnectivity(context: Context?):Boolean{
        val connectivityManager= context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo?=connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected!=null){
            progressLayout.visibility=View.GONE
            progressbar.visibility=View.GONE
            return activeNetwork.isConnected
        }
        else{
            return false
        }
    }
}
