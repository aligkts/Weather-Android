package com.aligkts.weatherapp.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.enums.WeatherStatus
import com.aligkts.weatherapp.helper.Singleton
import kotlinx.android.synthetic.main.fragment_weather_detail.*


class WeatherDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList = Singleton.instance?.getArrayList()

        txtCurrentLocDetail.text = dataList?.name


        val temp = dataList?.main?.temp
        var centi = (temp?.toInt()?.minus(32))?.div(1.8000)
        centi = Math.round(centi!!).toDouble()
        txtCurrentTempDetail.text = centi.toString() + 0x00B0.toChar()

        setWeatherIcon(dataList?.weather?.get(0)?.main.toString())

        txtHumidity.text = dataList?.main?.humidity.toString()

        txtRainPossibility.text = dataList?.clouds?.all.toString()

        txtWindSpeed.text = dataList?.wind?.speed?.toInt().toString()


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
