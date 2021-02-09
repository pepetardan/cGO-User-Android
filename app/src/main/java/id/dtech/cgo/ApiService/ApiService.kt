package id.dtech.cgo.ApiService

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // GET EXPERIENCE SEARCH
    @GET("service/experience/filter-search?")
    fun getExperienceSearch(@Query("harbor_id") harbor_id : String?,
                            @Query("city_id") city_id : Int?,
                            @Query("province_id") province_id : Int?,
                            @Query("type", encoded = true) type : String?,
                            @Query("startDate") startdate : String?,
                            @Query("endDate") enddate : String?,
                            @Query("guest") guest : Int?,
                            @Query("trip") trip : Int?,
                            @Query("bottomprice") bottomprice : Long?,
                            @Query("upprice") upperprice : Long?,
                            @Query("sortby") sortby : String?,
                            @Query("status") status : String?,
                            @Query("payment_type") payment_type : String?,
                            @Query("booking_type") booking_type : String?,
                            @Query("page") page : Int?,
                            @Query("size") size : Int?
                            ) : Observable<Response<ResponseBody>>

    // GET EXPERIENCE SEARCH
    @GET("service/transportation/filter-search?")
    fun getTransportationSearch(
                            @Query("page") page : Int?,
                            @Query("size") size : Int?,
                            @Query("harbor_source_id") harbor_source_id : String?,
                            @Query("harbor_dest_id") harbor_dest_id : String?,
                            @Query("guest") guest : Int?,
                            @Query("departure_date") departure_date : String?,
                            @Query("class") transportClass : String?,
                            @Query("isReturn") isReturn : Int?,
                            @Query("not_return") not_return : Boolean?,
                            @Query("dep_timeoption_id") dep_timeoption_id : Int?,
                            @Query("arr_timeoption_id") arr_timeoption_id : Int?,
                            @Query("sortBy") sortBy : String?,
                            @Query("return_trans_id") return_trans_id : String?
    ) : Observable<Response<ResponseBody>>

    // GET USER INFO
    @GET("account/info?type=user")
    fun getUserInfo() : Observable<Response<ResponseBody>>

    // GET TRIP INSPIRATION
    @GET("cgo/article")
    fun getTripInspiration() : Observable<Response<ResponseBody>>

    // GET PROMO
    @GET("service/special-promo")
    fun getPromo() : Observable<Response<ResponseBody>>

    // GET WISHLIST
    @GET("profile/wishlists")
    fun getWishlist() : Observable<Response<ResponseBody>>

    // GET DISCOVER PREFERANCE
    @GET("service/experience/get-user-discover-preference")
    fun getDiscoverPreferance() : Observable<Response<ResponseBody>>

    // GET DESTINATION
    @GET("service/exp-destination?")
    fun getDestination(@Query("search") query : String) : Observable<Response<ResponseBody>>

    // GET MY BOOKING
    @GET("booking/my?")
    fun getMyBooking(@Query("status") query : String) : Observable<Response<ResponseBody>>

    // GET MY BOOKING HISTORY
    @GET("booking/history-user?")
    fun getMyBookingHistory(@Query("month_type") query : String) : Observable<Response<ResponseBody>>

    // GET EXPERIENCE DETAIL
    @GET("service/experience/{id}")
    fun getExperienceDetail(@Path("id") id : String) : Observable<Response<ResponseBody>>

    // GET EXPERIENCE REVIEW
    @GET("product/exp-reviews?")
    fun getExperienceReview(@Query("exp_id") id : String) : Observable<Response<ResponseBody>>

    // GET ADD ON
    @GET("product/product-add-ons?")
    fun getAddOn(@Query("exp_id") id : String) : Observable<Response<ResponseBody>>

    // GET VERSION
    @GET("version?")
    fun getVersion(@Query("type") type : Int) : Observable<Response<ResponseBody>>

    // GET EXCHANGE RATE
    @GET("misc/exchange-rate?")
    fun getExhangeRates(@Query("from") from : String, @Query("to") to : String) :
            Observable<Response<ResponseBody>>

    // GET PAYMENT METHOD
    @GET("transaction/payment-method")
    fun getPaymentMethod() : Observable<Response<ResponseBody>>

    // GET PAYMENT METHOD
    @GET("service/special-promo/{code}?")
    fun getSpecialPromo(@Path("code") code : String, @Query("promo_type") promoType : Int)
            : Observable<Response<ResponseBody>>

    // GET PAYMENT METHOD
    @GET("profile/check-wishlists?")
    fun getCheckWishlist(@Query("exp_id") exp_id : String) : Observable<Response<ResponseBody>>

    // GET TIME OPTION
    @GET("service/transportation/time-options")
    fun getTimeOption() : Observable<Response<ResponseBody>>

    // GET DETAIL BOOKING EXERIENCE
    @GET("booking/detail/{code}")
    fun getDetailBookingExperience(@Path("code") code : String) : Observable<Response<ResponseBody>>

    // GET PDF
    @GET("booking/download-ticket?")
    fun getBookingPDF(@Query("order_id") order_id : String, @Query("type") type : String
    ) : Observable<Response<ResponseBody>>

    // GET FILE PDF
    @GET
    fun getFilePDF(@Url url : String) : Observable<Response<ResponseBody>>

    // CALCULATE PRICE
    @GET("service/experience/calculate-price?")
    fun getCalculatePrice(@Query("date") date : String,
                          @Query("total_guest") total_guest : Int,
                          @Query("package_id") package_id : Int,
                          @Query("currency") currency : String,
                          @Query("exp_id") exp_id : String
    ) : Observable<Response<ResponseBody>>

    // GET ACCOMODATION
    @GET("master/accomodation?")
    fun getAccomodation(@Query("page") page : Int, @Query("size") size : Int
    ) : Observable<Response<ResponseBody>>

    // GET LANGUAGE
    @GET("master/language?")
    fun getLanguage(@Query("page") page : Int, @Query("size") size : Int
    ) : Observable<Response<ResponseBody>>

    // GET CATEGORIES
    @GET("service/experience/categories")
    fun getCategories() : Observable<Response<ResponseBody>>

    // POST CREATE BOOKING
    @FormUrlEncoded
    @POST("booking/checkout")
    fun postCreateBooking(@FieldMap body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // POST CREATE PAYMENT TRANSACTION
    @POST("transaction/payments")
    fun postCreatePaymentTransaction(@Body body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // POST CREATE PAYMENT TRANSACTION
    @PUT("transaction/payments/confirm")
    fun postConfirmPayment(@Body body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // POST REQUEST OTP
    @FormUrlEncoded
    @POST("account/request-otp")
    fun postRequestOtp(@Field("phone_number") phone : String) : Observable<Response<ResponseBody>>

    // POST LOGIN
    @FormUrlEncoded
    @POST("account/login")
    fun postLogin(@FieldMap body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // POST REGISTER
    @FormUrlEncoded
    @POST("users")
    fun postRegister(@FieldMap body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // POST REFRESH TOKEN
    @FormUrlEncoded
    @POST("account/refresh-token")
    fun postRefreshToken(@FieldMap body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // POST WISHLIST
    @POST("profile/wishlists")
    fun postCreateWishList(@Body body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // POST REVIEW
    @POST("product/exp-reviews")
    fun postCreateReview(@Body body : HashMap<String,Any>) : Observable<Response<ResponseBody>>

    // PUT EDIT PROFILE
    @Multipart
    @PUT("users/{id}")
    fun postEditProfile(@Path("id") user_id : String,
                        @Part file : MultipartBody.Part?,
                        @PartMap body : HashMap<String,Any>)
            : Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @PUT("users/{id}")
    fun postEditProfileWithoutPhoto(@Path("id") user_id : String, @FieldMap body : HashMap<String,Any>)
            : Observable<Response<ResponseBody>>

}