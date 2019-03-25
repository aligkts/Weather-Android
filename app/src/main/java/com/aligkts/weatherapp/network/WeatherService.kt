package com.aligkts.weatherapp.network

import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun getWeatherByLatLng(
            @Query("lat") lat: Double?,
            @Query("lon") lon: Double?,
            @Query("APPID") appID: String,
            @Query("units") units: String
    ): Call<WeatherByLocationResponse>

    /*
    @Path("lat") lat: Double,
    @Path("lon") lon: Double*/


    @GET("data/2.5/weather?q={city name}")
    fun getWeatherByCityName()

}