package com.aligkts.weatherapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.dto.forecastByLocation.ListItem
import com.aligkts.weatherapp.enums.WeatherStatus
import java.text.SimpleDateFormat

class DetailViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_bookmark,
                        viewGroup,
                        false
                )
        ) {

    private val txtItemTitle by lazy { itemView.findViewById<TextView>(R.id.txtItemTitle) }
    private val txtItemTemp by lazy { itemView.findViewById<TextView>(R.id.txtItemTemp) }

    fun bindTo(context: Context, model: ListItem) {

        txtItemTitle.text = dateToDay(model.dt_txt)
        txtItemTemp.text = tempFormatter(model.main?.temp)
        setWeatherIcon(model.weather?.get(0)?.main.toString())


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

    private fun setWeatherIcon(weatherStatus: String) {
        when (weatherStatus) {
            WeatherStatus.Clear.toString() -> txtItemTemp.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_clear,
                    0
            )
            WeatherStatus.Clouds.toString() -> txtItemTemp.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_clouds,
                    0
            )
            WeatherStatus.Rain.toString() -> txtItemTemp.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_rainy,
                    0
            )
            else -> txtItemTemp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_weather_other, 0)

        }
    }
}