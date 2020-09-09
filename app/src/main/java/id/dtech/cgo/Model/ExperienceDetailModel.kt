package id.dtech.cgo.Model

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDate

class ExperienceDetailModel() : Parcelable {

    var experience_id : String? = null
    var exp_title : String? = null
    var exp_trip_type : String? = null
    var exp_booking_type : String? = null
    var exp_desc : String? = null
    var exp_max_guest : Int = 0
    var exp_pickup_place : String? = null
    var exp_pickup_time  : String? = null
    var exp_pickup_place_longitude  : Double = 0.0
    var exp_pickup_place_latitude : Double = 0.0
    var exp_pickup_place_maps_name : String? = null
    var status : Int = 0
    var rating : Double = 0.0
    var exp_location_latitude : String? = null
    var exp_location_longitude : String? = null
    var exp_location_name : String? = null
    var exp_cover_photo : String? = null
    var exp_duration : Int = 0
    var count_rating : Int = 0
    var merchant_id : String? = null
    var harbors_name : String? = null
    var city : String? = null
    var province : String? = null
    var exp_type : ArrayList<String>? = null
    var exp_itenerary : ArrayList<ItemItenaryModel>? = null
    var exp_facility :  ArrayList<FacilityModel>? = null
    var exp_inclusion : ArrayList<InclusionModel>? = null
    var exp_exclusion : ArrayList<InclusionModel>? = null
    var exp_rules : ArrayList<RulesModel>? = null
    var exp_avaibility : ArrayList<LocalDate>? = null
    var exp_payment : ArrayList<PaymentModel>? = null
    var exp_photos : ArrayList<HashMap<String,Any>>? = null
    var minimum_booking : MinimumBookingModel? = null

    constructor(parcel: Parcel) : this() {
        experience_id = parcel.readString()
        exp_title = parcel.readString()
        exp_trip_type = parcel.readString()
        exp_booking_type = parcel.readString()
        exp_desc = parcel.readString()
        exp_max_guest = parcel.readInt()
        exp_pickup_place = parcel.readString()
        exp_pickup_time = parcel.readString()
        exp_pickup_place_longitude = parcel.readDouble()
        exp_pickup_place_latitude = parcel.readDouble()
        exp_pickup_place_maps_name = parcel.readString()
        status = parcel.readInt()
        rating = parcel.readDouble()
        exp_location_latitude = parcel.readString()
        exp_location_longitude = parcel.readString()
        exp_location_name = parcel.readString()
        exp_cover_photo = parcel.readString()
        exp_duration = parcel.readInt()
        count_rating = parcel.readInt()
        merchant_id = parcel.readString()
        harbors_name = parcel.readString()
        city = parcel.readString()
        province = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(experience_id)
        parcel.writeString(exp_title)
        parcel.writeString(exp_trip_type)
        parcel.writeString(exp_booking_type)
        parcel.writeString(exp_desc)
        parcel.writeInt(exp_max_guest)
        parcel.writeString(exp_pickup_place)
        parcel.writeString(exp_pickup_time)
        parcel.writeDouble(exp_pickup_place_longitude)
        parcel.writeDouble(exp_pickup_place_latitude)
        parcel.writeString(exp_pickup_place_maps_name)
        parcel.writeInt(status)
        parcel.writeDouble(rating)
        parcel.writeString(exp_location_latitude)
        parcel.writeString(exp_location_longitude)
        parcel.writeString(exp_location_name)
        parcel.writeString(exp_cover_photo)
        parcel.writeInt(exp_duration)
        parcel.writeInt(count_rating)
        parcel.writeString(merchant_id)
        parcel.writeString(harbors_name)
        parcel.writeString(city)
        parcel.writeString(province)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExperienceDetailModel> {
        override fun createFromParcel(parcel: Parcel): ExperienceDetailModel {
            return ExperienceDetailModel(parcel)
        }

        override fun newArray(size: Int): Array<ExperienceDetailModel?> {
            return arrayOfNulls(size)
        }
    }


}