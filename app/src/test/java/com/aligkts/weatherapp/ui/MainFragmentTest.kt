package com.aligkts.weatherapp.ui

import com.aligkts.weatherapp.dto.weatherbylocation.Coord
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import org.junit.Assert.*
import java.net.HttpURLConnection
import java.net.URL

class MainFragmentTest {

    @Test
    fun testSetCurrentWeather() {
        val testCoord = Coord(28.8381373, 41.0322807)
        var result = ""
        val deneme = "Bagcilar"
        val url ="http://api.openweathermap.org/data/2.5/weather?lat="
            .plus(testCoord.lat).plus("&lon=")
            .plus(testCoord.lon).plus("&&APPID=3c75e1a077769372966bc6050f85b57a&units=Metric")

        val text: String
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        text = connection.inputStream.use { it.reader().use { _reader -> _reader.readText() } }
        val jsonObject = Gson().fromJson(text,WeatherByLocationResponse::class.java)
        result = jsonObject.name.toString()
        assertEquals(deneme,result)
    }
}

