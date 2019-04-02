package com.aligkts.weatherapp.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.dto.forecastByLocation.ListItem
import com.aligkts.weatherapp.enums.WeatherStatus
import com.aligkts.weatherapp.helper.Singleton
import com.aligkts.weatherapp.network.RetrofitClient
import com.aligkts.weatherapp.network.WeatherService
import com.aligkts.weatherapp.network.response.ForecastByLocationResponse
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import com.aligkts.weatherapp.ui.adapter.DetailAdapter
import kotlinx.android.synthetic.main.fragment_weather_detail.*
import retrofit2.Call
import retrofit2.Response


class WeatherDetailFragment : Fragment() {

    private var dataList = WeatherByLocationResponse()
    private var mAdapter = DetailAdapter(ArrayList())
    private var dataListForecastFromRequest = ArrayList<ListItem>()
    private var responseModel = ForecastByLocationResponse()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentCheck = arguments?.getString("bundle")

        dataList = if (currentCheck == "current_clicked") {
            Singleton.instance?.getCurrentList()!!
        } else {
            Singleton.instance?.getOtherList()!!
        }
        txtCurrentLocDetail.text = dataList.name

        txtCurrentTempDetail.text = tempFormatter(dataList.main?.temp)
        setWeatherIcon(dataList.weather?.get(0)?.main.toString())
        txtHumidity.text = dataList.main?.humidity.toString()
        txtRainPossibility.text = dataList.clouds?.all.toString()
        txtWindSpeed.text = dataList.wind?.speed?.toInt().toString()

        requestForForecast(dataList.coord?.lat, dataList.coord?.lon)


    }

    private fun requestForForecast(lat: Double?, lon: Double?) {
        RetrofitClient.getClient()
                .create(WeatherService::class.java)
                .getForecastByLatLng(lat, lon, getString(R.string.weather_app_id), "Imperial")
                .enqueue(object : retrofit2.Callback<ForecastByLocationResponse> {
                    override fun onFailure(call: Call<ForecastByLocationResponse>, t: Throwable) {
                        Toast.makeText(activity, "Request basarısız".plus(t), Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                            call: Call<ForecastByLocationResponse>,
                            response: Response<ForecastByLocationResponse>
                    ) {
                        responseModel = response.body() as ForecastByLocationResponse
                        for (i in 0 until responseModel.list!!.size step 8) {
                            dataListForecastFromRequest.add(responseModel.list!![i]!!)

                        }
                        setRecyclerAdapter(dataListForecastFromRequest)
                    }
                })

    }

    private fun tempFormatter(temp: Double?): String {
        var centi = (temp?.toInt()?.minus(32))?.div(1.8000)
        centi = Math.round(centi!!).toDouble()
        return centi.toString() + 0x00B0.toChar()
    }



    private fun setWeatherIcon(weatherStatus: String?) {
        when (weatherStatus) {
            WeatherStatus.Clear.toString() -> imgWeatherIconDetail.setImageResource(R.drawable.ic_clear)
            WeatherStatus.Clouds.toString() -> imgWeatherIconDetail.setImageResource(R.drawable.ic_clouds)
            WeatherStatus.Rain.toString() -> imgWeatherIconDetail.setImageResource(R.drawable.ic_rainy)
            else -> imgWeatherIconDetail.setImageResource(R.drawable.ic_weather_other)

        }

    }

    private fun setRecyclerAdapter(list: ArrayList<ListItem>) {
        recyclerDetail.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            (this.adapter as DetailAdapter).setNewList(list)
        }
    }
}
