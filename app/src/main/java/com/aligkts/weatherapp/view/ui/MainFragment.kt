package com.aligkts.weatherapp.view.ui


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.IDownloadedImageBitmap
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.data.network.IRequestResult
import com.aligkts.weatherapp.util.DownloadImage
import com.aligkts.weatherapp.data.INotifyRecycler
import com.aligkts.weatherapp.data.SingletonModel
import com.aligkts.weatherapp.data.network.Proxy
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.presenter.MainContract
import com.aligkts.weatherapp.presenter.MainPresenter
import com.aligkts.weatherapp.view.ui.adapter.FavoritesAdapter
import com.aligkts.weatherapp.util.Constant.Companion.API_IMAGE_BASE_URL
import com.aligkts.weatherapp.util.tempFormatter
import com.aligkts.weatherapp.util.toast
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), INotifyRecycler, IRequestResult, MainContract.view, IDownloadedImageBitmap {

    private val LOCATION_REQUEST_CODE = 101
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var dataListFavoritesFromRequest = ArrayList<ModelResponse>()
    private var mAdapter = FavoritesAdapter(ArrayList(),this)
    private val proxy = Proxy(this)
    private lateinit var presenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter = MainPresenter(activity!!.applicationContext, this)
        dataListFavoritesFromRequest.clear()
        presenter.getBookmarkListFromDb()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, LOCATION_REQUEST_CODE)
        } else {
            // Permission has already been granted
            presenter.getCurrentLocationCoordFromUser()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutCurrentTemp.setOnClickListener {
            presenter.navigateToWeatherDetail()
        }
        fabButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_main_to_add_location)
        }
        searchView.setOnSearchClickListener {
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mAdapter.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mAdapter.filter.filter(newText)
                    return false
                }
            })
        }
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
                proxy.getRequestByLocation(LatLng(_latitude, _longitude))
            }
        }
    }

    private fun setRecyclerAdapter(list: ArrayList<ModelResponse>) {
        recyclerFavorites.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = mAdapter
            mAdapter.setNewList(list)
        }
    }

    override fun removeBookmarkFromDb(position: Int) {
        dataListFavoritesFromRequest.removeAt(position)
        mAdapter.notifyDataSetChanged()
    }

    override fun onSuccess(modelResponse: ModelResponse) {
        setCurrentUiComponents(modelResponse)
    }

    override fun onFailure(t: Throwable) {
        t.localizedMessage toast (activity!!)
    }

    private fun setCurrentUiComponents(response: ModelResponse) {
        SingletonModel.instance?.let { _singleton ->
            _singleton.setCurrentList(response)
        }
        val location = response.name
        txtCurrentLocation.text = location
        response.main?.let { _main ->
            val temp = _main.temp
            temp?.let {
                txtCurrentTemp.text = it.tempFormatter()
            }
        }
        response.weather?.let { _listWeather ->
            _listWeather.first()?.let { _index ->
                val url = API_IMAGE_BASE_URL.plus(_index.icon.toString()).plus(getString(R.string.imageType))
                DownloadImage(this).execute(url)
            }
        }
        weatherPanel.visibility = View.VISIBLE
        progressLoading.visibility = View.GONE
    }

    override fun sendDownloadedBitmap(bitmap: Bitmap) {
        imgWeatherIcon.setImageBitmap(bitmap)
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






