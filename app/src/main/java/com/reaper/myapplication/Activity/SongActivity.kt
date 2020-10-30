package com.reaper.myapplication.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.reaper.myapplication.R

class SongActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
    }
}