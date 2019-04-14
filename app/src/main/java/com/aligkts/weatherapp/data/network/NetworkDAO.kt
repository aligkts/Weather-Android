package com.aligkts.weatherapp.data.network

import android.os.AsyncTask
import com.aligkts.weatherapp.data.network.model.ForecastByLocationResponse
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class NetworkDAO : AsyncTask<String, String, String>() {

    lateinit var listener: IRequestResult
    override fun doInBackground(vararg url: String?): String {
        val text: String
        val connection = URL(url.first()).openConnection() as HttpURLConnection
        try {
            connection.connect()
            text = connection.inputStream.use { it.reader().use { _reader -> _reader.readText() } }
        } finally {
            connection.disconnect()
        }
        return text
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        val responseBody = Gson().fromJson(result,ForecastByLocationResponse::class.java)
        responseBody.list?.let {_list ->
            for (i in 0 until _list.size step 8) {
                _list[i]?.let { _response ->
                    listener.onSuccess(_response)
                }
            }
        }
    }
}