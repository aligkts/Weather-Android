package com.aligkts.weatherapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.SimpleDateFormat

/**
 * Simplify the functions that use frequently
 */

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Double.tempFormatter(): String {
    var centi = (this.toInt().minus(32)).div(1.8000)
    centi = Math.round(centi).toDouble()
    return centi.toString() + 0x00B0.toChar()
}

infix fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun String.dateDoDay(): String {
    val inFormat = SimpleDateFormat("yyy-MM-dd")
    val date = inFormat.parse(this)
    val outFormat = SimpleDateFormat("EEEE")
    val day = outFormat.format(date)
    return day
}
