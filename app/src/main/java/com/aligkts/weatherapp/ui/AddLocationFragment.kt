package com.aligkts.weatherapp.ui


import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.database.DBConnectionManager
import com.aligkts.weatherapp.dto.sqlite.FavoriteLocationEntity
import com.aligkts.weatherapp.util.Singleton
import com.aligkts.weatherapp.network.Proxy
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_add_location.*


class AddLocationFragment : Fragment(), OnMapReadyCallback {

    lateinit var mGoogleMap: GoogleMap
    private var currentLat: Double? = 0.0
    private var currentLng: Double? = 0.0
    private val db by lazy { DBConnectionManager(activity!!.applicationContext) }
    private val favoritesList by lazy { db.readFavoritesList() }
    private var currentList = WeatherByLocationResponse()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Singleton.instance?.let {
           currentList = it.getCurrentList()
           currentList.coord?.let {_coord ->
               _coord.lat?.let {_latitude ->
                   currentLat = _latitude
               }
               _coord.lon?.let {_longitude ->
                   currentLng = _longitude
               }
           }
        }
        if (googleServicesAvailable()) {
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            mapView.getMapAsync(this)
        }
        edtPlace.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                        keyCode == EditorInfo.IME_ACTION_DONE ||
                        event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    geoLocate(view)
                }
                return false
            }
        })
        btnFindPlace.setOnClickListener { geoLocate(view) }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        if (favoritesList.size > 0) {
            for (i in 0 until favoritesList.size) {
                addMarkerToMap(mGoogleMap, favoritesList[i].latitude, favoritesList[i].longitude)
            }
        }
        currentLat?.let {_latitude ->
            currentLng?.let {_longitude ->
                goToLocationZoom(_latitude, _longitude, 15F)
            }
        }
        mGoogleMap.setOnMapLongClickListener {
            Proxy().getRequestByLocation(it.latitude, it.longitude) { isSuccess, response ->
                if (isSuccess) {
                    response?.let { _response ->
                        _response.coord?.let { _coord ->
                            _coord.lat?.let { _latitude ->
                                _coord.lon?.let { _longitude ->
                                    val marker = addMarkerToMap(mGoogleMap, _latitude, _longitude)
                                    AlertDialog.Builder(activity)
                                            .setMessage("Bu lokasyonu eklemek istediğinize emin misiniz?")
                                            .setNegativeButton("Hayır") { dialog, which ->
                                                marker?.remove()
                                                dialog.dismiss()
                                            }
                                            .setPositiveButton("Evet") { dialog, which ->
                                                val model = FavoriteLocationEntity()
                                                _response.id?.let {_id ->
                                                    _response.coord.lat?.let {_lat  ->
                                                        _response.coord.lon?.let {_lon ->
                                                            model.id = _id
                                                            model.latitude = _lat
                                                            model.longitude = _lon
                                                        }
                                                    }
                                                }
                                                db.insertData(model)

                                            }.show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToLocationZoom(latitude: Double, longitude: Double, zoom: Float) {
        val latLng = LatLng(latitude, longitude)
        val update = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mGoogleMap.moveCamera(update)
    }

    private fun geoLocate(view: View) {
        val location = edtPlace.text.toString()
        val geocoder = Geocoder(activity)
        val list = geocoder.getFromLocationName(location, 1) as List<Address>
        val address = list.first()
        val latitude = address.latitude
        val longitude = address.longitude
        goToLocationZoom(latitude, longitude, 15F)
        view.hideKeyboard()
    }

    private fun addMarkerToMap(googleMap: GoogleMap, latitude: Double, longitude: Double): Marker? {
        val options = MarkerOptions().position(LatLng(latitude, longitude))
        return googleMap.addMarker(options)
    }

    private fun googleServicesAvailable(): Boolean {
        val api = GoogleApiAvailability.getInstance()
        val isAvailable = api.isGooglePlayServicesAvailable(activity!!)
        when {
            isAvailable == ConnectionResult.SUCCESS -> return true
            api.isUserResolvableError(isAvailable) -> {
                val dialog = api.getErrorDialog(activity, isAvailable, 0)
                dialog.show()
            }
            else -> Toast.makeText(activity, "Can't connect to play services", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
