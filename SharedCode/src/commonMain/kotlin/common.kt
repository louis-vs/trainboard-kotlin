package com.softwire.lner.trainboard.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.parse

expect fun platformName(): String

fun createAppTitle(): String {
    return "Journey Planner"
}

fun dateTimeTzToString(dateTimeTz: DateTimeTz) = dateTimeTz.format("HH:mm")

fun extraDay(dateTimeTz: DateTimeTz): String {
    val targetDate = dateTimeTz.dayOfYear
    val nowDate = DateTimeTz.nowLocal().dayOfYear
    if (targetDate != nowDate){
        return "+${targetDate - nowDate}"
    }
    return ""
}

fun stringToDateTimeTz(dateTimeTz: String) : DateTimeTz {
    return DateFormat("YYYY-MM-ddTHH:mm:ss.000XXX").parse(dateTimeTz)
}


