package com.aligkts.weatherapp.presenter

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.SingletonModel
import com.aligkts.weatherapp.data.database.DBConnectionManager
import com.aligkts.weatherapp.data.database.model.FavoriteLocation
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.network.Proxy
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.util.toast
import com.aligkts.weatherapp.view.ui.MainActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.LatLng

class AddLocationPresenter(private var context: Context, private var mView: AddLocationContract.View) : AddLocationContract.Presenter{

    private val proxy by lazy { Proxy() }
    private val db by lazy { DBConnectionManager(context) }

    override fun getCurrentSingletonData() {
        var currentLatitude: Double? = null
        var currentLongitude: Double? = null
        SingletonModel.instance?.let {
            it.getCurrentList().coord?.let { _coord ->
                _coord.lat?.let { _latitude ->
                    currentLatitude = _latitude
                }
                _coord.lon?.let { _longitude ->
                    currentLongitude = _longitude
                }
            }
        }
        mView.currentLocationData(Coord(currentLongitude, currentLatitude))
    }

    override fun getResponseFromApiByLatLng(latLng: LatLng) {
        proxy.getResponseFromApiByLatLng(latLng) {isSuccess, response ->
            if (isSuccess) {
                response?.let { _response ->
                    val dbModel = FavoriteLocation()
                    _response.id?.let { _id ->
                        dbModel.id = _id
                    }
                    _response.coord?.let { _coord ->
                        _coord.lat?.let { _latitude ->
                            dbModel.latitude = _latitude
                        }
                        _coord.lon?.let { _longitude ->
                            dbModel.longitude = _longitude
                        }
                    }
                    db.insertData(dbModel)
                }
            }
        }
    }

    override fun checkWhetherGoogleServicesAvailable(): Boolean {
        val api = GoogleApiAvailability.getInstance()
        val isAvailable = api.isGooglePlayServicesAvailable(context)
        when {
            isAvailable == ConnectionResult.SUCCESS -> return true
            api.isUserResolvableError(isAvailable) -> {
                val dialog = api.getErrorDialog(MainActivity(), isAvailable, 0)
                dialog.show()
            }
            else -> context.getString(R.string.warning_play_services) toast (context)
        }
        return false
    }

    override fun findSearchedLocation(location: String): LatLng {
        val geocoder = Geocoder(context)
        val list = geocoder.getFromLocationName(location, 1) as List<Address>
        val address = list.first()
        val latitude = address.latitude
        val longitude = address.longitude
        return LatLng(latitude,longitude)
    }
}