package com.aligkts.weatherapp.view.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.MemoryCache
import com.aligkts.weatherapp.data.IDownloadedImageBitmap
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.*
import com.aligkts.weatherapp.util.Constant.Companion.API_IMAGE_BASE_URL

class DetailViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(
                        R.layout.item_bookmark,
                        viewGroup,
                        false)) ,IDownloadedImageBitmap{

    private val txtItemTitle by lazy { itemView.findViewById<TextView>(R.id.txtItemTitle) }
    private val txtItemTemp by lazy { itemView.findViewById<TextView>(R.id.txtItemTemp) }
    private val imgBookmarkItem by lazy { itemView.findViewById<ImageView>(R.id.imgBookmarkItem) }
    lateinit var iconCode: String

    fun bindTo(context: Context, model: ModelResponse) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        model.dt_txt?.let { _datetime ->
            txtItemTitle.text = _datetime.dateDoDay()
        }
        model.main?.let { _main ->
            _main.temp?.let {_temp ->
                when(prefs.getString("unitType", "Metric")) {
                    UnitType.Metric.toString() -> txtItemTemp.text = _temp.tempToCentigrade()
                    UnitType.Imperial.toString() -> txtItemTemp.text = _temp.tempToFahrenheit()
                }
            }
        }
        model.weather?.let { _list ->
            _list.first()?.let {_index ->
                iconCode = _index.icon.toString()
                val url = API_IMAGE_BASE_URL.plus(iconCode).plus(context.getString(R.string.imageType))
                MemoryCache.instance?.let { _cache ->
                    val bitmapFromCache = _cache.getLru().get(iconCode)
                    if(bitmapFromCache != null) {
                        imgBookmarkItem.setImageBitmap(bitmapFromCache)
                    } else {
                        DownloadImage(this).execute(url)
                    }
                }
            }
        }
    }

    override fun sendDownloadedBitmap(bitmap: Bitmap?) {
        bitmap?.let { _bitmap ->
            MemoryCache.instance?.let { _cache ->
                _cache.getLru().put(iconCode,_bitmap)
                imgBookmarkItem.setImageBitmap(_bitmap)
            }
        }
    }

}