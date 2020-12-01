package com.reaper.myapplication.utils

import android.net.Uri

data class MySongInfo(

    val name:String,
    val artist:String,
    val image:String,
    val uri: Uri?

)