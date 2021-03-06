package com.finalproject.calendar.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.finalproject.calendar.R
import com.finalproject.calendar.Utils
import com.finalproject.calendar.enums.Repeticao
import com.finalproject.calendar.models.EventModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_event.*
import java.lang.Exception
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
    var edit = false

    private lateinit var  test : EventModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, Repeticao.values()).also { adapter->
            repeticao_dropdown.adapter = adapter
        }
        if(intent.getStringExtra("edit")!= null) {
            test = intent.getSerializableExtra("event") as EventModel
            editStuff()
        }


    }

    fun editStuff() {
        edit = true
        changeLayoult(test ,"Editar Evento")
    }


    fun changeLayoult(event : EventModel, kind : String){
        //todo adapt spiner to show event spinner, in database all data are ok
        findViewById<Button>(R.id.delete).visibility = Button.VISIBLE
        findViewById<TextView>(R.id.toolbar_title).text = kind
        findViewById<TextView>(R.id.event_title).hint = event.title
        val alarms  =resources.getStringArray(R.array.alarm_array)
        findViewById<TextView>(R.id.alarm).text = alarms[event.alert]
        findViewById<TextView>(R.id.event_location_input).hint = event.place
        val square = findViewById<ImageView>(R.id.importance_square)
        when (event.importance) {
            0 -> square.setImageResource(R.drawable.ic_square_low)
            1 -> square.setImageResource(R.drawable.ic_square_medium)
            2 -> square.setImageResource(R.drawable.ic_square_high)
        }
    }


    fun setAlarmOnClick(view:View) {

            val builder = AlertDialog.Builder(this)
            lateinit var dialog: AlertDialog
            val array = resources.getStringArray(R.array.alarm_array)
            builder.setSingleChoiceItems(array, -1) { _, which ->
                if(!edit) alert = which
                else test.alert = which

                findViewById<TextView>(R.id.alarm).text = array[which]
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
            if(!edit) {
                if (view.id == R.id.end_date) end = "$dayOfMonth/$monthh/$year"
                else start = "$dayOfMonth/$monthh/$year"
            }else{
                if (view.id == R.id.end_date) test.end = "$dayOfMonth/$monthh/$year"
                else test.start = "$dayOfMonth/$monthh/$year"
            }


        }, year, month, day)

        val timePickerDialog : TimePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                _, hourOfDay, minute ->
            if(!edit) {
                if (view.id == R.id.end_date) {
                    end += " $hourOfDay:$minute "
                    endT.text = "Fim do evento marcado para : $end"
                } else {
                    start += " $hourOfDay:$minute "
                    startT.text = "Inicio do evento marcado para : $start"
                }
            }
            else{
                if (view.id == R.id.end_date) {
                    test.end += " $hourOfDay:$minute "
                    endT.text = "Fim do evento marcado para : ${test.end}"
                } else {
                    test.start += " $hourOfDay:$minute "
                    startT.text = "Inicio do evento marcado para : ${test.start}"
                }
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
            if(!edit) importance = which
            else test.importance = which
            when (which) {
                0 -> square.setImageResource(R.drawable.ic_square_low)
                1 -> square.setImageResource(R.drawable.ic_square_medium)
                2 -> square.setImageResource(R.drawable.ic_square_high)
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()

    }

    fun saveEvent(view: View){
        if(edit) {
            val spinner = findViewById<Spinner>(R.id.repeticao_dropdown)
            test.repeticao = spinner.selectedItem as Repeticao
            if(validate()) {
                var document = FirebaseFirestore.getInstance().collection("events").document(test.id_event.toString())
                document.update(
                        mapOf(
                                "title" to test.title,
                                "alert" to test.alert,
                                "end" to test.end,
                                "start" to test.start,
                                "place" to test.place,
                                "importance" to test.importance,
                                "repeticao" to test.repeticao as Repeticao
                        )
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Evento atualizado!", Toast.LENGTH_LONG)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG)
                    }
                }
            }else{
                Toast.makeText(this, "Você deve fornecer todas as informações", Toast.LENGTH_LONG).show()
            }
        }
        else{
            if(validate()){
                title = findViewById<EditText>(R.id.event_title).text.toString()
                place = findViewById<EditText>(R.id.event_location_input).text.toString()
                if (!this::end.isInitialized) end = start
                val spinner = findViewById<Spinner>(R.id.repeticao_dropdown)
                val repeticao : Repeticao = spinner.selectedItem as Repeticao
                val event = EventModel(this.uid,null,title,start,end,repeticao,place,importance,alert)
                FirebaseFirestore.getInstance().collection("events").add(event).addOnSuccessListener { ref ->
                    Utils.createAlarm(this, "Você tem uma nova atividade", event.title, event, 0)
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Você deve fornecer todas as informações", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun validate(): Boolean{
        val startV = findViewById<TextView>(R.id.start_date)
        val endV = findViewById<TextView>(R.id.end_date)
        val titleV = findViewById<TextView>(R.id.event_title)
        val placeV = findViewById<TextView>(R.id.event_location_input)

        if(!edit){
            if(
                startV.text.isEmpty()||
                titleV.text.isEmpty()||
                placeV.text.isEmpty()){
                return false
            }
        }
        else{
            if(!startV.text.isEmpty()) test.start = startV.text.toString()
            if(!endV.text.isEmpty()) test.end = endV.text.toString()
            if(!titleV.text.isEmpty()) test.title = titleV.text.toString()
            if(!placeV.text.isEmpty()) test.place = placeV.text.toString()
        }
        return true
    }

    fun cancelEvent(view:View){
        val intent = Intent(this, MainActivity::class.java).apply {  }
        startActivity(intent)
        finish()
    }

    fun deleteEvent(view:View){
        try {
            var id: String = ""
            id = test.id_event.toString()
            FirebaseFirestore.getInstance().collection("events").document(id).delete().addOnSuccessListener {
                Toast.makeText(this, "Evento excluido com sucesso!", Toast.LENGTH_LONG)
                val intent = Intent(this, MainActivity::class.java).apply {  }
                startActivity(intent)
                finish()
            }
        }
        catch (ex: Exception){
            Log.w("ERROR: ", ex.message)
        }
    }





}