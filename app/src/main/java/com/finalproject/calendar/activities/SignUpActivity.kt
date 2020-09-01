package com.finalproject.calendar.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.finalproject.calendar.R
import com.finalproject.calendar.models.User
import com.finalproject.calendar.services.LoginService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_sign_up.*

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
        val passwordConfirm: String = this.confirm_password.text.toString()
        user.full_name = this.full_name.text.toString()

        if (user.password != passwordConfirm){
            this.password.setBackgroundColor(Color.parseColor("#FF8484"))
            this.confirm_password.setBackgroundColor(Color.parseColor("#FF8484"))

            this.password.text.clear()
            this.confirm_password.text.clear()
            Toast.makeText(baseContext, "As senhas devem ser iguais", Toast.LENGTH_LONG).show()
        }
        else if(user.email != null && user.password != null && user.full_name != null){
            createAccount()
        }
        else{
            Toast.makeText(baseContext, "Você deve informar todos os campos", Toast.LENGTH_LONG).show()
        }
    }

    fun navigateLoginOnClick(view: View){
        val intent = Intent(baseContext, LoginActivity::class.java).apply {  }
        startActivity(intent)
        finish()
    }

    fun createAccount() {
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                Log.d("SUCCESS", "Login efetuado com sucesso")
                task.result.also { authResult ->
                    user.uid = authResult?.user!!.uid
                }
                val profileUpdater = UserProfileChangeRequest.Builder()
                    .setDisplayName(user.full_name)
                    .build()
                
                auth.currentUser?.updateProfile(profileUpdater)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "Conta criada com sucesso",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
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