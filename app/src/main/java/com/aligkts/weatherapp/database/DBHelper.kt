package com.aligkts.weatherapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aligkts.weatherapp.dto.sqlite.FavoriteLocationEntity

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
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_LAT  VARCHAR(100),$COL_LON  VARCHAR(100))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


    fun insertData(favoriteLocationEntity: FavoriteLocationEntity): Boolean {
        val sqliteDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_LAT, favoriteLocationEntity.lat)
        contentValues.put(COL_LON, favoriteLocationEntity.lon)

        val result = sqliteDB.insert(TABLE_NAME, null, contentValues)
        //Toast.makeText(context, if (result != -1L) "Kayıt Başarılı"  else "Kayıt yapılamadı.", Toast.LENGTH_SHORT).show()


        return result != -1L

    }


    fun readFavoritesList(): ArrayList<FavoriteLocationEntity> {
        val locationList = mutableListOf<FavoriteLocationEntity>()
        val sqliteDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = sqliteDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val favorites = FavoriteLocationEntity()
                favorites.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                favorites.lat = result.getString(result.getColumnIndex(COL_LAT)).toDouble()
                favorites.lon = result.getString(result.getColumnIndex(COL_LON)).toDouble()
                locationList.add(favorites)
            } while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return locationList as ArrayList<FavoriteLocationEntity>
    }

}