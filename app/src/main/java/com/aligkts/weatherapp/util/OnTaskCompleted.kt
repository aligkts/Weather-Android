package com.aligkts.weatherapp.util

import com.aligkts.weatherapp.network.response.ForecastByLocationResponse

interface OnTaskCompleted {
    fun onTaskCompleted(values:ForecastByLocationResponse)
}