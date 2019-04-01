package com.aligkts.weatherapp.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aligkts.weatherapp.R
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
import java.text.SimpleDateFormat


class WeatherDetailFragment : Fragment() {

    private var dataList = WeatherByLocationResponse()
    private var mAdapter = DetailAdapter(ArrayList())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

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

                    override fun onResponse(call: Call<ForecastByLocationResponse>, response: Response<ForecastByLocationResponse>) {

                        val dayName = dateToDay(response.body()?.list?.get(2)?.dt_txt)
                        val temp = tempFormatter(response.body()?.list?.get(2)?.main?.temp)
                        setWeatherIcon(response.body()?.list?.get(2)?.weather?.get(0)?.main)


                        Toast.makeText(activity, "Request başarılı", Toast.LENGTH_SHORT).show()
                    }
                })

    }

    private fun tempFormatter(temp: Double?): String {
        var centi = (temp?.toInt()?.minus(32))?.div(1.8000)
        centi = Math.round(centi!!).toDouble()
        return centi.toString() + 0x00B0.toChar()
    }

    private fun dateToDay(input: String?): String {
        val inFormat = SimpleDateFormat("yyy-MM-dd")
        val date = inFormat.parse(input)
        val outFormat = SimpleDateFormat("EEEE")
        val day = outFormat.format(date)

        return day
    }

    private fun setWeatherIcon(weatherStatus: String?) {
        when (weatherStatus) {
            WeatherStatus.Clear.toString() -> imgWeatherIconDetail.setImageResource(R.drawable.ic_clear)
            WeatherStatus.Clouds.toString() -> imgWeatherIconDetail.setImageResource(R.drawable.ic_clouds)
            WeatherStatus.Rain.toString() -> imgWeatherIconDetail.setImageResource(R.drawable.ic_rainy)
            else -> imgWeatherIconDetail.setImageResource(R.drawable.ic_weather_other)

        }

    }
}
