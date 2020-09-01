package com.finalproject.calendar.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.finalproject.calendar.R
import com.finalproject.calendar.models.User
import com.finalproject.calendar.services.LoginService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val loginService = LoginService()
    var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar_login) as Toolbar
        toolbar.setNavigationOnClickListener{view ->
            finish()
        }
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
//        updateUI(currentUser)
    }

    fun loginOnClick(view: View){
        user.email = this.email.text.toString()
        user.password = this.password.text.toString()

        if(user.email != null && user.password != null){
            auth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener(this){task: Task<AuthResult> ->
                if(task.isSuccessful){
                    Log.d("SUCCESS", "Login efetuado com sucesso")
                    Toast.makeText(
                        baseContext, "Entrou !",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("user", user)
                    }
                    startActivity(intent)
                    finish()
                }
                else{
                    Log.w("LOGIN-ERROR", "Erro ao efetuar login", task.exception)
                    Toast.makeText(
                        baseContext, task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        else{
            Toast.makeText(baseContext, "Você deve informar todos os campos", Toast.LENGTH_LONG).show()
        }
    }
}
