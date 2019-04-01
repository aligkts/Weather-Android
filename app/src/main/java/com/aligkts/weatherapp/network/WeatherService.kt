package com.aligkts.weatherapp.network

import com.aligkts.weatherapp.network.response.ForecastByLocationResponse
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


    @GET("forecast")
    fun getForecastByLatLng(
            @Query("lat") lat: Double?,
            @Query("lon") lon: Double?,
            @Query("APPID") appID: String,
            @Query("units") units: String
    ): Call<ForecastByLocationResponse>


}