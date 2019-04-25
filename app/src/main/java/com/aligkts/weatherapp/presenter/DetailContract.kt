package com.aligkts.weatherapp.presenter

import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.google.android.gms.maps.model.LatLng

/**
 * Define the contract between WeatherDetailFragment and DetailPresenter
 */

interface DetailContract {

    interface View {
        fun getForecastModelResponse(list: ArrayList<ModelResponse>)
    }

    interface Presenter {
        fun getSingletonData(currentCheck: String?) : ModelResponse?
        fun getResponseWithoutRetrofitByLatLng(latLng: LatLng)
        fun presentedForecast(modelResponse: ModelResponse)
    }

}