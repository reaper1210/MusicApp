package com.reaper.myapplication.activity

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer
import com.marcinmoskala.arcseekbar.ArcSeekBar
import com.marcinmoskala.arcseekbar.ProgressListener
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.adapter.PlaylistFragmentAdapter
import com.reaper.myapplication.adapter.PlaylistSongsAdapter
import com.reaper.myapplication.database.*
import com.reaper.myapplication.databinding.ActivitySongBinding
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.PlaylistInfo
import kotlinx.coroutines.Runnable

class SongActivity : AppCompatActivity() {

    private lateinit var favourites:ImageView
    private lateinit var favourites_selected:ImageView
    private lateinit var addToPlaylists:ImageView
    private lateinit var addToPlaylistsSelected:ImageView
    private lateinit var play:ImageView
    private lateinit var pause:ImageView
    private lateinit var songName: TextView
    private lateinit var songArtist: TextView
    private lateinit var songImage: ImageView
    private lateinit var previous: ImageView
    private lateinit var next: ImageView
    private lateinit var txtRunningMinutes: TextView
    private lateinit var txtRunningSeconds: TextView
    private lateinit var txtTotalMinutes: TextView
    private lateinit var txtTotalSeconds: TextView
    private lateinit var progressbarSongLoading: ProgressBar
    private lateinit var volumeSeekbar:SeekBar
    private lateinit var arcSeekbar: ArcSeekBar
    private lateinit var visualizer: CircleLineVisualizer
    private lateinit var binding: ActivitySongBinding
    private lateinit var applic: MusicApplication
    private lateinit var runnable: Runnable
    private var handler = Handler()
    private var seconds: Int = 0
    private var minutes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applic = application as MusicApplication

        txtRunningMinutes=binding.txtRunningMinutes
        txtRunningSeconds=binding.txtRunningSeconds
        txtTotalMinutes=binding.txtTotalMinutes
        txtTotalSeconds=binding.txtTotalSeconds
        txtRunningMinutes.text = "0"
        txtRunningSeconds.text = "00"


        arcSeekbar = binding.arcSeekBar
        arcSeekbar.progress = 0
        arcSeekbar.onProgressChangedListener = ProgressListener {
            arcSeekbar.onProgressChangedListener = ProgressListener {
                seconds = arcSeekbar.progress/1000
                minutes = seconds/60
                txtRunningMinutes.text = minutes.toString()
                if(seconds>=60){
                    seconds -= (minutes * 60)
                }
                txtRunningSeconds.text = seconds.toString()
            }
        }
        arcSeekbar.onStartTrackingTouch = ProgressListener {
            arcSeekbar.onProgressChangedListener = ProgressListener { p0 ->
                applic.mediaPlayer.seekTo(p0)
                seconds = arcSeekbar.progress/1000
                minutes = seconds/60
                txtRunningMinutes.text = minutes.toString()
                if(seconds>=60){
                    seconds -= (minutes * 60)
                }
                txtRunningSeconds.text = seconds.toString()
            }

        }
        arcSeekbar.onStopTrackingTouch = ProgressListener {
            arcSeekbar.onProgressChangedListener = ProgressListener {
                seconds = arcSeekbar.progress/1000
                minutes = seconds/60
                txtRunningMinutes.text = minutes.toString()
                if(seconds>=60){
                    seconds -= (minutes * 60)
                }
                txtRunningSeconds.text = seconds.toString()
            }
        }

        previous = binding.previous
        next = binding.next
        previous.isClickable = true
        next.isClickable = true
        songName = binding.txtSongNameText
        songArtist = binding.txtSingerName
        songImage = binding.imgSongImage
        favourites= binding.favourites
        favourites_selected=binding.favouritesSelected
        favourites_selected.visibility=View.GONE
        addToPlaylists=binding.addtoplaylists
        addToPlaylistsSelected=binding.addtoplaylistsselected
        addToPlaylistsSelected.visibility=View.GONE

        val audioManager:AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        volumeSeekbar=binding.volumeseekbar
        volumeSeekbar.max=maxVolume
        volumeSeekbar.progress = currentVolume

        volumeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,p1,0)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        play=binding.play
        pause= binding.pause
        pause.visibility = View.GONE
        play.visibility = View.INVISIBLE
        play.isClickable = false
        pause.isClickable = false
        progressbarSongLoading = binding.progressBarSongLoading
        progressbarSongLoading.isClickable = false

        visualizer= binding.visualizer
        val audioSessionId=applic.mediaPlayer.audioSessionId

        if(audioSessionId != -1){
            visualizer.setAudioSessionId(audioSessionId)
        }

        if(intent.getBooleanExtra("isLoaded", false)){
            progressbarSongLoading.visibility = View.GONE
            play.visibility=View.VISIBLE
            if(applic.mediaPlayer.isPlaying){
                play.visibility = View.VISIBLE
                pause.visibility = View.INVISIBLE
            }
            else{
                play.visibility = View.INVISIBLE
                pause.visibility = View.VISIBLE
            }
        }

        when {
            applic.currentOnlineSongsInfo != null -> {

                favourites.visibility = View.GONE
                favourites_selected.visibility = View.GONE
                addToPlaylists.visibility = View.GONE
                addToPlaylistsSelected.visibility = View.GONE

                songName.text = applic.currentOnlineSongsInfo?.name
                songName.isSelected = true
                songArtist.text = applic.currentOnlineSongsInfo?.artist
                if(songArtist.text=="<unknown>"){
                    songArtist.visibility = View.INVISIBLE
                }
                val imageUrl = applic.currentOnlineSongsInfo?.image
                Glide.with(this@SongActivity).load(imageUrl).error(R.drawable.music_image).into(songImage)

                var sec = applic.currentOnlineSongsInfo?.duration!! / 1000
                val min = sec / 60
                sec -= min*60
                if(sec/10==0){
                    txtTotalSeconds.text = "0$sec"
                }
                else{
                    txtTotalSeconds.text = sec.toString()
                }
                txtTotalMinutes.text = min.toString()

                arcSeekbar.maxProgress = applic.currentOnlineSongsInfo?.duration!!
                updateSeekBar()

                previous.setOnClickListener {
                    previous.isClickable = false
                    next.isClickable = false
                    play.visibility = View.INVISIBLE
                    pause.visibility = View.INVISIBLE
                    progressbarSongLoading.visibility = View.VISIBLE
                    val currentIndex = applic.onlineSongs.indexOf(applic.currentOnlineSongsInfo)
                    val previousIndex = if(currentIndex==0){
                        applic.onlineSongs.size - 1
                    } else{
                        currentIndex - 1
                    }
                    applic.mediaPlayer.stop()
                    applic.mediaPlayer.reset()
                    applic.mediaPlayer.setDataSource(applic.onlineSongs[previousIndex].url)
                    applic.mediaPlayer.prepareAsync()
                    applic.mediaPlayer.setOnCompletionListener {
                        applic.mainActivity?.onlinePlay?.visibility = View.GONE
                        applic.mainActivity?.onlinePause?.visibility = View.VISIBLE
                        applic.musicIsPlaying = false
                        next.callOnClick()
                    }
                    applic.mainActivity?.txtSongName?.text = applic.onlineSongs[previousIndex].name
                    applic.mainActivity?.txtSongArtist?.text = applic.onlineSongs[previousIndex].artist
                    applic.currentMySongInfo = null
                    applic.currentOnlineSongsInfo = applic.onlineSongs[previousIndex]
                    visualizer.release()
                    val intent= Intent(this@SongActivity, SongActivity::class.java)
                    intent.putExtra("isLoaded", false)
                    startActivity(intent)
                    finish()
                }
                next.setOnClickListener {
                    next.isClickable = false
                    previous.isClickable = false
                    play.visibility = View.INVISIBLE
                    pause.visibility = View.INVISIBLE
                    progressbarSongLoading.visibility = View.VISIBLE
                    val currentIndex = applic.onlineSongs.indexOf(applic.currentOnlineSongsInfo)
                    val nextIndex = if(currentIndex==applic.onlineSongs.size - 1){
                        0
                    }
                    else{
                        currentIndex + 1
                    }
                    applic.mediaPlayer.stop()
                    applic.mediaPlayer.reset()
                    applic.mediaPlayer.setDataSource(applic.onlineSongs[nextIndex].url)
                    applic.mediaPlayer.prepareAsync()
                    applic.mediaPlayer.setOnCompletionListener {
                        applic.mainActivity?.onlinePlay?.visibility = View.GONE
                        applic.mainActivity?.onlinePause?.visibility = View.VISIBLE
                        applic.musicIsPlaying = false
                        next.callOnClick()

                    }
                    applic.mainActivity?.txtSongName?.text = applic.onlineSongs[nextIndex].name
                    applic.mainActivity?.txtSongArtist?.text = applic.onlineSongs[nextIndex].artist
                    applic.currentMySongInfo = null
                    applic.currentOnlineSongsInfo = applic.onlineSongs[nextIndex]
                    visualizer.release()
                    val intent= Intent(this@SongActivity, SongActivity::class.java)
                    intent.putExtra("isLoaded", false)
                    startActivity(intent)
                    finish()
                }

            }
            applic.currentMySongInfo != null -> {

                val isFav = FavDbAsyncTask(applicationContext,applic.currentMySongInfo!!,1).execute().get()
                if(isFav){
                    favourites.visibility = View.GONE
                    favourites_selected.visibility = View.VISIBLE
                }
                else{
                    favourites.visibility = View.VISIBLE
                    favourites_selected.visibility = View.GONE
                }

                if(PlaylistDbAsyncTask(this,applic.currentMySongInfo!!.uri, PlaylistInfo(-1000,"aognoasidnfoa",ArrayList<String>().joinToString(",")),1).execute().get()){
                    addToPlaylistsSelected.visibility = View.VISIBLE
                    addToPlaylists.visibility = View.GONE
                }
                else{
                    addToPlaylistsSelected.visibility = View.GONE
                    addToPlaylists.visibility = View.VISIBLE
                }

                songName.text = applic.currentMySongInfo?.name
                songName.isSelected = true
                songArtist.text = applic.currentMySongInfo?.artist
                if(songArtist.text=="<unknown>"){
                    songArtist.visibility = View.INVISIBLE
                }

                var sec = applic.mediaPlayer.duration / 1000
                val min = sec / 60
                sec -= min*60
                if(sec/10==0){
                    txtTotalSeconds.text = "0$sec"
                }
                else{
                    txtTotalSeconds.text = sec.toString()
                }
                txtTotalMinutes.text = min.toString()

                arcSeekbar.maxProgress = applic.mediaPlayer.duration
                updateSeekBar()

                previous.setOnClickListener {
                    previous.isClickable = false
                    next.isClickable = false
                    play.visibility = View.INVISIBLE
                    pause.visibility = View.INVISIBLE
                    progressbarSongLoading.visibility = View.VISIBLE
                    val currentIndex = applic.mySongs.indexOf(applic.currentMySongInfo)
                    val previousIndex = if(currentIndex==0){
                        applic.mySongs.size - 1
                    } else{
                        currentIndex - 1
                    }
                    applic.mediaPlayer.stop()
                    applic.mediaPlayer.reset()
                    applic.mediaPlayer.setDataSource(this, Uri.parse(applic.mySongs[previousIndex].uri))
                    applic.mediaPlayer.prepareAsync()
                    applic.mediaPlayer.setOnCompletionListener {
                        applic.mainActivity?.onlinePlay?.visibility = View.GONE
                        applic.mainActivity?.onlinePause?.visibility = View.VISIBLE
                        applic.musicIsPlaying = false
                        next.callOnClick()
                    }
                    applic.mainActivity?.txtSongName?.text = applic.mySongs[previousIndex].name
                    applic.mainActivity?.txtSongArtist?.text = applic.mySongs[previousIndex].artist
                    applic.currentMySongInfo = null
                    applic.currentMySongInfo = applic.mySongs[previousIndex]
                    visualizer.release()
                    val intent= Intent(this@SongActivity, SongActivity::class.java)
                    intent.putExtra("isLoaded", false)
                    startActivity(intent)
                    finish()
                }
                next.setOnClickListener {
                    next.isClickable = false
                    previous.isClickable = false
                    play.visibility = View.INVISIBLE
                    pause.visibility = View.INVISIBLE
                    progressbarSongLoading.visibility = View.VISIBLE
                    val currentIndex = applic.mySongs.indexOf(applic.currentMySongInfo)
                    val nextIndex = if(currentIndex==applic.mySongs.size - 1){
                        0
                    }
                    else{
                        currentIndex + 1
                    }
                    applic.mediaPlayer.stop()
                    applic.mediaPlayer.reset()
                    applic.mediaPlayer.setDataSource(this,Uri.parse(applic.mySongs[nextIndex].uri))
                    applic.mediaPlayer.prepareAsync()
                    applic.mediaPlayer.setOnCompletionListener {
                        applic.mainActivity?.onlinePlay?.visibility = View.GONE
                        applic.mainActivity?.onlinePause?.visibility = View.VISIBLE
                        applic.musicIsPlaying = false
                        next.callOnClick()
                    }
                    applic.mainActivity?.txtSongName?.text = applic.mySongs[nextIndex].name
                    applic.mainActivity?.txtSongArtist?.text = applic.mySongs[nextIndex].artist
                    applic.currentMySongInfo = null
                    applic.currentMySongInfo = applic.mySongs[nextIndex]
                    visualizer.release()
                    val intent= Intent(this@SongActivity, SongActivity::class.java)
                    intent.putExtra("isLoaded", false)
                    startActivity(intent)
                    finish()
                }
            }
            else -> {
                this.finish()
            }
        }

        if(applic.mediaPlayer.isPlaying){
            progressbarSongLoading.visibility = View.GONE
            play.visibility=View.VISIBLE
        }

        applic.mediaPlayer.setOnCompletionListener {
            applic.mainActivity?.onlinePlay?.visibility = View.GONE
            applic.mainActivity?.onlinePause?.visibility = View.VISIBLE
            applic.musicIsPlaying = false
            visualizer.release()
            next.callOnClick()
        }

        applic.mediaPlayer.setOnPreparedListener {
            it.start()
            applic.musicIsPlaying = true
            progressbarSongLoading.visibility = View.GONE
            play.visibility=View.VISIBLE
        }

        favourites.setOnClickListener {

            if(!FavDbAsyncTask(applicationContext,applic.currentMySongInfo!!,1).execute().get()){
                val result = FavDbAsyncTask(applicationContext,applic.currentMySongInfo!!,2).execute().get()
                if(result){
                    favourites.visibility=View.GONE
                    favourites_selected.visibility=View.VISIBLE
                    applic.favSongs.add(applic.currentMySongInfo!!)
                    if(applic.favAdapter!=null){
                        applic.favAdapter?.notifyDataSetChanged()
                    }
                    if(applic.favSongs.size == 1){
                        applic.noFavLayout?.visibility = View.GONE
                    }
                    Toast.makeText(this@SongActivity, "Added To Favourites", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@SongActivity, "Failed to Add Song to Favourites", Toast.LENGTH_SHORT).show()
                }
            }

        }

        favourites_selected.setOnClickListener {

            if(FavDbAsyncTask(applicationContext,applic.currentMySongInfo!!,1).execute().get()){
                val result = FavDbAsyncTask(applicationContext,applic.currentMySongInfo!!,3).execute().get()
                if(result){
                    favourites_selected.visibility=View.GONE
                    favourites.visibility=View.VISIBLE
                    applic.favSongs.remove(applic.currentMySongInfo!!)
                    if(applic.favAdapter!=null){
                        applic.favAdapter?.notifyDataSetChanged()
                    }
                    if(applic.favSongs.isNullOrEmpty()){
                        applic.noFavLayout?.visibility = View.VISIBLE
                    }
                    Toast.makeText(this@SongActivity, "Removed From Favourites", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@SongActivity, "Failed to Remove Song from Favourites", Toast.LENGTH_SHORT).show()
                }
            }
        }

        addToPlaylists.setOnClickListener {

            val allPlaylists = ArrayList<PlaylistInfo>()
            allPlaylists.addAll(RetrievePlaylists(this).execute().get())

            if(allPlaylists.isEmpty()){
                val playlistInfo = PlaylistInfo(1,"Test 1",ArrayList<String>().joinToString(","))
                PlaylistDbAsyncTask(this,applic.currentMySongInfo!!.uri,playlistInfo,4).execute()
            }
            else if(allPlaylists.size == 1){
                val playlistInfo = PlaylistInfo(2,"Test 2",ArrayList<String>().joinToString(","))
                PlaylistDbAsyncTask(this,applic.currentMySongInfo!!.uri,playlistInfo,4).execute()
            }

            val layoutInflater = LayoutInflater.from(this)
            val view = layoutInflater.inflate(R.layout.layout_dialog_playlist,null)

            val dialog = AlertDialog.Builder(this)
                    .setView(view)
                    .create()

            val recyclerView = view.findViewById<RecyclerView>(R.id.playlistDialogRecycler)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val playlistDialogAdapter = PlaylistFragmentAdapter(this,dialog,allPlaylists)
            playlistDialogAdapter.SetOnItemClickListener(object: PlaylistFragmentAdapter.PlaylistDialogOnItemClickListener {
                override fun onItemClick(dialog:AlertDialog?,context: Context, view: View, playlistInfo: PlaylistInfo, position: Int) {
                    PlaylistDbAsyncTask(context,applic.currentMySongInfo!!.uri,playlistInfo,2).execute()
                    addToPlaylists.visibility=View.GONE
                    addToPlaylistsSelected.visibility=View.VISIBLE
                    dialog?.cancel()
                }
            })
            recyclerView.adapter = playlistDialogAdapter

            val cancel = view.findViewById<Button>(R.id.playlistDialogCancel)
            cancel.setOnClickListener {
                dialog.cancel()
            }

            dialog.show()

        }

        addToPlaylistsSelected.setOnClickListener {

            PlaylistDbAsyncTask(this,applic.currentMySongInfo!!.uri,PlaylistInfo(-1000,"oanoigoasrfg",""),3).execute()

            addToPlaylists.visibility=View.VISIBLE
            addToPlaylistsSelected.visibility=View.GONE

        }

        play.setOnClickListener {
            play.visibility=View.INVISIBLE
            pause.visibility=View.VISIBLE
            applic.mediaPlayer.pause()
            applic.musicIsPlaying = false
        }

        pause.setOnClickListener {
            pause.visibility=View.INVISIBLE
            play.visibility=View.VISIBLE
            applic.mediaPlayer.start()
            applic.musicIsPlaying = true
        }
    }

    override fun onPause() {
        applic.pauseSeconds = txtRunningSeconds.text.toString()
        applic.pauseMinutes = txtRunningMinutes.text.toString()
        super.onPause()
    }

    override fun onResume() {
        arcSeekbar.progress = applic.mediaPlayer.currentPosition
        txtRunningSeconds.text = applic.pauseSeconds
        txtRunningMinutes.text = applic.pauseMinutes
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        visualizer.release()
    }

    private fun updateSeekBar(){
        if(applic.mediaPlayer.isPlaying) {
            arcSeekbar.progress = applic.mediaPlayer.currentPosition
            if(seconds >= 60) {
                seconds = 0
                minutes++
                txtRunningMinutes.text = minutes.toString()
            }
            if(seconds/10==0){
                txtRunningSeconds.text = "0$seconds"
            }
            else{
                txtRunningSeconds.text = seconds.toString()
            }
            seconds++
        }
        runnable = Runnable {
            updateSeekBar()
        }
        handler.postDelayed(runnable,100)

    }

    override fun onBackPressed() {
        applic.mainActivity?.dragUpButton?.callOnClick()
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val audioManager:AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        return when (event!!.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                val currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,0)
                volumeSeekbar.progress=currentVolume
                true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,0)
                volumeSeekbar.progress=currentVolume
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}
