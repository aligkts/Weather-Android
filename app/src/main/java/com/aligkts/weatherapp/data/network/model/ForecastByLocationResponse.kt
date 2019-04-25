package com.aligkts.weatherapp.data.network.model

import com.aligkts.weatherapp.data.dto.forecastbylocation.City

data class ForecastByLocationResponse(val city: City? = null,
                                      val cnt: Int? = null,
                                      val cod: String? = null,
                                      val message: Double? = null,
                                      val list: List<ModelResponse?>? = null)
