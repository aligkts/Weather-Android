package com.aligkts.weatherapp.presenter

import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.Before

class DetailPresenterTest {

    lateinit var presenter : DetailPresenter
    private val mView : DetailContract.View = mock()

    @Before
    fun setUp() {
        presenter = DetailPresenter(mView)
    }

    @Test
    fun testPresentedForecast() {
        val testArrayList = ArrayList<ModelResponse>()
        val testModel = ModelResponse(null,null,null,null,null,null,null,null)
        testArrayList.add(testModel)
        presenter.presentedForecast(testModel)
        verify(mView).getForecastModelResponse(testArrayList)
    }

}