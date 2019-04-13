package com.aligkts.weatherapp.data.network.model

import com.aligkts.weatherapp.data.dto.weatherbylocation.*
import com.google.gson.annotations.SerializedName

data class ModelResponse(@field:SerializedName("dt_txt") val dt_txt: String? = null,
                         @field:SerializedName("coord") val coord: Coord? = null,
                         @field:SerializedName("weather") val weather: List<WeatherItem?>? = null,
                         @field:SerializedName("name") val name: String? = null,
                         @field:SerializedName("main") val main: Main? = null,
                         @field:SerializedName("clouds") val clouds: Clouds? = null,
                         @field:SerializedName("id") val id: Int? = null,
                         @field:SerializedName("wind") val wind: Wind? = null)

