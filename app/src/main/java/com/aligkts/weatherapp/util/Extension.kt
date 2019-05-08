package com.aligkts.weatherapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Simplify the functions that use frequently
 */

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun Double.tempToCentigrade(): String {
    return Math.round(this).toString() + 0x00B0.toChar()
}

fun Double.tempToFahrenheit(): String {
    return Math.round(this).toString() + " \u2109"
}

infix fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

@SuppressLint("SimpleDateFormat")
fun String.dateDoDay(): String {
    val inFormat = SimpleDateFormat("yyy-MM-dd")
    val date = inFormat.parse(this)
    val lang = Locale.getDefault().language
    return if (lang == "tr") {
        val outFormat = SimpleDateFormat("EEEE")
        outFormat.format(date)
    } else {
        val outFormat = SimpleDateFormat("EEEE", Locale.US)
        outFormat.format(date)
    }
}
