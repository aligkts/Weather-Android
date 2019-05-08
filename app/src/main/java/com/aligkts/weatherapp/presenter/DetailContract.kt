package com.aligkts.weatherapp.presenter

import com.aligkts.weatherapp.data.dto.weatherbylocation.Clouds
import com.aligkts.weatherapp.data.dto.weatherbylocation.Main
import com.aligkts.weatherapp.data.dto.weatherbylocation.WeatherItem
import com.aligkts.weatherapp.data.dto.weatherbylocation.Wind
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.google.android.gms.maps.model.LatLng

/**
 * Define the contract between WeatherDetailFragment and DetailPresenter
 */

interface DetailContract {

    interface View {
        fun getForecastModelResponse(list: ArrayList<ModelResponse>)
        fun setDetailHeaderComponents(name: String?, main: Main?, clouds: Clouds?, wind: Wind?, weather: List<WeatherItem?>?)
    }

    interface Presenter {
        fun getSingletonData(currentCheck: String?): ModelResponse?
        fun getResponseWithoutRetrofitByLatLng(latLng: LatLng)
        fun presentedForecast(modelResponse: ModelResponse)
        fun setUiFromCache(currentCheck: String)
        fun putDataListToCache(dataList: ModelResponse)
        fun putForecastListToCache(list: ArrayList<ModelResponse>) {

        }
    }

}