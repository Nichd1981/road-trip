package be.nicolas.road_trip.services

import android.content.Context
import android.net.Uri
import be.nicolas.road_trip.R
import be.nicolas.road_trip.models.CityModel
import be.nicolas.road_trip.models.MeteoModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class OpenWeather( private val context: Context, private val scope: CoroutineScope){

    interface OnReponseListener {
        fun onRequestReponseSucces(result: MeteoModel)
        fun onRequestReponseError(message: String?)
    }

    fun meteoDulieu(ville: CityModel, responseListener: OnReponseListener){

        val Base_Url = context.getString(R.string.Adresse_OpenWeather)
        val appid = context.getString(R.string.appid_openWeather)
        val lat = ville.lat
        val lon = ville.lon

        scope.launch {
            val urlBuilder = Uri.parse(Base_Url).buildUpon().apply {
                appendQueryParameter("lat", lat)
                appendQueryParameter("lon", lon)
                appendQueryParameter("units", "metric")
                appendQueryParameter("lang", "fr")
                appendQueryParameter("appid", appid)
            }
            val url = URL(urlBuilder.build().toString())
            withContext(Dispatchers.IO) {

                val connection = url.openConnection() as? HttpURLConnection

                if (connection == null) {
                    withContext(Dispatchers.Main) {
                        responseListener.onRequestReponseError("Connection not open")
                    }
                    return@withContext
                }

                try {
                    val json = connection.run {
                        requestMethod = "GET"
                        setRequestProperty("content-type", "application/json; charset=utf-8")
                        doInput = true

                        return@run inputStream.bufferedReader().lineSequence().joinToString("\n")
                    }

                    val typeToken = MeteoModel::class.java
                    val result = Gson().fromJson(json, typeToken)

                    withContext(Dispatchers.Main) {
                        responseListener.onRequestReponseSucces(result)
                    }

                } catch (error: Exception) {
                    withContext(Dispatchers.Main) {
                        responseListener.onRequestReponseError(error.message)
                    }
                }
            }
        }
    }
}

