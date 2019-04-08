package com.aligkts.weatherapp.network

import com.aligkts.weatherapp.network.response.ForecastByLocationResponse
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import retrofit2.Call
import retrofit2.Response

class Proxy {

    val weatherAppId = "3c75e1a077769372966bc6050f85b57a"

    fun getRequestByLocation(lat: Double,
                             lon: Double,
                             requestCallback: (isSuccess: Boolean, response: WeatherByLocationResponse?) -> (Unit)) {
        RetrofitClient.getClient()
                .create(WeatherService::class.java)
                .getWeatherByLatLng(lat, lon, weatherAppId, "Imperial")
                .enqueue(object : retrofit2.Callback<WeatherByLocationResponse> {
                    override fun onFailure(call: Call<WeatherByLocationResponse>, t: Throwable) {
                        requestCallback(false, null)
                    }

                    override fun onResponse(call: Call<WeatherByLocationResponse>, response: Response<WeatherByLocationResponse>) {
                        requestCallback(true, response.body())
                    }
                })
    }

    fun getRequestForForecast(lat: Double?,
                              lon: Double?,
                              requestCallback: (isSuccess: Boolean, response: ForecastByLocationResponse?) -> (Unit)) {
        RetrofitClient.getClient()
                .create(WeatherService::class.java)
                .getForecastByLatLng(lat, lon, weatherAppId, "Imperial")
                .enqueue(object : retrofit2.Callback<ForecastByLocationResponse> {
                    override fun onFailure(call: Call<ForecastByLocationResponse>, t: Throwable) {
                        requestCallback(false, null)
                    }

                    override fun onResponse(call: Call<ForecastByLocationResponse>,
                                            response: Response<ForecastByLocationResponse>) {
                        requestCallback(true, response.body())
                    }
                })
    }



}