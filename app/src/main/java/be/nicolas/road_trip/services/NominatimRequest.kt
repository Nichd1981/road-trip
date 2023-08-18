package be.nicolas.road_trip.services


import android.content.Context
import android.net.Uri
import be.nicolas.road_trip.R
import be.nicolas.road_trip.models.CityModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class NominatimRequest(private val context : Context, private val scope: CoroutineScope) {

    interface OnNominatimRequestResultListener {
        fun onNominatimSuccess(cities : List<CityModel>)
        fun onNominatimError(message : String) // ← A customiser
    }

    fun rechercheParVille(city: String, resultListener: OnNominatimRequestResultListener) {

        val urlDeRecherche = context.getString(R.string.Adresse_Nominatim)

        scope.launch {

            val constructiondeLurl = Uri.parse(urlDeRecherche).buildUpon().apply {
                appendQueryParameter("q", city)
                appendQueryParameter("format", "json")
                appendQueryParameter("addressdetails", "1")
            }
            val url = URL(constructiondeLurl.build().toString())

            withContext(Dispatchers.IO){
                val connection = url.openConnection() as? HttpURLConnection

                val json = connection?.run {
                    requestMethod = "GET"
                    setRequestProperty("content-type", "application/json; charset=utf-8")
                    doInput = true
                    return@run inputStream.bufferedReader().lineSequence().joinToString("\n")
                }

                if(json != null) {
                    val valeurDonnee = object : TypeToken<List<CityModel>>() {}.type
                    val resultat = Gson().fromJson<List<CityModel>>(json, valeurDonnee)

                    withContext(Dispatchers.Main) {
                        resultListener.onNominatimSuccess(resultat)
                    }
                }
                else {
                    withContext(Dispatchers.Main) {
                        resultListener.onNominatimError(context.getString(R.string.pas_de_connection))
                    }
                }

            }
        }
    }
}