package com.finalproject.calendar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.finalproject.calendar.R
import com.finalproject.calendar.models.User
import com.finalproject.calendar.services.LoginService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.email
import kotlinx.android.synthetic.main.activity_signin.password

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val loginService = LoginService()
    var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }

    fun buttonListener(view: View){
        user.email = this.email.text.toString()
        user.password = this.password.text.toString()
        user.full_name = this.full_name.text.toString()
        user.username = this.username.text.toString()

        if(user.email != null && user.password != null){
            createAccount()
        }
        else{
            Toast.makeText(baseContext, "Você deve informar todos os campos", Toast.LENGTH_LONG).show()
        }
    }

    fun createAccount() {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                Log.d("SUCCESS", "Login efetuado com sucesso")
                task.result.also { authResult ->
                    user.uid = authResult?.user!!.uid
                }
                loginService.createUser(user)
                Toast.makeText(
                    baseContext, "Conta criada com sucesso",
                    Toast.LENGTH_LONG
                ).show()
            }
            else{
                Log.w("SIGNIN-ERROR", "Deu ruim", task.exception)
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}