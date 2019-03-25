package com.aligkts.weatherapp.ui.fragment


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.dto.byLocation.Coord
import com.aligkts.weatherapp.enums.WeatherStatus
import com.aligkts.weatherapp.helper.Singleton
import com.aligkts.weatherapp.network.RetrofitClient
import com.aligkts.weatherapp.network.WeatherService
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Response


class MainFragment : Fragment() {

    private val LOCATION_REQUEST_CODE = 101
    lateinit var locationManager: LocationManager
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var lat: Double? = 0.0
    private var lon: Double? = 0.0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(permissions, LOCATION_REQUEST_CODE)
        } else {
            // Permission has already been granted
            lat = findLocation().lat
            lon = findLocation().lon

            requestByLocation(lat, lon)
        }



        return inflater.inflate(com.aligkts.weatherapp.R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutCurrentTemp.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_main_to_detail)
        }
    }


    private fun requestByLocation(lat: Double?, lon: Double?) {

        RetrofitClient.getClient()
                .create(WeatherService::class.java)
                .getWeatherByLatLng(lat, lon, "3c75e1a077769372966bc6050f85b57a", "Imperial")
                .enqueue(object : retrofit2.Callback<WeatherByLocationResponse> {
                    override fun onFailure(call: Call<WeatherByLocationResponse>, t: Throwable) {
                        Toast.makeText(activity, "response basarısız".plus(t), Toast.LENGTH_SHORT).show()

                    }

                    override fun onResponse(
                            call: Call<WeatherByLocationResponse>,
                            response: Response<WeatherByLocationResponse>
                    ) {

                        Singleton.instance?.setArrayList(response.body()!!)

                        val location = response.body()?.name
                        txtCurrentLocation.text = location

                        val temp = response.body()?.main?.temp
                        var centi = (temp?.toInt()?.minus(32))?.div(1.8000)
                        centi = Math.round(centi!!).toDouble()
                        txtCurrentTemp.text = centi.toString() + 0x00B0.toChar()

                        val weatherStatus = response.body()?.weather?.get(0)?.main.toString()
                        setWeatherIcon(weatherStatus)

                        weatherPanel.visibility = View.VISIBLE
                        progressLoading.visibility = View.GONE
                    }

                })

    }

    private fun setWeatherIcon(weatherStatus: String) {
        when (weatherStatus) {
            WeatherStatus.Clear.toString() -> imgWeatherIcon.setImageResource(R.drawable.ic_clear)
            WeatherStatus.Clouds.toString() -> imgWeatherIcon.setImageResource(R.drawable.ic_clouds)
            WeatherStatus.Rain.toString() -> imgWeatherIcon.setImageResource(R.drawable.ic_rainy)
            else -> imgWeatherIcon.setImageResource(R.drawable.ic_weather_other)

        }

    }


    private fun findLocation(): Coord {

        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var lat: Double? = 0.0
        var lon: Double? = 0.0

        for (provider in providers) {
            locationManager.requestLocationUpdates(provider, 1000L, 0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {

                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }

                    })
            val location = locationManager.getLastKnownLocation(provider)

            if (location != null) {
                lat = location.latitude
                lon = location.longitude

            }
        }
        return Coord(lon, lat)


    }

    fun showAlertDialogForPermissionDeniedWithCheck() {
        val alertDialog = AlertDialog.Builder(activity!!)
                .setMessage("App needs your location permission to get weather status")
                .setPositiveButton("Settings") { dialog, which ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", activity!!.packageName, null)
                    intent.data = uri
                    context?.startActivity(intent)

                }
                .setCancelable(false).create().show()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    lat = findLocation().lat
                    lon = findLocation().lon

                    requestByLocation(lat, lon)
                } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    showAlertDialogForPermissionDeniedWithCheck()

                    //Toast.makeText(activity!!,"İzin kalıcı olarak verilmedi lokasyon",Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(activity!!,"İzin kalıcı olarak verilmedi lokasyon",Toast.LENGTH_SHORT).show()
                }
            }

            else -> {

            }

        }
    }

}






