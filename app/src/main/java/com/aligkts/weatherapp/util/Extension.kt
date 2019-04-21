package com.aligkts.weatherapp.util

import android.annotation.SuppressLint
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
    val centi = (this.toInt().minus(32)).div(1.8000)
    return Math.round(centi).toString() + 0x00B0.toChar()
}

infix fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

@SuppressLint("SimpleDateFormat")
fun String.dateDoDay(): String {
    val inFormat = SimpleDateFormat("yyy-MM-dd")
    val date = inFormat.parse(this)
    val outFormat = SimpleDateFormat("EEEE")
    return outFormat.format(date)
}
