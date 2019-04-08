package com.aligkts.weatherapp.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.dto.forecastbylocation.ListItem
import com.aligkts.weatherapp.network.AsyncTaskHandleJson
import com.aligkts.weatherapp.util.DownloadImage
import com.aligkts.weatherapp.util.Singleton
import com.aligkts.weatherapp.network.Proxy
import com.aligkts.weatherapp.network.response.ForecastByLocationResponse
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import com.aligkts.weatherapp.ui.adapter.DetailAdapter
import com.aligkts.weatherapp.util.OnTaskCompleted
import kotlinx.android.synthetic.main.fragment_weather_detail.*


class WeatherDetailFragment : Fragment() , OnTaskCompleted{

    private var dataList = WeatherByLocationResponse()
    private var mAdapter = DetailAdapter(ArrayList())
    private var dataListForecastFromRequest = ArrayList<ListItem>()
    private var responseModel = ForecastByLocationResponse()
    private val API_IMAGE_BASE_URL = "http://openweathermap.org/img/w/"
    private val API_FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast?"
    private val APP_ID = "3c75e1a077769372966bc6050f85b57a"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentCheck = arguments?.let {
            it.getString("bundle")
        }
        Singleton.instance?.let { _instance ->
            dataList = if (currentCheck == "current_clicked") {
                _instance.getCurrentList()
            } else {
                _instance.getOtherList()
            }
        }
        txtCurrentLocDetail.text = dataList.name
        dataList.main?.let {
            txtCurrentTempDetail.text = tempFormatter(it.temp)
            txtHumidity.text = it.humidity.toString()
        }
        dataList.clouds?.let { _clouds ->
            txtRainPossibility.text = _clouds.all.toString()
        }
        dataList.wind?.let { _wind ->
            _wind.speed?.let { _speed ->
                txtWindSpeed.text = _speed.toInt().toString()
            }
        }
        dataList.weather?.let { _listWeather ->
            _listWeather.first()?.let { _index ->
                val weatherStatus = _index.icon.toString()
                val url = API_IMAGE_BASE_URL.plus(weatherStatus).plus(".png")
                DownloadImage(imgWeatherIconDetail).execute(url)
            }
        }
        dataList.coord?.let { _coord ->
            val asyncTaskHandleJson = AsyncTaskHandleJson()
            asyncTaskHandleJson.listener= this
            asyncTaskHandleJson.execute(API_FORECAST_BASE_URL+"lat="+_coord.lat+"&lon="+_coord.lon+"&&APPID="+APP_ID+"&units=Metric")
        }
    }

    override fun onTaskCompleted(model: ForecastByLocationResponse) {
        model.list?.let {_responseList ->
            for(i in 0 until _responseList.size step 8) {
                _responseList[i]?.let {
                    dataListForecastFromRequest.add(it)
                }
            }
            setRecyclerAdapter(dataListForecastFromRequest)
        }
    }

    private fun tempFormatter(temp: Double?): String? {
        temp?.let { _temp ->
            var centi = (_temp.toInt().minus(32)).div(1.8000)
            centi = Math.round(centi).toDouble()
            return centi.toString() + 0x00B0.toChar()
        }
        return null
    }

    private fun setRecyclerAdapter(list: ArrayList<ListItem>) {
        recyclerDetail.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            mAdapter.setNewList(list)
        }
    }
}
