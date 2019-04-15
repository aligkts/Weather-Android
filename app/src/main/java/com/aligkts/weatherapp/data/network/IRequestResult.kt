package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.network.model.ModelResponse

/**
 * Request result
 */

interface IRequestResult {

    fun onSuccess(modelResponse: ModelResponse)
    fun onFailure(t: Throwable)

}