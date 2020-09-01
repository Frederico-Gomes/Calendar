package com.finalproject.calendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.finalproject.calendar.R
import com.finalproject.calendar.enums.Repeticao
import kotlinx.android.synthetic.main.activity_create_event.*

class CreateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, Repeticao.values()).also { adapter->
            repeticao_dropdown.adapter = adapter
        }
    }
}