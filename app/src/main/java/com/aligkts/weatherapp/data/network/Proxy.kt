package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.Constant.Companion.weatherAppId
import com.aligkts.weatherapp.util.UnitType
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Response

class Proxy(var listenerRequestResult: IRequestResult) {

    lateinit var parsedModel: ModelResponse
    val list = ArrayList<ModelResponse>()

    fun getRequestByLocation(latLng: LatLng) {
        RetrofitClient.getApi()
                .create(ApiHelper::class.java)
                .getWeatherByLatLng(latLng.latitude, latLng.longitude, weatherAppId, UnitType.Imperial.toString())
                .enqueue(object : retrofit2.Callback<ModelResponse> {
                    override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                        listenerRequestResult?.let {
                            it.onFailure(t)
                        }
                    }

                    override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {
                        response.body()?.let {
                            parsedModel = it
                        }
                        listenerRequestResult?.let {
                            it.onSuccess(parsedModel)
                        }
                    }
                })
    }

    fun getRequestByLocationBookmark(lat: Double,
                                     lon: Double) {
        RetrofitClient.getApi()
                .create(ApiHelper::class.java)
                .getWeatherByLatLng(lat, lon, weatherAppId, UnitType.Imperial.toString())
                .enqueue(object : retrofit2.Callback<ModelResponse> {
                    override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {
                        response.body()?.let {
                            parsedModel = it
                        }
                        list.add(parsedModel)
                        /*listener?.let {
                            it.bookmarkList(list)
                        }*/

                    }
                })
    }
}