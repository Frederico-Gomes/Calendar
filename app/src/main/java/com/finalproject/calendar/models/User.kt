package com.finalproject.calendar.models

import java.io.Serializable

class User : Serializable {
    lateinit var full_name: String
    lateinit var username: String
    lateinit var uid: String
    lateinit var email: String
    lateinit var password: String

    constructor(){

    }

    fun toHashMap(): HashMap<String, String> {
        return hashMapOf(
            "full_name" to this.full_name,
            "username" to this.username,
            "uid" to this.uid
        )
    }
}