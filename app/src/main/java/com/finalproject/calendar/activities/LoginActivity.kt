package com.finalproject.calendar.activities

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.finalproject.calendar.R
import com.finalproject.calendar.models.User
import com.finalproject.calendar.services.LoginService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val loginService = LoginService()
    var user: User = User()
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        verifyAuth()
    }

    fun verifyAuth(){
        currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("user", user)
            }
            startActivity(intent)
        }
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
                    verifyAuth()
                }
                else{
                    Log.w("LOGIN-ERROR", "Erro ao efetuar login", task.exception)
                }
            }
        }
        else{
            Toast.makeText(baseContext, "Você deve informar todos os campos", Toast.LENGTH_LONG).show()
        }
    }

    fun login(){
        if(loginService.login(user)){
            val user = auth.currentUser
            Toast.makeText(
                baseContext, "Entrou !",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                baseContext, "Authentication failed.",
                Toast.LENGTH_LONG
            ).show()
        }

    }
}
