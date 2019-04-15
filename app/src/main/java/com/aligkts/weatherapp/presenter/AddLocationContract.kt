package com.aligkts.weatherapp.presenter

import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.google.android.gms.maps.model.LatLng


interface AddLocationContract {

    interface View {
        fun currentLocationData(coord: Coord)
    }

    interface Presenter {
        fun getCurrentSingletonData()
        fun getResponseFromApiByLatLng(latLng: LatLng)
        fun checkWhetherGoogleServicesAvailable() : Boolean
        fun findSearchedLocation(location: String) : LatLng
    }

}