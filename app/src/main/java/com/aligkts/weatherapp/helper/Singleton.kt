package com.aligkts.weatherapp.helper

import com.aligkts.weatherapp.network.response.WeatherByLocationResponse

class Singleton private constructor() {
    var dataList = WeatherByLocationResponse()


    companion object {
        private var uniqInstance: Singleton? = null
        val instance: Singleton?
            get() {
                if (uniqInstance == null) {
                    run {
                        if (uniqInstance == null)
                            uniqInstance = Singleton()
                    }
                }
                return uniqInstance
            }
    }

    fun setArrayList(list : WeatherByLocationResponse){
        this.dataList = list
    }

    fun getArrayList(): WeatherByLocationResponse {

        return this.dataList
    }


}