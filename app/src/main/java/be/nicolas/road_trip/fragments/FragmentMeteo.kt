package be.nicolas.road_trip.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import be.nicolas.road_trip.R
import be.nicolas.road_trip.models.MeteoModel
import com.bumptech.glide.Glide

private const val ARG_METEO = "LaSuperMeteo"


class FragmentMeteo : Fragment() {

    private var meteo: MeteoModel? = null
    private lateinit var IviconMeteo: ImageView
    private lateinit var tvTempMax: TextView
    private lateinit var tvTempMin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            meteo = it.getParcelable(ARG_METEO)
            Log.d("rechercherMeteoVille Final", meteo.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Create View
        val view = inflater.inflate(R.layout.fragment_meteo, container, false)

        IviconMeteo = view.findViewById(R.id.icon_meteo)
        tvTempMax = view.findViewById(R.id.temp_max)
        tvTempMin = view.findViewById(R.id.temp_min)

        val temperaturMax = meteo?.main?.tempMax
        val temperaturMin = meteo?.main?.tempMin
        val iconTemp = meteo?.weather?.get(0)?.icon

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/$iconTemp@2x.png")
            .placeholder(R.drawable.default_icon)
            .into(IviconMeteo)
        tvTempMax.text = "%s °C".format(temperaturMax.toString())
        tvTempMin.text = "%s °C".format(temperaturMin.toString())
        // Return view
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: MeteoModel) =
            FragmentMeteo().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_METEO, param1)
                }
            }
    }
}