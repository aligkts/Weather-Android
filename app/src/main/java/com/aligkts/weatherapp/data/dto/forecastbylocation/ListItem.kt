package com.aligkts.weatherapp.data.dto.forecastbylocation

data class ListItem(val dt: Long? = null,
                    val rain: Rain? = null,
                    val dt_txt: String? = null,
                    val weather: List<WeatherItem?>? = null,
                    val main: Main? = null,
                    val clouds: Clouds? = null,
                    val sys: Sys? = null,
                    val wind: Wind? = null)
