package com.aligkts.weatherapp.presenter

import android.os.Bundle
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.google.android.gms.maps.model.LatLng

/**
 * Define the contract between MainFragment and MainPresenter
 */


interface MainContract {

    interface View  {
        fun findUserLocation(coord: Coord)
        fun currentWeatherClicked(bundle: Bundle)
        fun bookmarkList(list: ArrayList<ModelResponse>)
        fun getCurrentParsedModel(modelResponse: ModelResponse)
    }

    interface Presenter {
        fun getCurrentLocationCoordFromUser()
        fun navigateToWeatherDetail()
        fun getBookmarkListFromDb()
        fun getLatLngResponse(latLng: LatLng)
        fun rateApp()
    }

}