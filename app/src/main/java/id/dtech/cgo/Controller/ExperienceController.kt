package id.dtech.cgo.Controller

import android.annotation.SuppressLint
import android.icu.util.LocaleData
import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Model.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ExperienceController {

         private val experienceList = ArrayList<ExperienceModel>()

          fun postPaymentConfirmation(body : HashMap<String,Any>, confirmationPaymentCallback :
          MyCallback.Companion.ConfirmationPaymentCallback){

              confirmationPaymentCallback.onConfirmationPaymentPrepare()

              val retrofit = MyConnection.myClient(ApiService::class.java,null)
              val discover = retrofit.postConfirmPayment(body)

              discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                  Observer<Response<ResponseBody>> {
                  override fun onComplete() {

                  }

                  override fun onSubscribe(d: Disposable) {

                  }

                  override fun onNext(t: Response<ResponseBody>) {
                      if (t.isSuccessful){
                          try {
                              val jsonObject = JSONObject(t.body()?.string())
                              val transaction_id = jsonObject.getString("transaction_id")
                              val status = jsonObject.getInt("status")
                              val message  = jsonObject.getString("message")

                              val confirmationMap = HashMap<String,Any>()
                              confirmationMap["transaction_id"] = transaction_id
                              confirmationMap["status"] = status
                              confirmationMap["message"] = message

                              confirmationPaymentCallback.onConfirmationPaymentSuccess(confirmationMap)
                          }
                          catch (e : Exception){
                              confirmationPaymentCallback.onConfirmationPaymentError(e.message ?: "")
                              Log.d("confirmation_error",e.message ?: "")
                          }
                      }
                      else{
                          confirmationPaymentCallback.onConfirmationPaymentError(t.message())
                          Log.d("confirmation_error",t.message())
                      }
                  }

                  override fun onError(e: Throwable) {
                      confirmationPaymentCallback.onConfirmationPaymentError(e.message ?: "")
                      Log.d("confirmation_error",e.message ?: "")
                  }
              })
          }

           fun getDiscoverPreference(discoverPreferanceCallback: MyCallback.Companion.DiscoverPreferanceCallback){

                discoverPreferanceCallback.onDiscoverPreferancePrepare()

               val retrofit = MyConnection.myClient(ApiService::class.java,null)
               val discover = retrofit.getDiscoverPreferance()

               discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                   Observer<Response<ResponseBody>> {
                   override fun onComplete() {

                   }

                   override fun onSubscribe(d: Disposable) {

                   }

                   override fun onNext(t: Response<ResponseBody>) {
                       if (t.isSuccessful){
                           try {
                               val jsonArray = JSONArray(t.body()?.string())
                               val totalLength = jsonArray.length()
                               val discoverArray = ArrayList<DiscoverPreferanceModel>()

                               for (i in 0 until totalLength){

                                   val discoverObject = jsonArray[i] as JSONObject
                                   val province_id = discoverObject.getInt("province_id")
                                   val province_name = discoverObject.getString("province_name")
                                   val city_id = discoverObject.getInt("city_id")
                                   val city = discoverObject.getString("city")
                                   val city_desc = discoverObject.getString("city_desc")
                                   val harbors_id = discoverObject.getString("harbors_id")
                                   val harbors_name = discoverObject.getString("harbors_name")

                                          val cityPhotos = ArrayList<PhotoModel>()
                                          val cityPhotoArray = discoverObject.getJSONArray("city_photos")
                                          val cityPhotoLength = cityPhotoArray.length()

                                          for (j in 0 until cityPhotoLength ){
                                              val cityPhotoObject = cityPhotoArray[j] as JSONObject
                                              val cityPhotoOriginal = cityPhotoObject.getString("original")
                                              val cityPhotoThumbnail = cityPhotoObject.getString("thumbnail")

                                              val cityPhotoModel = PhotoModel()
                                              cityPhotoModel.original = cityPhotoOriginal
                                              cityPhotoModel.thumbnail = cityPhotoThumbnail

                                              cityPhotos.add(cityPhotoModel)
                                          }

                                          val discoverItems = ArrayList<ExperienceModel>()
                                          val discoverItemArray = discoverObject.getJSONArray("item")
                                          val discoverItemLength = discoverItemArray.length()

                                          var lengCount = 0

                                          if (discoverItemLength > 10){
                                              lengCount = 10
                                          }
                                          else{
                                              lengCount = discoverItemLength
                                          }

                                          for (k in 0 until lengCount){

                                              val discoverItemObject = discoverItemArray[k] as JSONObject
                                                    val item_id = discoverItemObject.getString("id")
                                                    val item_exp_title = discoverItemObject.getString("exp_title")
                                                    val item_rating = discoverItemObject.getDouble("rating")
                                                    val item_count_rating = discoverItemObject.getInt("count_rating")
                                                    val item_currency = discoverItemObject.getString("currency")
                                                    val item_price = discoverItemObject.getLong("price")
                                                    val item_payment_type = discoverItemObject.getString("payment_type")

                                                    val expTypeList = ArrayList<String>()
                                                    val expTypeArray = discoverItemObject.getJSONArray("exp_type")
                                                    val expTypeArrayLength = expTypeArray.length()

                                                    for (l in 0 until expTypeArrayLength) {
                                                        val type = expTypeArray.get(l) as String
                                                        expTypeList.add(type)
                                                    }

                                                    val photoModel = PhotoModel()
                                                    val itemPhotoObject = discoverItemObject.getJSONObject("cover_photo")
                                                    val original = itemPhotoObject.getString("original")
                                                    val thumbnail = itemPhotoObject.getString("thumbnail")

                                              photoModel.original = original
                                              photoModel.thumbnail = thumbnail

                                              val experienceModel = ExperienceModel()
                                              experienceModel.id = item_id
                                              experienceModel.exp_title = item_exp_title
                                              experienceModel.rating  = item_rating
                                              experienceModel.count_rating = item_count_rating
                                              experienceModel.currency = item_currency
                                              experienceModel.price = item_price
                                              experienceModel.payment_type  = item_payment_type
                                              experienceModel.exp_type = expTypeList
                                              experienceModel.cover_photo  = photoModel

                                              discoverItems.add(experienceModel)
                                          }

                                   val discoverModel = DiscoverPreferanceModel()
                                   discoverModel.province_name = province_name
                                   discoverModel.province_id = province_id
                                   discoverModel.city_id  = city_id
                                   discoverModel.city  = city
                                   discoverModel.city_desc  = city_desc
                                   discoverModel.city_photos = cityPhotos
                                   discoverModel.harbors_name = harbors_name
                                   discoverModel.harbors_id = harbors_id
                                   discoverModel.item  = discoverItems
                                   discoverArray.add(discoverModel)
                               }

                              discoverPreferanceCallback.onDiscoverPreferanceLoaded(discoverArray)
                           }
                           catch (e : Exception){
                               Log.d("discover_error",e.message ?: "")
                               discoverPreferanceCallback.onDiscoverPreferanceError()
                           }
                       }
                       else{
                           discoverPreferanceCallback.onDiscoverPreferanceError()
                           Log.d("discover_error",t.message())
                       }
                   }

                   override fun onError(e: Throwable) {
                       discoverPreferanceCallback.onDiscoverPreferanceError()
                       Log.d("discover_error",e.message ?: "")
                   }
               })
           }

        fun getExperienceSearch(harbor_id : String?, city_id : Int?,province_id : Int?, type : String?, startdate : String?,
                                 enddate : String?, guest : Int?, trip : Int?, bottomprice : Long?,
                                 upperprice : Long?, sortby : String?, payment_type : String?, booking_type : String?, page : Int?, size : Int?,loadFrom : Int,
                                 experienceSearchCallback : MyCallback.Companion.ExperienceSearchCallback){

            experienceSearchCallback.onExperienceSearchPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,null)
            val experience = retrofit.getExperienceSearch(harbor_id
                ,city_id,province_id, type,startdate,enddate, guest,trip,bottomprice,upperprice,sortby,
                "published", payment_type, booking_type, page, size)

                experience.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                    Observer<Response<ResponseBody>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Response<ResponseBody>) {
                       if (t.isSuccessful){
                            try {

                                if (loadFrom == 1){
                                    experienceList.clear()
                                }

                                val experienceJsonObject = JSONObject(t.body()?.string() ?: "")
                                val jsonArray = experienceJsonObject.getJSONArray("data")
                                val metaObject = experienceJsonObject.getJSONObject("meta")
                                val totalRecords = metaObject.getInt("total_records")
                                val jsonCount = jsonArray.length()

                                for (i in 0 until jsonCount){

                                    val experienceObject = jsonArray[i] as JSONObject
                                    val id = experienceObject.getString("id")
                                    val exp_title = experienceObject.getString("exp_title")
                                    val rating = experienceObject.getDouble("rating")
                                    val count_rating = experienceObject.getInt("count_rating")
                                    val is_certified_guide = experienceObject.getInt("is_certified_guide")
                                    val currency = experienceObject.getString("currency")
                                    val price = experienceObject.getLong("price")
                                    val special_price = experienceObject.getLong("special_price")
                                    val payment_type = experienceObject.getString("payment_type")

                                    val expTypeList = ArrayList<String>()
                                    val expTypeArray = experienceObject.getJSONArray("exp_type")
                                    val expTypeArrayLength = expTypeArray.length()

                                    val cover_photo = experienceObject.getJSONObject("cover_photo")
                                    val coverOriginal = cover_photo.getString("original")
                                    val coverThumnail = cover_photo.getString("thumbnail")

                                    val coverPhotoModel = PhotoModel()
                                    coverPhotoModel.original = coverOriginal
                                    coverPhotoModel.thumbnail = coverThumnail

                                    for (j in 0 until expTypeArrayLength) {
                                        val type = expTypeArray.get(j) as String
                                        expTypeList.add(type)
                                    }

                                    val listPhotoList = ArrayList<HashMap<String,Any>>()

                                    if (!experienceObject.isNull("list_photo")){
                                        val listPhotoArray = experienceObject.getJSONArray("list_photo")
                                        val listPhotoSize = listPhotoArray.length()


                                        for (k in 0 until listPhotoSize){
                                            val listPhotoObject = listPhotoArray[k] as JSONObject
                                            val folder = listPhotoObject.getString("folder")

                                            val expPhotoImageArray = listPhotoObject.getJSONArray("exp_photo_image")
                                            val expPhotoSize = expPhotoImageArray.length()
                                            val expPhotoList = ArrayList<PhotoModel>()

                                            for (l in 0 until expPhotoSize){
                                                val photoObject = expPhotoImageArray.get(l) as JSONObject
                                                val original = photoObject.getString("original")
                                                val thumbnail = photoObject.getString("thumbnail")

                                                val photoModel = PhotoModel()
                                                photoModel.original = original
                                                photoModel.thumbnail = thumbnail

                                                expPhotoList.add(photoModel)
                                            }

                                            val hashMap = HashMap<String,Any>()
                                            hashMap["folder"] = folder
                                            hashMap["exp_photo_image"] = expPhotoList

                                            listPhotoList.add(hashMap)
                                        }
                                    }

                                    val experienceModel = ExperienceModel()
                                    experienceModel.id = id
                                    experienceModel.exp_title = exp_title
                                    experienceModel.rating  = rating
                                    experienceModel.count_rating = count_rating
                                    experienceModel.currency = currency
                                    experienceModel.price = price
                                    experienceModel.payment_type  = payment_type
                                    experienceModel.exp_type = expTypeList
                                    experienceModel.cover_photo = coverPhotoModel
                                    experienceModel.list_photo = listPhotoList
                                    experienceModel.viewType = 0
                                    experienceModel.is_certified_guide = is_certified_guide
                                    experienceModel.special_price = special_price

                                    experienceList.add(experienceModel)
                                }

                                experienceSearchCallback.onExperienceSearchLoaded(experienceList,totalRecords)
                            }
                            catch (e : Exception){
                                experienceSearchCallback.onExperienceSearchError()
                                Log.d("experience_error",e.message ?: "")
                            }
                       }
                        else{
                           experienceSearchCallback.onExperienceSearchError()
                           Log.d("experience_error",t.message())
                       }
                    }

                    override fun onError(e: Throwable) {
                        experienceSearchCallback.onExperienceSearchError()
                        Log.d("experience_error",e.message ?: "")
                    }
                })
             }

        fun getExperienceDetail(id : String, experienceDetailCallback : MyCallback.Companion.ExperienceDetailCallback){

            experienceDetailCallback.onExperienceDetailPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,null)
            val experienceDetail = retrofit.getExperienceDetail(id)

            experienceDetail.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                    Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                @SuppressLint("SimpleDateFormat")
                override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){
                        try {
                            val jsonObject = JSONObject(t.body()?.string() ?: "")
                            val experience_id = jsonObject.getString("id")
                            val exp_title = jsonObject.getString("exp_title")
                            val exp_trip_type = jsonObject.getString("exp_trip_type")
                            val exp_booking_type = jsonObject.getString("exp_booking_type")
                            val exp_desc = jsonObject.getString("exp_desc")
                            val exp_max_guest = jsonObject.getInt("exp_max_guest")
                            val exp_pickup_place = jsonObject.getString("exp_pickup_place")
                            val exp_pickup_time  = jsonObject.getString("exp_pickup_time")
                            val exp_pickup_place_longitude  = jsonObject.getDouble("exp_pickup_place_longitude")
                            val exp_pickup_place_latitude = jsonObject.getDouble("exp_pickup_place_latitude")
                            val exp_pickup_place_maps_name = jsonObject.getString("exp_pickup_place_maps_name")
                            val status = jsonObject.getInt("status")
                            val rating = jsonObject.getDouble("rating")
                            val exp_location_latitude = jsonObject.getString("exp_location_latitude")
                            val exp_location_longitude = jsonObject.getString("exp_location_longitude")
                            val exp_location_name = jsonObject.getString("exp_location_name")
                            val exp_cover_photo = jsonObject.getString("exp_cover_photo")
                            val exp_duration = jsonObject.getInt("exp_duration")
                            val merchant_id = jsonObject.getString("merchant_id")
                            val harbors_name = jsonObject.getString("harbors_name")
                            val trading_hour_start = jsonObject.getString("trading_hour_start")
                            val trading_hour_end = jsonObject.getString("trading_hour_end")
                            val city = jsonObject.getString("city")
                            val province = jsonObject.getString("province")
                            val exp_address = jsonObject.getString("exp_address")
                            val how_to_get_location = jsonObject.getString("how_to_get_location")
                            val count_rating = jsonObject.getInt("count_rating")
                            val is_certified_guide = jsonObject.getInt("is_certified_guide")

                            var is_customised_intinerary = 0
                            var is_request_exp_pickup_place = 0

                            var is_flexible_ticket = 0
                            var exp_validity_amount = 0
                            var exp_validity_type = ""

                            var isGuideMaleExist = false
                            var isGuideFemaleExist = false

                            if (!jsonObject.isNull("is_customised_intinerary")){
                                is_customised_intinerary = jsonObject.getInt("is_customised_intinerary")
                            }

                            if (!jsonObject.isNull("is_request_exp_pickup_place")){
                                is_request_exp_pickup_place = jsonObject.getInt("is_request_exp_pickup_place")
                            }

                            if (!jsonObject.isNull("is_flexible_ticket")){

                                if (!jsonObject.isNull("is_flexible_ticket")){
                                    is_flexible_ticket = jsonObject.getInt("is_flexible_ticket")
                                }

                                if (!jsonObject.isNull("exp_validity_amount")){
                                    exp_validity_amount = jsonObject.getInt("exp_validity_amount")
                                }

                                exp_validity_type = jsonObject.getString("exp_validity_type")
                            }

                            val expTypeList = ArrayList<String>()

                            if (!jsonObject.isNull("exp_type")){
                                val expTypeArray = jsonObject.getJSONArray("exp_type")
                                val expTypeSize = expTypeArray.length()

                                for (i in 0 until expTypeSize) {
                                    val expType = expTypeArray[i] as String
                                    expTypeList.add(expType)
                                }
                            }

                            val expItenaryItemList = ArrayList<ItemItenaryModel>()

                            if (!jsonObject.isNull("exp_internary")){
                                val expItenaryObject = jsonObject.getJSONObject("exp_internary")

                                if (!expItenaryObject.isNull("item")){

                                    val expItenaryItemArray = expItenaryObject.getJSONArray("item")
                                    val expItenaryItemSize = expItenaryItemArray.length()

                                    for (j in 0 until expItenaryItemSize){

                                        val itemObject = expItenaryItemArray.get(j) as JSONObject
                                        val day = itemObject.getInt("day")

                                        val itenaryArray = itemObject.getJSONArray("itinerary")
                                        val itenarySize = itenaryArray.length()
                                        val itenaryList = ArrayList<ItenaryModel>()

                                        for (k in 0 until itenarySize){
                                            val itenaryObject = itenaryArray.get(k) as JSONObject
                                            val time = itenaryObject.getString("time")
                                            val activity = itenaryObject.getString("activity")

                                            val itenaryModel = ItenaryModel()
                                            itenaryModel.time = time
                                            itenaryModel.activity = activity

                                            itenaryList.add(itenaryModel)
                                        }

                                        val itemItenaryModel = ItemItenaryModel()
                                        itemItenaryModel.day = day
                                        itemItenaryModel.itenary = itenaryList

                                        expItenaryItemList.add(itemItenaryModel)
                                    }
                                }
                            }

                            val expFacilityList = ArrayList<FacilityModel>()

                            if (!jsonObject.isNull("exp_facilities")){
                                val expFacilityArray = jsonObject.getJSONArray("exp_facilities")
                                val expFacilitySize = expFacilityArray.length()

                                for (l in 0 until expFacilitySize) {
                                    val facilityObject = expFacilityArray.get(l) as JSONObject
                                    val name = facilityObject.getString("name")
                                    val icon = facilityObject.getString("icon")
                                    val amount = facilityObject.getInt("amount")

                                    val facilityModel = FacilityModel()
                                    facilityModel.name = name
                                    facilityModel.icon = icon
                                    facilityModel.amount = amount

                                    expFacilityList.add(facilityModel)
                                }
                            }

                            val expInclusionList = ArrayList<InclusionModel>()
                            val expExclusionList = ArrayList<InclusionModel>()

                            if (!jsonObject.isNull("exp_inclusion")){
                                val expInclusionArray = jsonObject.getJSONArray("exp_inclusion")
                                val expInclusionSize = expInclusionArray.length()

                                for (m in 0 until expInclusionSize) {
                                    val facilityObject = expInclusionArray.get(m) as JSONObject
                                    val name = facilityObject.getString("name")
                                    val type = facilityObject.getInt("type")

                                    val inclusionModel = InclusionModel()
                                    inclusionModel.name = name
                                    inclusionModel.type = type

                                    if (type == 0){
                                        expInclusionList.add(inclusionModel)
                                    }
                                    else{
                                        expExclusionList.add(inclusionModel)
                                    }
                                }
                            }

                            val expRulesList = ArrayList<RulesModel>()

                            if (!jsonObject.isNull("exp_rules")){
                                val expRulesArray = jsonObject.getJSONArray("exp_rules")
                                val expRulesSize = expRulesArray.length()

                                for (n in 0 until expRulesSize){
                                    val rulesObject = expRulesArray[n] as JSONObject
                                    val name  = rulesObject.getString("name")
                                    val desc = rulesObject.getString("desc")

                                    val rulesModel = RulesModel()
                                    rulesModel.name = name
                                    rulesModel.desc = desc

                                    expRulesList.add(rulesModel)
                                }
                            }

                            val avaibilityLocaleDate = ArrayList<LocalDate>()

                            if (!jsonObject.isNull("exp_availability")){
                                val avaibilityArray = jsonObject.getJSONArray("exp_availability")
                                val avaibilityArraySize = avaibilityArray.length()

                                for (o in 0 until avaibilityArraySize) {

                                    val avaibilityObject = avaibilityArray.get(o) as JSONObject
                                    val year = avaibilityObject.getInt("year")
                                    val month = avaibilityObject.getString("month")

                                    val dateList = ArrayList<String>()

                                    if (!avaibilityObject.isNull("date")){
                                        val dateArray = avaibilityObject.getJSONArray("date")
                                        val dateArraySize = dateArray.length()

                                        for (p in 0 until dateArraySize){
                                            val date = dateArray[p] as String
                                            val sdf = SimpleDateFormat("yyyy-MM-dd")
                                            val dates = sdf.parse(date) ?: Date()
                                            val instant = Instant.ofEpochMilli(dates.time)
                                            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                                            val localDate = localDateTime.toLocalDate()
                                            avaibilityLocaleDate.add(localDate)
                                            dateList.add(date)
                                        }
                                    }

                                    val avaibilityModel = AvaibilityModel()
                                    avaibilityModel.year = year
                                    avaibilityModel.month = month
                                    avaibilityModel.date = dateList
                                }
                            }

                            val expPaymentList = ArrayList<PaymentModel>()

                            if (!jsonObject.isNull("exp_payment")){
                                val expPaymentArray = jsonObject.getJSONArray("exp_payment")
                                val expPaymentSize = expPaymentArray.length()

                                for (q in 0 until expPaymentSize ){
                                    val paymentObject = expPaymentArray.get(q) as JSONObject
                                    val paymentId  = paymentObject.getString("id")
                                    val currency = paymentObject.getString("currency")
                                    val price = paymentObject.getLong("price")
                                    val price_item_type = paymentObject.getString("price_item_type")
                                    val payment_type_id = paymentObject.getString("payment_type_id")
                                    val payment_type_name = paymentObject.getString("payment_type_name")
                                    val payment_type_desc = paymentObject.getString("payment_type_desc")

                                    val paymentModel = PaymentModel()
                                    paymentModel.id  = paymentId
                                    paymentModel.currency  = currency
                                    paymentModel.price  = price
                                    paymentModel.price_item_type  = price_item_type
                                    paymentModel.payment_type_id  = payment_type_id
                                    paymentModel.payment_type_name = payment_type_name
                                    paymentModel.payment_type_desc = payment_type_desc

                                    expPaymentList.add(paymentModel)
                                }
                            }

                            val listPhotoList = ArrayList<HashMap<String,Any>>()

                            if (!jsonObject.isNull("exp_photos")){
                                val listPhotoArray = jsonObject.getJSONArray("exp_photos")
                                val listPhotoSize = listPhotoArray.length()

                                for (r in 0 until listPhotoSize){
                                    val listPhotoObject = listPhotoArray[r] as JSONObject
                                    val folder = listPhotoObject.getString("folder")

                                    val expPhotoImageArray = listPhotoObject.getJSONArray("exp_photo_image")
                                    val expPhotoSize = expPhotoImageArray.length()
                                    val expPhotoList = ArrayList<PhotoModel>()

                                    for (s in 0 until expPhotoSize){
                                        val photoObject = expPhotoImageArray.get(s) as JSONObject
                                        val original = photoObject.getString("original")
                                        val thumbnail = photoObject.getString("thumbnail")

                                        val photoModel = PhotoModel()
                                        photoModel.original = original
                                        photoModel.thumbnail = thumbnail
                                        expPhotoList.add(photoModel)
                                    }

                                    val hashMap = HashMap<String,Any>()
                                    hashMap["folder"] = folder
                                    hashMap["exp_photo_image"] = expPhotoList

                                    listPhotoList.add(hashMap)
                                }
                            }

                            val  minimumBookingObject = jsonObject.getJSONObject("minimum_booking")
                            val  minimum_booking_desc  = minimumBookingObject.getString("minimum_booking_desc")
                            val  minimum_booking_amount = minimumBookingObject.getInt("minimum_booking_amount")

                            val minimumBookingModel = MinimumBookingModel()
                            minimumBookingModel.minimum_booking_desc = minimum_booking_desc
                            minimumBookingModel.minimum_booking_amount = minimum_booking_amount

                            val guideList = ArrayList<HashMap<String,Any>>()
                            val guideArrayList = jsonObject.getJSONArray("guide")
                            val guideSize = guideArrayList.length()

                            for (r in 0 until guideSize){
                                val guideObject = guideArrayList[r] as JSONObject
                                val guide_id = guideObject.getString("id")
                                val guide_name = guideObject.getString("name")
                                val guide_bio = guideObject.getString("bio")
                                val guide_desc = guideObject.getString("desc")
                                val guide_photo = guideObject.getString("photo")
                                val guide_rating = guideObject.getDouble("rating")
                                val guide_certified = guideObject.getInt("is_certified")
                                val guide_licence = guideObject.getString("license")
                                val guide_available = guideObject.getBoolean("is_available")

                                val genderObject = guideObject.getJSONObject("gender")
                                val gender_id = genderObject.getInt("id")
                                val gender_value = genderObject.getString("value")

                                val genderLanguages = ArrayList<HashMap<String,Any>>()
                                val genderLanguageArray = guideObject.getJSONArray("language")
                                val genderLanguageArraySize = genderLanguageArray.length()

                                if (gender_value == "Male"){
                                    isGuideMaleExist = true
                                }
                                else{
                                    isGuideFemaleExist = true
                                }

                                for (s in 0 until genderLanguageArraySize){
                                    val genderLanguageObject = genderLanguageArray[s] as JSONObject
                                    val genderLanguageId = genderLanguageObject.getInt("id")
                                    val genderLanguageName = genderLanguageObject.getString("name")

                                    val genderLanguageMap = HashMap<String,Any>()
                                    genderLanguageMap["genderLanguageId"] = genderLanguageId
                                    genderLanguageMap["genderLanguageName"] = genderLanguageName

                                    genderLanguages.add(genderLanguageMap)
                                }

                                val guideMap = HashMap<String,Any>()
                                guideMap["guide_id"] = guide_id
                                guideMap["guide_name"] = guide_name
                                guideMap["guide_bio"] = guide_bio
                                guideMap["guide_desc"] = guide_desc
                                guideMap["guide_photo"] = guide_photo
                                guideMap["guide_rating"] = guide_rating
                                guideMap["guide_certified"] = guide_certified
                                guideMap["guide_licence"] = guide_licence
                                guideMap["guide_available"] = guide_available
                                guideMap["gender_id"] = gender_id
                                guideMap["gender_value"] = gender_value
                                guideMap["genderLanguages"] = genderLanguages

                                guideList.add(guideMap)
                            }

                            val languagesArray = jsonObject.getJSONArray("languages")
                            val languagesSize = languagesArray.length()
                            val languagesList = ArrayList<HashMap<String,Any>>()

                            for (i in 0 until languagesSize){
                                val languagesObject = languagesArray[i] as JSONObject
                                val languageId = languagesObject.getInt("id")
                                val languageName = languagesObject.getString("name")

                                val languageMap = HashMap<String,Any>()
                                languageMap["languageId"] = languageId
                                languageMap["languageName"] = languageName

                                languagesList.add(languageMap)
                            }

                            val accomodationsArray = jsonObject.getJSONArray("accomodations")
                            val accomodationsSize = accomodationsArray.length()
                            val accomodationsList = ArrayList<HashMap<String,Any>>()

                            for (i in 0 until accomodationsSize){
                                val accomodationsObject = accomodationsArray[i] as JSONObject
                                val accomodationsId = accomodationsObject.getInt("id")
                                val accomodationsName = accomodationsObject.getString("name")

                                val accomodationsMap = HashMap<String,Any>()
                                accomodationsMap["accomodationsId"] = accomodationsId
                                accomodationsMap["accomodationsName"] = accomodationsName

                                accomodationsList.add(accomodationsMap)
                            }

                            var startPoint = ""
                            var endPoint = ""

                            if (!jsonObject.isNull("exp_intinerary_start_end_point")){
                                val startEndObject = jsonObject.getJSONObject("exp_intinerary_start_end_point")
                                startPoint = startEndObject.getString("start")
                                endPoint = startEndObject.getString("end")
                            }

                            val packageArray = jsonObject.getJSONArray("package")
                            val packageArraySize = packageArray.length()
                            val packageList = ArrayList<HashMap<String,Any>>()

                            for (i in 0 until packageArraySize){
                                val packageObject = packageArray[i] as JSONObject
                                val packageId = packageObject.getInt("id")
                                val packageValue = packageObject.getInt("value")
                                val packageName = packageObject.getString("name")
                                val packageDetails = packageObject.getString("details")
                                val packageDesc = packageObject.getString("desc")
                                val packagePrice = packageObject.getLong("price")
                                val packageCurrency = packageObject.getString("currency")
                                val packageTypePayment = packageObject.getString("type_payment")

                                var isDownpaymentAvailable = false

                                val packageImagesList = ArrayList<String>()

                                if (!packageObject.isNull("images")){
                                    val packageImagesArray = packageObject.getJSONArray("images")
                                    val packageImagesArraySize = packageImagesArray.length()

                                    for(j in 0 until packageImagesArraySize){
                                        val strPackageImages = packageImagesArray[j] as String
                                        packageImagesList.add(strPackageImages)
                                    }
                                }

                                val packageImageList = ArrayList<PhotoModel>()

                                if (!packageObject.isNull("package_image")){
                                    val packageImageArray = packageObject.getJSONArray("package_image")
                                    val packageImageArraySize = packageImageArray.length()

                                    for(k in 0 until packageImageArraySize){
                                        val packageImageObject = packageImageArray[k] as JSONObject
                                        val package_image_original = packageImageObject.getString("original")
                                        val package_image_thumbnail = packageImageObject.getString("thumbnail")

                                        val photoModel = PhotoModel()
                                        photoModel.original = package_image_original
                                        photoModel.thumbnail = package_image_thumbnail

                                        packageImageList.add(photoModel)
                                    }
                                }

                                val pakageExpPayment = ArrayList<HashMap<String,Any>>()

                                if (!packageObject.isNull("exp_payment")){

                                    val packagePaymentArray = packageObject.getJSONArray("exp_payment")
                                    val packagePaymentArraySize = packagePaymentArray.length()

                                    for (l in 0 until packagePaymentArraySize){

                                        val packagePaymentObject = packagePaymentArray[l] as JSONObject
                                        val packagePaymentId = packagePaymentObject.getString("id")
                                        val packageId1 = packagePaymentObject.getInt("package_id")
                                        val packagePaymentCurrency = packagePaymentObject.getString("currency")
                                        val packagePaymentPrice = packagePaymentObject.getLong("price")
                                        val packagePriceItemType = packagePaymentObject.getString("price_item_type")
                                        val packagePaymentTypeId = packagePaymentObject.getString("payment_type_id")
                                        val packagePaymentTypeName = packagePaymentObject.getString("payment_type_name")
                                        val packagePaymentTypeDesc = packagePaymentObject.getString("payment_type_desc")

                                        var paymentPerecentage = 0

                                        if (!packagePaymentObject.isNull("payment_precentage")){
                                            paymentPerecentage = packagePaymentObject.getInt("payment_precentage")
                                        }

                                        val packagePaymentCustomPriceList = ArrayList<HashMap<String,Any>>()

                                        if (!packagePaymentObject.isNull("custom_price")){
                                            val packagePaymentCustomPriceArray = packagePaymentObject.getJSONArray("custom_price")
                                            val packagePaymentCustomPriceArraySize = packagePaymentCustomPriceArray.length()

                                            for (m in 0 until packagePaymentCustomPriceArraySize){
                                                val packagePaymentCustomPriceObject = packagePaymentCustomPriceArray[m] as JSONObject
                                                val customPriceCurrency = packagePaymentCustomPriceObject.getString("currency")
                                                val customPricePrice = packagePaymentCustomPriceObject.getLong("price")
                                                val customPriceGuest = packagePaymentCustomPriceObject.getInt("guest")

                                                val customPriceMap = HashMap<String,Any>()
                                                customPriceMap["customPriceCurrency"] = customPriceCurrency
                                                customPriceMap["customPricePrice"] = customPricePrice
                                                customPriceMap["customPriceGuest"] = customPriceGuest

                                                packagePaymentCustomPriceList.add(customPriceMap)
                                            }
                                        }

                                        val specialPriceList = ArrayList<HashMap<String,Any>>()

                                        if (!packagePaymentObject.isNull("special_prices")){
                                            val specialPriceArray = packagePaymentObject.getJSONArray("special_prices")
                                            val specialPriceArraySize = specialPriceArray.length()

                                            for (n in 0 until specialPriceArraySize){
                                                val specialPriceObject = specialPriceArray[n] as JSONObject
                                                val specialPriceId = specialPriceObject.getString("id")
                                                val specialPricePrice = specialPriceObject.getLong("special_price")
                                                val specialPriceCurrency = specialPriceObject.getString("currency")
                                                val specialPriceStartDate = specialPriceObject.getString("start_date")
                                                val specialPriceEndDate = specialPriceObject.getString("end_date")

                                                val specialPriceMap = HashMap<String,Any>()
                                                specialPriceMap["specialPriceId"] = specialPriceId
                                                specialPriceMap["specialPricePrice"] = specialPricePrice
                                                specialPriceMap["specialPriceCurrency"] = specialPriceCurrency
                                                specialPriceMap["specialPriceStartDate"] = specialPriceStartDate
                                                specialPriceMap["specialPriceEndDate"] = specialPriceEndDate

                                                specialPriceList.add(specialPriceMap)
                                            }
                                        }

                                        if (packagePaymentTypeName == "Down Payment"){
                                            isDownpaymentAvailable = true
                                        }

                                        val packagePaymentHashMap = HashMap<String,Any>()
                                        packagePaymentHashMap["packagePaymentId"] = packagePaymentId
                                        packagePaymentHashMap["packageId"] = packageId1
                                        packagePaymentHashMap["packagePaymentCurrency"] = packagePaymentCurrency
                                        packagePaymentHashMap["packagePaymentPrice"] = packagePaymentPrice
                                        packagePaymentHashMap["packagePriceItemType"] = packagePriceItemType
                                        packagePaymentHashMap["packagePaymentTypeId"] = packagePaymentTypeId
                                        packagePaymentHashMap["packagePaymentTypeName"] = packagePaymentTypeName
                                        packagePaymentHashMap["packagePaymentTypeDesc"] = packagePaymentTypeDesc
                                        packagePaymentHashMap["packagePaymentCustomPriceList"] = packagePaymentCustomPriceList
                                        packagePaymentHashMap["paymentPrecentage"] = paymentPerecentage
                                        packagePaymentHashMap["specialPriceList"] = specialPriceList

                                        pakageExpPayment.add(packagePaymentHashMap)
                                    }
                                }

                                val packageAvaibility = ArrayList<LocalDate>()

                                if (!packageObject.isNull("exp_availability")){

                                    val avaibilityArray = packageObject.getJSONArray("exp_availability")
                                    val avaibilityArraySize = avaibilityArray.length()

                                        for (o in 0 until avaibilityArraySize) {

                                            val avaibilityObject = avaibilityArray.get(o) as JSONObject
                                            val year = avaibilityObject.getInt("year")
                                            val month = avaibilityObject.getString("month")

                                            val dateList = ArrayList<String>()

                                            if (!avaibilityObject.isNull("date")){
                                                val dateArray = avaibilityObject.getJSONArray("date")
                                                val dateArraySize = dateArray.length()

                                                for (p in 0 until dateArraySize){
                                                    val date = dateArray[p] as String
                                                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                                                    val dates = sdf.parse(date) ?: Date()
                                                    val instant = Instant.ofEpochMilli(dates.time)
                                                    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                                                    val localDate = localDateTime.toLocalDate()

                                                    packageAvaibility.add(localDate)
                                                    dateList.add(date)
                                                }
                                            }

                                            val avaibilityModel = AvaibilityModel()
                                            avaibilityModel.year = year
                                            avaibilityModel.month = month
                                            avaibilityModel.date = dateList
                                        }
                                     }

                                val packageMap = HashMap<String,Any>()
                                packageMap["packageId"] = packageId
                                packageMap["packageValue"] = packageValue
                                packageMap["packageName"] = packageName
                                packageMap["packageDetails"] = packageDetails
                                packageMap["packageDesc"] = packageDesc
                                packageMap["packagePrice"] = packagePrice
                                packageMap["packageCurrency"] = packageCurrency
                                packageMap["packageTypePayment"] = packageTypePayment
                                packageMap["packageImagesList"] = packageImagesList
                                packageMap["packageImageList"] = packageImageList
                                packageMap["packageExpPayment"] = pakageExpPayment
                                packageMap["packageAvaibility"] = packageAvaibility
                                packageMap["isDownpaymentAvailable"] = isDownpaymentAvailable

                                packageList.add(packageMap)
                            }

                            val experienceDetailModel = ExperienceDetailModel()
                            experienceDetailModel.experience_id  = experience_id
                            experienceDetailModel.exp_title  = exp_title
                            experienceDetailModel.exp_trip_type = exp_trip_type
                            experienceDetailModel.exp_booking_type  = exp_booking_type
                            experienceDetailModel.exp_desc = exp_desc
                            experienceDetailModel.exp_max_guest  = exp_max_guest
                            experienceDetailModel.exp_pickup_place = exp_pickup_place
                            experienceDetailModel.exp_pickup_time   = exp_pickup_time
                            experienceDetailModel.exp_pickup_place_longitude  = exp_pickup_place_longitude
                            experienceDetailModel.exp_pickup_place_latitude = exp_pickup_place_latitude
                            experienceDetailModel.exp_pickup_place_maps_name = exp_pickup_place_maps_name
                            experienceDetailModel.status = status
                            experienceDetailModel.rating = rating
                            experienceDetailModel.is_request_exp_pickup_place = is_request_exp_pickup_place
                            experienceDetailModel.how_to_get_location = how_to_get_location
                            experienceDetailModel.is_certified_guide = is_certified_guide
                            experienceDetailModel.exp_address = exp_address
                            experienceDetailModel.exp_location_latitude  = exp_location_latitude
                            experienceDetailModel.exp_location_longitude = exp_location_longitude
                            experienceDetailModel.exp_location_name = exp_location_name
                            experienceDetailModel.exp_cover_photo = exp_cover_photo
                            experienceDetailModel.exp_duration = exp_duration
                            experienceDetailModel.merchant_id = merchant_id
                            experienceDetailModel.harbors_name = harbors_name
                            experienceDetailModel.trading_hour_start = trading_hour_start
                            experienceDetailModel.trading_hour_end = trading_hour_end
                            experienceDetailModel.city = city
                            experienceDetailModel.is_customised_intinerary = is_customised_intinerary
                            experienceDetailModel.start_point = startPoint
                            experienceDetailModel.end_point = endPoint
                            experienceDetailModel.province = province
                            experienceDetailModel.exp_type = expTypeList
                            experienceDetailModel.exp_itenerary   = expItenaryItemList
                            experienceDetailModel.exp_facility  = expFacilityList
                            experienceDetailModel.exp_inclusion  = expInclusionList
                            experienceDetailModel.exp_exclusion  = expExclusionList
                            experienceDetailModel.exp_rules  = expRulesList
                            experienceDetailModel.exp_avaibility  = avaibilityLocaleDate
                            experienceDetailModel.exp_payment  = expPaymentList
                            experienceDetailModel.exp_photos  = listPhotoList
                            experienceDetailModel.exp_guides = guideList
                            experienceDetailModel.exp_packages = packageList
                            experienceDetailModel.exp_languages = languagesList
                            experienceDetailModel.exp_accomodations = accomodationsList
                            experienceDetailModel.minimum_booking  = minimumBookingModel
                            experienceDetailModel.count_rating = count_rating
                            experienceDetailModel.is_flexible_ticket = is_flexible_ticket
                            experienceDetailModel.exp_validity_amount = exp_validity_amount
                            experienceDetailModel.exp_validity_type = exp_validity_type
                            experienceDetailModel.isGuideMaleExist = isGuideMaleExist
                            experienceDetailModel.isGuideFemaleExist = isGuideFemaleExist

                            experienceDetailCallback.onExperienceDetailLoaded(experienceDetailModel)
                        }
                        catch (e : Exception){
                            experienceDetailCallback.onExperienceDetailError()
                            Log.d("experience_detail_error",e.message ?: "")
                            e.printStackTrace()
                        }
                    }
                    else{
                        experienceDetailCallback.onExperienceDetailError()
                        Log.d("experience_detail_error",t.message())
                    }
                }

                override fun onError(e: Throwable) {
                    experienceDetailCallback.onExperienceDetailError()
                    Log.d("experience_detail_error",e.message ?: "")
                    e.printStackTrace()
                }

                })
            }

            fun loadExperienceReview(id : String, experienceReviewCallback : MyCallback.Companion.ExperienceReviewCallback){

                experienceReviewCallback.onExperienceReviewPrepare()

                val retrofit = MyConnection.myClient(ApiService::class.java,null)
                val experienceReview = retrofit.getExperienceReview(id)

                experienceReview.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                        Observer<Response<ResponseBody>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Response<ResponseBody>) {
                        if (t.isSuccessful){
                            try {

                                val jsonObject = JSONObject(t.body()?.string() ?: "")

                                if (!jsonObject.isNull("data")){
                                    val jsonArray = jsonObject.getJSONArray("data")
                                    val jsonSize = jsonArray.length()
                                    val reviewList = ArrayList<ReviewModel>()

                                    for (i in 0 until jsonSize){
                                        val reviewObject = jsonArray[i] as JSONObject
                                        val name = reviewObject.getString("name")
                                        val desc = reviewObject.getString("desc")
                                        val values = reviewObject.getDouble("values")

                                        val reviewModel = ReviewModel()
                                        reviewModel.name = name
                                        reviewModel.desc = desc
                                        reviewModel.values = values

                                        reviewList.add(reviewModel)
                                    }

                                    experienceReviewCallback.onExperienceReviewLoaded(reviewList)
                                }
                            }
                            catch (e : Exception){
                                experienceReviewCallback.onExperienceReviewError()
                                Log.d("experience_review_error",e.message ?: "")
                            }
                        }
                        else{
                            experienceReviewCallback.onExperienceReviewError()
                            Log.d("experience_review_error",t.message())
                        }
                    }

                    override fun onError(e: Throwable) {
                        experienceReviewCallback.onExperienceReviewError()
                        Log.d("experience_review_error",e.message ?: "")
                    }
                })
            }

        fun getAddOn(experience_id : String, currentPrice : Long, addOnCallback : MyCallback.Companion.AddOnCallback){

            addOnCallback.onAddOnPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,null)
            val discover = retrofit.getAddOn(experience_id)

            discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                    Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){

                        val addOnList = ArrayList<AddOnModel>()

                        try {

                            val jsonArray = JSONArray(t.body()?.string())
                            val jsonArraySize = jsonArray.length()

                            for (i in 0 until jsonArraySize){
                                val jsonObject = jsonArray[i] as JSONObject

                                val id  = jsonObject.getString("id")
                                val name  = jsonObject.getString("name")
                                val desc   = jsonObject.getString("desc")
                                val currency = jsonObject.getString("currency")
                                val amount  = jsonObject.getLong("amount")
                                val exp_id = jsonObject.getString("exp_id")

                                val addOnModel = AddOnModel()
                                addOnModel.id = id
                                addOnModel.name = name
                                addOnModel.desc = desc
                                addOnModel.currency = currency
                                addOnModel.amount = amount
                                addOnModel.total_price = amount + currentPrice
                                addOnModel.exp_id = exp_id

                                addOnList.add(addOnModel)
                            }

                            addOnCallback.onAddOnSuccess(addOnList)
                        }
                        catch (e : Exception){
                            addOnCallback.onAddOnSuccess(addOnList)
                            Log.d("add_on_error",e.message ?: "")
                        }
                    }
                    else{
                        addOnCallback.onAddOnError()
                        Log.d("add_on_error",t.message())
                    }
                }

                override fun onError(e: Throwable) {
                    addOnCallback.onAddOnError()
                   Log.d("add_on_error",e.message ?: "")
                }
            })
        }

        fun getPaymentMethod(paymentMethodCallback : MyCallback.Companion.PaymentMethodCallback) {

            paymentMethodCallback.onPaymentMethodPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,null)
            val discover = retrofit.getPaymentMethod()

            discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                    Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){
                        try {

                            val jsonArray = JSONArray(t.body()?.string())
                            val jsonArraySize = jsonArray.length()

                            val bankMethodModelList = ArrayList<PaymentMethodModel>()
                            val creditCardMethodModelList = ArrayList<PaymentMethodModel>()
                            val paypalCardMethodModelList = ArrayList<PaymentMethodModel>()

                            for (i in 0 until jsonArraySize){

                                val jsonObject = jsonArray[i] as JSONObject
                                val id = jsonObject.getString("id")
                                val name = jsonObject.getString("name")
                                val type = jsonObject.getInt("type")
                                val desc = jsonObject.getString("desc")
                                val icon = jsonObject.getString("icon")

                                val paymentMethodModel = PaymentMethodModel()
                                paymentMethodModel.id  = id
                                paymentMethodModel.name = name
                                paymentMethodModel.type  = type
                                paymentMethodModel.desc = desc
                                paymentMethodModel.icon = icon

                                if (type == 1){
                                    bankMethodModelList.add(paymentMethodModel)
                                }
                                else if (type == 2){
                                    creditCardMethodModelList.add(paymentMethodModel)
                                }
                                else{
                                    paypalCardMethodModelList.add(paymentMethodModel)
                                }

                                paymentMethodCallback.onPaymentMethodSuccess(bankMethodModelList,
                                    creditCardMethodModelList,paypalCardMethodModelList)

                            }
                        }
                        catch (e : Exception){
                            Log.d("payment_method_error",e.message ?: "")
                            Log.d("payment_method_error",e.message ?: "")
                        }
                    }
                    else{
                        paymentMethodCallback.onPaymentMethodError(t.message())
                        Log.d("payment_method_error",t.message())
                    }
                }

                override fun onError(e: Throwable) {
                    paymentMethodCallback.onPaymentMethodError(e.message ?: "")
                    Log.d("payment_method_error",e.message ?: "")
                }
            })
        }

        fun createReview(token : String, body : HashMap<String,Any>, createReviewCallback :
        MyCallback.Companion.CreateReviewCallback){

            createReviewCallback.onCreateReviewPrepare()

            val retrofit = MyConnection.myClient(ApiService::class.java,token)
            val discover = retrofit.postCreateReview(body)

            discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){
                        createReviewCallback.onCreateReviewSuccess()
                    }
                    else{
                        val code = t.code()
                        createReviewCallback.onCreateReviewError(t.message() ?: "",code)
                        Log.d("review_error",t.message() ?: "")
                    }
                }

                override fun onError(e: Throwable) {
                    createReviewCallback.onCreateReviewError(e.message ?: "",0)
                    Log.d("review_error",e.message ?: "")
                }
            })
        }
    }


