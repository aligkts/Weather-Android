package com.aligkts.weatherapp.data

import android.graphics.Bitmap
import androidx.collection.LruCache
import com.aligkts.weatherapp.data.network.model.ModelResponse

/**
 * Created by Ali Göktaş on 02,May,2019
 */

class MemoryCache private constructor() {

    var maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    var catchSize = maxMemory / 8
    private var lru :LruCache<String, Bitmap> = LruCache(catchSize)
    private val lruFavoritesList :LruCache<String,Array<ModelResponse?>> = LruCache(catchSize)
    private val lruCurrentWeather :LruCache<String,ModelResponse> = LruCache(catchSize)

    companion object {
        private var uniqInstance: MemoryCache? = null
        val instance: MemoryCache?
            get() {
                if (uniqInstance == null) {
                    run {
                        if (uniqInstance == null)
                            uniqInstance = MemoryCache()
                    }
                }
                return uniqInstance
            }
    }

    fun getLru(): LruCache<String, Bitmap> {
        return this.lru
    }

    fun getLruFavoritesList(): LruCache<String,Array<ModelResponse?>> {
        return this.lruFavoritesList
    }

    fun getLruCurrentWeather(): LruCache<String,ModelResponse> {
        return this.lruCurrentWeather
    }



}