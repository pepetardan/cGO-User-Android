package id.dtech.cgo.Model

import android.os.Parcel
import android.os.Parcelable

class PaymentModel() : Parcelable{
    var id : String? = null
    var currency : String? = null
    var price : Long = 0
    var price_item_type : String? = null
    var payment_type_id : String? = null
    var payment_type_name : String? = null
    var payment_type_desc : String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        currency = parcel.readString()
        price = parcel.readLong()
        price_item_type = parcel.readString()
        payment_type_id = parcel.readString()
        payment_type_name = parcel.readString()
        payment_type_desc = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(currency)
        parcel.writeLong(price)
        parcel.writeString(price_item_type)
        parcel.writeString(payment_type_id)
        parcel.writeString(payment_type_name)
        parcel.writeString(payment_type_desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentModel> {
        override fun createFromParcel(parcel: Parcel): PaymentModel {
            return PaymentModel(parcel)
        }

        override fun newArray(size: Int): Array<PaymentModel?> {
            return arrayOfNulls(size)
        }
    }
}