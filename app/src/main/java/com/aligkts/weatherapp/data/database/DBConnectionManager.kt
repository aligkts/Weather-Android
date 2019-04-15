package com.aligkts.weatherapp.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aligkts.weatherapp.R
import com.aligkts.weatherapp.data.database.model.FavoriteLocation

/**
 * Doing database operations
 */

class DBConnectionManager(val context: Context) :
      SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val TABLE_NAME = context.getString(R.string.table_name)
    private val COL_ID = context.getString(R.string.column_id)
    private val COL_LAT = context.getString(R.string.column_lat)
    private val COL_LON = context.getString(R.string.column_lon)

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

    fun insertData(model: FavoriteLocation): Boolean {
        val sqliteDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, model.id)
        contentValues.put(COL_LAT, model.latitude)
        contentValues.put(COL_LON, model.longitude)
        val result = sqliteDB.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun readFavoritesList(): ArrayList<FavoriteLocation> {
        val locationList = mutableListOf<FavoriteLocation>()
        val sqliteDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = sqliteDB.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val favorites = FavoriteLocation()
                favorites.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                favorites.latitude = result.getString(result.getColumnIndex(COL_LAT)).toDouble()
                favorites.longitude = result.getString(result.getColumnIndex(COL_LON)).toDouble()
                locationList.add(favorites)
            } while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return locationList as ArrayList<FavoriteLocation>
    }

    fun deleteClickedItem(position: Int) {
        val sqliteDB = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME WHERE id=$position"
        sqliteDB.execSQL(query)
        sqliteDB.close()
    }

}