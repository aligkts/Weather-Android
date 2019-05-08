package com.aligkts.weatherapp.presenter

import android.content.Context
import android.preference.PreferenceManager
import com.aligkts.weatherapp.data.MemoryCache
import com.aligkts.weatherapp.data.SingletonModel
import com.aligkts.weatherapp.data.network.NetworkDAO
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.Constant.Companion.API_FORECAST_BASE_URL
import com.aligkts.weatherapp.util.Constant.Companion.weatherAppId
import com.google.android.gms.maps.model.LatLng

class DetailPresenter(context: Context, private var mView: DetailContract.View) : DetailContract.Presenter {

    private val asyncTaskRequest by lazy { NetworkDAO() }
    private val dataListForecastFromRequest by lazy { ArrayList<ModelResponse>() }
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val language by lazy { prefs.getString("language", "en") }
    private val cache by lazy { MemoryCache.instance }

    override fun getSingletonData(currentCheck: String?): ModelResponse? {
        SingletonModel.instance?.let { _model ->
            return if (currentCheck == "current_clicked") {
                val value = _model.getCurrentList()
                mView.setDetailHeaderComponents(value.name,
                                                value.main,
                                                value.clouds,
                                                value.wind,
                                                value.weather)
                putDataListToCache(value)
                value
            } else {
                val value = _model.getOtherList()
                mView.setDetailHeaderComponents(value.name,
                                                value.main,
                                                value.clouds,
                                                value.wind,
                                                value.weather)
                putDataListToCache(value)
                value
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
        putForecastListToCache(dataListForecastFromRequest)
    }

    override fun setUiFromCache(currentCheck: String) {
        cache?.let { _cache ->
                val value = _cache.getLruWeatherDetail().get(currentCheck)
                value?.let { _value ->
                    mView.setDetailHeaderComponents(_value.name,
                                                    _value.main,
                                                    _value.clouds,
                                                    _value.wind,
                                                    _value.weather)
                }
        }
        cache?.let { _cache ->
            _cache.getLruForecastDetail().get("recyclerlist")?.let { _lastRecyclerList ->
                val listFromCache = ArrayList<ModelResponse>()
                for (i in 0 until _lastRecyclerList.size) {
                        _lastRecyclerList[i]?.let { listFromCache.add(it) }
                    mView.getForecastModelResponse(listFromCache)
                }
            }
        }
    }

    override fun putDataListToCache(dataList: ModelResponse) {
        cache?.let { _cache ->
            _cache.getLruWeatherDetail().put(dataList.name.toString(), dataList)
        }
    }

    override fun putForecastListToCache(list: ArrayList<ModelResponse>) {
        cache?.let { _cache ->
            val array = arrayOfNulls<ModelResponse>(list.size)
            list.toArray(array)
            _cache.getLruForecastDetail().put("recyclerlist", array)
        }
    }
}