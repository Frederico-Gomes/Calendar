package com.finalproject.calendar.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finalproject.calendar.R
import com.finalproject.calendar.activities.CreateEventActivity
import com.finalproject.calendar.adapters.EventAdapter
import com.finalproject.calendar.adapters.EventClickListener
import com.finalproject.calendar.enums.Repeticao
import com.finalproject.calendar.models.AlertModel
import com.finalproject.calendar.models.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_event.*
import kotlinx.android.synthetic.main.fragment_event.view.*
import kotlin.properties.Delegates

class EventsFragment : Fragment(), EventClickListener {
    private var eventList : MutableList<EventModel> = mutableListOf()
    companion object{
        fun newIntance() = EventsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container,false)
        val activity = activity as Context
        var auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser?.uid
        var eventsList = mutableListOf<EventModel>()
        val recyclerView = view.events_recycler_view
        val events =  FirebaseFirestore.getInstance().collection("events").whereEqualTo("uid",uid.toString()).get()
            .addOnSuccessListener {eventss ->
                for (event in eventss) {
                    val tempEvent = EventModel (uid,event["id"].toString(),event["title"].toString(),event["start"].toString(),event["end"].toString(),
                        Repeticao.valueOf(event["repeticao"].toString()),event["place"].toString(), (event["importance"] as Long).toInt(), (event["alert"] as Long).toInt()
                    )
                    eventsList.add(tempEvent)
                    recyclerView.adapter = EventAdapter(eventsList,this)
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                }
            }

        return view

    }

    override fun onClick(event: EventModel, position: Int) {
        val activity = activity as Context
        val intent = Intent(getActivity(), CreateEventActivity::class.java)
        intent.putExtra("event",event)
        intent.putExtra("edit","0")
        startActivity(intent)
    }

}