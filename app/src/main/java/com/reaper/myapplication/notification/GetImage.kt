package com.reaper.myapplication.notification

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.log

class GetImage: AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg p0: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            var url = URL(p0[0])
            var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            var input: InputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(input)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e(TAG, "doInBackground: $bitmap ", )
        return bitmap
    }

}