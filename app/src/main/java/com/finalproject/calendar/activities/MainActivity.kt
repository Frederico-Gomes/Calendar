package com.finalproject.calendar.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.finalproject.calendar.R
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun logoutOnClick(view: View){
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        Toast.makeText(this, "Bye !", Toast.LENGTH_SHORT).show()
    }
}

