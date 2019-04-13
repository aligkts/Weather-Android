package com.aligkts.weatherapp.view.ui

import com.aligkts.weatherapp.data.dto.weatherbylocation.Coord
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito
import org.junit.Before
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class MainFragmentTest {

    val weatherAppId = "3c75e1a077769372966bc6050f85b57a"

    /* @Test
     fun testSetCurrentWeather() {
         val testCoord = Coord(28.8381373, 41.0322807)
         var result = ""
         val testNam    e = "Bagcilar"
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

    lateinit var mainFragment: MainFragment

    @Before
    fun setUp() {
        mainFragment = Mockito.mock(MainFragment::class.java)
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun findLocationTest() {
        val testCoord = Coord(28.8381373, 41.0322807)
        Mockito.`when`(mainFragment.findLocation()).thenReturn(testCoord)
        val coord = mainFragment.findLocation()
        assertEquals(testCoord,coord)
    }


    @Test
    fun testGetCurrentWeatherFromApi() {
        val testCoord = Coord(28.8381373, 41.0322807)

        val latitude = testCoord.lat!!
        val longitude = testCoord.lon!!

        assertEquals(testCoord.lat!!, latitude, 0.001)
        assertEquals(testCoord.lon!!, longitude, 0.001)

        `when`(mainFragment.getCurrentWeatherFromApi(testCoord))
        //doNothing().`when`(mainFragment).setCurrentWeather(testCoord)
    }
}

