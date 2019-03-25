package com.aligkts.weatherapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aligkts.weatherapp.dto.sqlite.FavoriteLocation

class DBHelper(val context: Context) : SQLiteOpenHelper(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION) {
    private val TABLE_NAME = "Favorites"
    private val COL_ID = "id"
    private val COL_LAT = "lat"
    private val COL_LON = "lon"

    companion object {
        private val DATABASE_NAME = "WEATHER_DATABASE"//database adı
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_LAT  VARCHAR(100),$COL_LON  VARCHAR(100))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


    fun insertData(favoriteLocation: FavoriteLocation): Boolean {
        val sqliteDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_LAT, favoriteLocation.lat)
        contentValues.put(COL_LON, favoriteLocation.lon)

        val result = sqliteDB.insert(TABLE_NAME, null, contentValues)
        //Toast.makeText(context, if (result != -1L) "Kayıt Başarılı"  else "Kayıt yapılamadı.", Toast.LENGTH_SHORT).show()
        return result != -1L

    }

}