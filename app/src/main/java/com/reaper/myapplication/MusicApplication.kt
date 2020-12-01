package com.reaper.myapplication

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import com.reaper.myapplication.activity.MainActivity
import com.reaper.myapplication.activity.SongActivity
import com.reaper.myapplication.databinding.ActivitySongBinding
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.OnlineSongsInfo
import kotlinx.coroutines.Runnable

class MusicApplication: Application() {

    val mediaPlayer = MediaPlayer()
    var mainActivity: MainActivity? = null
    var musicIsPlaying = false
    var currentOnlineSongsInfo: OnlineSongsInfo? = null
    var currentMySongInfo: MySongInfo? = null
    var onlineSongs = ArrayList<OnlineSongsInfo>()
    var mySongs = ArrayList<MySongInfo>()


}