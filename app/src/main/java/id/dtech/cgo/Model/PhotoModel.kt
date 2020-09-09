package id.dtech.cgo.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class PhotoModel() : Parcelable, Serializable {

    var original : String? = null
    var thumbnail : String? = null

    constructor(parcel: Parcel) : this() {
        original = parcel.readString()
        thumbnail = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(original)
        parcel.writeString(thumbnail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoModel> {
        override fun createFromParcel(parcel: Parcel): PhotoModel {
            return PhotoModel(parcel)
        }

        override fun newArray(size: Int): Array<PhotoModel?> {
            return arrayOfNulls(size)
        }
    }
}