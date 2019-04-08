package com.aligkts.weatherapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aligkts.weatherapp.dto.sqlite.FavoriteLocationEntity

class DBConnectionManager(val context: Context) :
    SQLiteOpenHelper(context, DBConnectionManager.DATABASE_NAME, null, DBConnectionManager.DATABASE_VERSION) {
    private val TABLE_NAME = "Favorites"
    private val COL_ID = "id"
    private val COL_LAT = "lat"
    private val COL_LON = "lon"

    companion object {
        private val DATABASE_NAME = "WEATHER_DATABASE"//database adı
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER, $COL_LAT  VARCHAR(100),$COL_LON  VARCHAR(100))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(model: FavoriteLocationEntity): Boolean {
        val sqliteDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, model.id)
        contentValues.put(COL_LAT, model.latitude) //can be better with using lambda
        contentValues.put(COL_LON, model.longitude)
        val result = sqliteDB.insert(TABLE_NAME, null, contentValues)
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
                favorites.latitude = result.getString(result.getColumnIndex(COL_LAT)).toDouble()
                favorites.longitude = result.getString(result.getColumnIndex(COL_LON)).toDouble()
                locationList.add(favorites)
            } while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return locationList as ArrayList<FavoriteLocationEntity>
    }

    fun deleteClickedItem(position: Int) {
        val sqliteDB = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME WHERE id=$position"
        sqliteDB.execSQL(query)
        sqliteDB.close()
    }
}