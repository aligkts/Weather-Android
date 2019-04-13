package com.aligkts.weatherapp.presenter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord

/**
 * Responsible for handling actions from the MainFragment and updating the UI as required
 */

class MainPresenter(private var context: Context,private var mView: MainContract.view) : MainContract.presenter{

    override fun getCurrentLocationCoordFromUser() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var latitude: Double? = 0.0
        var longitude: Double? = 0.0
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            for (provider in providers) {
                locationManager.requestLocationUpdates(provider, 1000L, 0F,
                    object : LocationListener {
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


}