package com.aligkts.weatherapp.dto.weatherByLocation

import com.google.gson.annotations.SerializedName

data class Clouds(

    @field:SerializedName("all")
    val all: Int? = null
)