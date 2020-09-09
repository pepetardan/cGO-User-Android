package id.dtech.cgo.Model

import android.os.Parcel
import android.os.Parcelable

class FacilityModel() : Parcelable{
    var name : String? = null
    var icon : String? = null
    var amount : Int  = 0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        icon = parcel.readString()
        amount = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeInt(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FacilityModel> {
        override fun createFromParcel(parcel: Parcel): FacilityModel {
            return FacilityModel(parcel)
        }

        override fun newArray(size: Int): Array<FacilityModel?> {
            return arrayOfNulls(size)
        }
    }
}