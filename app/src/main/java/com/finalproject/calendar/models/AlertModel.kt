package com.finalproject.calendar.models

data class AlertModel (
    val importance : Int,
    val date : String,
    val alarm : String,
    val place : String = "none"

)