package com.aligkts.weatherapp.data

import com.aligkts.weatherapp.data.network.model.ModelResponse

/**
 * Access data from everywhere
 */

class SingletonModel private constructor() {

    private var dataList = ModelResponse()

    companion object {
        private var uniqInstance: SingletonModel? = null
        val instance: SingletonModel?
            get() {
                if (uniqInstance == null) {
                    run {
                        if (uniqInstance == null)
                            uniqInstance =
                                SingletonModel()
                    }
                }
                return uniqInstance
            }
    }

    fun setCurrentList(list: ModelResponse) {
        this.dataList = list
    }

    fun getCurrentList(): ModelResponse {
        return this.dataList
    }

    fun setOtherList(list: ModelResponse?) {
        if (list != null) this.dataList = list
    }

    fun getOtherList(): ModelResponse {
        return this.dataList
    }

}