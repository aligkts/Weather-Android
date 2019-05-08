package com.aligkts.weatherapp.presenter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.aligkts.weatherapp.data.MemoryCache
import com.aligkts.weatherapp.data.database.DBConnectionManager
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.network.Proxy
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.UnitType
import com.aligkts.weatherapp.util.toast
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

/**
 * Responsible for handling actions from the MainFragment and updating the UI
 */

class MainPresenter(private var context: Context,
                    private var mView: MainContract.View) : MainContract.Presenter {

    private val db by lazy { DBConnectionManager(context) }
    private val proxy by lazy { Proxy() }
    private val dataListFavoritesFromRequest = ArrayList<ModelResponse>()
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val language by lazy { prefs.getString("language", "en") }
    lateinit var currentUnitType: String
    private val cache by lazy { MemoryCache.instance }

    override fun getDeviceLanguage() {
        val lang = Locale.getDefault().language
        if (lang == "tr") {
            prefs.edit().putString("language", lang).apply()
        } else {
            prefs.edit().putString("language", "en").apply()
        }
    }

    override fun getCurrentLocationCoordFromUser() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var latitude: Double? = 0.0
        var longitude: Double? = 0.0
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            for (provider in providers) {
                locationManager.requestLocationUpdates(provider, 1000L, 0F,
                    object :LocationListener {
                        override fun onLocationChanged(location: Location?) {

                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

                        }

                    })
                val location = locationManager.getLastKnownLocation(provider)
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        }
        mView.findUserLocation(Coord(longitude, latitude))
    }

    override fun navigateToWeatherDetail() {
        val bundle = Bundle()
        bundle.putString("bundle", "current_clicked")
        mView.currentWeatherClicked(bundle)
    }

    override fun setBookmarkListFromRequest() {
        currentUnitType = prefs.getString("unitType", "Metric")
        val bookmarkList = db.readFavoritesList()
        if (bookmarkList.isNotEmpty()) {
            for (i in 0 until bookmarkList.size) {
                proxy.getResponseFromApiByLatLng(LatLng(bookmarkList[i].latitude,
                                                        bookmarkList[i].longitude),
                                                        language, currentUnitType) { isSuccess, response, message ->
                    if (isSuccess) {
                        response?.let { _response ->
                            dataListFavoritesFromRequest.add(_response)
                        }
                    } else {
                        message.toString() toast (context)
                    }
                    if(db.readFavoritesList().size == dataListFavoritesFromRequest.size)
                        mView.bookmarkList(dataListFavoritesFromRequest)
                }
            }
        }
    }

    override fun getLatLngResponse(latLng: LatLng) {
        currentUnitType = prefs.getString("unitType", "Metric")
        proxy.getResponseFromApiByLatLng(latLng,
                                         language,
                                         currentUnitType) { isSuccess, response, message ->
            if (isSuccess) {
                response?.let { _response ->
                    mView.getCurrentParsedModel(_response)
                }
            } else {
                message.toString() toast (context)
            }
        }
    }

    override fun rateApp() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.pepsico.kazandirio"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun putCurrentWeatherToCache(currentModel: ModelResponse) {
        cache?.let { _cache ->
            _cache.getLruCurrentWeather().put("current",currentModel)
        }
    }

    override fun putFavoritesListToCache(list: ArrayList<ModelResponse>) {
        cache?.let { _cache ->
            val array = arrayOfNulls<ModelResponse>(list.size)
            list.toArray(array)
            _cache.getLruFavoritesList().put("lastList",array)
        }
    }

    override fun setUiFromCache() {
        cache?.let { _cache ->
            _cache.getLruCurrentWeather().get("current")?.let { _currentWeatherFromCache ->
                mView.getCurrentParsedModel(_currentWeatherFromCache)
            }
        }
        cache?.let { _cache ->
            _cache.getLruFavoritesList().get("lastList")?.let { _lastRecyclerList ->
                val listFromCache = ArrayList<ModelResponse>()
                _lastRecyclerList?.let { _array ->
                    for (i in 0 until _array.size) {
                        _array[i]?.let { listFromCache.add(it) }
                    }
                    mView.bookmarkList(listFromCache)
                }
            }
        }
    }
}