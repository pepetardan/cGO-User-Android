package id.dtech.cgo.Controller

import android.content.Context
import android.os.Environment
import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Connection.StorageConnection
import id.dtech.cgo.Model.AddOnModel
import id.dtech.cgo.Model.TransportationModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.net.URLDecoder

class BookingController {

    fun createBooking(body : HashMap<String,Any>, createBookingCallback :
    MyCallback.Companion.CreateBookingCallback){

        createBookingCallback.onCreateBookingPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.postCreateBooking(body)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    try {
                        val jsonArray = JSONArray(t.body()?.string() ?: "")
                        val jsonObject = jsonArray[0] as JSONObject

                        val id = jsonObject.getString("id")
                        val exp_id = jsonObject.getString("exp_id")
                        val guest_desc = jsonObject.getString("guest_desc")
                        val booked_by = jsonObject.getString("booked_by")
                        val booked_by_email = jsonObject.getString("booked_by_email")
                        val booking_date = jsonObject.getString("booking_date")
                        val user_id = jsonObject.getString("user_id")
                        val status = jsonObject.getString("status")
                        val order_id = jsonObject.getString("order_id")
                        val ticket_code = jsonObject.getString("ticket_code")
                        val ticket_qr_code = jsonObject.getString("ticket_qr_code")

                        val experience_add_on_id : String

                        if (!jsonObject.isNull("experience_add_on_id"))   {
                            experience_add_on_id = jsonObject.getString("experience_add_on_id")
                        }
                        else{
                            experience_add_on_id = ""
                        }

                        val mapResult = HashMap<String,Any>()
                        mapResult["id"] = id
                        mapResult["exp_id"] = exp_id
                        mapResult["guest_desc"] = guest_desc
                        mapResult["booked_by"] = booked_by
                        mapResult["booked_by_email"] = booked_by_email
                        mapResult["booking_date"] = booking_date
                        mapResult["user_id"] = user_id
                        mapResult["status"] = status
                        mapResult["order_id"] = order_id
                        mapResult["ticket_code"] = ticket_code
                        mapResult["ticket_qr_code"] = ticket_qr_code
                        mapResult["experience_add_on_id"] = experience_add_on_id

                        createBookingCallback.onCreateBookingSuccess(mapResult)
                    }
                    catch (e : Exception){
                        createBookingCallback.onCreateBookingError(e.message ?: "")
                        Log.d("create_booking_error",e.message ?: "")
                    }
                }
                else{
                    val errorBody = JSONObject(t.errorBody()?.string() ?: "")
                    val message = errorBody.getString("message")

                    createBookingCallback.onCreateBookingError(message)
                    Log.d("create_booking_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                createBookingCallback.onCreateBookingError(e.message ?: "")
                Log.d("create_booking_error",e.message ?: "")
            }
        })
    }

    fun getDetailBooking(checkout_id : String, detailBookingCallback : MyCallback.Companion.DetailBookingCallback){

        detailBookingCallback.onDetailBookingPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getDetailBookingExperience(checkout_id)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    try {

                        val jsonObject = JSONObject(t.body()?.string() ?: "")

                        val id = jsonObject.getString("id")
                        val booked_by_email = jsonObject.getString("booked_by_email")
                        val booking_date = jsonObject.getString("booking_date")
                        val expired_date_payment = jsonObject.getString("expired_date_payment")
                        val created_date_transaction = jsonObject.getString("created_date_transaction")
                        val user_id = jsonObject.getString("user_id")
                        val status  = jsonObject.getInt("status")
                        val transaction_status  = jsonObject.getInt("transaction_status")
                        val order_id  = jsonObject.getString("order_id")
                        val ticket_qr_code  = jsonObject.getString("ticket_qr_code")
                        val total_price = jsonObject.getLong("total_price")
                        val currency  = jsonObject.getString("currency")
                        val payment_type  = jsonObject.getString("payment_type")
                        val account_number  = jsonObject.getString("account_number")
                        val account_holder  = jsonObject.getString("account_holder")
                        val bank_icon  = jsonObject.getString("bank_icon")
                        val experience_payment_id  = jsonObject.getString("experience_payment_id")

                        var guide_review  = 0
                        var activities_review  = 0
                        var service_review = 0
                        var cleanliness_review = 0
                        var value_review  = 0

                        if (!jsonObject.isNull("guide_review")){
                            guide_review = jsonObject.getInt("guide_review")
                        }

                        if (!jsonObject.isNull("activities_review")){
                            activities_review = jsonObject.getInt("activities_review")
                        }

                        if (!jsonObject.isNull("service_review")){
                            service_review = jsonObject.getInt("service_review")
                        }

                        if (!jsonObject.isNull("cleanliness_review")){
                            cleanliness_review = jsonObject.getInt("cleanliness_review")
                        }

                        if (!jsonObject.isNull("value_review")){
                            value_review = jsonObject.getInt("value_review")
                        }

                        val guestDescArray = jsonObject.getJSONArray("guest_desc")
                        val guestDescSize = guestDescArray.length()
                        val guestDescList = ArrayList<HashMap<String,Any>>()

                        for (i in 0 until guestDescSize){
                            val guestDecObject = guestDescArray[i] as JSONObject
                            val fullname = guestDecObject.getString("fullname")
                            val idtype = guestDecObject.getString("idtype")
                            val idnumber = guestDecObject.getString("idnumber")
                            val type = guestDecObject.getString("type")

                            val guestMap = HashMap<String,Any>()
                            guestMap["fullname"] = fullname
                            guestMap["idtype"] = idtype
                            guestMap["idnumber"] = idnumber
                            guestMap["type"] = type

                            guestDescList.add(guestMap)
                        }

                        val bookedByArray = jsonObject.getJSONArray("booked_by")
                        val bookedBySize = bookedByArray.length()
                        val bookedByList = ArrayList<HashMap<String,Any>>()

                        for (j in 0 until bookedBySize){
                            val bookedObject = bookedByArray[j] as JSONObject
                            val title = bookedObject.getString("title")
                            val fullname = bookedObject.getString("fullname")
                            val email = bookedObject.getString("email")
                            val phonenumber = bookedObject.getString("phonenumber")
                            val idtype = bookedObject.getString("idtype")
                            val idnumber = bookedObject.getString("idnumber")

                            val bookedMap = HashMap<String,Any>()
                            bookedMap["title"] = title
                            bookedMap["fullname"] = fullname
                            bookedMap["email"] = email
                            bookedMap["phonenumber"] = phonenumber
                            bookedMap["idtype"] = idtype
                            bookedMap["idnumber"] = idnumber

                            bookedByList.add(bookedMap)
                        }

                        val experienceMap = HashMap<String,Any>()

                        if (!jsonObject.isNull("experience")){

                            val experienceObject = jsonObject.getJSONArray("exper" +
                                    "ience")[0] as JSONObject

                            val expTypeArray = experienceObject.getJSONArray("exp_type")
                            val expTypeSize = expTypeArray.length()
                            val expTypeList = ArrayList<String>()

                            for (h in 0 until expTypeSize){
                                val type = expTypeArray[h] as String
                                expTypeList.add(type)
                            }

                            val exp_id = experienceObject.getString("exp_id")
                            val exp_title = experienceObject.getString("exp_title")
                            val exp_pickup_place = experienceObject.getString("exp_pickup_place")
                            val exp_pickup_time = experienceObject.getString("exp_pickup_time")
                            val merchant_name = experienceObject.getString("merchant_name")
                            val merchant_phone = experienceObject.getString("merchant_phone")
                            val merchant_picture = experienceObject.getString("merchant_picture")
                            val total_guest = experienceObject.getInt("total_guest")
                            val exp_duration = experienceObject.getInt("exp_duration")
                            val province_name = experienceObject.getString("province_name")
                            val harbors_name = experienceObject.getString("harbors_name")
                            val package_name = experienceObject.getString("package_name")
                            val exp_payment_deadline_amount = experienceObject.getInt("exp_payment_deadline_amount")
                            val exp_payment_deadline_type = experienceObject.getString("exp_payment_deadline_type")
                            val ticket_valid_date = experienceObject.getString("ticket_valid_date")

                            var addOnModel : AddOnModel? = null

                            if (!experienceObject.isNull("experience_add_on")){
                                val addonArray =  experienceObject.getJSONArray("experience_add_on")

                                if (addonArray.length() > 0){
                                    val addOnObject = addonArray[0] as JSONObject

                                    addOnModel = AddOnModel()
                                    addOnModel.id = addOnObject.getString("id")
                                    addOnModel.name = addOnObject.getString("name")
                                    addOnModel.desc = addOnObject.getString("desc")
                                    addOnModel.currency = addOnObject.getString("currency")
                                    addOnModel.amount = addOnObject.getLong("amount")
                                }
                            }

                            if (addOnModel != null ){
                                experienceMap["add_on_model"] = addOnModel
                            }

                            val addOnlist = ArrayList<AddOnModel>()

                            if (!jsonObject.isNull("experience_add_on")){
                                val addOnArray = jsonObject.getJSONArray("experience_add_on")

                                for (i in 0 until addOnArray.length()){

                                }
                            }

                            experienceMap["exp_type"] = expTypeList
                            experienceMap["exp_id"] = exp_id
                            experienceMap["exp_title"] = exp_title
                            experienceMap["exp_pickup_place"] = exp_pickup_place
                            experienceMap["exp_pickup_time"] = exp_pickup_time
                            experienceMap["merchant_name"] = merchant_name
                            experienceMap["merchant_phone"] = merchant_phone
                            experienceMap["merchant_picture"] = merchant_picture
                            experienceMap["total_guest"] = total_guest
                            experienceMap["exp_duration"] = exp_duration
                            experienceMap["province_name"] = province_name
                            experienceMap["harbors_name"] = harbors_name
                            experienceMap["ticket_valid_date"] = ticket_valid_date
                            experienceMap["exp_payment_deadline_amount"] = exp_payment_deadline_amount
                            experienceMap["exp_payment_deadline_type"] = exp_payment_deadline_type
                            experienceMap["package_name"] = package_name
                            experienceMap["addOnlist"] = addOnlist
                        }

                        val transportList = ArrayList<TransportationModel>()

                        if (!jsonObject.isNull("transportation")) {
                            val transportationArray = jsonObject.getJSONArray("tra" +
                                    "nsportation")
                            val transportArraySize = transportationArray.length()

                            for (k in 0 until transportArraySize){
                                val transportObject = transportationArray[k] as JSONObject
                                val trans_id = transportObject.getString("trans_id")
                                val trans_name = transportObject.getString("trans_name")
                                val trans_title = transportObject.getString("trans_title")
                                val trans_status = transportObject.getString("trans_status")
                                val trans_class = transportObject.getString("trans_class")
                                val departure_date = transportObject.getString("departure_date")
                                val departure_time = transportObject.getString("departure_time")
                                val arrival_time = transportObject.getString("arrival_time")
                                val trip_duration = transportObject.getString("trip_duration")
                                val harbor_source_name = transportObject.getString("harbor_source_name")
                                val harbor_dest_name = transportObject.getString("harbor_dest_name")
                                val merchant_name = transportObject.getString("merchant_name")
                                val merchant_phone = transportObject.getString("merchant_phone")
                                val merchant_picture = transportObject.getString("merchant_picture")
                                val total_guest = transportObject.getInt("total_guest")

                                val transportModel = TransportationModel()
                                transportModel.departure_date  = departure_date
                                transportModel.departure_time = departure_time
                                transportModel.arrival_time = arrival_time
                                transportModel.trip_duration  = trip_duration
                                transportModel.transportation_id = trans_id
                                transportModel.transportation_name  = trans_name
                                transportModel.transportation_status = trans_status
                                transportModel.harbor_source_name  = harbor_source_name
                                transportModel.harbor_destination_name  = harbor_dest_name
                                transportModel.merchant_name  = merchant_name
                                transportModel.merchant_picture  = merchant_picture
                                transportModel.transportClass  = trans_class
                                transportModel.totalGuest = total_guest

                                transportList.add(transportModel)
                            }
                        }

                        var remainingPayment = 0L
                        var name = ""

                        if (!jsonObject.isNull("experience_payment_type")){
                            val experimentPaymentTypeObject = jsonObject.getJSONObject("experience_payment_type")
                            remainingPayment = experimentPaymentTypeObject.getLong("remaining_payment")
                            name = experimentPaymentTypeObject.getString("name")
                        }

                        val expPaymentMap = HashMap<String,Any>()

                        if (!jsonObject.isNull("exp_payment")){
                            val expPaymentObject = jsonObject.getJSONObject("exp_payment")
                            val packageId = expPaymentObject.getInt("package_id")
                            expPaymentMap["packageId"] = packageId
                        }

                        val bookingDetailMap = HashMap<String,Any>()
                        bookingDetailMap["id"] = id
                        bookingDetailMap["booked_by_email"] = booked_by_email
                        bookingDetailMap["booking_date"] = booking_date
                        bookingDetailMap["expired_date_payment"] = expired_date_payment
                        bookingDetailMap["created_date_transaction"] = created_date_transaction
                        bookingDetailMap["user_id"] = user_id
                        bookingDetailMap["status"] = status
                        bookingDetailMap["order_id"] = order_id
                        bookingDetailMap["ticket_qr_code"] = ticket_qr_code
                        bookingDetailMap["total_price"] = total_price
                        bookingDetailMap["currency"] = currency
                        bookingDetailMap["payment_type"] = payment_type
                        bookingDetailMap["account_number"] = account_number
                        bookingDetailMap["account_holder"] = account_holder
                        bookingDetailMap["bank_icon"] = bank_icon
                        bookingDetailMap["guide_review"] = guide_review
                        bookingDetailMap["activities_review"] = activities_review
                        bookingDetailMap["service_review"] = service_review
                        bookingDetailMap["cleanliness_review"] = cleanliness_review
                        bookingDetailMap["value_review"] = value_review
                        bookingDetailMap["transaction_status"] = transaction_status
                        bookingDetailMap["experience_payment_id"] = experience_payment_id
                        bookingDetailMap["guest_desc"] = guestDescList
                        bookingDetailMap["booked_by"] = bookedByList
                        bookingDetailMap["experience"] = experienceMap
                        bookingDetailMap["remaining_payment"] = remainingPayment
                        bookingDetailMap["name"] = name
                        bookingDetailMap["transportation"] = transportList
                        bookingDetailMap["expPaymentMap"] = expPaymentMap

                        detailBookingCallback.onDetailBookingSuccess(bookingDetailMap)
                    }
                    catch (e : Exception){
                        detailBookingCallback.onDetailBookingError(e.message ?: "")
                        Log.d("create_booking_error",e.message ?: "")
                    }
                }
                else{
                    detailBookingCallback.onDetailBookingError(t.message() ?: "")
                    Log.d("create_booking_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                detailBookingCallback.onDetailBookingError(e.message ?: "")
                Log.d("detail_booking_error",e.message ?: "")
            }
        })
    }

    fun getPDF(order_id : String, type : String, pdfFileCallback : MyCallback.Companion.PdfFileCallback){

        pdfFileCallback.onPdfFilePrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getBookingPDF(order_id,type)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                Observer<Response<ResponseBody>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){
                        val pdfURL = t.body()?.string() ?: ""
                        pdfFileCallback.onPdfFileSuccess(pdfURL)
                    }
                    else{
                        pdfFileCallback.onPdfFileError(t.message() ?: "")
                    }
                }

                override fun onError(e: Throwable) {
                    pdfFileCallback.onPdfFileError(e.message ?: "")
                }
            })
        }

    fun getCalculationPrice(dataMap : HashMap<String,Any>, calculatePriceCallback : MyCallback.Companion.CalculatePriceCallback){

        calculatePriceCallback.onCalculatePricePrepare()

        val date = dataMap["date"] as String
        val total_guest = dataMap["total_guest"] as Int
        val package_id = dataMap["package_id"] as Int
        val currency = dataMap["currency"] as String
        val exp_id = dataMap["exp_id"] as String

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.getCalculatePrice(date,total_guest
            ,package_id,currency,exp_id)

        discover.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                Observer<Response<ResponseBody>> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    val jsonObject = JSONObject(t.body()?.string() ?: "")
                    var price = 0L

                    if (!jsonObject.isNull("price")){
                        price = jsonObject.getLong("price")
                    }

                    val strCurrency = jsonObject.getString("currency")

                    val priceMap = HashMap<String,Any>()
                    priceMap["price"] = price
                    priceMap["currency"] = strCurrency

                    calculatePriceCallback.onCalculatePriceSuccess(priceMap)
                }
                else{
                    calculatePriceCallback.onCalculatePriceError()
                    Log.d("price_error",""+t.errorBody())
                }
            }

            override fun onError(e: Throwable) {
                    calculatePriceCallback.onCalculatePriceError()
                    e.printStackTrace()
            }

            override fun onComplete() {

            }
        })
    }
}