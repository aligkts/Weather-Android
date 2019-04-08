package com.aligkts.weatherapp.ui

import com.aligkts.weatherapp.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.dto.weatherbylocation.WeatherItem
import com.aligkts.weatherapp.network.WeatherService
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import com.aligkts.weatherapp.util.CallFake
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import java.net.HttpURLConnection
import java.net.URL

class MainFragmentTest {
    val appId ="3c75e1a077769372966bc6050f85b57a"

   /* @Test
    fun testSetCurrentWeather() {
        val testCoord = Coord(28.8381373, 41.0322807)
        var result = ""
        val testName = "Bagcilar"
        val url ="http://api.openweathermap.org/data/2.5/weather?lat="
            .plus(testCoord.lat).plus("&lon=")
            .plus(testCoord.lon).plus("&&APPID=3c75e1a077769372966bc6050f85b57a&units=Metric")

        val text: String
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        text = connection.inputStream.use { it.reader().use { _reader -> _reader.readText() } }
        val jsonObject = Gson().fromJson(text,WeatherByLocationResponse::class.java)
        result = jsonObject.name.toString()
        assertEquals(testName,result)
    }*/

    @Test
    fun testApiResponse() {
        val testCoord = Coord(28.8381373, 41.0322807)
        val testWeatherItem = WeatherItem("01d","clear sky","Clear",800)
        val mockedApiInterface = Mockito.mock(WeatherService::class.java)
        val jsonArray = WeatherByLocationResponse(751324,testCoord,10000,)
        Mockito.`when`(mockedApiInterface.getWeatherByLatLng(testCoord.lat,testCoord.lon,appId,"Imperial"))
            .thenReturn(CallFake.buildSuccess(WeatherByLocationResponse(name = "NameValue")))
        val call = mockedApiInterface.getWeatherByLatLng(testCoord.lat,testCoord.lon,appId,"Imperial")
        val response = call.execute()
        val weatherResponse = response.body() as WeatherByLocationResponse
        Assert.assertEquals("NameValue", weatherResponse.name)
    }
}

