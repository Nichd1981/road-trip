package be.nicolas.road_trip.models

import com.google.gson.annotations.SerializedName

data class CityAddressModel(

    @SerializedName("town"           ) var town           : String? = null,
    @SerializedName("state"          ) var state          : String? = null,
    @SerializedName("region"         ) var region         : String? = null,
    @SerializedName("postcode"       ) var postcode       : String? = null,
    @SerializedName("country"        ) var country        : String? = null,
    @SerializedName("country_code"   ) var countryCode    : String? = null

)

