package com.finalproject.calendar

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.finalproject.calendar.enums.Repeticao
import com.finalproject.calendar.models.EventModel
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {
    private var CHANNEL_ID: String = "Default 1"
    private var NAME: String = "Teste"
    private var DESCRIPTION = "Ahhhh"
    private var db: FirebaseFirestore? = null

    override fun onReceive(context: Context?, intent: Intent?){

        var msg: String? = intent?.getStringExtra("msg")
        var title: String? = intent?.getStringExtra("title")
        var event_id: String? = intent?.getStringExtra("id")

        val notificationManager: NotificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var db = FirebaseFirestore.getInstance()
        db.collection("events").document(event_id!!).get().addOnSuccessListener { event ->
            var evento = EventModel (event["uid"].toString(),event.id,event["title"].toString(),event["start"].toString(),event["end"].toString(),
                Repeticao.valueOf(event["repeticao"].toString()),event["place"].toString(), (event["importance"] as Long).toInt(), (event["alert"] as Long).toInt()
            )
            var periodicidade: Long = 0
            when(evento.repeticao){
                Repeticao.UNICA -> periodicidade = 0
                Repeticao.HORARIO -> periodicidade = AlarmManager.INTERVAL_HOUR
                Repeticao.DIARIO -> periodicidade = AlarmManager.INTERVAL_DAY
                Repeticao.MENSAL -> periodicidade = AlarmManager.INTERVAL_DAY * 30
                Repeticao.ANUAL -> periodicidade = AlarmManager.INTERVAL_DAY * 365
            }

            val l = LocalDate.parse(evento.start, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val timestamp = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond

            if(System.currentTimeMillis() > timestamp && evento.repeticao != Repeticao.UNICA){
                Utils.createAlarm(
                    context,
                    "Você tem uma atividade marcada para agora",
                    evento.title,
                    evento!!,
                    periodicidade
                )
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            var builder: Notification.Builder;
            //checking if android version is greater than oreo(API 26) or not

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, NAME, importance).apply {
                description = DESCRIPTION
            }
            // Register the channel with the system

            notificationManager.createNotificationChannel(channel)

            builder = Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_calendario)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(pendingIntent)

            notificationManager.notify(Random.nextInt(), builder.build())
        }
    }
}