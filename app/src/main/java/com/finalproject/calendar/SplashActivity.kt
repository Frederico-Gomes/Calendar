package com.finalproject.calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.finalproject.calendar.activities.CreateEventActivity
import com.finalproject.calendar.activities.MainActivity
import com.finalproject.calendar.activities.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val splashTime = 3000L
    private lateinit var handler: Handler
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()

        handler = Handler()
        handler.postDelayed({
            authVerify()
        }, splashTime)
    }

    private fun authVerify(){
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("user", currentUser)
            }
            startActivity(intent)
        }
        else{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}