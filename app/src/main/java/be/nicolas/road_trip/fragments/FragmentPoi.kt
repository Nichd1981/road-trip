package be.nicolas.road_trip.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.nicolas.road_trip.R
import be.nicolas.road_trip.models.CityModel
import com.mapbox.maps.MapView

class FragmentPoi : Fragment() {

    private lateinit var mapPoi: MapView

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_poi, container, false)
        }
}