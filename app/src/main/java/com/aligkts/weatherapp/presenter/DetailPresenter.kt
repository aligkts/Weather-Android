package com.aligkts.weatherapp.presenter

import android.content.Context
import android.preference.PreferenceManager
import com.aligkts.weatherapp.data.SingletonModel
import com.aligkts.weatherapp.data.network.NetworkDAO
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.Constant.Companion.API_FORECAST_BASE_URL
import com.aligkts.weatherapp.util.Constant.Companion.weatherAppId
import com.aligkts.weatherapp.util.UnitType
import com.google.android.gms.maps.model.LatLng

class DetailPresenter(context: Context,private var mView: DetailContract.View) : DetailContract.Presenter {

    private val asyncTaskRequest by lazy { NetworkDAO() }
    private val dataListForecastFromRequest by lazy {  ArrayList<ModelResponse>() }
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val language by lazy { prefs.getString("language","en") }

    override fun getSingletonData(currentCheck: String?): ModelResponse? {
        SingletonModel.instance?.let { _model ->
            return if (currentCheck == "current_clicked") {
                _model.getCurrentList()
            } else {
                _model.getOtherList()
            }
        }
        return null
    }

    override fun getResponseWithoutRetrofitByLatLng(latLng: LatLng) {
        val unitType = prefs.getString("unitType", "Metric")
        asyncTaskRequest.listener = this
        asyncTaskRequest.execute(API_FORECAST_BASE_URL + "lat=" +
                                          latLng.latitude +
                                          "&lon=" + latLng.longitude +
                                          "&&APPID=" + weatherAppId +
                                          "&units="+ unitType+
                                          "&lang="+language)
    }

    override fun presentedForecast(modelResponse: ModelResponse) {
        dataListForecastFromRequest.add(modelResponse)
        mView.getForecastModelResponse(dataListForecastFromRequest)
    }

}