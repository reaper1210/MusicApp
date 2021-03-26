package com.reaper.myapplication.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.reaper.myapplication.MusicApplication
import com.reaper.myapplication.R
import com.reaper.myapplication.utils.MySongInfo
import com.reaper.myapplication.utils.OnlineSongsInfo
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class CreateNotification {

    var CHANNEL_ID = "channel_1"
    val ACTION_PREVIOUS = "action_previous"
    val ACTION_PLAY = "action_play"
    val ACTION_NEXT = "action_next"

    lateinit var notification: Notification

    fun createNotification(context: Context, mySongInfo: MySongInfo?, onlineSongsInfo: OnlineSongsInfo?, playButtons: Int){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val mediaSessionCompat = MediaSessionCompat(context,"music_op")

            if(mySongInfo!=null){

                val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.music_image)

                val intentPrevious = Intent(context,NotificationActionService::class.java).setAction(ACTION_PREVIOUS)
                val pendingIntentPrevious = PendingIntent.getBroadcast(context,0,intentPrevious,PendingIntent.FLAG_UPDATE_CURRENT)

                val intentPlay = Intent(context,NotificationActionService::class.java).setAction(ACTION_PLAY)
                val pendingIntentPlay = PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT)

                val intentNext = Intent(context,NotificationActionService::class.java).setAction(ACTION_NEXT)
                val pendingIntentNext = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT)

                notification = NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_app_icon_foreground)
                    .setContentTitle(mySongInfo.name)
                    .setContentText(mySongInfo.artist)
                    .setLargeIcon(bitmap)
                    .addAction(R.drawable.previous,"Previous",pendingIntentPrevious)
                    .addAction(playButtons,"Play",pendingIntentPlay)
                    .addAction(R.drawable.next,"Next",pendingIntentNext)
                    .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build()

                notificationManagerCompat.notify(1,notification)


            }
            else{

                var bitmap = GetImage().execute(onlineSongsInfo?.image).get()
                println("Mohit Noob")

                val intentPrevious = Intent(context,NotificationActionService::class.java).setAction(ACTION_PREVIOUS)
                val pendingIntentPrevious = PendingIntent.getBroadcast(context,0,intentPrevious,PendingIntent.FLAG_UPDATE_CURRENT)

                val intentPlay = Intent(context,NotificationActionService::class.java).setAction(ACTION_PLAY)
                val pendingIntentPlay = PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT)

                val intentNext = Intent(context,NotificationActionService::class.java).setAction(ACTION_NEXT)
                val pendingIntentNext = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_UPDATE_CURRENT)

                notification = NotificationCompat.Builder(context,CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_music_app_icon_foreground)
                        .setContentTitle(onlineSongsInfo?.name)
                        .setContentText(onlineSongsInfo?.artist)
                        .setLargeIcon(bitmap)
                        .addAction(R.drawable.previous,"Previous",pendingIntentPrevious)
                        .addAction(playButtons,"Play",pendingIntentPlay)
                        .addAction(R.drawable.next,"Next",pendingIntentNext)
                        .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                        .setOnlyAlertOnce(false)
                        .setShowWhen(false)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .build()

                notificationManagerCompat.notify(1,notification)
            }
            }

        }

    }
