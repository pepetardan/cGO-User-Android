package id.dtech.cgo.Controller

import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Model.FacilityModel
import id.dtech.cgo.Model.TimeOptionModel
import id.dtech.cgo.Model.TransportationModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception

class TransportationController {

    private val transportList = ArrayList<TransportationModel>()
    private val returnTransportList = ArrayList<TransportationModel>()

    fun getTransportationSearch(body : HashMap<String,Any?>,from : Int,type : Int, transportationSearchCallback :
    MyCallback.Companion.TransportationSearchCallback){

        transportationSearchCallback.onTransportationSearchPrepare()

        val page  = body["page"] as Int?
        val size = body["size"] as Int?
        val harbor_source_id = body["harbor_source_id"] as String?
        val harbor_dest_id = body["harbor_dest_id"] as String?
        val guest= body["guest"] as Int?
        val departure_date  = body["departure_date"] as String?
        val transportClass  = body["transportClass"] as String?
        val isReturn = body["isReturn"] as Int?
        val notReturn = body["not_return"] as Boolean?
        val dep_timeoption_id = body["dep_timeoption_id"] as Int?
        val arr_timeoption_id = body["arr_timeoption_id"] as Int?
        val sortBy = body["sortBy"] as String?
        val return_trans_id = body["return_trans_id"] as String?

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getTransportationSearch(page,size,
        harbor_source_id,harbor_dest_id,guest,departure_date,transportClass,isReturn,notReturn,dep_timeoption_id,
            arr_timeoption_id,sortBy,return_trans_id)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe(
            object : Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    try {

                        if (from == 1){
                            if (type == 0){
                                transportList.clear()
                            }
                            else{
                                returnTransportList.clear()
                            }
                        }

                        val metaMap = HashMap<String,Any>()
                        val jsonObject = JSONObject(t.body()?.string() ?: "")
                        val jsonArray = jsonObject.getJSONArray("data")
                        val jsonArraySize = jsonArray.length()

                        for (i in 0 until jsonArraySize){
                            val transportationObject = jsonArray[i] as JSONObject
                            val transport_schedule_id  = transportationObject.getString("schedule_id")
                            val transport_departure_date = transportationObject.getString("departure_date")
                            val transport_departure_time = transportationObject.getString("departure_time")
                            val transport_arrival_time = transportationObject.getString("arrival_time")
                            val transport_trip_duration = transportationObject.getString("trip_duration")
                            val transport_transportation_id = transportationObject.getString("transportation_id")
                            val transport_transportation_name = transportationObject.getString("transportation_name")
                            val transport_transportation_status = transportationObject.getString("transportation_status")
                            val transport_harbor_source_id = transportationObject.getString("harbor_source_name")
                            val transport_harbor_source_name = transportationObject.getString("harbor_source_name")
                            val transport_harbor_destination_id = transportationObject.getString("harbor_destination_id")
                            val transport_harbor_destination_name = transportationObject.getString("harbor_destination_name")

                            val transport_city_source_id = transportationObject.getInt("city_source_id")
                            val transport_city_source_name = transportationObject.getString("city_source_name")
                            val transport_city_dest_id = transportationObject.getInt("city_dest_id")
                            val transport_city_dest_name = transportationObject.getString("city_dest_name")

                            val transport_province_source_id = transportationObject.getInt("province_source_id")
                            val transport_province_source_name = transportationObject.getString("province_source_name")
                            val transport_province_dest_id = transportationObject.getInt("province_dest_id")
                            val transport_province_dest_name = transportationObject.getString("province_dest_name")

                            val transport_merchant_name = transportationObject.getString("merchant_name")
                            val transport_merchant_picture= transportationObject.getString("merchant_picture")
                            val transport_class = transportationObject.getString("class")

                            val return_trans_id = transportationObject.getString("return_trans_id")

                            val transImageList = ArrayList<HashMap<String,Any>>()

                            if (!transportationObject.isNull("transportation_images")){
                                val transportationImageArray = transportationObject.getJSONArray(
                                    "transportation_images")
                                val transImageSize = transportationImageArray.length()

                                for (j in 0 until transImageSize){
                                    val transportImageObject = transportationImageArray[j] as JSONObject
                                    val original = transportImageObject.getString("original")
                                    val thumbnail = transportImageObject.getString("thumbnail")

                                    val imageMap = HashMap<String,Any>()
                                    imageMap["original"] = original
                                    imageMap["thumbnail"] = thumbnail

                                    transImageList.add(imageMap)
                                }
                            }

                            val trsFacilityList = ArrayList<FacilityModel>()

                            if (!transportationObject.isNull("trans_facilities")){
                                val trsFacilityArray = transportationObject.getJSONArray(
                                    "trans_facilities")
                                val trsFacilitySize = trsFacilityArray.length()

                                for (l in 0 until trsFacilitySize) {
                                    val facilityObject = trsFacilityArray.get(l) as JSONObject
                                    val name = facilityObject.getString("name")
                                    val icon = facilityObject.getString("icon")
                                    val amount = facilityObject.getInt("amount")

                                    val facilityModel = FacilityModel()
                                    facilityModel.name = name
                                    facilityModel.icon = icon
                                    facilityModel.amount = amount

                                    trsFacilityList.add(facilityModel)
                                }
                            }

                            val priceObject = transportationObject.getJSONObject("price")
                            val adult_price = priceObject.getLong("adult_price")
                            val children_price = priceObject.getLong("children_price")
                            val currency = priceObject.getLong("currency")
                            val currency_label = priceObject.getString("currency_label")
                            val price_type = priceObject.getString("price_type")

                            val priceMap = HashMap<String,Any>()
                            priceMap["adult_price"]  = adult_price
                            priceMap["children_price"] = children_price
                            priceMap["currency"] = currency
                            priceMap["currency_label"] = currency_label
                            priceMap["price_type"] = price_type

                            val boatDetailMap = HashMap<String,Any>()
                            val transportBoatDetailObject = transportationObject.getJSONObject(
                                "boat_specification")
                            val length = transportBoatDetailObject.getDouble("length")
                            val width = transportBoatDetailObject.getDouble("width")
                            val machine = transportBoatDetailObject.getString("machine")
                            val cabin = transportBoatDetailObject.getInt("cabin")

                            boatDetailMap["length"] = length
                            boatDetailMap["width"] = width
                            boatDetailMap["machine"] = machine
                            boatDetailMap["cabin"] = cabin

                            val transportationModel = TransportationModel()
                            transportationModel.schedule_id = transport_schedule_id
                            transportationModel.departure_date  = transport_departure_date
                            transportationModel.departure_time  = transport_departure_time
                            transportationModel.arrival_time  = transport_arrival_time
                            transportationModel.trip_duration  = transport_trip_duration
                            transportationModel.transportation_id  = transport_transportation_id
                            transportationModel.transportation_name  = transport_transportation_name
                            transportationModel.transportation_status  = transport_transportation_status
                            transportationModel.harbor_source_id = transport_harbor_source_id
                            transportationModel.harbor_source_name = transport_harbor_source_name
                            transportationModel.harbor_destination_id  = transport_harbor_destination_id
                            transportationModel.harbor_destination_name = transport_harbor_destination_name

                            transportationModel.city_source_id = transport_city_source_id
                            transportationModel.city_source_name  = transport_city_source_name
                            transportationModel.city_dest_id = transport_city_dest_id
                            transportationModel.city_dest_name = transport_city_dest_name

                            transportationModel.province_source_id = transport_province_source_id
                            transportationModel.province_source_name  = transport_province_source_name
                            transportationModel.province_dest_id = transport_province_dest_id
                            transportationModel.province_dest_name = transport_province_dest_name

                            transportationModel.merchant_name = transport_merchant_name
                            transportationModel.merchant_picture = transport_merchant_picture
                            transportationModel.transportClass = transport_class

                            transportationModel.facilities = trsFacilityList
                            transportationModel.price  = priceMap
                            transportationModel.transportation_images = transImageList

                            transportationModel.viewType = 0
                            transportationModel.boat_details = boatDetailMap

                            transportationModel.return_trans_id = return_trans_id

                            if (type == 0){
                                transportList.add(transportationModel)
                            }
                            else{
                                returnTransportList.add(transportationModel)
                            }
                        }

                        val metaObject = jsonObject.getJSONObject("meta")
                        val meta_page = metaObject.getInt("page")
                        val total_pages= metaObject.getInt("total_pages")
                        val total_records= metaObject.getInt("total_records")
                        val prev= metaObject.getInt("prev")
                        val next= metaObject.getInt("next")
                        val record_per_page= metaObject.getInt("record_per_page")

                        Log.d("huhuhu",""+total_records)
                        metaMap["page"] = meta_page
                        metaMap["total_pages"] = total_pages
                        metaMap["total_records"] = total_records
                        metaMap["prev"] = prev
                        metaMap["next"] = next
                        metaMap["record_per_page"] = record_per_page

                        if (type == 0){
                            transportationSearchCallback.onTransportationSearchSuccess(transportList,metaMap)
                        }
                        else{
                            transportationSearchCallback.onTransportationSearchSuccess(returnTransportList,metaMap)
                        }
                    }
                    catch (e : Exception){
                        transportationSearchCallback.onTransportationSearchError(e.message ?: "")
                        Log.d("search_error",e.message ?: "")
                    }
                }
                else{
                    transportationSearchCallback.onTransportationSearchError(t.message() ?: "")
                    Log.d("search_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                transportationSearchCallback.onTransportationSearchError(e.message ?: "")
                Log.d("search_error",e.message ?: "")
            }
        })
    }

    fun getTimeOption(timeOptionCallback : MyCallback.Companion.TimeOptionCallback){

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getTimeOption()

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe(
            object : Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<ResponseBody>) {
                   if (t.isSuccessful){
                       try {
                           val jsonArray = JSONArray(t.body()?.string() ?: "")
                           val arraySize = jsonArray.length()
                           val timeOptionList = ArrayList<TimeOptionModel>()

                           for (i in 0 until arraySize){
                               val timeJsonObject = jsonArray[i] as JSONObject
                               val id = timeJsonObject.getInt("id")
                               val start_time = timeJsonObject.getString("start_time")
                               val end_time = timeJsonObject.getString("end_time")

                               val timeOptionModel = TimeOptionModel()
                               timeOptionModel.id = id
                               timeOptionModel.start_time = start_time
                               timeOptionModel.end_time = end_time

                               timeOptionList.add(timeOptionModel)
                           }

                           timeOptionCallback.onTimeOptionSuccess(timeOptionList)
                       }
                       catch (e : Exception){
                           timeOptionCallback.onTimeOptionError(e.message ?: "")
                           Log.d("time_option_error",e.message ?: "")
                       }
                   }
                    else{
                       timeOptionCallback.onTimeOptionError(t.message() ?: "")
                       Log.d("time_option_error",t.message() ?: "")
                   }
                }

                override fun onError(e: Throwable) {
                    timeOptionCallback.onTimeOptionError(e.message ?: "")
                    Log.d("time_option_error",e.message ?: "")
                }
            })
    }
}