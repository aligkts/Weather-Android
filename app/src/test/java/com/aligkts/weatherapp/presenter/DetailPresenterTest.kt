package com.aligkts.weatherapp.presenter

import com.aligkts.weatherapp.data.dto.weatherbylocation.*
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
        val testWeatherList = ArrayList<WeatherItem>()
        testWeatherList.add(WeatherItem("icon","desc","main",1))
        val testMain = Main(15.0,7.0,54,1026.0,27.0)
        val testModel = ModelResponse("", Coord(0.0,0.0), testWeatherList,"Kağıthane",testMain,Clouds(40),1, Wind(70.0,8.05))
        testArrayList.add(testModel)
        presenter.presentedForecast(testModel)
        verify(mView).getForecastModelResponse(testArrayList)
    }

}