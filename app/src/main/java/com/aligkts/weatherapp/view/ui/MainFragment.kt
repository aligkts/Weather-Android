package com.aligkts.weatherapp.view.ui


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.IDownloadedImageBitmap
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.INotifyRecycler
import com.aligkts.weatherapp.data.SingletonModel
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.presenter.MainContract
import com.aligkts.weatherapp.presenter.MainPresenter
import com.aligkts.weatherapp.util.*
import com.aligkts.weatherapp.view.ui.adapter.FavoritesAdapter
import com.aligkts.weatherapp.util.Constant.Companion.API_IMAGE_BASE_URL
import com.aligkts.weatherapp.util.Constant.Companion.RUN_ONCE_MAIN
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.custom_alert_dialog_rate_app.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.collections.ArrayList


class MainFragment : Fragment(), INotifyRecycler, MainContract.View, IDownloadedImageBitmap {

    private val LOCATION_REQUEST_CODE = 101
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var dataListFavoritesFromRequest = ArrayList<ModelResponse>()
    private val mAdapter by lazy { FavoritesAdapter(ArrayList(),this) }
    private lateinit var presenter: MainPresenter
    private var searchText = ""
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }
    private val appRated by lazy { prefs.getBoolean("rated", false) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        presenter = MainPresenter(activity!!.applicationContext, this)
        presenter.getDeviceLanguage()
        dataListFavoritesFromRequest.clear()
        presenter.setBookmarkListFromRequest()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerFavorites.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        if (RUN_ONCE_MAIN) {
            RUN_ONCE_MAIN = false
        } else {
            progressLoading.visibility = View.GONE
            presenter.setUiFromCache()
        }
        searchView.post {
            searchView.setQuery("", false)
        }
        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, LOCATION_REQUEST_CODE)
        } else {
            presenter.getCurrentLocationCoordFromUser()
            if (!appRated) {
                showRateDialog()
            }
        }
        currentPanel.setOnClickListener {
            presenter.navigateToWeatherDetail()
        }
        questionMark.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_main_to_viewpager)
        }
        imgSettings.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_main_to_settings)
        }
        fabButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_main_to_add_location)
        }
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchText = it
                }
                mAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun bookmarkList(list: ArrayList<ModelResponse>) {
        dataListFavoritesFromRequest = list
        setRecyclerAdapter(list)
    }

    override fun currentWeatherClicked(bundle: Bundle) {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_main_to_detail, bundle)
        }
    }

    override fun findUserLocation(coord: Coord) {
        coord.lat?.let { _latitude ->
            coord.lon?.let { _longitude ->
                presenter.getLatLngResponse(LatLng(_latitude, _longitude))
            }
        }
    }

    private fun setRecyclerAdapter(list: ArrayList<ModelResponse>) {
        val sortedList = list.sortedWith(compareBy { it.name }).toMutableList()
        presenter.putFavoritesListToCache(sortedList as ArrayList<ModelResponse>)
        mAdapter.setNewList(sortedList)
    }

    override fun itemRemoved(id: Int) {
        for (i in 0 until dataListFavoritesFromRequest.size) {
            if (dataListFavoritesFromRequest[i].id == id) {
                dataListFavoritesFromRequest.removeAt(i)
                break
            }
        }
        val sortedList = dataListFavoritesFromRequest.sortedWith(compareBy { it.name }).toMutableList()
        mAdapter.setNewList(sortedList)
        presenter.putFavoritesListToCache(sortedList as ArrayList<ModelResponse>)
        mAdapter.filter.filter(searchText)
    }

    override fun getCurrentParsedModel(modelResponse: ModelResponse) {
        setCurrentUiComponents(modelResponse)
        SingletonModel.instance?.let { _singleton ->
            _singleton.setCurrentList(modelResponse)
        }
        presenter.putCurrentWeatherToCache(modelResponse)
    }

    private fun setCurrentUiComponents(response: ModelResponse) {
        val location = response.name
        txtCurrentLocation?.let {
            it.text = location
        }
        response.main?.let { _main ->
            val temp = _main.temp
            temp?.let { _temp ->
                txtCurrentTemp?.let {
                    when(prefs.getString("unitType", "Metric")) {
                        UnitType.Metric.toString() -> it.text = _temp.tempToCentigrade()
                        UnitType.Imperial.toString() -> it.text = _temp.tempToFahrenheit()
                    }
                }
            }
        }
        response.weather?.let { _listWeather ->
            _listWeather.first()?.let { _index ->
                val url = API_IMAGE_BASE_URL.plus(_index.icon.toString()).plus(getString(R.string.imageType))
                DownloadImage(this).execute(url)
            }
        }
        weatherPanel?.let {
            it.visibility = View.VISIBLE
        }
        progressLoading?.let {
            it.visibility = View.GONE
        }
    }

    override fun sendDownloadedBitmap(bitmap: Bitmap?) {
        bitmap?.let { _bitmap ->
            imgWeatherIcon?.let {
                it.setImageBitmap(_bitmap)
            }
        }
    }

    private fun showRateDialog() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.custom_alert_dialog_rate_app, null)
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setCancelable(false).show()
        val textFontLightx = Typeface.createFromAsset(activity!!.assets, "fonts/MontserratLight.ttf")
        mDialogView.txtRateApp.typeface = textFontLightx
        mDialogView.btnSendRate.setOnClickListener {
            prefs.edit().putBoolean("rated", true).apply()
            mBuilder.dismiss()
            presenter.rateApp()
        }
        mDialogView.imgCancelDialog.setOnClickListener {
            mBuilder.dismiss()
        }
    }

    private fun showAlertDialogForPermissionDeniedWithCheck() = AlertDialog.Builder(activity!!)
            .setMessage(getString(R.string.permission_message))
            .setPositiveButton(getString(R.string.settings)) { dialog, which ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(getString(R.string.scheme), activity!!.packageName, null)
                intent.data = uri
                context?.startActivity(intent)
            }.setNegativeButton(getString(R.string.exit)) { dialog, which ->
                activity!!.finish()
            }.setCancelable(false).show()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!appRated) {
                        showRateDialog()
                    }
                    presenter.getCurrentLocationCoordFromUser()
                } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    showAlertDialogForPermissionDeniedWithCheck()
                }
            }
            else -> {

            }
        }
    }

}






