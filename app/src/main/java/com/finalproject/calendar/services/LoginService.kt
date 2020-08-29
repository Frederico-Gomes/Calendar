package com.finalproject.calendar.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.finalproject.calendar.iservices.ILoginService
import com.finalproject.calendar.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginService : ILoginService{

    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    constructor(){
        auth = FirebaseAuth.getInstance()
    }

    override fun login(user: User): Boolean {
        var task = auth.signInWithEmailAndPassword(user.email, user.password)

        if(task.isSuccessful){
            Log.d("SUCCESS", "Login efetuado com sucesso")
            return true;
        }
        else{
            Log.w("LOGIN-ERROR", "Erro ao efetuar login", task.exception)
            return false;
        }
    }

    override fun createAccount(user: User): Boolean {
        var task = auth.createUserWithEmailAndPassword(user.email, user.password)

        if(task.isSuccessful){
            Log.d("SUCCESS", "Login efetuado com sucesso")
            task.result.also { authResult ->
                user.uid = authResult?.user!!.uid
            }
            return true;
        }
        else{
            Log.w("SIGNIN-ERROR", "Deu ruim", task.exception)
            return false;
        }
    }

    fun createUser(user: User){
        val userData = user.toHashMap()
        db.collection("users").add(userData).addOnCompleteListener { task -> if (!task.isSuccessful) throw task.exception!!}
    }
}
