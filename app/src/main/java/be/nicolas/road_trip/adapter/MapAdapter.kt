package be.nicolas.road_trip.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import be.nicolas.road_trip.R

import be.nicolas.road_trip.models.CityModel

class MapAdapter(context: Context, val nominatimList: MutableList<CityModel>) : ArrayAdapter<CityModel>(context, R.layout.item_city, R.id.tv_item_city_nom, nominatimList) {

    private lateinit var tvNom : TextView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        tvNom = view.findViewById(R.id.tv_item_city_nom)

        populateView(position)

        return view
    }

    fun populateView(position : Int) {
        val city = nominatimList[position]

        tvNom.setText(city.displayName)

        // TODO Traiter le click
    }
    
}