package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.Constant.Companion.weatherAppId
import com.aligkts.weatherapp.util.UnitType
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Response

/**
 * Retrofit request class
 */

class Proxy(var listenerRequestResult: IRequestResult) {

    lateinit var parsedModel: ModelResponse

    fun getRequestByLocation(latLng: LatLng) {
        RetrofitClient.getApi()
                .create(ApiHelper::class.java)
                .getWeatherByLatLng(latLng.latitude, latLng.longitude, weatherAppId, UnitType.Imperial.toString())
                .enqueue(object : retrofit2.Callback<ModelResponse> {
                    override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                        listenerRequestResult.onFailure(t)
                    }

                    override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {
                        response.body()?.let {
                            parsedModel = it
                        }
                        listenerRequestResult.onSuccess(parsedModel)
                    }
                })
    }

    fun getRequestByLocationBookmark(latitude: Double,
                                     longitude: Double) {
        RetrofitClient.getApi()
                .create(ApiHelper::class.java)
                .getWeatherByLatLng(latitude, longitude, weatherAppId, UnitType.Imperial.toString())
                .enqueue(object : retrofit2.Callback<ModelResponse> {
                    override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                        listenerRequestResult.onFailure(t)
                    }

                    override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {
                        response.body()?.let {
                            parsedModel = it
                        }
                        listenerRequestResult.onSuccess(parsedModel)
                    }
                })
    }
}