package be.nicolas.road_trip.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import be.nicolas.road_trip.R
import be.nicolas.road_trip.models.CityModel
import be.nicolas.road_trip.models.MeteoModel
import be.nicolas.road_trip.services.OpenWeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.LocationPuck3D
import com.mapbox.maps.plugin.locationcomponent.location


class FragmentHome : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mapView = view.findViewById(R.id.mapView)
        positionActuel()
        return view
    }

    private fun positionActuel() {

        checkLocationPermission()

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location : Location ->
                positionActuelDuUser(location)
                }

    }

    private fun localisationPosition(){
        val pointeur = mapView.location
        pointeur.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    requireContext(), R.drawable.pointeur
                )
            )
        }
    }
    private fun positionActuelDuUser(location: Location) {

        val lon = location.longitude
        val lat = location.latitude

        val cameraPosition = CameraOptions.Builder()
            .zoom(14.0)
            .center(
                Point.fromLngLat(lon, lat)
            )
            .build()
        localisationPosition()
        mapView.getMapboxMap().apply {
            loadStyleUri(Style.MAPBOX_STREETS)
            setCamera(cameraPosition)
        }
    }

    fun villeSurLaCarte(villeChoisie: CityModel){

        val lat = villeChoisie.lat!!.toDouble()
        val lon = villeChoisie.lon!!.toDouble()
        val positionDeLaVue = CameraOptions.Builder()
            .zoom(10.0)
            .center(Point.fromLngLat(lon, lat))
            .build()

        mapView.getMapboxMap().setCamera(positionDeLaVue)
    }

    private fun checkLocationPermission() {

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(requireContext(),
                    getString(R.string.Toast_fragment_Map_Acces_localisation_non_non_non), Toast.LENGTH_LONG).show()

            }
        }

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
