package com.aligkts.weatherapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Double.tempFormatter(): String {
    var centi = (this.toInt().minus(32)).div(1.8000)
    centi = Math.round(centi).toDouble()
    return centi.toString() + 0x00B0.toChar()
}
