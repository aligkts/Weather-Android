package com.aligkts.weatherapp.network.response

import com.aligkts.weatherapp.dto.forecastByLocation.City
import com.aligkts.weatherapp.dto.forecastByLocation.ListItem

data class ForecastByLocationResponse(
        val city: City? = null,
        val cnt: Int? = null,
        val cod: String? = null,
        val message: Double? = null,
        val list: List<ListItem?>? = null
)
