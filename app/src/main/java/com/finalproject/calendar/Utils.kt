package com.finalproject.calendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.finalproject.calendar.models.EventModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.random.Random


object Utils {

    fun createAlarm(context: Context, title: String, msg: String, event: EventModel, interval: Long){
        // Intent to start the Broadcast Receiver
        val broadcastIntent = Intent(context, NotificationReceiver::class.java)

        broadcastIntent.putExtra("title", title)
        broadcastIntent.putExtra("msg", msg)
        broadcastIntent.putExtra("id", event.id_event)

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Random.nextInt(),
            broadcastIntent,
            Intent.FILL_IN_DATA
        )
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
        }
        val l = LocalDateTime.parse(event.start, DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm"))
        val unix = l.toEpochSecond(ZoneOffset.UTC)

        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            unix + interval,
            pendingIntent
        )
    }
}