package com.aligkts.weatherapp.view.ui


import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.IDownloadedImageBitmap
import com.aligkts.weatherapp.data.dto.weatherbylocation.Clouds
import com.aligkts.weatherapp.data.dto.weatherbylocation.Main
import com.aligkts.weatherapp.data.dto.weatherbylocation.WeatherItem
import com.aligkts.weatherapp.data.dto.weatherbylocation.Wind
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.presenter.DetailContract
import com.aligkts.weatherapp.presenter.DetailPresenter
import com.aligkts.weatherapp.util.*
import com.aligkts.weatherapp.view.ui.adapter.DetailAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_weather_detail.*


class WeatherDetailFragment : Fragment(), DetailContract.View, IDownloadedImageBitmap{

    private var dataList = ModelResponse()
    private var mAdapter = DetailAdapter(ArrayList())
    lateinit var presenter: DetailPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.let {
            it.hideKeyboard()
        }
        presenter = DetailPresenter(activity!!.applicationContext,this)
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentCheck = arguments?.let {
            it.getString("bundle")
        }
        presenter.getSingletonData(currentCheck)?.let {
            dataList = it
        }
        setDetailHeaderComponents(dataList.name,
                                  dataList.main,
                                  dataList.clouds,
                                  dataList.wind,
                                  dataList.weather)
        dataList.coord?.let { _coord ->
            _coord.lat?.let { _latitude ->
                _coord.lon?.let { _longitude ->
                    presenter.getResponseWithoutRetrofitByLatLng(LatLng(_latitude, _longitude))
                }
            }
        }
    }

    private fun setDetailHeaderComponents(name: String?,
                                          main: Main?,
                                          clouds: Clouds?,
                                          wind: Wind?,
                                          weather: List<WeatherItem?>?) {
        name?.let { _name ->
            txtCurrentLocDetail.text = _name
        }
        main?.let { _main ->
            txtCurrentTempDetail.text = _main.temp?.let { _temp ->
                _temp.tempToCentigrade()
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
            _list.first()?.let { _index ->
                val url = Constant.API_IMAGE_BASE_URL.plus(_index.icon.toString()).plus(getString(R.string.imageType))
                DownloadImage(this).execute(url)
            }
        }
    }

    override fun sendDownloadedBitmap(bitmap: Bitmap) {
        imgWeatherIconDetail.setImageBitmap(bitmap)
    }

    override fun getForecastModelResponse(list: ArrayList<ModelResponse>) {
        setRecyclerAdapter(list,recyclerDetail)
    }

    private fun setRecyclerAdapter(list: ArrayList<ModelResponse>, recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            mAdapter.setNewList(list)
        }
    }

}
