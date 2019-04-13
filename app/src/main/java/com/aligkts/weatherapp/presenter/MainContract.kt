package com.aligkts.weatherapp.presenter

import android.os.Bundle
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord

/**
 * Define the contract between MainFragment and MainPresenter
 */


interface MainContract {

    interface view {
        fun findUserLocation(coord: Coord)
        fun currentWeatherClicked(bundle: Bundle)
        //fun currentWeatherResponseFromApi(response: ModelResponse)
    }

    interface presenter {
        fun getCurrentLocationCoordFromUser()
        fun navigateToWeatherDetail()
        //fun getCurrentWeatherFromApi(latitude: Double,longitude: Double)
    }



}