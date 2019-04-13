package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.dto.weatherbylocation.*
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.`when`
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProxyTest {

    //private val view: OnModelParsed = mock()
    private val api: ApiHelper = mock()
    private lateinit var proxy: Proxy

    @Before
    fun setUp() {
       // proxy = Proxy(null)
    }

    @Test
    fun getRequestByLocation() {
        val list : List<WeatherItem> = listOf(WeatherItem("04d","broken clouds","Clouds",803))
        val mockedCall: Call<ModelResponse> = mock()
        val mockedResponse = ModelResponse("2019-04-10 15:00:00",
                                            Coord(41.12,28.34),
                                            list,
                                            "Bagcilar",
                                            Main(41.12,28.34,10,23.21,42.33),
                                            Clouds(40),
                                            1,
                                            Wind(14.32,22.45))

        `when`(api.getWeatherByLatLng(anyDouble(), anyDouble(), anyString(), anyString())).thenReturn(mockedCall)

        doAnswer {
            val callBack: Callback<ModelResponse> = it.getArgument(0)

            callBack.onResponse(mockedCall, Response.success(mockedResponse))
        }.`when`(mockedCall).enqueue(any())

        proxy.getRequestByLocation(mockedResponse.coord?.lat!!, mockedResponse.coord?.lon!!)

        //verify(view).setUiComponents(mockedResponse)

    }

    @Test
    fun getRequestByLocationBookmark() {
    }
}