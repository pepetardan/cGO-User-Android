package id.dtech.cgo.Model

import android.os.Parcel
import android.os.Parcelable

class TransportationModel() : Parcelable {
    var schedule_id : String? = null
    var departure_date : String? = null
    var departure_time : String? = null
    var arrival_time : String? = null
    var trip_duration : String? = null
    var transportation_id : String? = null
    var transportation_name : String? = null
    var transportation_status : String? = null
    var harbor_source_id : String? = null
    var harbor_source_name : String? = null
    var harbor_destination_id : String? = null
    var harbor_destination_name : String? = null

    var city_source_id : Int = 0
    var city_source_name : String? = null
    var city_dest_id : Int = 0
    var city_dest_name : String? = null

    var province_source_id : Int = 0
    var province_source_name : String? = null
    var province_dest_id : Int = 0
    var province_dest_name : String? = null
    var return_trans_id : String? = null

    var merchant_name : String? = null
    var merchant_picture : String? = null
    var transportClass : String? = null
    var totalGuest : Int = 0
    var facilities : ArrayList<FacilityModel>? = null
    var price : HashMap<String,Any>? = null
    var transportation_images : ArrayList<HashMap<String,Any>>? = null
    var boat_details : HashMap<String,Any>? = null

    var viewType : Int = 0

    constructor(parcel: Parcel) : this() {
        schedule_id = parcel.readString()
        departure_date = parcel.readString()
        departure_time = parcel.readString()
        arrival_time = parcel.readString()
        trip_duration = parcel.readString()
        transportation_id = parcel.readString()
        transportation_name = parcel.readString()
        transportation_status = parcel.readString()
        harbor_source_id = parcel.readString()
        harbor_source_name = parcel.readString()
        harbor_destination_id = parcel.readString()
        harbor_destination_name = parcel.readString()
        city_source_id = parcel.readInt()
        city_source_name = parcel.readString()
        city_dest_id = parcel.readInt()
        city_dest_name = parcel.readString()
        province_source_id = parcel.readInt()
        province_source_name = parcel.readString()
        province_dest_id = parcel.readInt()
        province_dest_name = parcel.readString()
        return_trans_id = parcel.readString()
        merchant_name = parcel.readString()
        merchant_picture = parcel.readString()
        transportClass = parcel.readString()
        totalGuest = parcel.readInt()
        viewType = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(schedule_id)
        parcel.writeString(departure_date)
        parcel.writeString(departure_time)
        parcel.writeString(arrival_time)
        parcel.writeString(trip_duration)
        parcel.writeString(transportation_id)
        parcel.writeString(transportation_name)
        parcel.writeString(transportation_status)
        parcel.writeString(harbor_source_id)
        parcel.writeString(harbor_source_name)
        parcel.writeString(harbor_destination_id)
        parcel.writeString(harbor_destination_name)
        parcel.writeInt(city_source_id)
        parcel.writeString(city_source_name)
        parcel.writeInt(city_dest_id)
        parcel.writeString(city_dest_name)
        parcel.writeInt(province_source_id)
        parcel.writeString(province_source_name)
        parcel.writeInt(province_dest_id)
        parcel.writeString(province_dest_name)
        parcel.writeString(return_trans_id)
        parcel.writeString(merchant_name)
        parcel.writeString(merchant_picture)
        parcel.writeString(transportClass)
        parcel.writeInt(totalGuest)
        parcel.writeInt(viewType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransportationModel> {
        override fun createFromParcel(parcel: Parcel): TransportationModel {
            return TransportationModel(parcel)
        }

        override fun newArray(size: Int): Array<TransportationModel?> {
            return arrayOfNulls(size)
        }
    }


}