package com.aligkts.weatherapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import android.util.Base64
import com.google.gson.Gson
import java.io.ByteArrayOutputStream


/**
 * Created by Ali Göktaş on 30,April,2019
 */

class MapUtil(context: Context) {

    private val iconMap = HashMap<String, String>()
    lateinit var bitmap: Bitmap
    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    private val mapStringFromSharedPreferences by lazy { prefs.getString("iconHashMap", null) }
    private val parsedMap = Gson().fromJson(mapStringFromSharedPreferences, iconMap::class.java)

    fun checkIconCode(iconCode: String): Bitmap? {
        if (parsedMap.containsKey(iconCode)) {
            parsedMap[iconCode]?.let {
                val imageAsBytes = Base64.decode(parsedMap[iconCode].toString(), Base64.DEFAULT)
                bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
                return bitmap
            }
        }
        return null
    }

    fun addValueToMap(key: String, value: Bitmap) {
        val baos = ByteArrayOutputStream()
        value.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val encoded = Base64.encodeToString(b, Base64.DEFAULT)
        parsedMap.put(key, encoded)
        val mapString = Gson().toJson(parsedMap)
        prefs.edit().putString("iconHashMap", mapString).apply()
    }

}