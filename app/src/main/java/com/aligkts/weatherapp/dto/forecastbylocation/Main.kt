package com.aligkts.weatherapp.dto.forecastbylocation

data class Main(val temp: Double? = null,
                val tempMin: Double? = null,
                val grndLevel: Double? = null,
                val tempKf: Double? = null,
                val humidity: Int? = null,
                val pressure: Double? = null,
                val seaLevel: Double? = null,
                val tempMax: Double? = null)
