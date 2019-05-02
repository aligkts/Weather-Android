package com.aligkts.weatherapp.data

import android.graphics.Bitmap
import androidx.collection.LruCache

/**
 * Created by Ali Göktaş on 02,May,2019
 */

class MemoryCache private constructor() {

    private var lru :LruCache<String, Bitmap> = LruCache<String, Bitmap>(1024)

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

}