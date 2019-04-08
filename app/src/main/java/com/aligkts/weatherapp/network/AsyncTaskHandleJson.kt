package com.aligkts.weatherapp.network

import android.os.AsyncTask
import com.aligkts.weatherapp.network.response.ForecastByLocationResponse
import com.aligkts.weatherapp.util.OnTaskCompleted
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskHandleJson : AsyncTask<String, String, String>() {

    lateinit var listener: OnTaskCompleted

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
        listener.onTaskCompleted(responseBody)
    }
}