package id.dtech.cgo.Controller

import android.util.Log
import id.dtech.cgo.ApiService.ApiService
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Connection.MyConnection
import id.dtech.cgo.Model.ExperienceModel
import id.dtech.cgo.Model.PhotoModel
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.lang.Exception

class UserController {

    fun getUserInfo(token : String, userInfoCallback : MyCallback.Companion.UserInfoCallback){

        userInfoCallback.onUserInfoPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,token)
        val discover = retrofit.getUserInfo()

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
                        Log.d("aldieys",id)

                        val created_date = jsonObject.getString("created_date")
                        Log.d("aldieys",created_date)

                        val is_active = jsonObject.getInt("is_active")
                        Log.d("aldieys",""+is_active)

                        val user_email= jsonObject.getString("user_email")
                        Log.d("aldieys",""+user_email)

                        val full_name = jsonObject.getString("full_name")
                        Log.d("aldieys",""+full_name)

                        val phone_number = jsonObject.getString("phone_number")
                        Log.d("aldieys",""+phone_number)

                        val profile_pict_url = jsonObject.getString("profile_pict_url")
                        Log.d("aldieys",""+profile_pict_url)

                        val address = jsonObject.getString("address")
                        Log.d("aldieys",""+address)

                        val dob  = jsonObject.getString("dob")
                        Log.d("aldieys",""+dob)

                        val gender = jsonObject.getInt("gender")
                        Log.d("aldieys",""+gender)

                        val id_type = jsonObject.getInt("id_type")
                        Log.d("aldieys",""+id_type)

                        val id_number = jsonObject.getString("id_number")
                        Log.d("aldieys",""+id_number)

                        val referral_code  = jsonObject.getString("referral_code")
                        Log.d("aldieys",""+referral_code)

                        val points = jsonObject.getLong("points")
                        Log.d("aldieys",""+points)

                        var updated_date = ""

                        if (!jsonObject.isNull("updated_date")){
                            updated_date = jsonObject.getString("updated_date") ?: ""
                        }

                        val dataMap = HashMap<String,Any>()
                        dataMap["id"] = id
                        dataMap["created_date"] = created_date
                        dataMap["updated_date"] = updated_date
                        dataMap["is_active"] = is_active
                        dataMap["user_email"] = user_email
                        dataMap["full_name"] = full_name
                        dataMap["phone_number"] = phone_number
                        dataMap["profile_pict_url"] = profile_pict_url
                        dataMap["address"] = address
                        dataMap["dob"] = dob
                        dataMap["gender"] = gender
                        dataMap["id_type"] = id_type
                        dataMap["id_number"] = id_number
                        dataMap["referral_code"] = referral_code
                        dataMap["points"] = points

