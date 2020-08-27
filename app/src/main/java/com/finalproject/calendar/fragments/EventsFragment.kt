package com.finalproject.calendar.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finalproject.calendar.R
import com.finalproject.calendar.adapters.EventAdapter
import com.finalproject.calendar.adapters.EventClickListener
import com.finalproject.calendar.models.EventModel
import kotlinx.android.synthetic.main.fragment_event.view.*

class EventsFragment : Fragment(), EventClickListener {
    companion object{
        fun newIntance() = CalendarFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container,false)
        val activity = activity as Context
        val testEvent1 = EventModel(2,"reuniao","22 de janeiro as 22:00")
        val testEvent2 = EventModel(1,"reuniao","22 de janeiro as 15:00")
        val events : MutableList<EventModel> = mutableListOf(testEvent1,testEvent2)
        val recyclerView = view.events_recycler_view
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = EventAdapter(events,this)
        return view
    }
}