package com.aligkts.weatherapp.dto.forecastbylocation

data class City(val country: String? = null,
                val coord: Coord? = null,
                val name: String? = null,
                val id: Int? = null,
                val population: Int? = null)
