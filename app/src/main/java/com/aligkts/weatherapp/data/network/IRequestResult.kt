package com.aligkts.weatherapp.data.network

import com.aligkts.weatherapp.data.network.model.ModelResponse

interface IRequestResult {

    fun onSuccess(modelResponse: ModelResponse)

    fun onFailure(t: Throwable)
}