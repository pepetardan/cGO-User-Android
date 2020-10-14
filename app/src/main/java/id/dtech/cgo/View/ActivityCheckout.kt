package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.RelativeLayout
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.paypal.android.sdk.payments.*
import com.squareup.picasso.Picasso
import com.xendit.Models.Card
import com.xendit.Models.Token
import com.xendit.Models.XenditError
import com.xendit.TokenCallback
import com.xendit.Xendit
import id.dtech.cgo.Adapter.AdapterAddonList
import id.dtech.cgo.Adapter.AddonNameAdapter
import id.dtech.cgo.Adapter.BankPaymentAdapter
import id.dtech.cgo.Adapter.PassengerAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.*
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.*
import id.dtech.cgo.Preferance.ProfileSession
import id.dtech.cgo.Preferance.UserSession
import id.dtech.cgo.R
import id.dtech.cgo.Util.CalendarUtil
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.layout_checkout.*
import kotlinx.android.synthetic.main.layout_confirmation.*
import kotlinx.android.synthetic.main.layout_payment.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityCheckout : AppCompatActivity(), View.OnClickListener, ApplicationListener.Companion
    .GuestListener, MyCallback.Companion.CreateBookingCallback,
    MyCallback.Companion.SpecialPromoCallback, ApplicationListener.Companion.BankMethodListener,
    MyCallback.Companion.PaymentMethodCallback, MyCallback.Companion.CreatePaymentCallback,
    MyCallback.Companion.ConfirmationPaymentCallback, MyCallback.Companion.DetailBookingCallback,
    MyCallback.Companion.ExchangeRatesCallback {

    private val passengerList = ArrayList<HashMap<String,Any>>()
    private val selectedDates = java.util.ArrayList<LocalDate>()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private var countDownTimer : CountDownTimer? = null

    private lateinit var priceRoundedBottomSheet: RoundedBottomSheetDialog
    private lateinit var imgBottomClose : ImageView
    private lateinit var txtTitlePackage : MyTextView
    private lateinit var txtPricePackage : MyTextView
    private lateinit var txtPricePromo : MyTextView
    private lateinit var txtPriceCredit : MyTextView
    private lateinit var txtSubTotalPrice : MyTextView
    private lateinit var txtPriceDp : MyTextView
    private lateinit var txtDp : MyTextView
    private lateinit var txtDpTotalPrice : MyTextView
    private lateinit var txtPriceDialog : MyTextView
    private lateinit var txtDialogTotalPrice : MyTextView
    private lateinit var rvAddonList : RecyclerView
    private lateinit var linearPricePromo : LinearLayout
    private lateinit var linearPriceCredit : LinearLayout
    private lateinit var linearPriceSubtotal : LinearLayout
    private lateinit var linearPriceDp : LinearLayout
    private lateinit var linearPriceDialog : LinearLayout
    private lateinit var viewDialog1 : View
    private lateinit var viewDialog2 : View

    private lateinit var currentCalendarMonth : CalendarMonth
    private lateinit var checkoutMap : HashMap<String,Any>
    private lateinit var paymentTransactionmap : HashMap<String,Any>

    private lateinit var guestList : ArrayList<HashMap<String,Any>>
    private lateinit var addOnList : ArrayList<AddOnModel>
    private lateinit var packageMap : HashMap<String,Any>
    private lateinit var intentMap : HashMap<String,Any>
    private var selectedGuideMap : HashMap<String,Any>? = null

    private lateinit var guideList : ArrayList<HashMap<String,Any>>
    private lateinit var packageList : ArrayList<HashMap<String,Any>>
    private lateinit var typeList : ArrayList<String>
    private lateinit var packageExpPayment : ArrayList<HashMap<String,Any>>

    private lateinit var transactionController : TransactionController
    private lateinit var experienController : ExperienceController
    private lateinit var bookingController : BookingController
    private lateinit var micsController : MicsController

    private lateinit var paymentMethodList : ArrayList<String>

    private lateinit var loadingDialog : AlertDialog
    private lateinit var backDialog : AlertDialog
    private lateinit var dialogCalendar : Dialog

    private lateinit var aBottomSheetDialog : RoundedBottomSheetDialog
    private lateinit var cBottomSheetDialog : RoundedBottomSheetDialog

    private lateinit var calendarView : CalendarView
    private lateinit var txtMonthYear : TextView
    private lateinit var calendarBtnApply : Button

    private lateinit var imgNext : ImageView
    private lateinit var imgPrev : ImageView

    private lateinit var userSession: UserSession
    private lateinit var profileSession: ProfileSession
    private lateinit var btnDetail : RelativeLayout

    private lateinit var experienceDetailModel: ExperienceDetailModel
    private lateinit var addOnModel : AddOnModel

    private lateinit var learnClose : ImageView

    private var currentPage = 1
    private var currentPassengerPosition = 0

    private var from = 0
    private var isSuccess = false
    private var redirectURL = ""

    private var date = ""
    private var selectedDateName = ""
    private var paymentType = ""

    private var isDownPaymentAvailable = false

    //TOTAL PRICE
    private var isPromoUsed = false
    private var isCreditsUsed = false
    private var creditPrice = 0L
    private var strCurrency = ""

    //PAYMENT
    private lateinit var bankMethodList : ArrayList<PaymentMethodModel>
    private lateinit var creditCardMethodList: ArrayList<PaymentMethodModel>
    private lateinit var paypalMethodList: ArrayList<PaymentMethodModel>

    private var paymentMethodModel: PaymentMethodModel? = null
    private var total_price = 0L
    private var fixed_total_price = 0L
    private var currentSubtotalPrice = 0L

    private var bankPosition = 0
    private var paymentPosition = 0
    private var isBankCLicked = false

    private var isPaypal = false
    private var payID = ""
    private var paymentTypePercentage = 0

    //CREDITS
    private var isCreditsChecked = false

    // PROMO
    private var promoTotalPrice = 0L
    private var promoModel : PromoModel? = null
    private var isPromoChecked  = false
    private var promoID = ""

    //BOOKED DATA
    private var bookedTitle = ""
    private var bookedPhone = ""
    private var bookedID = ""

    //MERCHANT DATA
    private var merchant_name = ""
    private var merchant_phone = ""
    private var merchant_picture = ""
    private var barcode_picture = ""

    // ADULT DIALOG OBJECT
    private lateinit var adultEdtFullname : android.widget.EditText
    private lateinit var edtAdultEdtIdNumber  : android.widget.EditText

    private lateinit var adultLinearTitle : LinearLayout
    private lateinit var adultLinearID : LinearLayout

    private lateinit var txtAdultTitle : TextView
    private lateinit var txtAdultType : TextView

    private lateinit var imgAdultClose : ImageView
    private lateinit var adultBtnSave : Button

    // CHILD DIALOG OBJECT
    private lateinit var imgChildClose : ImageView
    private lateinit var edtChildFullname : android.widget.EditText

    private lateinit var childLinearTitle : LinearLayout
    private lateinit var childLinearBirthday : LinearLayout

    private lateinit var txtChildTitle : TextView
    private lateinit var txtChildBirthDay : TextView

    private lateinit var btnChildSave :Button

    // GUEST COUNT OBJECT
    private var adultCount = 0
    private var childrenCount = 0
    private var infantCount = 0

    private lateinit var dialogType : Dialog

    // TRANSPORTATION OBJECT
    private  var transportationModel: TransportationModel? = null
    private  var transportationModelReturn: TransportationModel? = null

    private  var transportImages : ArrayList<HashMap<String,Any>>? = null
    private  var transportReturnImages : ArrayList<HashMap<String,Any>>? = null

    private  var transportPrice : HashMap<String,Any>? = null
    private  var transportReturnPrice : HashMap<String,Any>? = null

    private var transportFacilities : ArrayList<FacilityModel>? = null
    private var transportReturnFacilities : ArrayList<FacilityModel>? = null

    private  var transportBoatDetails : HashMap<String,Any>? = null
    private  var transportReturnBoatDetails : HashMap<String,Any>? = null

    private var totalPricePayment = 0L

    private var totalAdultPrice = 0L
    private var totalChildrenPrice = 0L

    private var isReturn = 0
    private var isReturnClicked = false
    private var isBankViewClicked = false
    private var isCCViewClicked = false

    private var fromDetail = 0
    private var fromView = 0

    private var order_id = ""
    private var fromPaymentMethod = 0

    private var promoType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setView()
    }

    private fun setView(){

        loadingDialog = ViewUtil.laodingDialog(this)
        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        profileSession = ProfileSession(getSharedPreferences("profile_session", Context.MODE_PRIVATE))

        micsController = MicsController()
        bookingController = BookingController()
        transactionController = TransactionController()
        experienController = ExperienceController()

        val b = intent.extras

        b?.let { bundle ->

            order_id = bundle.getString("order_id") ?: ""

            if (order_id.isNotEmpty()){
                currentPage = 3
                setCurrentPage()
                setConfirmation()
            }
            else{
               initiatePriceBottomSheet()

               from = bundle.getInt("from")
               adultCount = bundle.getInt("adult_count")
               childrenCount = bundle.getInt("children_count")
               infantCount = bundle.getInt("infant_count")

               if (from == 1){
                   promoType = 1

                   intentMap = bundle.getSerializable("intentMap") as HashMap<String, Any>
                   packageMap = intentMap["packageMap"] as HashMap<String, Any>
                   guideList = intentMap["guideList"] as ArrayList<HashMap<String, Any>>
                   packageList = intentMap["packageList"] as ArrayList<HashMap<String, Any>>
                   packageExpPayment = intentMap["packageExpPayment"] as ArrayList<HashMap<String, Any>>

                   if (intentMap["selectedGuideMap"] != null) {
                       selectedGuideMap = intentMap["selectedGuideMap"] as HashMap<String, Any>
                   }

                   experienceDetailModel = bundle.getParcelable("experience_model") ?:
                           ExperienceDetailModel()
                   addOnModel = bundle.getParcelable("addon_model") ?:
                           AddOnModel()

                   selectedDateName = bundle.getString("selected_date") ?: ""
                   date = bundle.getString("date") ?: ""
                   paymentType = bundle.getString("payment_type") ?: ""
                   isDownPaymentAvailable = packageMap["isDownpaymentAvailable"] as Boolean
                   addOnList = bundle.getParcelableArrayList("addOnList") ?: ArrayList()

                   if (isDownPaymentAvailable){
                       linearPaymentType.visibility = View.VISIBLE
                   }
                   else{
                       linearPaymentType.visibility = View.GONE
                   }

                   experienceHeader1.visibility = View.VISIBLE
                   experienceHeader2.visibility = View.VISIBLE

                   transportationHeader1.visibility = View.GONE
                   transportationHeader2.visibility = View.GONE

                   initiatePaymentMethod()
                   setHeaderContent()
               }
               else{
                   promoType = 1
                   transportationModel = bundle.getParcelable<TransportationModel>("transportation" +
                           "Model") ?: TransportationModel()
                   transportationModelReturn = bundle.getParcelable<TransportationModel>("transporta" +
                           "tionModelReturn") ?: TransportationModel()

                   transportImages = intent.getSerializableExtra("transportImages") as ArrayList<HashMap<String, Any>>

                   intent.getSerializableExtra("transportReturnImages")?.let {
                       transportReturnImages = it as ArrayList<HashMap<String, Any>>
                   }

                   transportPrice = intent.getSerializableExtra("transportPrice") as HashMap<String, Any>

                   intent.getSerializableExtra("transportReturnPrice")?.let {
                       transportReturnPrice = it as HashMap<String, Any>
                   }

                   transportFacilities = bundle.getParcelableArrayList("transportFacilities")
                   transportReturnFacilities = bundle.getParcelableArrayList("transportReturnFacilities")

                   transportBoatDetails = intent.getSerializableExtra("transportBoatDetails") as HashMap<String, Any>

                   intent.getSerializableExtra("transportReturnBoatDetails")?.let {
                       transportReturnBoatDetails = it as HashMap<String, Any>
                   }

                   totalAdultPrice = bundle.getLong("totalAdultPrice")
                   totalChildrenPrice = bundle.getLong("totalChildrenPrice")

                   totalPricePayment = bundle.getLong("totalPricePayment")
                   date = bundle.getString("date") ?: ""
                   isReturn = bundle.getInt("is_return")

                   total_price = totalPricePayment

                   linearLogin.visibility = View.GONE
                   linearLogin2.visibility = View.GONE

                   experienceHeader1.visibility = View.GONE
                   experienceHeader2.visibility = View.GONE

                   transportationHeader1.visibility = View.VISIBLE
                   transportationHeader2.visibility = View.VISIBLE

                   txtFullPayment.visibility = View.GONE
                   txtDownPayment.visibility = View.GONE

                   setTransportPrice()
                   checkIsReturn()
               }

               initiatePassenger()
           }
        }

        experienController.getPaymentMethod(this)

        rvBank.layoutManager = LinearLayoutManager(this)
        rvGuest.layoutManager = LinearLayoutManager(this)
        rvGuest.adapter = PassengerAdapter(this,passengerList,this)

        checkIsLogin()
        setCreditBackground()
        setBackDialog()
        showAdultDialog()
        showChildDialog()
        showLearnMore()
        setCalenderAlertDialog()

        ivBack.setOnClickListener(this)
        btnNext.setOnClickListener(this)

        txtFullPayment.setOnClickListener(this)
        txtDownPayment.setOnClickListener(this)
        txtHowtoPay.setOnClickListener(this)

        txtPaymentLearnMore.setOnClickListener(this)
        btnPromo.setOnClickListener(this)

        txtDetail1.setOnClickListener(this)
        txtDetail2.setOnClickListener(this)

        linearTotalBottomSheet.setOnClickListener(this)
        linearPaymentTotalBottomSheet.setOnClickListener(this)

        linearBankPayment.setOnClickListener(this)
        linearCCMethod.setOnClickListener(this)
        linearPaypalMethod.setOnClickListener(this)
        linearCredit.setOnClickListener(this)

        linearDeparture1.setOnClickListener(this)
        linearDeparture2.setOnClickListener(this)

        linearReturn1.setOnClickListener(this)
        linearReturn2.setOnClickListener(this)
        imgCopy.setOnClickListener(this)

        linearTitle.setOnClickListener(this)
        linearLogin.setOnClickListener(this)
        linearLogin2.setOnClickListener(this)
        linearPhone.setOnClickListener(this)
        linearIdCard.setOnClickListener(this)
    }

    private fun initiatePaymentMethod(){
        paymentMethodList = ArrayList()
        paymentMethodList.add(packageExpPayment[0]["packagePaymentId"] as String)

        if (packageExpPayment.size > 1){
            paymentMethodList.add(packageExpPayment[1]["packagePaymentId"] as String)
            paymentTypePercentage = packageExpPayment[1]["paymentPrecentage"] as Int
        }
    }

    private fun initiatePriceBottomSheet(){
        val view = LayoutInflater.from(this).inflate(R.layout.price_details_bottomsheet, null)
        priceRoundedBottomSheet = RoundedBottomSheetDialog(this)
        priceRoundedBottomSheet.setContentView(view)

        imgBottomClose = view.findViewById(R.id.imgBottomClose)
        txtTitlePackage = view.findViewById(R.id.txtTitlePackage)
        txtPricePackage = view.findViewById(R.id.txtPricePackage)
        txtDialogTotalPrice = view.findViewById(R.id.txtDialogTotalPrice)
        txtPricePromo = view.findViewById(R.id.txtPricePromo)
        txtPriceCredit = view.findViewById(R.id.txtPriceCredit)
        txtSubTotalPrice = view.findViewById(R.id.txtSubTotalPrice)
        txtPriceDp = view.findViewById(R.id.txtPriceDp)
        txtDp = view.findViewById(R.id.txtDp)
        txtDpTotalPrice = view.findViewById(R.id.txtDpTotalPrice)
        txtPriceDialog = view.findViewById(R.id.txtPriceDialog)
        rvAddonList = view.findViewById(R.id.rvAddonList)
        linearPricePromo = view.findViewById(R.id.linearPricePromo)
        linearPriceCredit = view.findViewById(R.id.linearPriceCredit)
        linearPriceSubtotal = view.findViewById(R.id.linearPriceSubtotal)
        linearPriceDp = view.findViewById(R.id.linearPriceDp)
        linearPriceDialog = view.findViewById(R.id.linearPriceDialog)
        viewDialog1 = view.findViewById(R.id.viewDialog1)
        viewDialog2 = view.findViewById(R.id.viewDialog2)

        rvAddonList.layoutManager = LinearLayoutManager(this)

        imgBottomClose.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setTransportPrice(){

        linearPromoDetail.visibility = View.VISIBLE
        linearPriceOne.visibility = View.GONE

        if (adultCount > 0){
            txtAdult.text = "Adult (x$adultCount)"
            linearAdult.visibility = View.VISIBLE
        }
        else{
            linearAdult.visibility = View.GONE
        }

        if (childrenCount > 0){
            txtChildren.text = "Children (x$childrenCount)"
            linearChildren.visibility = View.VISIBLE
        }
        else{
            linearChildren.visibility = View.GONE
        }

        val strAdultPrice = "$strCurrency " +CurrencyUtil.decimal(totalAdultPrice).replace(
            ",",".")
        val strChildrenPrice = "$strCurrency " +CurrencyUtil.decimal(totalChildrenPrice).replace(
            ",",".")
        val strTotalPrice = "$strCurrency " +CurrencyUtil.decimal(totalPricePayment).replace(
            ",",".")

        txtAdultPrice.text = strAdultPrice
        txtChildrenPrice.text = strChildrenPrice
        txtPaymentType2.text = strTotalPrice
        txtPaymentTotalPrice.text = strTotalPrice
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkIsReturn(){
        if (isReturn == 0){
            linearReturn1.visibility = View.GONE
            linearReturn2.visibility = View.GONE
        }
        else{
            linearReturn1.visibility = View.VISIBLE
            linearReturn2.visibility = View.VISIBLE
        }

        transportationModel?.let { transportModel ->

            val strMerchantImg = transportModel.merchant_picture ?: ""

            if (strMerchantImg.isNotEmpty()){
                Picasso.get().load(strMerchantImg).into(imgMerchant1)
                Picasso.get().load(strMerchantImg).into(imgMerchant2)
            }

            txtMerchantName1.text = transportModel.merchant_name ?: ""
            txtClass1.text = transportModel.transportClass ?: ""
            txtLongTime1.text = transportModel.trip_duration ?: ""
            txtOrigin1.text = transportModel.harbor_source_name ?: ""
            txtDestination1.text = transportModel.harbor_destination_name ?: ""

            txtMerchantName2.text = transportModel.merchant_name ?: ""
            txtClass2.text = transportModel.transportClass ?: ""
            txtLongTime2.text = transportModel.trip_duration ?: ""
            txtOrigin2.text = transportModel.harbor_source_name ?: ""
            txtDestination2.text = transportModel.harbor_destination_name ?: ""

            val sdf = SimpleDateFormat("HH:mm:ss")
            val sdfs = SimpleDateFormat("HH:mm")

            val startDateTime = sdf.parse(transportModel.departure_time ?: "")
            val endDateTime = sdf.parse(transportModel.arrival_time ?: "")

            txtOriginTime1.text = sdfs.format(startDateTime ?: Date())
            txtDestinationTime1.text = sdfs.format(endDateTime ?: Date())

            txtOriginTime2.text = sdfs.format(startDateTime ?: Date())
            txtDestinationTime2.text = sdfs.format(endDateTime ?: Date())
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkReturnState(){
        if (isReturn == 1){
            if (!isReturnClicked){
                transportationModelReturn?.let { returnModel ->

                    val strMerchantImg = returnModel.merchant_picture ?: ""

                    if (strMerchantImg.isNotEmpty()){
                        Picasso.get().load(strMerchantImg).into(imgMerchant1)
                        Picasso.get().load(strMerchantImg).into(imgMerchant2)
                    }

                    txtMerchantName1.text = returnModel.merchant_name ?: ""
                    txtClass1.text = returnModel.transportClass ?: ""
                    txtLongTime1.text = returnModel.trip_duration ?: ""
                    txtOrigin1.text = returnModel.harbor_source_name ?: ""
                    txtDestination1.text = returnModel.harbor_destination_name ?: ""

                    txtMerchantName2.text = returnModel.merchant_name ?: ""
                    txtClass2.text = returnModel.transportClass ?: ""
                    txtLongTime2.text = returnModel.trip_duration ?: ""
                    txtOrigin2.text = returnModel.harbor_source_name ?: ""
                    txtDestination2.text = returnModel.harbor_destination_name ?: ""

                    txtDeparture1.setTextColor(Color.parseColor("#A4A4A4"))
                    viewDeparture1.visibility = View.INVISIBLE
                    txtReturn1.setTextColor(Color.parseColor("#616161"))
                    viewReturn1.visibility = View.VISIBLE

                    txtDeparture2.setTextColor(Color.parseColor("#A4A4A4"))
                    viewDeparture2.visibility = View.INVISIBLE
                    txtReturn2.setTextColor(Color.parseColor("#616161"))
                    viewReturn2.visibility = View.VISIBLE

                    val sdf = SimpleDateFormat("HH:mm:ss")
                    val sdfs = SimpleDateFormat("HH:mm")

                    val startDateTime = sdf.parse(returnModel.departure_time ?: "")
                    val endDateTime = sdf.parse(returnModel.arrival_time ?: "")

                    txtOriginTime1.text = sdfs.format(startDateTime ?: Date())
                    txtDestinationTime1.text = sdfs.format(endDateTime ?: Date())

                    txtOriginTime2.text = sdfs.format(startDateTime ?: Date())
                    txtDestinationTime2.text = sdfs.format(endDateTime ?: Date())

                    isReturnClicked = true
                    fromDetail = 1
                }
            }
            else{
                transportationModel?.let { transportModel ->

                    val strMerchantImg = transportModel.merchant_picture ?: ""

                    if (strMerchantImg.isNotEmpty()){
                        Picasso.get().load(strMerchantImg).into(imgMerchant1)
                        Picasso.get().load(strMerchantImg).into(imgMerchant2)
                    }

                    txtMerchantName1.text = transportModel.merchant_name ?: ""
                    txtClass1.text = transportModel.transportClass ?: ""
                    txtLongTime1.text = transportModel.trip_duration ?: ""
                    txtOrigin1.text = transportModel.harbor_source_name ?: ""
                    txtDestination1.text = transportModel.harbor_destination_name ?: ""

                    txtMerchantName2.text = transportModel.merchant_name ?: ""
                    txtClass2.text = transportModel.transportClass ?: ""
                    txtLongTime2.text = transportModel.trip_duration ?: ""
                    txtOrigin2.text = transportModel.harbor_source_name ?: ""
                    txtDestination2.text = transportModel.harbor_destination_name ?: ""

                    txtDeparture1.setTextColor(Color.parseColor("#616161"))
                    viewDeparture1.visibility = View.VISIBLE
                    txtReturn1.setTextColor(Color.parseColor("#A4A4A4"))
                    viewReturn1.visibility = View.INVISIBLE

                    txtDeparture2.setTextColor(Color.parseColor("#616161"))
                    viewDeparture2.visibility = View.VISIBLE
                    txtReturn2.setTextColor(Color.parseColor("#A4A4A4"))
                    viewReturn2.visibility = View.INVISIBLE

                    val sdf = SimpleDateFormat("HH:mm:ss")
                    val sdfs = SimpleDateFormat("HH:mm")

                    val startDateTime = sdf.parse(transportModel.departure_time ?: "")
                    val endDateTime = sdf.parse(transportModel.arrival_time ?: "")

                    txtOriginTime1.text = sdfs.format(startDateTime ?: Date())
                    txtDestinationTime1.text = sdfs.format(endDateTime ?: Date())

                    txtOriginTime2.text = sdfs.format(startDateTime ?: Date())
                    txtDestinationTime2.text = sdfs.format(endDateTime ?: Date())

                    isReturnClicked = false
                    fromDetail = 0
                }
            }
        }
    }

    private fun checkIsLogin(){
        if (userSession.access_token != null){
            linearLogin.visibility = View.GONE
            linearLogin2.visibility = View.GONE

            edtBookedFullname.setText(profileSession.full_name ?: "")
            edtBookedEmail.setText(profileSession.user_email ?: "")
         //   edtBookedPhoneNumber.setText(profileSession.phone_number ?: "")
        }
        else{
            linearLogin.visibility = View.VISIBLE
            linearLogin2.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setHeaderContent(){

        val packageid = packageMap["packageId"] as Int
        val name = packageMap["packageName"] as String
        val packageCurrency = packageMap["packageCurrency"] as String
        strCurrency = packageMap["packageCurrency"] as String

        if (packageid != 0){
            linearPackage.visibility = View.VISIBLE
            txtPackageName.text = name

            linearPackage2.visibility = View.VISIBLE
            txtPackageName2.text = name
        }
        else{
            linearPackage.visibility = View.GONE
            linearPackage2.visibility = View.GONE
        }

        for (i in 0 until addOnList.size){
            val addOnModel = addOnList[i]
            total_price += addOnModel.amount
        }

        rvAddonName.layoutManager = LinearLayoutManager(this)
        rvAddonName2.layoutManager = LinearLayoutManager(this)

        val newRvList = ArrayList<AddOnModel>()
        val newRvName = ArrayList<AddOnModel>()

        for (i in 1 until addOnList.size){
            newRvList.add(addOnList[i])
            newRvName.add(addOnList[i])
        }

        if (addOnList.size > 1){
            linearAddOn.visibility = View.VISIBLE
            linearAddOn2.visibility = View.VISIBLE
        }
        else{
            linearAddOn.visibility = View.GONE
            linearAddOn2.visibility = View.GONE
        }

        rvAddonName.adapter = AddonNameAdapter(this,newRvName)
        rvAddonName2.adapter = AddonNameAdapter(this,newRvName)

        rvAddonList.adapter = AdapterAddonList(this,newRvList,packageCurrency)

        fixed_total_price = total_price
        val price = CurrencyUtil.decimal(total_price).replace(",",
            ".")
        val totalGuest = adultCount + childrenCount

        val addOnTicketPrice : String

        if (paymentType == "Per Pax"){
             addOnTicketPrice = CurrencyUtil.decimal(addOnList[0].amount / totalGuest).replace(",",
                ".")
        }
        else{
             addOnTicketPrice = CurrencyUtil.decimal(addOnList[0].amount).replace(",",
                ".")
        }

        val strPrice = "$packageCurrency $price"

        val totalGuestWithoutInfant = adultCount + childrenCount

        if (totalGuestWithoutInfant > 1){
            txtPackageGuide.text = "$totalGuestWithoutInfant Guest(s)"
            txtPackageGuide2.text = "$totalGuestWithoutInfant Guest(s)"
        }
        else{
            txtPackageGuide.text = "$totalGuestWithoutInfant Guest"
            txtPackageGuide2.text = "$totalGuestWithoutInfant Guest"
        }

        if (packageid != 0){
            txtTitlePackage.text = "Price ($name)"
            txtPricePackage.text = "$packageCurrency $addOnTicketPrice (x$totalGuestWithoutInfant)"
        }
        else{
            txtTitlePackage.text = "Price"
            txtPricePackage.text = "$packageCurrency $addOnTicketPrice (x$totalGuestWithoutInfant)"
        }

        txtAddOnDate.text = selectedDateName
        txtAddOnDate2.text = selectedDateName

        txtDialogTotalPrice.text = strPrice

        txtTitleCheckout.text = experienceDetailModel.exp_title ?: ""
        txtTitleHeader2.text = experienceDetailModel.exp_title ?: ""
        txtPriceDetails.text = "$packageCurrency $price"
        txtPrice.text = strPrice
        txtPaymentType2.text = "$packageCurrency $price"
        txtPaymentTotalPrice.text = strPrice
    }

    private fun setCreditBackground(){
        if (userSession.access_token != null){
            linearCredit.setBackgroundColor(Color.parseColor("#FFFFFF"))
            linearCredit.elevationShadowColor = ColorStateList.valueOf(Color.parseColor(
                "#377BE8"))

            txtCreditName.setTextColor(Color.parseColor("#35405A"))
            txtCreditContent.setTextColor(Color.parseColor("#233E98"))
            txtCreditContent.text = CurrencyUtil.decimal(profileSession.points).replace(",",
                ".")

            imgCreditCheck.visibility = View.VISIBLE
            linearCgoCredit.visibility = View.VISIBLE
        }
        else{
            linearCredit.setBackgroundResource(0)
            linearCredit.setBackgroundColor(Color.parseColor("#F8F8F8"))
            linearCredit.elevationShadowColor = ColorStateList.valueOf(Color.parseColor(
                "#50000000"))

            txtCreditName.setTextColor(Color.parseColor("#BDBDBD"))
            txtCreditContent.setTextColor(Color.parseColor("#BDBDBD"))
            txtCreditContent.text = getString(R.string.create_account)

            imgCreditCheck.visibility = View.GONE
            linearCgoCredit.visibility = View.GONE
        }
    }

    private fun initiatePassenger(){
        for (i in 0 until adultCount){
            val adultMap = HashMap<String,Any>()
            adultMap["guest_type"] = 1
            adultMap["name"] = "Adult"
            adultMap["is_filled"] = false
            adultMap["fullname"] = ""
            adultMap["birthdate"] = ""
            adultMap["idtype"] = ""
            adultMap["idnumber"] = ""
            adultMap["type"] = ""
            passengerList.add(adultMap)
        }

        for (i in 0 until childrenCount){
            val childrenMap = HashMap<String,Any>()
            childrenMap["guest_type"] = 2
            childrenMap["name"] = "Children"
            childrenMap["is_filled"] = false
            childrenMap["fullname"] = ""
            childrenMap["birthdate"] = ""
            childrenMap["idtype"] = ""
            childrenMap["idnumber"] = ""
            childrenMap["type"] = ""
            passengerList.add(childrenMap)
        }

        for (i in 0 until infantCount){
            val infantMap = HashMap<String,Any>()
            infantMap["guest_type"] = 3
            infantMap["name"] = "Infant"
            infantMap["is_filled"] = false
            infantMap["fullname"] = ""
            infantMap["birthdate"] = ""
            infantMap["idtype"] = ""
            infantMap["idnumber"] = ""
            infantMap["type"] = ""
            passengerList.add(infantMap)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentPage (){

        if (currentPage == 1){
            btnNext.text = "continue to payment"
            view_checkout.visibility = View.VISIBLE
            view_payment.visibility = View.GONE
            view_confirmation.visibility = View.GONE

            imgBar1.setImageResource(R.drawable.ic_blue_one)
            txtBar1.text = "Fill in your data"
            txtBar1.setTextColor(Color.parseColor("#000000"))

            linearBar2.visibility = View.VISIBLE
            imgBar2.setImageResource(R.drawable.ic_gray_two)
            txtBar2.text = "Fill in your data"
            txtBar2.setTextColor(Color.parseColor("#979797"))

        }
        else if (currentPage == 2){
            btnNext.text = "pay now"
            view_checkout.visibility = View.GONE
            view_payment.visibility = View.VISIBLE
            view_confirmation.visibility = View.GONE

            imgBar1.setImageResource(R.drawable.ic_blue_two)
            txtBar1.text = "Payment"
            txtBar1.setTextColor(Color.parseColor("#000000"))

            linearBar2.visibility = View.VISIBLE
            imgBar2.setImageResource(R.drawable.ic_gray_three)
            txtBar2.text = "Confirmation"
            txtBar2.setTextColor(Color.parseColor("#979797"))
        }
        else{
            btnNext.text = "i have completed payment"
            view_checkout.visibility = View.GONE
            view_payment.visibility = View.GONE
            view_confirmation.visibility = View.VISIBLE

            imgBar1.setImageResource(R.drawable.ic_blue_three)
            txtBar1.text = "Confirmation"
            txtBar1.setTextColor(Color.parseColor("#000000"))

            linearBar2.visibility = View.GONE
            linearLogin.visibility = View.GONE
        }
    }

    private fun setPromoActionState(){
        if (!isPromoUsed){
            loadPromo()
        }
        else{
            setPromoButtonState(2)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSuccessDialog(){
        val mBottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.fragment_success_payment, null)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.setCancelable(false)
        mBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        mBottomSheetDialog.show()

        val txtBookingConfirmation = sheetView.findViewById<TextView>(R.id.txtBookingConfirmation)
        val txtTitle = sheetView.findViewById<TextView>(R.id.txtTitle)
        val txtSuccessOrigin = sheetView.findViewById<TextView>(R.id.txtSuccessOrigin)
        val txtSuccessDestination = sheetView.findViewById<TextView>(R.id.txtSuccessDestination)
        val txtPaymentSuccessPrice = sheetView.findViewById<TextView>(R.id.txtPaymentSuccessPrice)
        val linearOriginDestination  = sheetView.findViewById<LinearLayout>(R.id.linearOriginDestination)

        btnDetail = sheetView.findViewById(R.id.btnDetail)

        val price = CurrencyUtil.decimal(total_price).replace(
            ",", ".")

        if (from == 1){
            val currency = addOnModel.currency ?: ""
            val strPrice = "$currency $price"
            val booking_type = experienceDetailModel.exp_booking_type ?: ""

            if (booking_type == "Instant Booking"){
                txtBookingConfirmation.visibility = View.GONE
            }
            else{
                txtBookingConfirmation.visibility = View.VISIBLE
            }

            txtTitle.text = experienceDetailModel.exp_title ?: ""
            txtPaymentSuccessPrice.text = strPrice
        }
        else{
            txtTitle.text = "Booking Transportation"
            txtSuccessOrigin.text = transportationModel?.harbor_source_name ?: ""
            txtSuccessDestination.text = transportationModel?.harbor_destination_name ?: ""
            linearOriginDestination.visibility = View.VISIBLE

            val strPrice = "IDR $price"
            txtPaymentSuccessPrice.text = strPrice
        }

        btnDetail.setOnClickListener(this)
    }

    private fun showAdultDialog(){
        aBottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.fragment_adult, null)

        aBottomSheetDialog.setContentView(sheetView)
        aBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        adultEdtFullname  = sheetView.findViewById(R.id.adultEdtFullname)
        edtAdultEdtIdNumber = sheetView.findViewById(R.id.edtAdultEdtIdNumber)

        adultLinearTitle = sheetView.findViewById(R.id.adultLinearTitle)
        adultLinearID = sheetView.findViewById(R.id.adultLinearID)

        txtAdultTitle = sheetView.findViewById(R.id.txtAdultTitle)
        txtAdultType = sheetView.findViewById(R.id.txtAdultType)

        txtAdultType = sheetView.findViewById(R.id.txtAdultType)
        adultBtnSave = sheetView.findViewById(R.id.adultBtnSave)

        imgAdultClose = sheetView.findViewById(R.id.imgAdultClose)

        adultLinearTitle.setOnClickListener(this)
        adultLinearID.setOnClickListener(this)
        adultBtnSave.setOnClickListener(this)
    }

    private fun showChildDialog(){
        cBottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.fragment_child, null)
        cBottomSheetDialog.setContentView(sheetView)
        cBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        imgChildClose = sheetView.findViewById(R.id.imgChildClose)
        edtChildFullname = sheetView.findViewById(R.id.edtChildFullname)

        childLinearTitle = sheetView.findViewById(R.id.childLinearTitle)
        childLinearBirthday = sheetView.findViewById(R.id.childLinearBirthday)

        txtChildTitle = sheetView.findViewById(R.id.txtChildTitle)
        txtChildBirthDay = sheetView.findViewById(R.id.txtChildBirthDay)

        btnChildSave = sheetView.findViewById(R.id.btnChildSave)

        childLinearBirthday.setOnClickListener(this)
        childLinearTitle.setOnClickListener(this)
        imgChildClose.setOnClickListener(this)
        btnChildSave.setOnClickListener(this)
        linearPromo.setOnClickListener(this)
    }

    private fun setNextPage(){
        if (currentPage == 1){
            if (from == 1){
                createExperienceBooking()
            }
            else{
                createTransportationBooking()
            }
        }
        else if (currentPage == 2){
            if (from == 1){
                createExperiencePayment()
            }
            else{
                createTransportationPayment()
            }
        }
        else{
            bookingController.getDetailBooking(order_id,this)
        }
    }

    private fun showTitleMenu(view : View, title : String, textView : TextView){
        val popupMenu = PopupMenu(this,view)
        popupMenu.menuInflater.inflate(R.menu.title_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            textView.text = menuItem.title.toString().trim()
            true
        }
        popupMenu.show()
    }

    private fun showPhoneMenu(view : View, title : String, textView : TextView){
        val popupMenu = PopupMenu(this,view)
        popupMenu.menuInflater.inflate(R.menu.phone_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            textView.text = menuItem.title.toString().trim()
            true
        }
        popupMenu.show()
    }

    private fun showIdMenu(view : View, title : String, textView : TextView){
        val popupMenu = PopupMenu(this,view)
        popupMenu.menuInflater.inflate(R.menu.id_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            textView.text = menuItem.title.toString().trim()
            true
        }
        popupMenu.show()
    }

    private fun setCalenderAlertDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_calender_dialog,null)
        dialogCalendar = Dialog(this,R.style.FullWidth_Dialog)
        dialogCalendar.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogCalendar.setContentView(view)
        dialogCalendar.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        calendarView = view.findViewById(R.id.calendarView)
        txtMonthYear = view.findViewById(R.id.txtMonthYear)
        calendarBtnApply = view.findViewById(R.id.calendarBtnApply)

        imgNext = view.findViewById(R.id.imgNext)
        imgPrev = view.findViewById(R.id.imgPrevious)

        imgNext.setOnClickListener(this)
        imgPrev.setOnClickListener(this)
        calendarBtnApply.setOnClickListener(this)

        setCalendar()
    }

    private fun setCalendar(){
        val daysOfWeek = CalendarUtil.daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(240)
        val endMonth = currentMonth.plusMonths(240)

        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {

            lateinit var day: CalendarDay

            val textView = view.exOneDayText

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.size > 0){
                            selectedDates.removeAt(0)
                            selectedDates.add(day.date)
                        }
                        else{
                            selectedDates.add(day.date)
                        }

                        calendarBtnApply.isEnabled = true
                        calendarBtnApply.setTextColor(Color.parseColor("#FFFFFF"))
                        calendarView.notifyCalendarChanged()
                    }
                }
            }
        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {

                container.day = day

                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    if (selectedDates.contains(day.date)){
                        textView.setTextColor(Color.parseColor("#FFFFFF"))
                        textView.setBackgroundResource(R.drawable.background_calender_selected)
                    }
                    else{
                        textView.setTextColor(Color.parseColor("#000000"))
                        textView.setBackgroundColor(0)
                    }
                }
                else {
                    textView.setTextColor(Color.parseColor("#BDBDBD"))
                    textView.setBackgroundColor(0)
                }
            }
        }

        calendarView.monthScrollListener = {
            currentCalendarMonth = it
            val year = it.yearMonth.year.toString()
            val month = monthTitleFormatter.format(it.yearMonth)
            val yearMonth = "$month $year"
            txtMonthYear.text = yearMonth
        }
    }

    private fun setAdultPassenger(){
        val fullname = adultEdtFullname.text.toString().trim()
        val title = txtAdultTitle.text.toString().trim()
        val type = txtAdultType.text.toString().trim()
        val idNumber = edtAdultEdtIdNumber.text.toString().trim()

        if (fullname.isEmpty()){
            Toast.makeText(this,"Enter passenger name", Toast.LENGTH_SHORT).show()
            return
        }

        if (idNumber.isEmpty()){
            Toast.makeText(this,"Enter ID number", Toast.LENGTH_SHORT).show()
            return
        }

        passengerList[currentPassengerPosition]["is_filled"] = true
        passengerList[currentPassengerPosition]["fullname"] = "$title $fullname"
        passengerList[currentPassengerPosition]["birthdate"] = ""
        passengerList[currentPassengerPosition]["idtype"] = type
        passengerList[currentPassengerPosition]["idnumber"] = idNumber
        passengerList[currentPassengerPosition]["type"] = type

        rvGuest.adapter = PassengerAdapter(this,passengerList,this)
        aBottomSheetDialog.dismiss()
    }

    private fun setChildPassenger(){
        val fullname = edtChildFullname.text.toString().trim()
        val title = txtChildTitle.text.toString().trim()
        val birthdate = txtChildBirthDay.text.toString().trim()

        if (fullname.isEmpty()){
            Toast.makeText(this,"Enter passenger name", Toast.LENGTH_SHORT).show()
            return
        }

        if (birthdate.isEmpty()){
            Toast.makeText(this,"Select birth date", Toast.LENGTH_SHORT).show()
            return
        }

        passengerList[currentPassengerPosition]["is_filled"] = true
        passengerList[currentPassengerPosition]["fullname"] = "$title $fullname"
        passengerList[currentPassengerPosition]["birthdate"] = birthdate

        rvGuest.adapter = PassengerAdapter(this,passengerList,this)
        cBottomSheetDialog.dismiss()
    }

    private fun selectBirthDate(){
        val strDate = selectedDates[0].toString()
        txtChildBirthDay.text = strDate
        txtChildBirthDay.setTextColor(Color.parseColor("#000000"))
        dialogCalendar.dismiss()
    }

    private fun setBackDialog(){
        backDialog = AlertDialog.Builder(this)
            .setMessage("Back to previous page ?")
            .setPositiveButton("Yes"
            ) { _, _ -> finish() }
            .setNegativeButton("No"
            ) { dialog, _ -> dialog?.dismiss() }
            .create()
    }

    private fun loadPromo(){
        val code = edtPromo.text.toString().trim()

        if (code.isEmpty()){
            ViewUtil.showBlackToast(this,"Enter promo code",0).show()
            return
        }

        PromoController.getSpecialPromo(code,promoType,this)
    }

    private fun setPromoState(){

        if (!isPromoChecked){

            if (promoModel != null){
                isPromoUsed = true
                linearPromoDetail.visibility = View.VISIBLE
                linearTotalPromo.visibility = View.VISIBLE
                linearPricePromo.visibility = View.VISIBLE
                setTotalPrice()
            }

            linearBluePromo.visibility = View.VISIBLE
            linearPromo.setBackgroundResource(R.drawable.background_add_on)
            imgDonePromo.setBackgroundResource(R.drawable.ic_checkmark_circle)

            isPromoChecked = true
        }
        else{
            linearBluePromo.visibility = View.GONE

            if (isPromoChecked && isPromoUsed){
                linearPromo.setBackgroundColor(Color.parseColor("#ffffff"))
                linearPromo.setBackgroundResource(R.drawable.background_add_on)
                imgDonePromo.setBackgroundResource(R.drawable.ic_checkmark_circle)
            }
            else{
                linearPromo.setBackgroundResource(0)
                linearPromo.setBackgroundColor(Color.parseColor("#ffffff"))
                imgDonePromo.setBackgroundResource(R.drawable.ic_gray_blankcircle)
                linearPricePromo.visibility = View.GONE
            }

            if (from == 1){
                if (!isCreditsUsed && !isPromoUsed){
                    linearPromoDetail.visibility = View.GONE
                }
            }

            isPromoChecked = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCreditButtonState(){
        if (userSession.access_token != null){

            if (!isCreditsChecked){

                imgCreditCheck.setImageResource(R.drawable.ic_checktype_blue)

                linearPromoDetail.visibility = View.VISIBLE
                linearTotalCredit.visibility = View.VISIBLE

                linearCredit.setBackgroundColor(Color.parseColor("#FFFFFF"))
                linearCredit.setBackgroundResource(R.drawable.background_add_on)

                val totalCredit = CurrencyUtil.decimal(profileSession.points).replace(
                    ",", ".")

                if (from == 1){
                    txtTotalCredits.text = "- $strCurrency $totalCredit"
                    txtPriceCredit.text = "- $strCurrency $totalCredit"
                    linearPriceCredit.visibility = View.VISIBLE
                }
                else{
                    transportPrice?.let {
                        val currency = it["currency_label"] as String
                        txtTotalCredits.text = "- $currency $totalCredit"
                    }
                }

                isCreditsUsed = true
                isCreditsChecked = true
            }
            else{

                imgCreditCheck.setImageResource(R.drawable.ic_gray_blankcircle)
                linearTotalCredit.visibility = View.GONE

                linearCredit.setBackgroundResource(0)
                linearCredit.setBackgroundColor(Color.parseColor("#FFFFFF"))

                if (from == 1){
                    if (!isPromoUsed){
                        linearPromoDetail.visibility = View.GONE
                        linearPriceCredit.visibility = View.GONE
                    }
                }

                isCreditsUsed = false
                isCreditsChecked = false
            }
        }

        setTotalPrice()
    }

    @SuppressLint("SetTextI18n")
    private fun setPromoButtonState(fromPromo : Int){

        if (fromPromo == 1){
            btnPromo.text = "REMOVE"
            btnPromo.setTextColor(Color.parseColor("#EE5858"))
            btnPromo.setBackgroundResource(R.drawable.background_promo_remove)

            imgCheckPromo.visibility = View.VISIBLE
            linearPromoDetail.visibility = View.VISIBLE
            linearTotalPromo.visibility = View.VISIBLE
            linearPricePromo.visibility = View.VISIBLE

            isPromoUsed = true
        }
        else{
            btnPromo.text = "APPLY"
            btnPromo.setTextColor(Color.parseColor("#FFFFFF"))
            btnPromo.setBackgroundResource(R.drawable.background_bluerounded)

            imgCheckPromo.visibility = View.GONE
            linearTotalPromo.visibility = View.GONE
            linearPricePromo.visibility = View.GONE

            promoID = ""
            promoModel = null
            isPromoUsed = false
        }

        setTotalPrice()
    }

    fun setPaymentMethod(fromPayment : Int){
        if (fromPayment == 1){
            if (!isBankViewClicked){
                rvBank.visibility = View.VISIBLE
                imgBankArrow.setImageResource(R.drawable.ic_arrow_down)
                isBankViewClicked = true
            }
            else{
                rvBank.visibility = View.GONE
                imgBankArrow.setImageResource(R.drawable.ic_right_arrow)
                isBankViewClicked = false
            }

            linearBankPayment.setBackgroundColor(Color.parseColor("#ffffff"))
            linearBankPayment.setBackgroundResource(R.drawable.background_add_on)

            linearCCMethod.setBackgroundResource(0)
            linearCCMethod.setBackgroundColor(Color.parseColor("#ffffff"))

            linearPaypalMethod.setBackgroundResource(0)
            linearPaypalMethod.setBackgroundColor(Color.parseColor("#ffffff"))

            linearCCContent.visibility = View.GONE

            if (isBankCLicked){
                paymentMethodModel = bankMethodList[bankPosition]
            }
        }
        else{
            rvBank.visibility = View.GONE
            linearBankPayment.setBackgroundResource(0)
            linearBankPayment.setBackgroundColor(Color.parseColor("#ffffff"))

            linearCCMethod.setBackgroundColor(Color.parseColor("#ffffff"))
            linearCCMethod.setBackgroundResource(R.drawable.background_add_on)

            linearPaypalMethod.setBackgroundResource(0)
            linearPaypalMethod.setBackgroundColor(Color.parseColor("#ffffff"))

            isBankViewClicked = false
            paymentMethodModel = creditCardMethodList[0]

            if (!isCCViewClicked){
                linearCCContent.visibility = View.VISIBLE
                imgCCArrow.setImageResource(R.drawable.ic_arrow_down)
                isCCViewClicked = true
            }
            else{
                linearCCContent.visibility = View.GONE
                imgCCArrow.setImageResource(R.drawable.ic_right_arrow)
                isCCViewClicked = false
            }
        }
    }

    private fun downPaymentPrice(price : Long) : Long {
        return (price * paymentTypePercentage) / 100
    }

    @SuppressLint("SetTextI18n")
    private fun setFullDownPayment(payment : Int){

        if (payment == 1){
            txtFullPayment.setBackgroundResource(R.drawable.background_payment)
            txtFullPayment.setTextColor(Color.parseColor("#233E98"))

            txtDownPayment.setBackgroundResource(R.drawable.background_more)
            txtDownPayment.setTextColor(Color.parseColor("#35405A"))

            txtPaymentTypeTitle2.text = "Full payment"
            txtPaymentLearnMore.visibility = View.GONE

            paymentPosition = 0
        }
        else{
            txtFullPayment.setBackgroundResource(R.drawable.background_more)
            txtFullPayment.setTextColor(Color.parseColor("#35405A"))

            txtDownPayment.setBackgroundResource(R.drawable.background_payment)
            txtDownPayment.setTextColor(Color.parseColor("#233E98"))

            txtPaymentTypeTitle2.text = "Down Payment $paymentTypePercentage%"
            txtDpTotalPrice.text = "(Down Payment $paymentTypePercentage%)"
            txtDp.text = "Down Payment ($paymentTypePercentage%)"
            txtPaymentLearnMore.visibility = View.VISIBLE

            paymentPosition = 1
        }

        if (from == 1){

            var paymentPrice: Long

            if (paymentPosition == 0){
                paymentPrice = fixed_total_price
            }
            else{
                paymentPrice = downPaymentPrice(total_price)
            }
            val price = CurrencyUtil.decimal(paymentPrice).replace(",",
                ".")
            val currency = packageMap["packageCurrency"] as String

            txtPaymentType2.text = "$currency $price"
            txtPriceDetails.text = "$currency $price"
        }
        else{

        }

        setTotalPrice()

    }

    private fun createExperienceBooking(){

        val bookedTitle = txtBookedTitle.text.toString().trim()
        val bookedFullname = edtBookedFullname.text.toString().trim()
        val bookedEmail = edtBookedEmail.text.toString().trim()
        val bookedCountryCode = txtBookedPhone.text.toString().trim()
        val bookedPhoneNumber = edtBookedPhoneNumber.text.toString().trim()
        val bookedIdCard = txtBookedID.text.toString().trim()
        val bookedIdNumber = edtBookedIdNumber.text.toString().trim()

        if (bookedFullname.isEmpty()){
            linearFullname.setBackgroundResource(R.drawable.background_border_red)
            txtFullnameError.visibility = View.VISIBLE
            return
        }
        else{
            linearFullname.setBackgroundResource(R.drawable.background_border)
            txtFullnameError.visibility = View.GONE
        }

        if (bookedEmail.isEmpty()){
            linearEmail.setBackgroundResource(R.drawable.background_border_red)
            txtEmailAddressError.visibility = View.VISIBLE
            txtEmailAddressError.text = "Fill in your email address"
            return
        }
        else{
            linearEmail.setBackgroundResource(R.drawable.background_border)
            txtEmailAddressError.visibility = View.GONE
        }

        if (!bookedEmail.isEmailValid()){
            linearEmail.setBackgroundResource(R.drawable.background_border_red)
            txtEmailAddressError.visibility = View.VISIBLE
            txtEmailAddressError.text = "Please enter valid email format"
            return
        }
        else{
            linearEmail.setBackgroundResource(R.drawable.background_border)
            txtEmailAddressError.visibility = View.GONE
        }

        if (bookedPhoneNumber.isEmpty()){
            linearPhoneNumber.setBackgroundResource(R.drawable.background_border_red)
            txtPhoneError.visibility = View.VISIBLE
            return
        }
        else{
            linearPhoneNumber.setBackgroundResource(R.drawable.background_border)
            txtPhoneError.visibility = View.GONE
        }

        if (bookedIdNumber.isEmpty()){
            linearId.setBackgroundResource(R.drawable.background_border_red)
            txtIdError.visibility = View.VISIBLE
            return
        }
        else{
            linearId.setBackgroundResource(R.drawable.background_border)
            txtIdError.visibility = View.GONE
        }

        var isFilled = false

        for (i in 0 until passengerList.size){
            val passengerMap = passengerList[i]
            isFilled = passengerMap["is_filled"] as Boolean

            if (!isFilled){
                break
            }
        }

        if (!isFilled){
            ViewUtil.showBlackToast(this,"Please fill passenger details",0).show()
            return
        }

        val strPhoneNumber = bookedCountryCode.removeSuffix(".")+""+bookedPhoneNumber
        val bookedJsonArray = JSONArray()
        val guestJsonArray = JSONArray()
        val bookedJsonObject = JSONObject()

        bookedJsonObject.put("title",bookedTitle)
        bookedJsonObject.put("fullname",bookedFullname)
        bookedJsonObject.put("email",bookedEmail)
        bookedJsonObject.put("phonenumber",strPhoneNumber)
        bookedJsonObject.put("idtype",bookedIdCard)
        bookedJsonObject.put("idnumber",bookedIdNumber)

        bookedJsonArray.put(bookedJsonObject)

        for (i in 0 until passengerList.size){
            val passengerCreatemap = passengerList[i]
            val guestJsonObject = JSONObject()

            val passengerFullname = passengerCreatemap["fullname"] as String
            val passengerBirthdate = passengerCreatemap["birthdate"] as String
            val passengerIdType = passengerCreatemap["idtype"] as String
            val passengerIdNumber = passengerCreatemap["idnumber"] as String
            val passengerType = passengerCreatemap["name"] as String

            guestJsonObject.put("fullname",passengerFullname)
            guestJsonObject.put("birthdate",passengerBirthdate)
            guestJsonObject.put("idtype",passengerIdType)
            guestJsonObject.put("idnumber",passengerIdNumber)
            guestJsonObject.put("type",passengerType)

            guestJsonArray.put(guestJsonObject)
        }

        var strEmail = ""

        if (userSession.access_token != null){
            strEmail = profileSession.user_email ?: ""
        }
        else{
            strEmail = bookedEmail
        }

        val packageId = packageMap["packageId"] as Int

        val dataMap = HashMap<String,Any>()
        dataMap["id"] = ""
        dataMap["exp_id"] = experienceDetailModel.experience_id ?: ""
        dataMap["guest_desc"] = guestJsonArray.toString()
        dataMap["booked_by"] = bookedJsonArray.toString()
        dataMap["booked_by_email"] = strEmail
        dataMap["booked_date"] = date
        dataMap["user_id"] = profileSession.id ?: ""
        dataMap["trans_id"] = ""
        dataMap["status"] = 0
        dataMap["experience_add_on_id"] = addOnModel.id ?: ""
        dataMap["package_id"] = packageId
        dataMap["x-mode"] = "mobile"

        if (selectedGuideMap != null){
            val guideId = selectedGuideMap!!["guide_id"] as String
            dataMap["guide_id"] = guideId
        }

        if (addOnList.size > 1){
            val addOnJsonArray = JSONArray()

            for (i in 1 until addOnList.size){
                val addOnId = addOnList[i].id ?: ""
                val addOnJsonObject = JSONObject()

                addOnJsonObject.put("id",addOnId)

                addOnJsonArray.put(addOnJsonObject)
            }

            dataMap["experience_add_on_id"] = addOnJsonArray.toString()
        }

        bookingController.createBooking(dataMap,this)
    }

    @SuppressLint("SetTextI18n")
    private fun createTransportationBooking(){
        val bookedTitle = txtBookedTitle.text.toString().trim()
        val bookedFullname = edtBookedFullname.text.toString().trim()
        val bookedEmail = edtBookedEmail.text.toString().trim()
        val bookedCountryCode = txtBookedPhone.text.toString().trim()
        val bookedPhoneNumber = edtBookedPhoneNumber.text.toString().trim()
        val bookedIdCard = txtBookedID.text.toString().trim()
        val bookedIdNumber = edtBookedIdNumber.text.toString().trim()

        if (bookedFullname.isEmpty()){
            linearFullname.setBackgroundResource(R.drawable.background_border_red)
            txtFullnameError.visibility = View.VISIBLE
            return
        }
        else{
            linearFullname.setBackgroundResource(R.drawable.background_border)
            txtFullnameError.visibility = View.GONE
        }

        if (bookedEmail.isEmpty()){
            linearEmail.setBackgroundResource(R.drawable.background_border_red)
            txtEmailAddressError.visibility = View.VISIBLE
            txtEmailAddressError.text = "Fill in your email address"
            return
        }
        else{
            linearEmail.setBackgroundResource(R.drawable.background_border)
            txtEmailAddressError.visibility = View.GONE
        }

        if (!bookedEmail.isEmailValid()){
            linearEmail.setBackgroundResource(R.drawable.background_border_red)
            txtEmailAddressError.visibility = View.VISIBLE
            txtEmailAddressError.text = "Please enter valid email format"
            return
        }
        else{
            linearEmail.setBackgroundResource(R.drawable.background_border)
            txtEmailAddressError.visibility = View.GONE
        }

        if (bookedPhoneNumber.isEmpty()){
            linearPhoneNumber.setBackgroundResource(R.drawable.background_border_red)
            txtPhoneError.visibility = View.VISIBLE
            return
        }
        else{
            linearPhoneNumber.setBackgroundResource(R.drawable.background_border)
            txtPhoneError.visibility = View.GONE
        }

        if (bookedIdNumber.isEmpty()){
            linearId.setBackgroundResource(R.drawable.background_border_red)
            txtIdError.visibility = View.VISIBLE
            return
        }
        else{
            linearId.setBackgroundResource(R.drawable.background_border)
            txtIdError.visibility = View.GONE
        }

        var isFilled = false

        for (i in 0 until passengerList.size){
            val passengerMap = passengerList[i]
            isFilled = passengerMap["is_filled"] as Boolean

            if (!isFilled){
                break
            }
        }

        if (!isFilled){
            ViewUtil.showBlackToast(this,"Please fill passenger details",0).show()
            return
        }

        val strPhoneNumber = bookedCountryCode.removeSuffix(".")+""+bookedPhoneNumber
        val bookedJsonArray = JSONArray()
        val guestJsonArray = JSONArray()
        val bookedJsonObject = JSONObject()

        bookedJsonObject.put("title",bookedTitle)
        bookedJsonObject.put("fullname",bookedFullname)
        bookedJsonObject.put("email",bookedEmail)
        bookedJsonObject.put("phonenumber",strPhoneNumber)
        bookedJsonObject.put("idtype",bookedIdCard)
        bookedJsonObject.put("idnumber",bookedIdNumber)

        bookedJsonArray.put(bookedJsonObject)

        for (i in 0 until passengerList.size){
            val passengerCreatemap = passengerList[i]
            val guestJsonObject = JSONObject()

            val passengerFullname = passengerCreatemap["fullname"] as String
            val passengerBirthdate = passengerCreatemap["birthdate"] as String
            val passengerIdType = passengerCreatemap["idtype"] as String
            val passengerIdNumber = passengerCreatemap["idnumber"] as String
            val passengerType = passengerCreatemap["name"] as String

            guestJsonObject.put("fullname",passengerFullname)
            guestJsonObject.put("birthdate",passengerBirthdate)
            guestJsonObject.put("idtype",passengerIdType)
            guestJsonObject.put("idnumber",passengerIdNumber)
            guestJsonObject.put("type",passengerType)

            guestJsonArray.put(guestJsonObject)
        }

        var strEmail = ""

        if (userSession.access_token != null){
            strEmail = profileSession.user_email ?: ""
        }
        else{
            strEmail = bookedEmail
        }

        val transportID = transportationModel?.transportation_id ?: ""
        val scheduleID = transportationModel?.schedule_id ?: ""

        val transportReturnID = transportationModelReturn?.transportation_id ?: ""
        val scheduleReturnID = transportationModelReturn?.schedule_id ?: ""

        val dataMap = HashMap<String,Any>()
        dataMap["id"] = ""
        dataMap["guest_desc"] = guestJsonArray.toString()
        dataMap["booked_by"] = bookedJsonArray.toString()
        dataMap["booked_by_email"] = strEmail
        dataMap["booked_date"] = date
        dataMap["user_id"] = profileSession.id ?: ""
        dataMap["status"] = 0

        if (isReturn == 0){
            dataMap["trans_id"] = transportID
            dataMap["schedule_id"] = scheduleID
        }
        else{
            dataMap["trans_id"] = transportID
            dataMap["schedule_id"] = scheduleID
            dataMap["trans_return_id"] = transportReturnID
            dataMap["schedule_return_id"] = scheduleReturnID
        }

        bookingController.createBooking(dataMap,this)
    }

    @SuppressLint("SetTextI18n")
    private fun showLearnMore(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_learn_more,null)
        dialogType = Dialog(this,R.style.FullWidth_Dialog)
        dialogType.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogType.setContentView(view)
        dialogType.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        learnClose = view.findViewById(R.id.learnClose)
        learnClose.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalPrice(){
              if (from == 1){

                  var currentPrice = fixed_total_price

                  if (promoModel != null && isPromoUsed){

                      promoModel?.let { model ->
                          val promoType = model.promo_type
                          val promoAmount = model.promo_value

                          if (promoType == 0){
                              currentPrice -= promoAmount

                              txtPromoPrice.text = "- $strCurrency " +CurrencyUtil
                                  .decimal(promoAmount).replace(",", ".")

                              txtPricePromo.text = "- $strCurrency " +CurrencyUtil
                                  .decimal(promoAmount).replace(",", ".")
                          }
                          else{
                              val promoPricePercentage = (currentPrice * promoAmount) / 100
                              currentPrice -= promoPricePercentage

                              txtPromoPrice.text = "- $strCurrency " +CurrencyUtil
                                  .decimal(promoPricePercentage).replace(",",
                                  ".")

                              txtPricePromo.text = "- $strCurrency " +CurrencyUtil
                                  .decimal(promoPricePercentage).replace(",",
                                      ".")
                          }

                          promoID = model.id ?: ""
                      }
                  }

                  if (isCreditsUsed){
                      currentPrice -= profileSession.points
                  }

                  currentSubtotalPrice = currentPrice
                  val subTotalPrice = CurrencyUtil.decimal(currentPrice).replace(",",
                      ".")
                  val strSubTotalPrice = "$strCurrency $subTotalPrice"
                  txtSubTotalPrice.text = strSubTotalPrice

                  if (paymentPosition == 0){
                      if (promoModel != null && isPromoUsed || isCreditsUsed){
                          viewDialog1.visibility = View.VISIBLE
                      }
                      else{
                          viewDialog1.visibility = View.GONE
                      }

                      linearPriceSubtotal.visibility = View.GONE
                      linearPriceDp.visibility = View.GONE
                      viewDialog2.visibility = View.GONE
                      txtDpTotalPrice.visibility = View.GONE
                  }
                  else{
                      if (promoModel != null && isPromoUsed || isCreditsUsed){
                          viewDialog1.visibility = View.VISIBLE

                          linearPriceSubtotal.visibility = View.VISIBLE
                          txtDpTotalPrice.visibility = View.VISIBLE
                          linearPriceDp.visibility = View.GONE
                      }
                      else{
                          viewDialog1.visibility = View.GONE

                          linearPriceSubtotal.visibility = View.VISIBLE
                          linearPriceDp.visibility = View.VISIBLE
                          txtDpTotalPrice.visibility = View.GONE
                      }

                      viewDialog2.visibility = View.VISIBLE
                      currentPrice = downPaymentPrice(currentPrice)
                  }

                  val price = CurrencyUtil.decimal(currentPrice).replace(",",
                      ".")
                  val strPrice = "$strCurrency $price"

                  total_price = currentPrice

                  txtPaymentType2.text = strPrice
                  txtPaymentTotalPrice.text = strPrice
                  txtDialogTotalPrice.text = strPrice
                  txtPriceDp.text = strPrice
              }
              else{
                  var currentPrice = totalPricePayment
                  val priceMap = transportationModel?.price ?: HashMap()
             //     val strCurency = priceMap["currency_label"] as String

                  if (promoModel != null && isPromoUsed){

                      promoModel?.let { model ->
                          val promoType = model.promo_type
                          val promoAmount = model.promo_value

                          if (promoType == 0){
                              currentPrice -= promoAmount

                              txtPromoPrice.text = "- IDR " +CurrencyUtil
                                  .decimal(promoAmount).replace(",", ".")
                          }
                          else{
                              val promoPricePercentage = (currentPrice * promoAmount) / 100
                              currentPrice -= promoPricePercentage

                              txtPromoPrice.text = "- IDR " +CurrencyUtil
                                  .decimal(promoPricePercentage).replace(",",
                                      ".")
                          }

                          promoID = model.id ?: ""
                      }
                  }

                  if (isCreditsUsed){
                      currentPrice -= profileSession.points
                  }

                  val price = CurrencyUtil.decimal(currentPrice).replace(",",
                      ".")
                  val strPrice = "$strCurrency $price"

                  total_price = currentPrice
                  txtPaymentTotalPrice.text = strPrice
              }
        }

    private fun createExperiencePayment(){
        if (paymentMethodModel == null){
            ViewUtil.showBlackToast(this,"Please select payment method",0).show()
            return
        }

        val dataMap = HashMap<String,Any>()

        val paymentName = paymentMethodModel?.name ?: ""

        if (paymentName == "Bank BRI" || paymentName == "Credit Card" || isPaypal){
            if (isPaypal){
                dataMap["paypal_order_id"] = payID
            }
            else if (paymentName == "Credit Card"){

                if (!isCCViewClicked){
                    linearCCContent.visibility = View.VISIBLE
                    imgCCArrow.setImageResource(R.drawable.ic_arrow_down)
                    isCCViewClicked = true
                }

                if (edtCardName.text.toString().trim().isEmpty()){
                    txtCardNameError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtCardNameError.visibility = View.GONE
                }

                if (edtCardNumber.text.toString().trim().isEmpty()){
                    txtCardError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtCardError.visibility = View.GONE
                }

                if (edtMonthCC.text.toString().trim().isEmpty() || edtMonthCC.text.toString()
                        .trim().length < 2 || edtYearCC.text.toString().trim().isEmpty() ||
                         edtYearCC.text.toString().trim().length < 2){
                    txtExpirationError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtExpirationError.visibility = View.GONE
                }

                if (edtCvvNumber.text.toString().trim().isEmpty()){
                    txtCvvError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtCvvError.visibility = View.GONE
                }
            }

            fromPaymentMethod = 1

        }
        else{
            fromPaymentMethod = 0
        }

        dataMap["booking_id"] = checkoutMap["id"] as String
        dataMap["booking_type"] = 0
        dataMap["promo_id"] = promoID
        dataMap["cc_token_id"] = ""
        dataMap["cc_auth_id"] = ""
        dataMap["user_id"] = profileSession.id ?: ""
        dataMap["payment_method_id"] = paymentMethodModel?.id ?: ""
        dataMap["experience_payment_id"] = paymentMethodList[paymentPosition] ?: ""
        dataMap["original_price"] = currentSubtotalPrice
        dataMap["total_price"] = total_price
        dataMap["currency"] = "IDR"

        if (userSession.access_token != null){
            if (isCreditsUsed){
                dataMap["points"] = profileSession.points
            }
            else{
                dataMap["points"] = 0
            }
        }

        dataMap["ex_change_rates"] = 1
        dataMap["ex_change_currency"] = "IDR"

        if (paymentName == "Credit Card"){
            val cardName = edtCardName.text.toString().trim()
            val cardNumber = edtCardNumber.text.toString().trim()
            val cardMonth = edtMonthCC.text.toString().trim()
            val cardYear = edtYearCC.text.toString().trim()
            val cardCVV = edtCvvNumber.text.toString().trim()

            createCCPaymentTransaction(cardName,cardNumber,
                cardMonth,cardYear,cardCVV,dataMap)
        }
        else{
            transactionController.createPaymentTransaction(dataMap,fromPaymentMethod,this)
        }
    }

    private fun createTransportationPayment(){
        if (paymentMethodModel == null){
            ViewUtil.showBlackToast(this,"Please select payment method",0).show()
            return
        }
        val dataMap = HashMap<String,Any>()
        val paymentName = paymentMethodModel?.name ?: ""

        if (paymentName == "Bank BRI" || paymentName == "Credit Card" || isPaypal){
            if (isPaypal){
                dataMap["paypal_order_id"] = payID
            }
            else if (paymentName == "Credit Card"){

                if (!isCCViewClicked){
                    linearCCContent.visibility = View.VISIBLE
                    imgCCArrow.setImageResource(R.drawable.ic_arrow_down)
                    isCCViewClicked = true
                }

                if (edtCardName.text.toString().trim().isEmpty()){
                    txtCardNameError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtCardNameError.visibility = View.GONE
                }

                if (edtCardNumber.text.toString().trim().isEmpty()){
                    txtCardError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtCardError.visibility = View.GONE
                }

                if (edtMonthCC.text.toString().trim().isEmpty() || edtMonthCC.text.toString()
                        .trim().length < 2 || edtYearCC.text.toString().trim().isEmpty() ||
                    edtYearCC.text.toString().trim().length < 2){
                    txtExpirationError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtExpirationError.visibility = View.GONE
                }

                if (edtCvvNumber.text.toString().trim().isEmpty()){
                    txtCvvError.visibility = View.VISIBLE
                    return
                }
                else{
                    txtCvvError.visibility = View.GONE
                }
            }
            fromPaymentMethod = 1
        }
        else{
            fromPaymentMethod = 0
        }

        dataMap["booking_id"] = ""
        dataMap["booking_type"] = 1
        dataMap["promo_id"] = promoID
        dataMap["cc_token_id"] = ""
        dataMap["cc_auth_id"] = ""
        dataMap["payment_method_id"] = paymentMethodModel?.id ?: ""
        dataMap["total_price"] = total_price
        dataMap["currency"] = "IDR"
        dataMap["order_id"] = checkoutMap["order_id"] as String
        dataMap["user_id"] = profileSession.id ?: ""

        if (paymentName == "Credit Card"){
            val cardName = edtCardName.text.toString().trim()
            val cardNumber = edtCardNumber.text.toString().trim()
            val cardMonth = edtMonthCC.text.toString().trim()
            val cardYear = edtYearCC.text.toString().trim()
            val cardCVV = edtCvvNumber.text.toString().trim()

            createCCPaymentTransaction(cardName,cardNumber,
                cardMonth,cardYear,cardCVV,dataMap)
        }
        else{
            transactionController.createPaymentTransaction(dataMap,fromPaymentMethod,this)
        }
    }

    private fun createCCPaymentTransaction(name : String, cardNumber : String, monthExpired : String,
                                           yearExpired : String, cvv : String, dataMap: HashMap<String, Any>){

        loadingDialog.show()

        val expiredYear = generateYearIndex()+yearExpired
        val xenditKey = getString(R.string.xendit_key)
        val xendit = Xendit(applicationContext,xenditKey)
        val card = Card(cardNumber, monthExpired, expiredYear, cvv)

        xendit.createSingleUseToken(card,total_price.toInt(),true, object : TokenCallback() {
            override fun onSuccess(token: Token?) {
                 token?.let {
                      val token_id = it.id
                      dataMap["cc_token_id"] = token_id
                      dataMap["cc_auth_id"] = it.authenticationId

                     transactionController.createPaymentTransaction(dataMap,fromPaymentMethod,
                         this@ActivityCheckout)

                 }
            }

            override fun onError(error: XenditError?) {
                loadingDialog.dismiss()
                ViewUtil.showBlackToast(this@ActivityCheckout,error?.errorMessage ?: "",1).show()
                Log.d("xendit_error",error?.errorCode ?: "")
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateYearIndex() : String {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy")
        val strYear = sdf.format(date)
        return "${strYear[0]}${strYear[1]}"
    }

    @SuppressLint("SimpleDateFormat")
    private fun setConfirmation(){
            val myBookingController  = BookingController()
            myBookingController.getDetailBooking(order_id, object : MyCallback.Companion.DetailBookingCallback {
                override fun onDetailBookingPrepare() {

                }

                @SuppressLint("SetTextI18n")
                override fun onDetailBookingSuccess(data: HashMap<String, Any>) {
                    val createdDateTransaction = data["created_date_transaction"] as String
                    val expiredDatePayment = data["expired_date_payment"] as String
                    val totalPriceBooking = data["total_price"] as Long

                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                    val sdfDate = SimpleDateFormat("EEE, dd-MMM-yyyy")
                    val sdfTime = SimpleDateFormat("HH:mm")

                    val nowDate = Calendar.getInstance()
                    val createDate = sdf.parse(createdDateTransaction) ?: Date()
                    val expiredDate = sdf.parse(expiredDatePayment) ?: Date()

                    val strPaymentDate = sdfDate.format(expiredDate.time)
                    val strPaymentTime = sdfTime.format(expiredDate.time)
                    val strPaymentDateTime = "$strPaymentDate at $strPaymentTime"

                    txtDatePayment.text = strPaymentDateTime
                    txtOrderID.text = order_id
                    txtPaymentName.text = data["payment_type"] as String
                    txtAccNumber.text = data["account_number"] as String
                    txtTransferAmount.text = "IDR ${CurrencyUtil.decimal(totalPriceBooking).replace(",",".")}"

                    val miliTime = expiredDate.time - nowDate.timeInMillis

                    setCoundown(miliTime,txtHour,txtMinute)
                }

                override fun onDetailBookingError(message: String) {

                }
            })
    }

    private fun setCoundown(timemilis : Long, txtHour : TextView, txtMinute : TextView){
          if (timemilis > 0){

              if (countDownTimer != null){
                  countDownTimer!!.cancel()
              }

              countDownTimer = object : CountDownTimer(timemilis, 1000) {
                  override fun onTick(p0: Long) {
                      val millis: Long = p0

                      val hour = String.format("%02d", TimeUnit.MILLISECONDS.toHours(millis)%60)
                      val minute = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millis)%60)

                      txtHour.text = hour
                      txtMinute.text = minute

                  }

                  override fun onFinish() {
                      countDownTimer!!.cancel()
                      showExpiredDialog()
                  }
              }

              countDownTimer?.start()
          }
        else{
              showExpiredDialog()
          }
    }

    private fun copyToClipBoard(){
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text",  txtAccNumber.text ?: "")
        myClipboard.setPrimaryClip(myClip)
        ViewUtil.showBlackToast(this,"Account number copied",0).show()
    }

    private fun showExpiredDialog(){
        val alertDialog = AlertDialog.Builder(this)
            .setMessage("Your booking has expired!")
            .setPositiveButton("ok") { p0, p1 ->
                val i = Intent(this,MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                i.putExtra("from",1)
                startActivity(i)
                finish()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    private fun intentPaymentDetail(){
        if (from == 1){

            val i = Intent(this,ActivityPaymentDetail::class.java)
            i.putExtra("order_id", checkoutMap["order_id"] as String)
            i.putExtra("experience_detail",experienceDetailModel)

            startActivity(i)
            finish()
        }
        else{
            val i = Intent(this,ActivityTransportationPaymentDetail::class.java)
            i.putExtra("booking_id",checkoutMap["id"] as String)
            i.putExtra("order_id",checkoutMap["order_id"] as String)
            i.putExtra("booked_name",edtBookedFullname.text.toString().trim())
            i.putExtra("isReturn",isReturn)
            i.putExtra("adultCount",adultCount)
            i.putExtra("childrenCount",childrenCount)
            i.putExtra("infantCount",infantCount)

            startActivity(i)
            finish()
        }
    }

    private fun intentPaymentRedirect(){
        val i = Intent(this,ActivityPaymentRedirect::class.java)
        i.putExtra("web_url",redirectURL)
        startActivityForResult(i,515)
    }

    private fun setPaypalBackgroundState(){
        rvBank.visibility = View.GONE
        linearBankPayment.setBackgroundResource(0)
        linearBankPayment.setBackgroundColor(Color.parseColor("#ffffff"))

        linearCCMethod.setBackgroundResource(0)
        linearCCMethod.setBackgroundColor(Color.parseColor("#ffffff"))

        linearPaypalMethod.setBackgroundColor(Color.parseColor("#ffffff"))
        linearPaypalMethod.setBackgroundResource(R.drawable.background_add_on)

        isBankViewClicked = false
        paymentMethodModel = paypalMethodList[0]
    }

    override fun onBackPressed() {
        backDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onGuestClicked(passengerMap : java.util.HashMap<String, Any>, position : Int) {

        val guest_type = passengerMap["guest_type"] as Int
        val isfilled = passengerMap["is_filled"] as Boolean
        val fullname = (passengerMap["fullname"] as String)
        val nameType = fullname.split(" ")[0]
        val birthdate = passengerMap["birthdate"] as String
        val idtype = passengerMap["idtype"] as String
        val idnumber = passengerMap["idnumber"] as String
        val type = passengerMap["type"] as String

        currentPassengerPosition = position

        if (guest_type == 1){

            if (isfilled){
                adultEdtFullname.setText(fullname.removePrefix(nameType))
                txtAdultTitle.text = fullname.split(" ")[0]
                txtAdultType.text = idtype
                edtAdultEdtIdNumber.setText(idnumber)
            }
            else{
                adultEdtFullname.setText("")
                txtAdultTitle.text = "Mr."
                txtAdultType.text = "ID Card"
                edtAdultEdtIdNumber.setText("")
            }

            aBottomSheetDialog.show()
        }
        else{
            if (isfilled){
                edtChildFullname.setText(fullname.removePrefix(nameType))
                txtChildTitle.text = fullname.split(" ")[0]
                txtChildBirthDay.text = birthdate
            }
            else{
                edtChildFullname.setText("")
                txtChildTitle.text = "Mr."
                txtChildBirthDay.text = "Select birth date"
                txtChildBirthDay.setTextColor(Color.parseColor("#979797"))
            }

            cBottomSheetDialog.show()
        }
    }

    override fun onCreateBookingPrepare() {
        loadingDialog.show()
    }

    override fun onCreateBookingSuccess(data: HashMap<String, Any>) {
       loadingDialog.dismiss()
       currentPage = 2
       checkoutMap = data
       setCurrentPage()
       order_id = checkoutMap["order_id"] as String
       ViewUtil.showBlackToast(this,"Success create booking",0).show()
    }

    override fun onCreateBookingError(message: String) {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,message,0).show()
    }

    override fun onPaymentMethodPrepare() {

    }

    override fun onPaymentMethodSuccess( bankMethodList: ArrayList<PaymentMethodModel>,
                                         creditCardMethodList: ArrayList<PaymentMethodModel>,
        paypalMethodList: ArrayList<PaymentMethodModel>) {

        this.bankMethodList = bankMethodList
        this.creditCardMethodList = creditCardMethodList
        this.paypalMethodList = paypalMethodList

        if (bankMethodList.size > 0){
            linearBank.visibility = View.VISIBLE
            rvBank.adapter = BankPaymentAdapter(this,bankMethodList,this)
        }
        else{
            linearBank.visibility = View.GONE
        }

        if (creditCardMethodList.size > 0){
            linearCC.visibility = View.VISIBLE
        }
        else{
            linearCC.visibility = View.GONE
        }

        if (paypalMethodList.size > 0){
            linearPaypal.visibility = View.VISIBLE
        }
        else{
            linearPaypal.visibility = View.VISIBLE
        }
    }

    override fun onPaymentMethodError(error: String) {

    }

    override fun onBankMethodClicked(paymentMethodModel: PaymentMethodModel, position: Int,
                                     isBankCLicked : Boolean) {
        isPaypal = false
        this.bankPosition = position
        this.isBankCLicked = isBankCLicked
        this.paymentMethodModel = paymentMethodModel
    }

    override fun onSpecialPromoPrepare() {

    }

    override fun onSpecialPromoLoaded(promoModel: PromoModel) {
            this.promoModel = promoModel
            setPromoButtonState(1)
            Snackbar.make(linearParent, "Promo code success", Snackbar.LENGTH_LONG).show()
    }

    override fun onSpecialPromoError(message: String) {
        Snackbar.make(linearParent, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreatePaymentPrepare() {
        loadingDialog.show()
    }

    override fun onCreatePaymentSuccess(data: HashMap<String, Any>) {
        loadingDialog.dismiss()
        paymentTransactionmap = data

        if (!isPaypal && fromPaymentMethod == 0){
            redirectURL = data["redirect_url"] as String
            intentPaymentRedirect()
        }
        else{
            currentPage = 3
            setCurrentPage()
            setConfirmation()

            if (isPaypal){
                showSuccessDialog()
            }
        }
    }

    override fun onCreatePaymentError(message: String) {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,message,0).show()
    }

    override fun onConfirmationPaymentPrepare() {
        loadingDialog.show()
    }

    override fun onConfirmationPaymentSuccess(data: HashMap<String, Any>) {
        loadingDialog.dismiss()
    }

    override fun onConfirmationPaymentError(message: String) {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,message,0).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 515){
            if (resultCode == ActivityPaymentRedirect.PAYMENT_REDIRECT_RESULT){
                data?.let { myIntent ->
                     myIntent.extras?.let { bundle ->
                         currentPage = 3
                         setCurrentPage()
                         setConfirmation()
                     }
                }
            }
        }
        else if (requestCode == 719){
            if (resultCode == Activity.RESULT_OK){

                val paymentConfirmation = data?.getParcelableExtra<PaymentConfirmation>(
                    PaymentActivity.EXTRA_RESULT_CONFIRMATION)

                if (paymentConfirmation != null){
                    try {
                        val paymentDetails = paymentConfirmation.toJSONObject()
                        payID = paymentDetails.getJSONObject("response").getString("id")
                        isPaypal = true
                        linearCCContent.visibility = View.GONE
                        setPaypalBackgroundState()

                        if (from == 1){
                            createExperiencePayment()
                        }
                        else{
                            createTransportationPayment()
                        }

                    }
                    catch (e : JSONException){
                        e.printStackTrace()
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                ViewUtil.showBlackToast(this,"Canceled",0).show()
            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                ViewUtil.showBlackToast(this,"Invalid",0).show()
            }
        }
    }

    override fun onDetailBookingPrepare() {
        loadingDialog.show()
    }

    override fun onDetailBookingSuccess(data: HashMap<String, Any>) {
        loadingDialog.dismiss()

        guestList = data["guest_desc"] as ArrayList<HashMap<String, Any>>
        barcode_picture = data["ticket_qr_code"] as String

        if (from == 1){
            val experienceMap = data["experience"] as HashMap<String,Any>
            merchant_name = experienceMap["merchant_name"] as String
            merchant_phone = experienceMap["merchant_phone"] as String
            merchant_picture = experienceMap["merchant_picture"] as String
            typeList = experienceMap["exp_type"] as ArrayList<String>
        }

        val transaction_status = data["transaction_status"] as Int

        if (transaction_status != 0){
            if (transaction_status == 3){
                ViewUtil.showBlackToast(this,"Transaction canceled", 0).show()
            }
            else{
                showSuccessDialog()
            }
        }
        else{
            ViewUtil.showBlackToast(this,"Please complete your payment",
                0).show()
        }
    }

    private fun intentPaypal(rates: Double){

        val usdPrice = BigDecimal.valueOf(total_price / rates)

        val paypalConfiguration = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(getString(R.string.paypal_key))

        val payPalPayment = PayPalPayment(usdPrice, "USD", "Transaction",
            PayPalPayment.PAYMENT_INTENT_SALE)

        val paypalService = Intent(this, PayPalService::class.java)
        paypalService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfiguration);
        startService(paypalService)

        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfiguration)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment)

        startActivityForResult(intent, 719)
    }

    private fun intentResultDetail(){

        val i = Intent(this,ActivityTransportationResultDetail::class.java)
        i.putExtra("is_return",isReturn)
        i.putExtra("adultCount",adultCount)
        i.putExtra("childrenCount",childrenCount)
        i.putExtra("infantCount",infantCount)
        i.putExtra("isReturnSelected",isReturn)
        i.putExtra("fromDetail",fromDetail)
        i.putExtra("fromView",1)
        i.putExtra("transportationModel",transportationModel)
        i.putExtra("transportationModelReturn",transportationModelReturn)
        i.putExtra("transportImages",transportImages)
        i.putExtra("transportReturnImages",transportReturnImages)
        i.putExtra("transportPrice",transportPrice)
        i.putExtra("transportReturnPrice",transportReturnPrice)
        i.putExtra("transportBoatDetails",transportBoatDetails)
        i.putExtra("transportReturnBoatDetails",transportReturnBoatDetails)
        i.putParcelableArrayListExtra("transportFacilities",transportFacilities)
        i.putParcelableArrayListExtra("transportReturnFacilities",transportReturnFacilities)

        startActivity(i)
    }

    override fun onDetailBookingError(message: String) {
        loadingDialog.dismiss()
    }

    override fun onExchangeRatesPrepare() {
        loadingDialog.show()
    }

    override fun onExchangeRatesSuccess(rates : Double) {
        loadingDialog.dismiss()
        intentPaypal(rates)
    }

    override fun onExchangeRatesError(message: String, code: Int) {
        loadingDialog.dismiss()
        ViewUtil.showBlackToast(this,message,0).show()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                backDialog.show()
            }

            R.id.btnNext -> {
                setNextPage()
            }

            R.id.linearTitle -> {
                showTitleMenu(linearTitle,bookedTitle,txtBookedTitle)
            }

            R.id.linearPhone -> {
                showPhoneMenu(linearPhone,bookedPhone,txtBookedPhone)
            }

            R.id.linearIdCard -> {
                showIdMenu(linearIdCard,bookedID,txtBookedID)
            }

            R.id.linearLogin -> {
                startActivity(Intent(this,ActivityLogin::class.java))
            }

            R.id.linearLogin2 -> {
                startActivity(Intent(this,ActivityLogin::class.java))
            }

            R.id.adultLinearTitle -> {
                showTitleMenu(adultLinearTitle,"",txtAdultTitle)
            }

            R.id.adultLinearID -> {
                showIdMenu(adultLinearID,"",txtAdultType)
            }

            R.id.adultBtnSave -> {
                setAdultPassenger()
            }

            R.id.childLinearTitle -> {
                showTitleMenu(childLinearTitle,"",txtChildTitle)
            }

            R.id.childLinearBirthday -> {
                dialogCalendar.show()
            }

            R.id.imgChildClose -> {
                cBottomSheetDialog.dismiss()
            }

            R.id.calendarBtnApply -> {
                selectBirthDate()
            }

            R.id.btnChildSave -> {
                setChildPassenger()
            }

            R.id.linearPromo -> {
                setPromoState()
            }

            R.id.linearBankPayment -> {
                setPaymentMethod(1)
            }

            R.id.linearCCMethod -> {
                setPaymentMethod(2)
            }

            R.id.linearPaypalMethod -> {
               micsController.loadExchangeRates("USD","IDR",this)
            }

            R.id.txtFullPayment -> {
                setFullDownPayment(1)
            }

            R.id.txtDownPayment -> {
                setFullDownPayment(2)
            }

            R.id.btnPromo -> {
                setPromoActionState()
            }

            R.id.linearCredit -> {
                setCreditButtonState()
            }

            R.id.btnDetail -> {
                intentPaymentDetail()
            }

            R.id.linearDeparture1 -> {
                checkReturnState()
            }

            R.id.linearDeparture2 -> {
                checkReturnState()
            }

            R.id.linearReturn1 -> {
                checkReturnState()
            }

            R.id.linearReturn2 -> {
                checkReturnState()
            }

            R.id.txtHowtoPay -> {
                intentPaymentRedirect()
            }

            R.id.txtPaymentLearnMore -> {
                dialogType.show()
            }

            R.id.txtDetail1 -> {
                intentResultDetail()
            }

            R.id.txtDetail2 -> {
                intentResultDetail()
            }

            R.id.learnClose -> {
                dialogType.dismiss()
            }

            R.id.imgCopy -> {
                copyToClipBoard()
            }

            R.id.imgPrevious -> {
                val currentYearMonth = currentCalendarMonth.yearMonth.minusMonths(
                    12)
                calendarView.scrollToMonth(currentYearMonth)
            }

            R.id.imgNext -> {
                val currentYearMonth = currentCalendarMonth.yearMonth.plusMonths(
                    12)
                calendarView.scrollToMonth(currentYearMonth)
            }

            R.id.linearTotalBottomSheet -> {
                priceRoundedBottomSheet.show()
            }

            R.id.linearPaymentTotalBottomSheet -> {
                priceRoundedBottomSheet.show()
            }

            R.id.imgBottomClose -> {
                priceRoundedBottomSheet.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, PayPalService::class.java))
    }
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


//midtrans kalo USD ubah ke Rupiah