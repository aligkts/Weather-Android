package com.aligkts.weatherapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.dto.forecastbylocation.ListItem
import com.aligkts.weatherapp.helper.DownloadImage
import java.text.SimpleDateFormat

class DetailViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_bookmark,
                        viewGroup,
                        false)) {

    private val txtItemTitle by lazy { itemView.findViewById<TextView>(R.id.txtItemTitle) }
    private val txtItemTemp by lazy { itemView.findViewById<TextView>(R.id.txtItemTemp) }
    private val imgBookmarkItem by lazy { itemView.findViewById<ImageView>(R.id.imgBookmarkItem) }
    private val API_IMAGE_BASE_URL = "http://openweathermap.org/img/w/"

    fun bindTo(context: Context, model: ListItem) {
        txtItemTitle.text = dateToDay(model.dt_txt)
        model.main?.let {
            txtItemTemp.text = tempFormatter(it.temp)
        }
        model.weather?.let {_list ->
            _list.first()?.let {_index ->
                val weatherStatus = _index.icon.toString()
                val url =API_IMAGE_BASE_URL.plus(weatherStatus).plus(".png")
                DownloadImage(imgBookmarkItem).execute(url)
            }
        }
    }

    private fun tempFormatter(temp: Double?): String? {
        temp?.let {
            var centi = (temp.toInt().minus(32)).div(1.8000)
            centi = Math.round(centi).toDouble()
            return centi.toString() + 0x00B0.toChar()
        }
        return null
    }

    private fun dateToDay(input: String?): String {
        val inFormat = SimpleDateFormat("yyy-MM-dd")
        val date = inFormat.parse(input)
        val outFormat = SimpleDateFormat("EEEE")
        val day = outFormat.format(date)
        return day
    }
}