package com.finalproject.calendar.broadcastreceivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.finalproject.calendar.R
import com.finalproject.calendar.activities.MainActivity
import com.finalproject.calendar.models.EventModel
import kotlin.random.Random

class EventBS: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        var place: String? = intent?.getStringExtra("place")
        var title: String? = intent?.getStringExtra("title")
        var alert: Int? = intent?.getIntExtra("alert",0)
        var freq: String? = intent?.getStringExtra("freq")
        val notification_manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel_id ="events"
        val channel_description = "chanel that administer events"
        val notification_channel = NotificationChannel(channel_id,channel_description, NotificationManager.IMPORTANCE_HIGH)
        notification_manager.createNotificationChannel(notification_channel)
        val intent1 = Intent(context, MainActivity :: class.java)

        val pending_intent = PendingIntent.getActivity(context,0,intent1, 0)
        Log.d("bs",title)



        val builder = Notification.Builder(context,channel_id)
            .setContentTitle(place)
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_calendario)
            .setContentIntent(pending_intent)
        notification_manager.notify(Random.nextInt(),builder.build())

    }


}