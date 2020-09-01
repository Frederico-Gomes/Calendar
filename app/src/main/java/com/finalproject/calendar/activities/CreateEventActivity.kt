package com.finalproject.calendar.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.finalproject.calendar.R
import com.finalproject.calendar.enums.Repeticao
import com.finalproject.calendar.models.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event.*
import java.util.*

class CreateEventActivity : AppCompatActivity() {


    lateinit var title : String
    private lateinit var start: String
    private lateinit var end: String
    private var place: String? = null
    private var importance :Int = 0
    private var alert : Int = 0
    var auth = FirebaseAuth.getInstance()
    var uid = auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, Repeticao.values()).also { adapter->
            repeticao_dropdown.adapter = adapter
        }

    }


    fun setAlarmOnClick(view:View) {
        val builder = AlertDialog.Builder(this)
        lateinit var dialog:AlertDialog
        val array  = resources.getStringArray(R.array.alarm_array)
        builder.setSingleChoiceItems(array,-1) { _, which->
            alert = which
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun setDateOnClick(view:View){
        val endT = findViewById<TextView>(R.id.end_date)
        val startT = findViewById<TextView>(R.id.start_date)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                _, year, monthOfYear, dayOfMonth ->
            val monthh = monthOfYear+1
            if (view.id == R.id.end_date) end = "$dayOfMonth/$monthh/$year"
            else start = "$dayOfMonth/$monthh/$year"


        }, year, month, day)

        val timePickerDialog : TimePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                _, hourOfDay, minute ->
            if (view.id == R.id.end_date) {
                end += " $hourOfDay:$minute "
                endT.text = "Finm do evento marcado para : $end"
            }
            else {
                start += " $hourOfDay:$minute "
                startT.text = "Inicio do evento marcado para : $start"
            }

        },hour,minute,true)
        timePickerDialog.show()
        dpd.show()


    }

    fun setImportanceOnClick(view:View) {
        val builder = AlertDialog.Builder(this)
        lateinit var dialog:AlertDialog
        val square = findViewById<ImageView>(R.id.importance_square)

        val array  = resources.getStringArray(R.array.importance_array)
        builder.setSingleChoiceItems(array,-1) { _, which->
            importance = which
            when (which){
                0 -> square.setImageResource(R.drawable.ic_square_low)
                1 -> square.setImageResource(R.drawable.ic_square_medium)
                2 -> square.setImageResource(R.drawable.ic_square_high)
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }

    fun saveEvent(view:View){
        val titleEt = findViewById<EditText>(R.id.event_title)
        val placeEt = findViewById<EditText>(R.id.event_location_input)
        if(titleEt.text != null && placeEt.text != null){
            title = titleEt.text.toString()
            place = placeEt.text.toString()
            if (!this::end.isInitialized) end = start
            val spinner = findViewById<Spinner>(R.id.repeticao_dropdown)
            val repeticao : Repeticao = spinner.selectedItem as Repeticao
            val event = EventModel(this.uid,title,start,end,repeticao,place,importance,alert)
            FirebaseFirestore.getInstance().collection("events").add(event)
        }
        val intent = Intent(this, MainActivity::class.java).apply {  }
        startActivity(intent)
        finish()

    }
    fun cancelEvent(view:View){
        val intent = Intent(this, MainActivity::class.java).apply {  }
        startActivity(intent)
        finish()
    }





}