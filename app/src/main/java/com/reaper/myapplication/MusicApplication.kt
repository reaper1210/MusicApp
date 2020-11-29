package com.reaper.myapplication

import android.app.Application
import android.media.MediaPlayer
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.OnlineSongsInfo

class MusicApplication: Application() {

    val mediaPlayer = MediaPlayer()
    var currentOnlineSongsInfo: OnlineSongsInfo? = null
    var currentMySongInfo: MySongInfo? = null

}