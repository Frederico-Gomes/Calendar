package com.finalproject.calendar.iservices

import com.finalproject.calendar.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface ILoginService {
    fun login(user: User): Boolean
    fun createAccount(user: User): Boolean
}