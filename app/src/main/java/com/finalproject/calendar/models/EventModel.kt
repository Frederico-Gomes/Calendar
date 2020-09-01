package com.finalproject.calendar.models

import com.finalproject.calendar.enums.Repeticao

class EventModel(
    var uid: String?,
    var title : String,
    var date : String,
    var start: String?,
    var end: String?,
    var repeticao: Repeticao?,
    var local: String?,
    var importance : Int
)