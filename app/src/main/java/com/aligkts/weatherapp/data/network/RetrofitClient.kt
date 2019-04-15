package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.util.Constant.Companion.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit base
 */

class RetrofitClient {

    companion object {
        fun getApi(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}

