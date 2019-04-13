package com.aligkts.weatherapp.view.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.dto.weatherbylocation.Clouds
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.dto.weatherbylocation.Main
import com.aligkts.weatherapp.data.dto.weatherbylocation.WeatherItem
import com.aligkts.weatherapp.data.dto.weatherbylocation.Wind
import com.aligkts.weatherapp.data.network.IRequestResult
import com.aligkts.weatherapp.data.network.NetworkDAO
import com.aligkts.weatherapp.util.DownloadImage
import com.aligkts.weatherapp.data.SingletonModel
import com.aligkts.weatherapp.data.network.model.ForecastByLocationResponse
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.view.ui.adapter.DetailAdapter
import com.aligkts.weatherapp.util.Constant.Companion.API_FORECAST_BASE_URL
import com.aligkts.weatherapp.util.Constant.Companion.API_IMAGE_BASE_URL
import com.aligkts.weatherapp.util.Constant.Companion.weatherAppId
import com.aligkts.weatherapp.util.UnitType
import com.aligkts.weatherapp.util.tempFormatter
import kotlinx.android.synthetic.main.fragment_weather_detail.*


class WeatherDetailFragment : Fragment(), IRequestResult {


    private var dataList = ModelResponse()
    private var mAdapter = DetailAdapter(ArrayList())
    private var dataListForecastFromRequest = ArrayList<ModelResponse>()
    private var responseModel = ForecastByLocationResponse()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentCheck = arguments?.let {
            it.getString("bundle")
        }
        SingletonModel.instance?.let { _instance ->
            dataList = if (currentCheck == "current_clicked") {
                _instance.getCurrentList()
            } else {
                _instance.getOtherList()
            }
        }
        setDetailComponents(dataList.name,
                            dataList.main,
                            dataList.clouds,
                            dataList.wind,
                            dataList.weather,
                            dataList.coord)
    }

    private fun setDetailComponents(
        name: String?,
        main: Main?,
        clouds: Clouds?,
        wind: Wind?,
        weather: List<WeatherItem?>?,
        coord: Coord?) {
        name?.let {
            txtCurrentLocDetail.text = it
        }
        main?.let {_main ->
            txtCurrentTempDetail.text = _main.temp?.let {_temp ->
                _temp.tempFormatter()
            }
            txtHumidity.text = _main.humidity.toString()
        }
        clouds?.let { _clouds ->
            txtRainPossibility.text = _clouds.all.toString()
        }
        wind?.let { _wind ->
            _wind.speed?.let { _speed ->
                txtWindSpeed.text = _speed.toInt().toString()
            }
        }
        weather?.let { _list ->
            _list.first()?.let {_index ->
                val weatherStatus = _index.icon.toString()
                val url = API_IMAGE_BASE_URL.plus(weatherStatus).plus(getString(R.string.imageType))
                DownloadImage(imgWeatherIconDetail).execute(url)
            }
        }
        coord?.let { _coord ->
            val asyncTaskHandleJson = NetworkDAO()
            asyncTaskHandleJson.listener = this
            asyncTaskHandleJson.execute(API_FORECAST_BASE_URL + "lat=" + _coord.lat + "&lon=" + _coord.lon + "&&APPID=" + weatherAppId + "&units="+UnitType.Imperial.toString())
        }
    }

    override fun onSuccess(modelResponse: ModelResponse) {
        dataListForecastFromRequest.add(modelResponse)
        setRecyclerAdapter(dataListForecastFromRequest,recyclerDetail)
    }

    override fun onFailure(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setRecyclerAdapter(list: ArrayList<ModelResponse>, recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            mAdapter.setNewList(list)
        }
    }
}
