package id.dtech.cgo.Model

import android.os.Parcel
import android.os.Parcelable

class AddOnModel() : Parcelable {

    var id : String? = null
    var name : String? = null
    var desc : String? = null
    var currency : String? = null
    var amount : Long = 0
    var total_price : Long = 0
    var exp_id : String? = null
    var selected : Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        desc = parcel.readString()
        currency = parcel.readString()
        amount = parcel.readLong()
        total_price = parcel.readLong()
        exp_id = parcel.readString()
        selected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeString(currency)
        parcel.writeLong(amount)
        parcel.writeLong(total_price)
        parcel.writeString(exp_id)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddOnModel> {
        override fun createFromParcel(parcel: Parcel): AddOnModel {
            return AddOnModel(parcel)
        }

        override fun newArray(size: Int): Array<AddOnModel?> {
            return arrayOfNulls(size)
        }
    }


}