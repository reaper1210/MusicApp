package com.reaper.myapplication.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.activity.MainActivity
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.adapter.MySongsAdapter
import com.reaper.myapplication.utils.MySongInfo
import java.io.File
import java.io.FileInputStream

class MySongs : Fragment() {

    private lateinit var OnlinerecyclerView:RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout:RelativeLayout
    private lateinit var noInternetImage:ImageView
    private lateinit var noInternetLayout:RelativeLayout
    private val pontext=this.context
    private lateinit var act: MainActivity
    private lateinit var applic: MusicApplication

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)

        applic = activity?.application as MusicApplication
        act = activity as MainActivity
        noInternetLayout= view.findViewById(R.id.noInternetLayout)
        noInternetImage=view.findViewById(R.id.imgNoInternetImage)
        noInternetLayout.visibility=View.GONE
        noInternetImage.visibility=View.GONE
        progressBar=view.findViewById(R.id.progressBarOnlineSongs)
        progressLayout=view.findViewById(R.id.progressLayoutOnlineSongs)
        OnlinerecyclerView= view.findViewById(R.id.onlineRecyclerView)
        OnlinerecyclerView.layoutManager=LinearLayoutManager(pontext)

        checkPermission()

        return view
    }
    private fun checkPermission() {
        if(Build.VERSION.SDK_INT >= 23){
            if(pontext?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.RECORD_AUDIO)
                this.requestPermissions(permissions, 5)
            }
            else{
                Toast.makeText(this.context,"Error", Toast.LENGTH_LONG).show()
            }
        }
        else{
            loadSongs()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==5){
            if(grantResults.isNotEmpty()){

                val permissionToRead = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToRecord = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if(permissionToRead && permissionToRecord){
                    progressLayout.visibility=View.GONE
                    progressBar.visibility=View.GONE
                    loadSongs()
                }
                else{
                    Toast.makeText(this.context,"Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
        else{
            Toast.makeText(this.context,"Permission ni li na", Toast.LENGTH_LONG).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadSongs(){
        applic.mySongs.clear()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection:String= MediaStore.Audio.Media.IS_MUSIC
        val cursor = requireActivity().contentResolver.query(uri,null,selection,null,null,null)!!

        if(cursor.moveToFirst()){
            do {
                val name: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val mediaId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val uri = Uri.parse(uri.toString() + File.separator + mediaId)
                val s = MySongInfo(Integer.valueOf(mediaId),name,artist,"",uri.toString())
                applic.mySongs.add(s)

            }while (cursor.moveToNext())
        }
        cursor.close()
        val songAdapter = MySongsAdapter(applic.mySongs,this.context)
        OnlinerecyclerView.adapter=songAdapter
        songAdapter.SetOnItemClickListener(object : MySongsAdapter.OnItemClickListener {

            override fun onItemClick(view: MySongsAdapter, songInfo: MySongInfo, position: Int) {
                if(songInfo==applic.currentMySongInfo){
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
