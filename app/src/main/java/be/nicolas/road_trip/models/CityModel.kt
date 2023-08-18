package be.nicolas.road_trip.models

import com.google.gson.annotations.SerializedName

data class CityModel(
    @SerializedName("lon"          ) var lon         : String?           = null,
    @SerializedName("lat"          ) var lat         : String?           = null,
    @SerializedName("importance"   ) var importance  : Double?           = null,
    @SerializedName("addresstype"  ) var addresstype : String?           = null,
    @SerializedName("name"         ) var name        : String?           = null,
    @SerializedName("display_name" ) var displayName : String?           = null,
    @SerializedName("address"      ) var address     : CityAddressModel?      = CityAddressModel()
)