                        userInfoCallback.onUserInfoSuccess(dataMap)
                    }
                    catch (e : JSONException){
                        userInfoCallback.onUserInfoError(e.message ?: "")
                        Log.d("user_info",e.message ?: "")
                    }
                    catch (e : Exception){
                        userInfoCallback.onUserInfoError(e.message ?: "")
                        Log.d("user_info",e.message ?: "")
                    }
                }
                else{
                    userInfoCallback.onUserInfoError(t.message() ?: "")
                    Log.d("user_info",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                userInfoCallback.onUserInfoError(e.message ?: "")
                Log.d("user_info",e.message ?: "")
            }
        })
    }

    fun requestOTP(body : HashMap<String,Any>, otpCallback : MyCallback.Companion.OTPCallback){

        otpCallback.onOTPPrepare()

        val phone = body["phone_number"] as String
        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.postRequestOtp(phone)

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

                            val otpCode = jsonObject.getString("OTP")
                            val expiredDate = jsonObject.getString("ExpiredDate")
                            val expiredMilis = jsonObject.getLong("ExpiredInMSecond")
                            Log.d("otp_code",otpCode)
                            otpCallback.onOTPSuccess(otpCode,expiredDate,expiredMilis)
                        }
                        catch (e : Exception){
                            Log.d("otp_error",e.message ?: "")
                        }
                    }
                    else{
                        otpCallback.onOTPError()
                        Log.d("otp_error",t.message() ?: "")
                    }
                }

                override fun onError(e: Throwable) {
                    otpCallback.onOTPError()
                    Log.d("otp_error",e.message ?: "")
                }
            })
    }

    fun requestLogin(body : HashMap<String,Any>, loginCallback: MyCallback.Companion.LoginCallback){

        loginCallback.onLoginPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.postLogin(body)

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
                        val accessToken = jsonObject.getString("access_token")
                        val expiresIn = jsonObject.getLong("expires_in")
                        val tokenType = jsonObject.getString("token_type")
                        val refreshToken = jsonObject.getString("refresh_token")

                        val dataMap = HashMap<String,Any>()
                        dataMap["access_token"] = accessToken
                        dataMap["expires_in"] = expiresIn
                        dataMap["token_type"] = tokenType
                        dataMap["refresh_token"] = refreshToken

                        loginCallback.onLoginSuccess(dataMap)
                    }
                    catch (e : Exception){
                        loginCallback.onLoginError("failed to parse json", 115)
                        Log.d("otp_error",e.message ?: "")
                    }
                }
                else{
                    val errorCode = t.code()

                    if (errorCode == 401){
                        loginCallback.onLoginError("otp error or has been expired", errorCode)
                    }
                    else if (errorCode == 404){
                        loginCallback.onLoginError("user not found", errorCode)
                    }
                    else{
                        loginCallback.onLoginError("", errorCode)
                    }

                    Log.d("otp_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                loginCallback.onLoginError("", 0)
                Log.d("otp_error",e.message ?: "")
            }
        })
    }

    fun requestRegister(body : HashMap<String,Any>, registerCallback : MyCallback.Companion.RegisterCallback){

        registerCallback.onRegisterPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val discover = retrofit.postRegister(body)

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
                       val user_email = jsonObject.getString("user_email")
                       val password = jsonObject.getString("password")
                       val full_name = jsonObject.getString("full_name")
                       val phone_number = jsonObject.getString("phone_number")
                       val verification_send_date = jsonObject.getString("verification_send_date")
                       val verification_code = jsonObject.getInt("verification_code")
                       val profile_pict_url = jsonObject.getString("profile_pict_url")
                       val address = jsonObject.getString("address")
                       val dob = jsonObject.getString("dob")
                       val gender = jsonObject.getInt("gender")
                       val id_type = jsonObject.getInt("id_type")
                       val id_number = jsonObject.getString("id_number")
                       val referral_code = jsonObject.getString("referral_code")
                       val points = jsonObject.getLong("points")
                       val token = jsonObject.getString("token")
                       Log.d("aldieygud",id)
                       val bodyMap = HashMap<String,Any>()
                       bodyMap["id"] = id
                       bodyMap["user_email"] = user_email
                       bodyMap["full_name"] = full_name
                       bodyMap["phone_number"] = phone_number
                       bodyMap["verification_send_date"] = verification_send_date
                       bodyMap["verification_code"] = verification_code
                       bodyMap["profile_pict_url"] = profile_pict_url
                       bodyMap["address"] = address
                       bodyMap["dob"] = dob
                       bodyMap["gender"] = gender
                       bodyMap["id_type"] = id_type
                       bodyMap["id_number"] = id_number
                       bodyMap["referral_code"] = referral_code
                       bodyMap["points"] = points
                       bodyMap["token"] = token

                       registerCallback.onRegisterSuccess(bodyMap)
                   }
                   catch (e : Exception){
                       registerCallback.onRegisterError("",0)
                   }
                }
                else{
                    val code = t.code()
                    registerCallback.onRegisterError("",code)
                    Log.d("otp_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                registerCallback.onRegisterError("",0)
                Log.d("otp_error",e.message ?: "")
            }
        })
    }

    fun getMyBooking(status : String, token : String, myBookingCallback : MyCallback.Companion.MyBookingCallback){

        myBookingCallback.onMyBookingPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,token)
        val discover = retrofit.getMyBooking(status)

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
                        val jsonArray = jsonObject.getJSONArray("data")
                        val jsonSize = jsonArray.length()
                        val dataList = ArrayList<HashMap<String,Any>>()

                        for (i in 0 until jsonSize){
                            val dataMap = HashMap<String,Any>()
                            val dataObject = jsonArray[i] as JSONObject

                            val order_id = dataObject.getString("order_id") ?: ""
                            val exp_type = dataObject.getString("exp_type") ?: ""
                            val exp_id = dataObject.getString("exp_id") ?: ""
                            val exp_title = dataObject.getString("exp_title") ?: ""
                            val trans_id = dataObject.getString("trans_id") ?: ""
                            val trans_name = dataObject.getString("trans_name") ?: ""
                            val trans_from = dataObject.getString("trans_from") ?: ""
                            val trans_to = dataObject.getString("trans_to") ?: ""
                            val trans_departure_time = dataObject.getString("trans_departure_time") ?: ""
                            val trans_arrival_time = dataObject.getString("trans_arrival_time") ?: ""
                            val trans_class = dataObject.getString("trans_class") ?: ""
                            val booking_date = dataObject.getString("booking_date") ?: ""
                            val exp_duration = dataObject.getInt("exp_duration")
                            val total_guest = dataObject.getInt("total_guest")
                            val city = dataObject.getString("city") ?: ""
                            val province = dataObject.getString("province") ?: ""
                            val country = dataObject.getString("country") ?: ""

                            val transGuestMap = HashMap<String,Any>()
                            val transGuestObject = dataObject.getJSONObject("trans_guest")
                            val adult = transGuestObject.getInt("adult")
                            val children = transGuestObject.getInt("children")

                            transGuestMap["adult"] = adult
                            transGuestMap["children"] = children

                            var booking_type : Int

                            if (trans_name.isEmpty()){
                                booking_type = 0
                            }
                            else{
                                booking_type = 1
                            }

                            dataMap["order_id"] = order_id
                            dataMap["exp_type"] = exp_type
                            dataMap["exp_id"] = exp_id
                            dataMap["exp_title"] = exp_title
                            dataMap["trans_id"] = trans_id
                            dataMap["trans_name"] = trans_name
                            dataMap["trans_from"] = trans_from
                            dataMap["trans_to"] = trans_to
                            dataMap["trans_departure_time"] = trans_departure_time
                            dataMap["trans_arrival_time"] = trans_arrival_time
                            dataMap["trans_class"] = trans_class
                            dataMap["booking_date"] = booking_date
                            dataMap["exp_duration"] = exp_duration
                            dataMap["total_guest"] = total_guest
                            dataMap["city"] = city
                            dataMap["province"] = province
                            dataMap["country"] = country
                            dataMap["booking_type"] = booking_type
                            dataMap["trans_guest"] = transGuestMap

                            dataList.add(dataMap)
                        }

                        myBookingCallback.onMyBookingSuccess(dataList)
                    }
                    catch (e : Exception){
                        myBookingCallback.onMyBookingError(e.message ?: "")
                        Log.d("mybook_error",e.message ?: "")
                    }
                }
                else{
                    myBookingCallback.onMyBookingError(t.message() ?: "")
                    Log.d("mybook_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                myBookingCallback.onMyBookingError(e.message ?: "")
                Log.d("mybook_error",e.message ?: "")
            }
        })
    }

    fun getMyBookingHistory(monthType : String, token : String, myBookingCallback :
        MyCallback.Companion.MyBookingHistoryCallback){

        myBookingCallback.onMyBookingHistoryPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,token)
        val discover = retrofit.getMyBookingHistory(monthType)

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
                        val jsonArray = jsonObject.getJSONArray("data")
                        val jsonSize = jsonArray.length()
                        val dataList = ArrayList<HashMap<String,Any>>()

                        for (i in 0 until jsonSize){

                            val jsonObject = jsonArray[i] as JSONObject
                            val itemArray = jsonObject.getJSONArray("items")
                            val itemSize = itemArray.length()

                            for (j in 0 until itemSize){
                                val dataMap = HashMap<String,Any>()
                                val dataObject = itemArray[j] as JSONObject

                                val order_id = dataObject.getString("order_id") ?: ""
                                val exp_type = dataObject.getString("exp_type") ?: ""
                                val exp_id = dataObject.getString("exp_id") ?: ""
                                val exp_title = dataObject.getString("exp_title") ?: ""
                                val trans_id = dataObject.getString("trans_id") ?: ""
                                val trans_name = dataObject.getString("trans_name") ?: ""
                                val trans_from = dataObject.getString("trans_from") ?: ""
                                val trans_to = dataObject.getString("trans_to") ?: ""
                                val trans_departure_time = dataObject.getString("trans_departure_time") ?: ""
                                val trans_arrival_time = dataObject.getString("trans_arrival_time") ?: ""
                                val trans_class = dataObject.getString("trans_class") ?: ""
                                val exp_booking_date = dataObject.getString("exp_booking_date") ?: ""
                                val exp_duration = dataObject.getInt("exp_duration")
                                val total_guest = dataObject.getInt("total_guest")
                                val city = dataObject.getString("city") ?: ""
                                val province = dataObject.getString("province") ?: ""
                                val country = dataObject.getString("country") ?: ""
                                val is_review = dataObject.getBoolean("is_review")

                                val transGuestMap = HashMap<String,Any>()
                                val transGuestObject = dataObject.getJSONObject("trans_guest")
                                val adult = transGuestObject.getInt("adult")
                                val children = transGuestObject.getInt("children")

                                transGuestMap["adult"] = adult
                                transGuestMap["children"] = children

                                var booking_type : Int

                                if (trans_name.isEmpty()){
                                    booking_type = 0
                                }
                                else{
                                    booking_type = 1
                                }

                                dataMap["order_id"] = order_id
                                dataMap["exp_type"] = exp_type
                                dataMap["exp_id"] = exp_id
                                dataMap["exp_title"] = exp_title
                                dataMap["trans_id"] = trans_id
                                dataMap["trans_name"] = trans_name
                                dataMap["trans_from"] = trans_from
                                dataMap["trans_to"] = trans_to
                                dataMap["trans_departure_time"] = trans_departure_time
                                dataMap["trans_arrival_time"] = trans_arrival_time
                                dataMap["trans_class"] = trans_class
                                dataMap["exp_booking_date"] = exp_booking_date
                                dataMap["exp_duration"] = exp_duration
                                dataMap["total_guest"] = total_guest
                                dataMap["city"] = city
                                dataMap["is_review"] = is_review
                                dataMap["province"] = province
                                dataMap["country"] = country
                                dataMap["booking_type"] = booking_type
                                dataMap["trans_guest"] = transGuestMap

                                dataList.add(dataMap)
                            }
                        }

                        myBookingCallback.onMyBookingHistorySuccess(dataList)
                    }
                    catch (e : Exception){
                        myBookingCallback.onMyBookingHistoryError(e.message ?: "")
                        Log.d("mybook_error",e.message ?: "")
                    }
                }
                else{
                    myBookingCallback.onMyBookingHistoryError(t.message() ?: "")
                    Log.d("mybook_error",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                myBookingCallback.onMyBookingHistoryError(e.message ?: "")
                Log.d("mybook_error",e.message ?: "")
            }
        })
    }

    fun getWishList(token : String,experienceSearchCallback : MyCallback.Companion.ExperienceSearchCallback){

        experienceSearchCallback.onExperienceSearchPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,token)
        val experience = retrofit.getWishlist()

        experience.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    try {

                        val experienceJsonObject = JSONObject(t.body()?.string() ?: "")
                        val jsonArray = experienceJsonObject.getJSONArray("data")
                        val jsonCount = jsonArray.length()
                        val metaObject = experienceJsonObject.getJSONObject("meta")
                        val totalRecords = metaObject.getInt("total_records")
                        val experienceList = ArrayList<ExperienceModel>()

                        for (i in 0 until jsonCount){

                            val experienceObject = jsonArray[i] as JSONObject
                            val id = experienceObject.getString("exp_id")
                            val exp_title = experienceObject.getString("exp_title")
                            val rating = experienceObject.getDouble("rating")
                            val count_rating = experienceObject.getInt("count_rating")
                            val currency = experienceObject.getString("currency")
                            val price = experienceObject.getLong("price")
                            val payment_type = experienceObject.getString("payment_type")

                            val expTypeList = ArrayList<String>()
                            val expTypeArray = experienceObject.getJSONArray("exp_type")
                            val expTypeArrayLength = expTypeArray.length()

                            val cover_photo = experienceObject.getString("cover_photo")

                            val coverPhotoModel = PhotoModel()
                            coverPhotoModel.original = cover_photo
                            coverPhotoModel.thumbnail = cover_photo

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

    fun createWishList(body : HashMap<String,Any>,token : String, createWishlistCallback :
    MyCallback.Companion.CreateWishlistCallback){

        createWishlistCallback.onCreateWishlistPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,token)
        val wishlist = retrofit.postCreateWishList(body)

        wishlist.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    createWishlistCallback.onCreateWishlistSuccess()
                }
                else{
                    val errorCode = t.code()
                    createWishlistCallback.onCreateWishlistError(errorCode)
                }
            }

            override fun onError(e: Throwable) {
                createWishlistCallback.onCreateWishlistError(0)
            }
        })
    }

    fun checkWishList(token : String, exp_id : String, checkWishlistCallback : MyCallback.Companion.CheckWishlistCallback){
        val retrofit = MyConnection.myClient(ApiService::class.java,token)
        val wishlist = retrofit.getCheckWishlist(exp_id)

        wishlist.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    val isExist = t.body()?.string()?.toBoolean() ?: false
                    checkWishlistCallback.onCheckWishlistSuccess(isExist)
                }
                else{
                    checkWishlistCallback.onCheckWishlistError()
                    Log.d("check_wishlist",t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                checkWishlistCallback.onCheckWishlistError()
                Log.d("check_wishlist",e.message ?: "")
            }
        })
    }

    fun updateProfile(token : String,file : File?,body : HashMap<String,Any>,editProfileCallback :
    MyCallback.Companion.EditProfileCallback){

        editProfileCallback.onEditProfilePrepare()

        val id = body["id"] as String
        val retrofit = MyConnection.myClient(ApiService::class.java,token)

        var user : Observable<Response<ResponseBody>>

        if (file != null){
            val myFile = RequestBody.create(MediaType.parse("image/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("profile_pict_url", file.name, myFile)
            user = retrofit.postEditProfile(id,fileToUpload,body)
        }
        else{
            user = retrofit.postEditProfileWithoutPhoto(id,body)
        }

        user.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                    if (t.isSuccessful){
                        try {
                            val jsonObject = JSONObject(t.body()?.string() ?: "")
                            val profilePictUrl = jsonObject.getString("profile_pict_url")
                            editProfileCallback.onEditProfileSuccess(profilePictUrl)
                        }
                        catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                    else{
                        val code = t.code()
                        editProfileCallback.onEditProfileError("",code)
                        Log.d("profile_error",t.message() ?: "")
                     }
            }

            override fun onError(e: Throwable) {
                editProfileCallback.onEditProfileError("",0)
                Log.d("profile_error",e.message ?: "")
            }
        })
    }

    fun createRefreshToken(body : HashMap<String,Any>, refreshTokenCallback: MyCallback.Companion
                .RefreshTokenCallback){

        refreshTokenCallback.onRefreshTokenPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val refreshTokenService = retrofit.postRefreshToken(body)

        refreshTokenService.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
            Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    val tokenObject = JSONObject(t.body()?.string() ?: "")
                    val access_token = tokenObject.getString("access_token")
                    val expires_in = tokenObject.getLong("expires_in")
                    val token_type = tokenObject.getString("token_type")
                    val refresh_token = tokenObject.getString("refresh_token")

                    val dataMap = HashMap<String,Any>()
                    dataMap["access_token"] = access_token
                    dataMap["expires_in"] = expires_in
                    dataMap["token_type"] = token_type
                    dataMap["refresh_token"] = refresh_token

                    refreshTokenCallback.onRefreshTokenSuccess(dataMap)
                }
                else{
                    val errorCode = t.code()
                    refreshTokenCallback.onRefreshTokenError("",errorCode)
                }
            }

            override fun onError(e: Throwable) {
                refreshTokenCallback.onRefreshTokenError("",0)
            }
        })
    }

    fun getVersion(type : Int, versionCallback : MyCallback.Companion.VersionCallback){

        versionCallback.onVersionPrepare()

        val retrofit = MyConnection.myClient(ApiService::class.java,null)
        val version = retrofit.getVersion(type)

        version.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe( object :
                Observer<Response<ResponseBody>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response<ResponseBody>) {
                if (t.isSuccessful){
                    val jsonArray = JSONArray(t.body()?.string())
                    val jsonObject = jsonArray[0] as JSONObject
                    val versionCode = jsonObject.getInt("version_code")
                    versionCallback.onVersionSuccess(versionCode)
                }
                else{
                    Log.d("version_error", t.message() ?: "")
                }
            }

            override fun onError(e: Throwable) {
                versionCallback.onVersionError()
                Log.d("version_error", e.message ?: "")
            }
        })
    }
}


