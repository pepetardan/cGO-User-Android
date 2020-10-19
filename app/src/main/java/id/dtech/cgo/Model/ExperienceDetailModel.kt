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
    var exp_pickup_place_desc : String? = null
    var exp_pickup_time  : String? = null
    var exp_pickup_place_longitude  : Double = 0.0
    var exp_pickup_place_latitude : Double = 0.0
    var exp_pickup_place_maps_name : String? = null
    var status : Int = 0
    var is_request_exp_pickup_place : Int = 0
    var rating : Double = 0.0
    var exp_location_latitude : String? = null
    var exp_location_longitude : String? = null
    var exp_location_name : String? = null
    var exp_cover_photo : String? = null
    var exp_duration : Int = 0
    var count_rating : Int = 0
    var is_customised_intinerary : Int = 0
    var is_certified_guide : Int = 0
    var is_flexible_ticket : Int = 0
    var exp_validity_amount : Int = 0
    var exp_validity_type : String? = null
    var merchant_id : String? = null
    var harbors_name : String? = null
    var trading_hour_start : String? = null
    var trading_hour_end : String? = null
    var city : String? = null
    var province : String? = null
    var exp_address : String? = null
    var start_point : String? = null
    var end_point : String? = null
    var how_to_get_location : String? = null
    var exp_type : ArrayList<String>? = null
    var exp_itenerary : ArrayList<ItemItenaryModel>? = null
    var exp_facility :  ArrayList<FacilityModel>? = null
    var exp_inclusion : ArrayList<InclusionModel>? = null
    var exp_exclusion : ArrayList<InclusionModel>? = null
    var exp_rules : ArrayList<RulesModel>? = null
    var exp_avaibility : ArrayList<LocalDate>? = null
    var exp_payment : ArrayList<PaymentModel>? = null
    var exp_photos : ArrayList<HashMap<String,Any>>? = null
    var exp_guides : ArrayList<HashMap<String,Any>>? = null
    var exp_packages : ArrayList<HashMap<String,Any>>? = null
    var exp_languages : ArrayList<HashMap<String,Any>>? = null
    var exp_accomodations : ArrayList<HashMap<String,Any>>? = null
    var minimum_booking : MinimumBookingModel? = null
    var isGuideMaleExist : Boolean? = false
    var isGuideFemaleExist : Boolean? = false

    constructor(parcel: Parcel) : this() {
        experience_id = parcel.readString()
        exp_title = parcel.readString()
        exp_trip_type = parcel.readString()
        exp_booking_type = parcel.readString()
        exp_desc = parcel.readString()
        exp_max_guest = parcel.readInt()
        exp_pickup_place = parcel.readString()
        exp_pickup_place_desc = parcel.readString()
        exp_pickup_time = parcel.readString()
        exp_pickup_place_longitude = parcel.readDouble()
        exp_pickup_place_latitude = parcel.readDouble()
        exp_pickup_place_maps_name = parcel.readString()
        status = parcel.readInt()
        is_request_exp_pickup_place = parcel.readInt()
        rating = parcel.readDouble()
        exp_location_latitude = parcel.readString()
        exp_location_longitude = parcel.readString()
        exp_location_name = parcel.readString()
        exp_cover_photo = parcel.readString()
        exp_duration = parcel.readInt()
        count_rating = parcel.readInt()
        is_customised_intinerary = parcel.readInt()
        is_certified_guide = parcel.readInt()
        is_flexible_ticket = parcel.readInt()
        exp_validity_amount = parcel.readInt()
        exp_validity_type = parcel.readString()
        merchant_id = parcel.readString()
        harbors_name = parcel.readString()
        trading_hour_start = parcel.readString()
        trading_hour_end = parcel.readString()
        city = parcel.readString()
        province = parcel.readString()
        exp_address = parcel.readString()
        start_point = parcel.readString()
        end_point = parcel.readString()
        how_to_get_location = parcel.readString()
        isGuideMaleExist = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        isGuideFemaleExist = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(experience_id)
        parcel.writeString(exp_title)
        parcel.writeString(exp_trip_type)
        parcel.writeString(exp_booking_type)
        parcel.writeString(exp_desc)
        parcel.writeInt(exp_max_guest)
        parcel.writeString(exp_pickup_place)
        parcel.writeString(exp_pickup_place_desc)
        parcel.writeString(exp_pickup_time)
        parcel.writeDouble(exp_pickup_place_longitude)
        parcel.writeDouble(exp_pickup_place_latitude)
        parcel.writeString(exp_pickup_place_maps_name)
        parcel.writeInt(status)
        parcel.writeInt(is_request_exp_pickup_place)
        parcel.writeDouble(rating)
        parcel.writeString(exp_location_latitude)
        parcel.writeString(exp_location_longitude)
        parcel.writeString(exp_location_name)
        parcel.writeString(exp_cover_photo)
        parcel.writeInt(exp_duration)
        parcel.writeInt(count_rating)
        parcel.writeInt(is_customised_intinerary)
        parcel.writeInt(is_certified_guide)
        parcel.writeInt(is_flexible_ticket)
        parcel.writeInt(exp_validity_amount)
        parcel.writeString(exp_validity_type)
        parcel.writeString(merchant_id)
        parcel.writeString(harbors_name)
        parcel.writeString(trading_hour_start)
        parcel.writeString(trading_hour_end)
        parcel.writeString(city)
        parcel.writeString(province)
        parcel.writeString(exp_address)
        parcel.writeString(start_point)
        parcel.writeString(end_point)
        parcel.writeString(how_to_get_location)
        parcel.writeValue(isGuideMaleExist)
        parcel.writeValue(isGuideFemaleExist)
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