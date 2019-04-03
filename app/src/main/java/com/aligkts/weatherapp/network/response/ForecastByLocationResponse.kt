package com.aligkts.weatherapp.network.response

import com.aligkts.weatherapp.dto.forecastbylocation.City
import com.aligkts.weatherapp.dto.forecastbylocation.ListItem

data class ForecastByLocationResponse(val city: City? = null,
                                      val cnt: Int? = null,
                                      val cod: String? = null,
                                      val message: Double? = null,
                                      val list: List<ListItem?>? = null)
