package com.aligkts.weatherapp.ui.fragment


import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.database.DBHelper
import com.aligkts.weatherapp.dto.sqlite.FavoriteLocationEntity
import com.aligkts.weatherapp.helper.Singleton
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_add_location.*


class AddLocationFragment : Fragment(), OnMapReadyCallback {

    lateinit var mGoogleMap: GoogleMap
    private var currentLat: Double? = 0.0
    private var currentLng: Double? = 0.0
    private val db by lazy { DBHelper(activity!!.applicationContext) }
    private val favoritesList by lazy { db.readFavoritesList() }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = Singleton.instance?.getArrayList()
        currentLat = list?.coord?.lat
        currentLng = list?.coord?.lon

        if (googleServicesAvailable()) {
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            mapView.getMapAsync(this)
        }

        btnFindPlace.setOnClickListener { geoLocate(view) }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        if (favoritesList.size > 0) {
            for (i in 0 until favoritesList.size) {
                addMarkerToMap(mGoogleMap, LatLng(favoritesList[i].lat, favoritesList[i].lon))
            }
        }

        goToLocationZoom(currentLat!!, currentLng!!, 15F)

        mGoogleMap.setOnMapLongClickListener {

            val alertDialog = AlertDialog.Builder(activity)
                    .setMessage("Bu lokasyonu eklemek istediğinize emin misiniz?")
                    .setNegativeButton("Hayır") { dialog, which -> dialog.dismiss() }
                    .setPositiveButton("Evet") { dialog, which ->
                        if (db.insertData(FavoriteLocationEntity(lat = it.latitude, lon = it.longitude))) {
                            addMarkerToMap(mGoogleMap, LatLng(it.latitude, it.longitude))
                            //goToLocationZoom(it.latitude, it.longitude, 15F)
                        }
                    }.show()


        }

    }

    private fun goToLocation(lat: Double, lng: Double) {
        val ll = LatLng(lat, lng)
        val update = CameraUpdateFactory.newLatLng(ll)
        mGoogleMap.moveCamera(update)
    }

    private fun goToLocationZoom(lat: Double, lng: Double, zoom: Float) {
        val ll = LatLng(lat, lng)
        val update = CameraUpdateFactory.newLatLngZoom(ll, zoom)
        mGoogleMap.moveCamera(update)
    }

    private fun geoLocate(view: View) {
        val location = edtPlace.text.toString()
        val gc = Geocoder(activity)
        val list = gc.getFromLocationName(location, 1) as List<Address>
        val address = list[0]
        val locality = address.locality

        val lat = address.latitude
        val lng = address.longitude

        goToLocationZoom(lat, lng, 15F)
        view.hideKeyboard()

    }

    private fun addMarkerToMap(googleMap: GoogleMap, latLng: LatLng) {
        val options = MarkerOptions().position(latLng)
        googleMap.addMarker(options)


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