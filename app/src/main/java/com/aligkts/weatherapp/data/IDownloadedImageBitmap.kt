package com.aligkts.weatherapp.data

import android.graphics.Bitmap

interface IDownloadedImageBitmap {
    fun sendDownloadedBitmap(bitmap: Bitmap)
}