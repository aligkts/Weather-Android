package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.junit.Before
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Response

class ProxyTest {

    private val api: ApiHelper = mock()
    private lateinit var proxy: Proxy

    @Before
    fun setUp() {
        proxy = Proxy()
    }

    @Test
    fun testApiResponseSuccess() {
        val mockedCall: Call<ModelResponse> = mock()
        val mockedResponse: ModelResponse = mock()
        Mockito.`when`(api.getWeatherByLatLng(41.0322807,
                                              28.8381373,
                                              "3c75e1a077769372966bc6050f85b57a",
                                              "Imperial")).thenReturn(mockedCall)
        doAnswer { _invocationOnMock ->
            val callBack: CustomCallBack<ModelResponse> = _invocationOnMock.getArgument(0)
            callBack.onResponse(mockedCall, Response.success(mockedResponse))
        }.`when`(mockedCall).enqueue(any())
    }

    @Test
    fun testApiResponseFailure() {
        val mockCall: Call<ModelResponse> = mock()
        val mockThrowable: Throwable = mock()
        Mockito.`when`(api.getWeatherByLatLng(41.0322807,
                                              28.8381373,
                                              "3c75e1a077769372966bc6050f85b57a",
                                              "Imperial")).thenReturn(mockCall)
        doAnswer {
            val callBack: CustomCallBack<ModelResponse> = it.getArgument(0)
            callBack.onFailure(mockCall, mockThrowable)
        }.`when`(mockCall).enqueue(any())
    }
}