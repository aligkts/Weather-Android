package com.aligkts.weatherapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.aligkts.weatherapp.data.IDownloadedImageBitmap

class DownloadImage(private var listener: IDownloadedImageBitmap) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String): Bitmap? {
        val urldisplay = urls.first()
        var mIcon: Bitmap? = null
        try {
            val `in` = java.net.URL(urldisplay).openStream()
            mIcon = BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            Log.e("Error", e.message)
            e.printStackTrace()
        }
        return mIcon
    }

    override fun onPostExecute(result: Bitmap) {
        listener.sendDownloadedBitmap(result)
    }

}