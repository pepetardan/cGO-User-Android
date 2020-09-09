package id.dtech.cgo.Model

import android.os.Parcel
import android.os.Parcelable

class ExpDestinationModel() : Parcelable {

    var id : String? = null
    var harbors_name : String? = null
    var harbors_longitude : Double = 0.0
    var harbors_latitude : Double = 0.0
    var harbors_image : String? = null
    var province_id : Int = 0
    var city_id : Int = 0
    var city : String? = null
    var province : String? = null
    var country : String? = null
    var type : Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        harbors_name = parcel.readString()
        harbors_longitude = parcel.readDouble()
        harbors_latitude = parcel.readDouble()
        harbors_image = parcel.readString()
        province_id = parcel.readInt()
        city_id = parcel.readInt()
        city = parcel.readString()
        province = parcel.readString()
        country = parcel.readString()
        type = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(harbors_name)
        parcel.writeDouble(harbors_longitude)
        parcel.writeDouble(harbors_latitude)
        parcel.writeString(harbors_image)
        parcel.writeInt(province_id)
        parcel.writeInt(city_id)
        parcel.writeString(city)
        parcel.writeString(province)
        parcel.writeString(country)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExpDestinationModel> {
        override fun createFromParcel(parcel: Parcel): ExpDestinationModel {
            return ExpDestinationModel(parcel)
        }

        override fun newArray(size: Int): Array<ExpDestinationModel?> {
            return arrayOfNulls(size)
        }
    }


}