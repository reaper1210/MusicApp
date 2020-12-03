package com.reaper.myapplication.activity

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer
import com.marcinmoskala.arcseekbar.ArcSeekBar
import com.marcinmoskala.arcseekbar.ProgressListener
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.databinding.ActivitySongBinding
import kotlinx.coroutines.Runnable

class SongActivity : AppCompatActivity() {

    private lateinit var favourites:ImageView
    private lateinit var favourites_selected:ImageView
    private lateinit var addToPlaylists:ImageView
    private lateinit var addToPlaylistsSelected:ImageView
    private lateinit var back:ImageView
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

        back= binding.btnBackSong
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
        progressbarSongLoading = binding.progressBarSongLoading

        visualizer= binding.visualizer
        val audioSessionId=applic.mediaPlayer.audioSessionId

        if(audioSessionId != -1){
            visualizer.setAudioSessionId(audioSessionId)
        }

        if(intent.getBooleanExtra("isLoaded", false)){
            progressbarSongLoading.visibility = View.GONE
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
                    applic.mediaPlayer.setDataSource(this,applic.mySongs[previousIndex].uri!!)
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
                    applic.mediaPlayer.setDataSource(this,applic.mySongs[nextIndex].uri!!)
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
        }

        applic.mediaPlayer.setOnCompletionListener {
            applic.mainActivity?.onlinePlay?.visibility = View.GONE
            applic.mainActivity?.onlinePause?.visibility = View.VISIBLE
            applic.musicIsPlaying = false
            if(visualizer != null){
                visualizer.release()
            }
            next.callOnClick()
        }

        applic.mediaPlayer.setOnPreparedListener {
            it.start()
            applic.musicIsPlaying = true
            progressbarSongLoading.visibility = View.GONE
        }

        back.setOnClickListener {
            onBackPressed()
        }

        favourites.setOnClickListener {
            favourites.visibility=View.GONE
            favourites_selected.visibility=View.VISIBLE
            Toast.makeText(this@SongActivity, "Added To Favourites", Toast.LENGTH_SHORT).show()
        }

        favourites_selected.setOnClickListener {
            favourites_selected.visibility=View.GONE
            favourites.visibility=View.VISIBLE
            Toast.makeText(this@SongActivity, "Removed From Favourites", Toast.LENGTH_SHORT).show()
        }

        addToPlaylists.setOnClickListener {
            addToPlaylists.visibility=View.GONE
            addToPlaylistsSelected.visibility=View.VISIBLE
            Toast.makeText(this@SongActivity, "Added To playlist", Toast.LENGTH_SHORT).show()
            val intent=Intent(this@SongActivity, PlaylistSongs::class.java)
            startActivity(intent)
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
