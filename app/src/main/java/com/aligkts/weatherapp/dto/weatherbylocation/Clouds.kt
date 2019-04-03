package com.aligkts.weatherapp.dto.weatherbylocation

import com.google.gson.annotations.SerializedName

data class Clouds(@field:SerializedName("all")  val all: Int? = null)