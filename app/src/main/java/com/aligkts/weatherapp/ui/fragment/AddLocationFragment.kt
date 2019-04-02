package com.aligkts.weatherapp.ui.fragment


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
import com.aligkts.weatherapp.database.DBHelper
import com.aligkts.weatherapp.helper.Singleton
import com.aligkts.weatherapp.network.RetrofitClient
import com.aligkts.weatherapp.network.WeatherService
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
import retrofit2.Call
import retrofit2.Response


class AddLocationFragment : Fragment(), OnMapReadyCallback {

    lateinit var mGoogleMap: GoogleMap
    private var currentLat: Double? = 0.0
    private var currentLng: Double? = 0.0
    private val db by lazy { DBHelper(activity!!.applicationContext) }
    private val favoritesList by lazy { db.readFavoritesList() }
    private var currentList = WeatherByLocationResponse()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentList = Singleton.instance?.getCurrentList()!!
        currentLat = currentList.coord?.lat
        currentLng = currentList.coord?.lon

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
                addMarkerToMap(mGoogleMap, favoritesList[i].lat, favoritesList[i].lon)
            }
        }

        goToLocationZoom(currentLat!!, currentLng!!, 15F)

        mGoogleMap.setOnMapLongClickListener {

            RetrofitClient.getClient()
                    .create(WeatherService::class.java)
                    .getWeatherByLatLng(it.latitude, it.longitude, getString(R.string.weather_app_id), "Imperial")
                    .enqueue(object : retrofit2.Callback<WeatherByLocationResponse> {
                        override fun onFailure(call: Call<WeatherByLocationResponse>, t: Throwable) {
                            Toast.makeText(activity, "Request basarısız".plus(t), Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                                call: Call<WeatherByLocationResponse>,
                                response: Response<WeatherByLocationResponse>
                        ) {

                            val marker = addMarkerToMap(mGoogleMap, response.body()!!.coord?.lat!!, response.body()!!.coord?.lon!!)


                            val alertDialog = AlertDialog.Builder(activity)
                                    .setMessage("Bu lokasyonu eklemek istediğinize emin misiniz?")
                                    .setNegativeButton("Hayır") { dialog, which ->
                                        marker?.remove()
                                        dialog.dismiss()
                                    }
                                    .setPositiveButton("Evet") { dialog, which ->
                                        db.insertData(response.body()!!)

                                    }.show()

                        }
                    })
        }


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

    private fun addMarkerToMap(googleMap: GoogleMap, lat: Double, lon: Double): Marker? {
        val options = MarkerOptions().position(LatLng(lat, lon))
        return googleMap.addMarker(options)

    }

    private fun removeMarkerFromMap(googleMap: GoogleMap, lat: Double, lon: Double) {
        googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                p0?.remove()
                return true
            }

        })
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
