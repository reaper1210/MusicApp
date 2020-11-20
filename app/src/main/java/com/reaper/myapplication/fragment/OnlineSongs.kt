package com.reaper.myapplication.fragment

import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContentResolverCompat.query
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.OnlineSongsAdapter
import com.reaper.myapplication.utils.OnlineSongsInfo
import java.util.jar.Manifest
import android.content.ContentResolver as ContentResolver
import android.content.Context as Context


class OnlineSongs : Fragment() {
    lateinit var OnlinerecyclerView:RecyclerView
    lateinit var cardView: CardView
    lateinit var songImage:ImageView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var databaseReference:DatabaseReference
    val songs= ArrayList<OnlineSongsInfo>()
    val pontext=this.context


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        databaseReference=FirebaseDatabase.getInstance().getReference("music-app-8423a")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds:DataSnapshot in dataSnapshot.children){
                    val songObj: OnlineSongsInfo? = ds.getValue(OnlineSongsInfo::class.java)

                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        val view=inflater.inflate(R.layout.fragment_online_songs, container, false)
        OnlinerecyclerView= view.findViewById(R.id.onlineRecyclerView)
        OnlinerecyclerView.layoutManager=LinearLayoutManager(this.context)
        val adapter: OnlineSongsAdapter = OnlineSongsAdapter(songs, this.context)

        checkPermission()

        return view
    }
    private fun checkPermission() {
        if(Build.VERSION.SDK_INT >= 23){
        if(pontext?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.READ_EXTERNAL_STORAGE) } !=
                PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            this.requestPermissions(permissions, 5)
        }
            else{
            Toast.makeText(this.context,"Error bahenchod",Toast.LENGTH_LONG)
        }
        }
        else{
            loadSongs()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       if(requestCode==5){
           if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
               loadSongs()
           }
           else{
               Toast.makeText(this.context,"Permission Denied",Toast.LENGTH_LONG)
           }
       }
        else{
           Toast.makeText(this.context,"Permission Leni chahiye na",Toast.LENGTH_LONG)
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadSongs(){
        val uri:Uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection:String=MediaStore.Audio.Media.IS_MUSIC
        val cursor = requireActivity().contentResolver.query(uri,null,selection,null,null,null)!!

        if(cursor.moveToFirst()){
            do {
                val name: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val url: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                val s:OnlineSongsInfo= OnlineSongsInfo(name,artist,url)
                songs.add(s)

            }while (cursor.moveToNext())
        }
        cursor.close()
        val songAdapter:OnlineSongsAdapter=OnlineSongsAdapter(songs,this.context)
        OnlinerecyclerView.adapter=songAdapter
        songAdapter.SetOnItemClickListener(object : OnlineSongsAdapter.OnItemClickListener {
            override fun onItemClick(view: View, songsInfo: OnlineSongsInfo, position: Int) {
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(songsInfo.url)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(p0: MediaPlayer?) {
                        mediaPlayer.start()
                    }
                })
            }
        })
    }
}
