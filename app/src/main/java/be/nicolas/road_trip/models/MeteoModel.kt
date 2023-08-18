package be.nicolas.road_trip.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class MeteoModel (

    @SerializedName("coord"      ) var coord      : Coord?             = Coord(),
    @SerializedName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf(),
    @SerializedName("base"       ) var base       : String?            = null,
    @SerializedName("main"       ) var main       : Main?              = Main(),

) : Parcelable {

    data class Coord(
        @SerializedName("lon") var lon: Double? = null,
        @SerializedName("lat") var lat: Double? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(lon)
            parcel.writeValue(lat)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Coord> {
            override fun createFromParcel(parcel: Parcel): Coord {
                return Coord(parcel)
            }

            override fun newArray(size: Int): Array<Coord?> {
                return arrayOfNulls(size)
            }
        }

    }


    data class Weather(
        @SerializedName("id") var id: Int? = null,
        @SerializedName("main") var main: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("icon") var icon: String? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(main)
            parcel.writeString(description)
            parcel.writeString(icon)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Weather> {
            override fun createFromParcel(parcel: Parcel): Weather {
                return Weather(parcel)
            }

            override fun newArray(size: Int): Array<Weather?> {
                return arrayOfNulls(size)
            }
        }
    }


    data class Main (
        @SerializedName("temp") var temp: Double? = null,
        @SerializedName("feels_like") var feelsLike: Double? = null,
        @SerializedName("temp_min") var tempMin: Double? = null,
        @SerializedName("temp_max") var tempMax: Double? = null,
        @SerializedName("pressure") var pressure: Int? = null,
        @SerializedName("humidity") var humidity: Int? = null,
        @SerializedName("sea_level") var seaLevel: Int? = null,
        @SerializedName("grnd_level") var grndLevel: Int? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(temp)
            parcel.writeValue(feelsLike)
            parcel.writeValue(tempMin)
            parcel.writeValue(tempMax)
            parcel.writeValue(pressure)
            parcel.writeValue(humidity)
            parcel.writeValue(seaLevel)
            parcel.writeValue(grndLevel)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Main> {
            override fun createFromParcel(parcel: Parcel): Main {
                return Main(parcel)
            }

            override fun newArray(size: Int): Array<Main?> {
                return arrayOfNulls(size)
            }
        }

    }




    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Coord::class.java.classLoader),
        arrayListOf<Weather>(),
        parcel.readString(),
        parcel.readParcelable(Main::class.java.classLoader)
    ) {
        parcel.readList(weather, Weather::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(coord, flags)
        parcel.writeString(base)
        parcel.writeParcelable(main, flags)
        parcel.writeParcelableList(weather, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MeteoModel> {
        override fun createFromParcel(parcel: Parcel): MeteoModel {
            return MeteoModel(parcel)
        }

        override fun newArray(size: Int): Array<MeteoModel?> {
            return arrayOfNulls(size)
        }
    }
}