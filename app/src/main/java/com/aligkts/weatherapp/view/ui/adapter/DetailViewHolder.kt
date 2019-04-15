package com.aligkts.weatherapp.view.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.IDownloadedImageBitmap
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.Constant.Companion.API_IMAGE_BASE_URL
import com.aligkts.weatherapp.util.DownloadImage
import com.aligkts.weatherapp.util.dateDoDay
import java.text.SimpleDateFormat

class DetailViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_bookmark,
                        viewGroup,
                        false)) ,IDownloadedImageBitmap{

    private val txtItemTitle by lazy { itemView.findViewById<TextView>(R.id.txtItemTitle) }
    private val txtItemTemp by lazy { itemView.findViewById<TextView>(R.id.txtItemTemp) }
    private val imgBookmarkItem by lazy { itemView.findViewById<ImageView>(R.id.imgBookmarkItem) }

    fun bindTo(context: Context, model: ModelResponse) {
        model.dt_txt?.let {_datetime ->
            txtItemTitle.text = _datetime.dateDoDay()
        }
        model.main?.let {
            txtItemTemp.text = tempFormatter(it.temp)
        }
        model.weather?.let {_list ->
            _list.first()?.let {_index ->
                val weatherStatus = _index.icon.toString()
                val url =API_IMAGE_BASE_URL.plus(weatherStatus).plus(context.getString(R.string.imageType))
                DownloadImage(this).execute(url)
            }
        }
    }

    override fun sendDownloadedBitmap(bitmap: Bitmap) {
        imgBookmarkItem.setImageBitmap(bitmap)
    }

    private fun tempFormatter(temp: Double?): String? {
        temp?.let {
            var centi = (temp.toInt().minus(32)).div(1.8000)
            centi = Math.round(centi).toDouble()
            return centi.toString() + 0x00B0.toChar()
        }
        return null
    }
}