package com.reaper.myapplication.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionService: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.sendBroadcast(Intent("notify_action")
            .putExtra("notify_action_name",intent?.action))

    }

}