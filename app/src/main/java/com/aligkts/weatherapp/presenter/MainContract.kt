package com.aligkts.weatherapp.presenter

import android.os.Bundle
import com.aligkts.weatherapp.data.database.model.FavoriteLocation
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.network.model.ModelResponse

/**
 * Define the contract between MainFragment and MainPresenter
 */


interface MainContract {

    interface view {
        fun findUserLocation(coord: Coord)
        fun currentWeatherClicked(bundle: Bundle)
        fun bookmarkList(list: ArrayList<ModelResponse>)
    }

    interface presenter {
        fun getCurrentLocationCoordFromUser()
        fun navigateToWeatherDetail()
        fun getBookmarkListFromDb()
    }
}