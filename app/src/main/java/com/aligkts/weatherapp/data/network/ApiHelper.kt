package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.network.model.ForecastByLocationResponse
import com.aligkts.weatherapp.data.network.model.ModelResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHelper {

    @GET("weather")
    fun getWeatherByLatLng(@Query("lat") lat: Double?,
                           @Query("lon") lon: Double?,
                           @Query("APPID") appID: String,
                           @Query("units") units: String): Call<ModelResponse>

    @GET("forecast")
    fun getForecastByLatLng(@Query("lat") lat: Double?,
                            @Query("lon") lon: Double?,
                            @Query("APPID") appID: String,
                            @Query("units") units: String): Call<ForecastByLocationResponse>
}