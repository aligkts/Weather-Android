package com.aligkts.weatherapp.presenter

import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.google.android.gms.maps.model.LatLng


interface AddLocationContract {

    interface view {
        fun currentLocationData(coord: Coord)
    }

    interface presenter {
        fun getCurrentSingletonData()
        fun getResponseFromApiByLatLng(latLng: LatLng)
        fun checkWhetherGoogleServicesAvailable() : Boolean
        fun findSearchedLocation(location: String) : LatLng
    }
}