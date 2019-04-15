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

class Proxy {

    lateinit var parsedModel: ModelResponse

     fun getResponseFromApiByLatLng(latLng: LatLng,
                                    requestCallback: (isSuccess: Boolean, response: ModelResponse?) -> (Unit)) {
        RetrofitClient.getApi()
            .create(ApiHelper::class.java)
            .getWeatherByLatLng(latLng.latitude, latLng.longitude, weatherAppId, UnitType.Imperial.toString())
            .enqueue(object : CustomCallBack<ModelResponse> {
                override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                    requestCallback(false,null)
                }

                override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {
                    response.body()?.let { _modelResponse ->
                        parsedModel = _modelResponse
                    }
                    requestCallback(true, parsedModel)
                }
            })
    }

}