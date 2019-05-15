package com.dicoding.soccer.utilities

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.invicible(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun dateFormat(value: String?): String {
    val parseDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val formatDate = SimpleDateFormat("EEE, d MMM yyyy", Locale("in", "ID"))
    formatDate.timeZone = TimeZone.getTimeZone("UTC")
    return formatDate.format(parseDate.parse(value))
}