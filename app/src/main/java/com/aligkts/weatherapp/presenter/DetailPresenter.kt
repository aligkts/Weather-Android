package com.aligkts.weatherapp.presenter

import android.content.Context
import android.util.Log
import com.aligkts.weatherapp.data.SingletonModel
import com.aligkts.weatherapp.data.network.IRequestResult
import com.aligkts.weatherapp.data.network.NetworkDAO
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.Constant.Companion.API_FORECAST_BASE_URL
import com.aligkts.weatherapp.util.Constant.Companion.weatherAppId
import com.aligkts.weatherapp.util.UnitType
import com.google.android.gms.maps.model.LatLng

class DetailPresenter(private var context: Context, private var mView: DetailContract.view) : DetailContract.presenter, IRequestResult{

    private val asyncTaskRequest by lazy { NetworkDAO() }
    private val dataListForecastFromRequest by lazy {  ArrayList<ModelResponse>() }

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
        asyncTaskRequest.listener = this
        asyncTaskRequest.execute(API_FORECAST_BASE_URL + "lat=" +
                                          latLng.latitude +
                                          "&lon=" + latLng.longitude +
                                          "&&APPID=" + weatherAppId +
                                          "&units="+ UnitType.Imperial.toString())
    }

    override fun onSuccess(modelResponse: ModelResponse) {
        dataListForecastFromRequest.add(modelResponse)
        mView.getForecastModelResponse(dataListForecastFromRequest)
    }

    override fun onFailure(t: Throwable) {
        Log.e("API","Request failed".plus(t.localizedMessage))
    }
}