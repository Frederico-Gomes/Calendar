package com.finalproject.calendar.adapters
import android.view.LayoutInflater
import com.finalproject.calendar.R
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.calendar.models.EventModel
import kotlinx.android.synthetic.main.card_event.view.*


//const val low_importance = 0
const val medium_importance = 1
const val high_importance = 2



class EventAdapter (val events : MutableList<EventModel>, var eventClickListener : EventClickListener ) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>(){

    class ViewHolder(var itemV: View) : RecyclerView.ViewHolder(itemV) {

        private val highImportanceColor : Int = R.drawable.ic_high_importance
        private val mediumImportanceColor : Int = R.drawable.ic_medium_importance
        private val lowImportanceColor : Int = R.drawable.ic_low_importance

        // binds list elements to view elements, and set up data for clicklistener
        fun bind(event : EventModel, action: EventClickListener){
            with(event){
                if(importance == high_importance) itemV.importance.setImageResource(highImportanceColor)
                else if(importance == medium_importance)  itemV.importance.setImageResource(mediumImportanceColor)
                else itemV.importance.setImageResource(lowImportanceColor)
                itemV.title.text = title
                itemV.date.text = date
            }

            itemV.setOnClickListener{
                action.onClick(event,adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_event,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position],eventClickListener)
    }

}

// interface that manage a click in an event
interface EventClickListener {
    fun onClick(event : EventModel, position: Int){
    }

}
