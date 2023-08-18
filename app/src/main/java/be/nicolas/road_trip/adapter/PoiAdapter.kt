package be.nicolas.road_trip.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import be.nicolas.road_trip.R
import be.nicolas.road_trip.models.CityModel

class PoiAdapter(context: Context, val nominatimList: MutableList<CityModel>) : ArrayAdapter<CityModel>(context, R.layout.item_poi, R.id.tv_item_poi_nom, nominatimList){

    private lateinit var tvPoi : TextView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        tvPoi = view.findViewById(R.id.tv_item_poi_nom)

        return view
    }
}
