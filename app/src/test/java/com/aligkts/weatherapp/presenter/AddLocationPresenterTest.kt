package com.aligkts.weatherapp.presenter

import android.content.Context
import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Test
import org.junit.Before

class AddLocationPresenterTest {

    lateinit var presenter : AddLocationPresenter
    private val mView : AddLocationContract.View = mock()
    private val mContext : Context = mock()

    @Before
    fun setUp() {
        presenter = AddLocationPresenter(mContext,mView)
    }

    @Test
    fun getCurrentSingletonData() {
        presenter.getCurrentSingletonData()
        verify(mView).currentLocationData(Coord(null,null))
    }

}