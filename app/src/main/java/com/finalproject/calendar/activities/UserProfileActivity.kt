package com.finalproject.calendar.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.finalproject.calendar.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.view.*

class UserProfileActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        auth = FirebaseAuth.getInstance()

        this.display_name.isEnabled = false
        this.email.isEnabled = false
        this.password.isEnabled = false
        this.password_confirm.isEnabled = false

    }

    fun habilitarEdicaoOnClick(view: View){
        this.display_name.isEnabled = true
        this.email.isEnabled = false
        this.password.isEnabled = true
        this.password_confirm.isEnabled = true
    }

    fun updateData(view: View){
        var password = this.password.text.toString()
        var password_confirm = this.password_confirm.text.toString()
        var displayName = this.display_name.text.toString()

        if(password_confirm != password){
            this.password.setBackgroundColor(Color.parseColor("#FF8484"))
            this.password_confirm.setBackgroundColor(Color.parseColor("#FF8484"))

            this.password.text.clear()
            this.password_confirm.text.clear()
            Toast.makeText(this, "As senhas devem ser iguais", Toast.LENGTH_LONG).show()
        }
        else if(password == null && displayName == null){
            Toast.makeText(this, "Você deve preencher todos os campos", Toast.LENGTH_LONG).show()
        }
        else{
            val profileUpdater = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            var user = auth.currentUser
            user?.updateProfile(profileUpdater)
            user?.updatePassword(password)?.addOnCompleteListener{task ->
                if (task.isSuccessful){
                    Toast.makeText(
                        baseContext, "Dados atualizados com sucesso!",
                        Toast.LENGTH_LONG
                    ).show()
                    clearFields()
                }
                else{
                    Toast.makeText(
                        baseContext, "Eita! Deu ruim",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun clearFields(){
        this.display_name.text.clear()
        this.email.text.clear()
        this.password.text.clear()
        this.password_confirm.text.clear()
    }
}