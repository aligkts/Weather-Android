package com.aligkts.weatherapp.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.database.DBConnectionManager
import com.aligkts.weatherapp.util.DownloadImage
import com.aligkts.weatherapp.util.INotifyRecycler
import com.aligkts.weatherapp.util.Singleton
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse

class FavoritesViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_bookmark,
                        viewGroup,
                        false)) {

    private val txtItemTitle by lazy { itemView.findViewById<TextView>(R.id.txtItemTitle) }
    private val txtItemTemp by lazy { itemView.findViewById<TextView>(R.id.txtItemTemp) }
    private val imgBookmarkItem by lazy { itemView.findViewById<ImageView>(R.id.imgBookmarkItem) }
    private val API_IMAGE_BASE_URL = "http://openweathermap.org/img/w/"

    fun bindTo(context: Context, model: WeatherByLocationResponse, listener: INotifyRecycler) {
        txtItemTitle.text = model.name
        model.main?.let {_main ->
            val temp = _main.temp
            temp?.let {_temp ->
                var centi = (_temp.toInt()).minus(32).div(1.8000)
                centi = Math.round(centi).toDouble()
                txtItemTemp.text = centi.toString() + 0x00B0.toChar()
            }
        }
        model.weather?.let { _listWeather ->
            _listWeather.first()?.let { _index ->
                val weatherStatus = _index.icon.toString()
                val url =API_IMAGE_BASE_URL.plus(weatherStatus).plus(".png")
                DownloadImage(imgBookmarkItem).execute(url)
            }
        }
        itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                AlertDialog.Builder(context)
                        .setMessage("Bu lokasyonu silmek istediğinize emin misiniz?")
                        .setNegativeButton("Hayır") { dialog, which -> dialog.dismiss() }
                        .setPositiveButton("Evet") { dialog, which ->
                            model.id?.let { DBConnectionManager(context).deleteClickedItem(it) }
                            listener.refreshRecycler(adapterPosition)
                        }.show()
                return true
            }
        })
        itemView.setOnClickListener {
            Singleton.instance?.setOtherList(model)
            Navigation.findNavController(it).navigate(R.id.weatherDetailFragment)
        }
    }
}