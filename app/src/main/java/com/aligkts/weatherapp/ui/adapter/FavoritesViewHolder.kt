package com.aligkts.weatherapp.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.database.DBHelper
import com.aligkts.weatherapp.enums.WeatherStatus
import com.aligkts.weatherapp.helper.INotifyRecycler
import com.aligkts.weatherapp.helper.Singleton
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse

class FavoritesViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_bookmark,
                        viewGroup,
                        false
                )
        ) {


    private val txtItemTitle by lazy { itemView.findViewById<TextView>(R.id.txtItemTitle) }
    private val txtItemTemp by lazy { itemView.findViewById<TextView>(R.id.txtItemTemp) }


    fun bindTo(context: Context, model: WeatherByLocationResponse, listener: INotifyRecycler) {

        txtItemTitle.text = model.name
        val temp = model.main?.temp
        var centi = (temp?.toInt()?.minus(32))?.div(1.8000)
        centi = Math.round(centi!!).toDouble()
        txtItemTemp.text = centi.toString() + 0x00B0.toChar()
        setWeatherIcon(model.weather!![0]!!.main.toString())


        itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {

                AlertDialog.Builder(context)
                        .setMessage("Bu lokasyonu silmek istediğinize emin misiniz?")
                        .setNegativeButton("Hayır") { dialog, which -> dialog.dismiss() }
                        .setPositiveButton("Evet") { dialog, which ->
                            model.id?.let { DBHelper(context).deleteClickedItem(it) }
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