package com.aligkts.weatherapp.presenter

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.mockito.Mockito

class MainPresenterTest {

    lateinit var presenter: MainPresenter
    private val mView: MainContract.View = mock()
    private val mContext: Context = mock()

    @Before
    fun setUp() {
        presenter = MainPresenter(mContext, mView)
    }

    @Test
    fun testCurrentLocationCoordFromUser() {
        val manager : LocationManager = mock()
        Mockito.`when`(mContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(manager)
        Mockito.`when`(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
    }

}