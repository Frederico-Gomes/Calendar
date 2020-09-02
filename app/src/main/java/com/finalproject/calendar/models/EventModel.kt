package com.finalproject.calendar.models

import com.finalproject.calendar.enums.Repeticao
import java.io.Serializable

class EventModel(
    var uid: String?,
    var id_event : String ?,
    var title: String,
    var start: String,
    var end: String,
    var repeticao: Repeticao?,
    var place: String?,
    var importance: Int,
    var alert: Int
):Serializable