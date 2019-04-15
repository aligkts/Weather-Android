package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.network.model.ModelResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api url dynamic values
 */

interface ApiHelper {

    @GET("weather")
    fun getWeatherByLatLng(@Query("lat") lat: Double?,
                           @Query("lon") lon: Double?,
                           @Query("APPID") appID: String,
                           @Query("units") units: String): Call<ModelResponse>
}