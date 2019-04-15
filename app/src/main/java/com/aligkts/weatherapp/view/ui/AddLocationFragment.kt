package com.aligkts.weatherapp.view.ui


import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.database.DBConnectionManager
import com.aligkts.weatherapp.data.database.model.FavoriteLocation
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.presenter.AddLocationPresenter
import com.aligkts.weatherapp.presenter.AddLocationContract
import com.aligkts.weatherapp.util.hideKeyboard
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_add_location.*


class AddLocationFragment : Fragment(), OnMapReadyCallback, AddLocationContract.View {

    lateinit var mGoogleMap: GoogleMap
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private val db by lazy { DBConnectionManager(activity!!.applicationContext) }
    private val favoritesList by lazy { db.readFavoritesList() }
    lateinit var presenter: AddLocationPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter = AddLocationPresenter(activity!!, this)
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getCurrentSingletonData()
        val serviceStatus = presenter.checkWhetherGoogleServicesAvailable()
        if (serviceStatus) {
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            mapView.getMapAsync(this)
        }
        edtPlace.setOnKeyListener { view, keyCode, event ->
            if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                    keyCode == EditorInfo.IME_ACTION_DONE ||
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                val searchedLatLng = presenter.findSearchedLocation(edtPlace.text.toString())
                zoomLocation(mGoogleMap, searchedLatLng, 15F)
            }
            false
        }
        btnFindPlace.setOnClickListener {
            val searchedLatLng = presenter.findSearchedLocation(edtPlace.text.toString())
            zoomLocation(mGoogleMap, searchedLatLng, 15F)
        }
    }

    override fun currentLocationData(coord: Coord) {
        coord.lat?.let { _latitude ->
            currentLat = _latitude
        }
        coord.lon?.let { _longitude ->
            currentLng = _longitude
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        zoomLocation(mGoogleMap, LatLng(currentLat, currentLng), 15F)
        mGoogleMap.setOnMapLongClickListener {
            val marker = addMarkerToMap(mGoogleMap, LatLng(it.latitude, it.longitude))
            AlertDialog.Builder(activity!!)
                    .setMessage(getString(R.string.alert_messade_add_location))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.alert_button_negative)) { dialog, which ->
                        marker?.let { _marker ->
                            _marker.remove()
                        }
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.alert_button_positive)) { dialog, which ->
                        presenter.getResponseFromApiByLatLng(LatLng(it.latitude, it.longitude))
                    }.show()
        }
        if (favoritesList.size > 0) {
            for (i in 0 until favoritesList.size) {
                addMarkerToMap(mGoogleMap, LatLng(favoritesList[i].latitude, favoritesList[i].longitude))
            }
        }
    }

    private fun addMarkerToMap(mGoogleMap: GoogleMap, latLng: LatLng): Marker? {
        val options = MarkerOptions().position(latLng)
        return mGoogleMap.addMarker(options)
    }

    private fun zoomLocation(mGoogleMap: GoogleMap, latLng: LatLng, zoom: Float) {
        val update = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mGoogleMap.moveCamera(update)
        view?.let {
            it.hideKeyboard()
        }
    }
}
