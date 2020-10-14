package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager

import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.facebook.FacebookSdk
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer

import id.dtech.cgo.Adapter.*
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.ExperienceController
import id.dtech.cgo.Controller.TripInspirationController
import id.dtech.cgo.Controller.UserController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.*
import id.dtech.cgo.Preferance.UserSession

import id.dtech.cgo.R
import id.dtech.cgo.Util.CalendarUtil
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.Util.MapUtil
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_detail_experience.*

import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.layout_calendar_header.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.io.Serializable
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityDetailExperience : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.ExperienceDetailCallback, MyCallback.Companion.ExperienceReviewCallback
    , MyCallback.Companion.TripInspirationCallback, MyCallback.Companion.CreateWishlistCallback,
    OnMapReadyCallback, MyCallback.Companion.CheckWishlistCallback,
    ApplicationListener.Companion.PackageListener {

    private lateinit var guestBottomSheetDialog : RoundedBottomSheetDialog
    private lateinit var dateBottomSheetDialog : RoundedBottomSheetDialog

    private lateinit var icCLose : ImageView
    private lateinit var icDateCLose : ImageView

    private lateinit var btnGuestApply : Button
    private lateinit var btnDateApply : Button

    private lateinit var imgAdultMinus : ImageView
    private lateinit var imgAdultPlus : ImageView
    private lateinit var txtAdultCount : TextView

    private lateinit var imgChildrenMinus : ImageView
    private lateinit var imgChildrenPlus : ImageView
    private lateinit var txtChildrenCount : TextView

    private lateinit var imgInfantMinus : ImageView
    private lateinit var imgInfantPlus : ImageView
    private lateinit var txtInfantCount : TextView

    private lateinit var linearDate : LinearLayout
    private lateinit var linearGuest : LinearLayout

    private lateinit var calendarViewDialog : CalendarView

    private val today = LocalDate.now()

    private val selectedDates = ArrayList<LocalDate>()

    private lateinit var dayAdapter : DayAdapter
    private lateinit var facilityAdapter : FacilityAdapter

    private lateinit var iteneraries : ArrayList<ItemItenaryModel>
    private lateinit var facilityList : ArrayList<FacilityModel>

    private lateinit var shareDialog : Dialog
    private lateinit var loadingDialog : AlertDialog
    private lateinit var userSession: UserSession

    private lateinit var userController: UserController
    private lateinit var experienceController: ExperienceController

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private lateinit var mapFragment : SupportMapFragment
    private lateinit var experienceDetailModel : ExperienceDetailModel
    private lateinit var photos : ArrayList<PhotoModel>
    private lateinit var paymentModelList : ArrayList<PaymentModel>
    private lateinit var avaibilityDate : ArrayList<LocalDate>
    private lateinit var currentCalendarMonth : CalendarMonth

    private lateinit var imgShareCLose : ImageView
    private lateinit var linearFacebook : LinearLayout
    private lateinit var linearTwitter : LinearLayout
    private lateinit var linearWhatsapp : LinearLayout
    private lateinit var linearGmail : LinearLayout
    private lateinit var linearLine : LinearLayout

    private lateinit var txtDialogPrice : MyTextView
    private lateinit var txtDialogDate : MyTextView
    private lateinit var txtDialogGuest : MyTextView
    private lateinit var btnDialogBook : Button

    private lateinit var facebookShareDialog : ShareDialog

    private var experience_id = ""
    private var paymentType = ""
    private var price = 0L
    private var isDownPaymentAvailable = false
    private var defaultCurrency = ""
    private val whatsAppAppId = "com.whatsapp"

    private lateinit var gMap : GoogleMap
    private lateinit var checkoutDialog : RoundedBottomSheetDialog

    private var adultCount = 0
    private var childrenCount = 0
    private var infantCount = 0

    private var strSelectedDate = ""
    private var date = ""
    private var guest = 0

    private var isWishList = false
    private var isSeeMore = false
    private var isItenarySeeMore = false
    private var isFacilitySeeMore = false

    private lateinit var selectedPackageMap : HashMap<String,Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_detail_experience)
        setView()
    }

    private fun setView(){

        iteneraries = ArrayList()
        dayAdapter = DayAdapter(this,iteneraries)

        facilityList = ArrayList()
        facilityAdapter = FacilityAdapter(this,facilityList)
        rvFacility.adapter = facilityAdapter

        userSession = UserSession(getSharedPreferences("user_session", Context.MODE_PRIVATE))
        avaibilityDate = ArrayList()

        experienceController = ExperienceController()
        userController = UserController()

        loadingDialog = ViewUtil.laodingDialog(this)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        rvGuide.layoutManager = LinearLayoutManager(this)

        rvInclude.layoutManager = LinearLayoutManager(this)

        rvExclude.layoutManager = LinearLayoutManager(this)

        rvReview.layoutManager = LinearLayoutManager(this)

        rvPackage.layoutManager = LinearLayoutManager(this)

        rvPolicy.layoutManager = LinearLayoutManager(this)
        rvPolicy.adapter = PolicyAdapter(this)

        rvRedeem.layoutManager = LinearLayoutManager(this)
        rvRedeem.adapter = RedeemAdapter(this)

        rvInspiration.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,
            false)

        rvServiceType.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,
            false)

        rvIteneraries.layoutManager = LinearLayoutManager(this)
        rvIteneraries.adapter = dayAdapter

        setCalendar()
        setShareDialog()
        setCheckoutDialog()
        setSelectDateDialog()
        setInputGuestDialog()

        val b = intent.extras

        b?.let { bundle ->
            experience_id = bundle.getString("experience_id") ?: ""
            experienceController.getExperienceDetail(experience_id,this)
            TripInspirationController.getTripInspiration(this)

            if (userSession.access_token != null){
                userController.checkWishList(userSession.access_token ?: "", experience_id, this)
            }
        }

        ivBack.setOnClickListener(this)
        imgPrevious.setOnClickListener(this)
        imgNext.setOnClickListener(this)
        btnMorePhotos.setOnClickListener(this)
        txtDirection.setOnClickListener(this)
        btnBook.setOnClickListener(this)
        imgWishlist.setOnClickListener(this)
        imgSearch.setOnClickListener(this)
        imgShare.setOnClickListener(this)

        txtSeeMore.setOnClickListener(this)
        txtItenarySeeMore.setOnClickListener(this)
        txtAllFacility.setOnClickListener(this)
        
        linearOverview.setOnClickListener(this)
        linearPackages.setOnClickListener(this)
        linearExperience.setOnClickListener(this)
        linearFacility.setOnClickListener(this)
        linearAvaibility.setOnClickListener(this)
        linearReview.setOnClickListener(this)
    }

    private fun setCheckoutDialog(){
        checkoutDialog = RoundedBottomSheetDialog(this)

        val sheetView = layoutInflater.inflate(R.layout.layout_detail_payment, null)
        linearDate = sheetView.findViewById(R.id.linearDate)
        linearGuest = sheetView.findViewById(R.id.linearGuest)

        txtDialogDate = sheetView.findViewById(R.id.txtDialogDate)
        txtDialogPrice = sheetView.findViewById(R.id.txtDialogPrice)
        txtDialogGuest = sheetView.findViewById(R.id.txtGuest)

        btnDialogBook = sheetView.findViewById(R.id.btnDialogBook)

        checkoutDialog.setContentView(sheetView)
        checkoutDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        linearDate.setOnClickListener(this)
        linearGuest.setOnClickListener(this)
        btnDialogBook.setOnClickListener(this)
    }

    private fun setSelectDateDialog() {
        dateBottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.fragment_select_date, null)
        dateBottomSheetDialog.setContentView(sheetView)
        dateBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        icDateCLose = sheetView.findViewById(R.id.icDateCLose)
        calendarViewDialog = sheetView.findViewById(R.id.calendarView)
        btnDateApply = sheetView.findViewById(R.id.btnDateApply)

        icDateCLose.setOnClickListener(this)
        btnDateApply.setOnClickListener(this)

        setDialogCalendar()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDialogCalendar(){

        val daysOfWeek = CalendarUtil.daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)

        calendarViewDialog.setup(startMonth, endMonth, daysOfWeek.first())
        calendarViewDialog.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {

            lateinit var day: CalendarDay

            val textView = view.exOneDayText

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH && avaibilityDate.contains(day.date) &&
                        day.date.dayOfYear > today.dayOfYear) {

                        if (selectedDates.size > 0){
                            selectedDates.removeAt(0)
                            selectedDates.add(day.date)
                        }
                        else{
                            selectedDates.add(day.date)
                        }

                        btnDateApply.isEnabled = true
                        calendarViewDialog.notifyCalendarChanged()
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.txtMonthYear
        }

        calendarViewDialog.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {

            override fun create(view: View) = MonthViewContainer(view)

            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }

        calendarViewDialog.dayBinder = object : DayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {

                container.day = day

                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH && avaibilityDate.contains(day.date) &&
                    day.date.dayOfYear >= today.dayOfYear) {

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
    }

    private fun setInputGuestDialog(){
        guestBottomSheetDialog = RoundedBottomSheetDialog(this)

        val sheetView = layoutInflater.inflate(R.layout.fragment_input_guest, null)
        guestBottomSheetDialog.setContentView(sheetView)
        guestBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        icCLose = sheetView.findViewById(R.id.icCLose)

        imgAdultMinus  = sheetView.findViewById(R.id.imgAdultMinus)
        imgAdultPlus = sheetView.findViewById(R.id.imgAdultPlus)
        txtAdultCount = sheetView.findViewById(R.id.txtAdultCount)

        imgChildrenMinus = sheetView.findViewById(R.id.imgChildrenMinus)
        imgChildrenPlus = sheetView.findViewById(R.id.imgChildrenPlus)
        txtChildrenCount = sheetView.findViewById(R.id.txtChildrenCount)

        imgInfantMinus = sheetView.findViewById(R.id.imgInfantMinus)
        imgInfantPlus = sheetView.findViewById(R.id.imgInfantPlus)
        txtInfantCount = sheetView.findViewById(R.id.txtInfantCount)

        btnGuestApply = sheetView.findViewById(R.id.btnGuestApply)

        imgAdultMinus.setOnClickListener(this)
        imgAdultPlus.setOnClickListener(this)

        imgChildrenMinus.setOnClickListener(this)
        imgChildrenPlus.setOnClickListener(this)

        imgInfantMinus.setOnClickListener(this)
        imgInfantPlus.setOnClickListener(this)

        icCLose.setOnClickListener(this)
        btnGuestApply.setOnClickListener(this)
    }

    @SuppressLint("SimpleDateFormat")
    private fun applyDate(){
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val sdfDayDate = SimpleDateFormat("dd MMM yyyy")
        val sdfDay = SimpleDateFormat("dd")
        val sdfDayMonth = SimpleDateFormat("dd MMMM yyyy")
        val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val strDate = selectedDates[0].toString()
        val dateCalendar = sdf.parse(strDate)
        val duration = experienceDetailModel.exp_duration

        if (duration > 1){
            val calendar = Calendar.getInstance()
            calendar.time = dateCalendar ?: Date()
            calendar.add(Calendar.DATE,duration - 1)

            val strDay = sdfDay.format(dateCalendar ?: Date())
            val strDayDate = sdfDayDate.format(calendar.time)

            val strDayDateYear = "$strDay - $strDayDate"
            txtDialogDate.text = strDayDateYear
            strSelectedDate = strDayDateYear
        }
        else{
            val strDay = sdfDayMonth.format(dateCalendar ?: Date())
            txtDialogDate.text = strDay
            strSelectedDate = strDay
        }

        date = sdfDate.format(dateCalendar ?: Date())
    }

    @SuppressLint("SetTextI18n")
    private fun setGuest(){
        val totalGuest = adultCount + childrenCount + infantCount

        if (totalGuest > 1){
            txtDialogGuest.text = "$totalGuest Guest(s)"
        }
        else{
            txtDialogGuest.text = "$totalGuest Guest"
        }
    }

    private fun setShareDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_sharewith,null)
        shareDialog = Dialog(this,R.style.FullWidth_Dialog)
        shareDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        shareDialog.setContentView(view)
        shareDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        imgShareCLose = view.findViewById(R.id.imgShareCLose)
        linearFacebook = view.findViewById(R.id.linearFacebook)
        linearTwitter = view.findViewById(R.id.linearTwitter)
        linearWhatsapp = view.findViewById(R.id.linearWhatsapp)
        linearGmail = view.findViewById(R.id.linearGmail)
        linearLine  = view.findViewById(R.id.linearLine)

        imgShareCLose.setOnClickListener(this)
        linearFacebook.setOnClickListener(this)
        linearTwitter.setOnClickListener(this)
        linearWhatsapp.setOnClickListener(this)
        linearGmail.setOnClickListener(this)
        linearLine.setOnClickListener(this)
    }

    private fun createWishList(){
       if (userSession.access_token != null){
           if (!isWishList){
               val body = HashMap<String,Any>()
               body["is_deleted"] = false
               body["exp_id"] = experience_id
               body["trans_id"] = ""
               userController.createWishList(body,userSession.access_token ?: "", this)
           }
           else{
               val body = HashMap<String,Any>()
               body["is_deleted"] = true
               body["exp_id"] = experience_id
               body["trans_id"] = ""
               userController.createWishList(body,userSession.access_token ?: "", this)
           }
       }
       else{
           ViewUtil.showBlackToast(this,"Please login to add to wishlist",0).show()
       }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCalendar(){

        val daysOfWeek = CalendarUtil.daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)

        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {

            lateinit var day: CalendarDay

            val textView = view.exOneDayText

            init {
                view.setOnClickListener {

                }
            }
        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {

                container.day = day

                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH && avaibilityDate.contains(day.date) &&
                    day.date.dayOfYear >= today.dayOfYear) {
                    textView.setTextColor(Color.parseColor("#000000"))
                    textView.setBackgroundColor(0)
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

    private fun setBtnGuestApplyEnable(){
        if (adultCount !=0 || childrenCount != 0 || infantCount != 0){
            btnGuestApply.isEnabled = true
            btnGuestApply.setTextColor(Color.parseColor("#FFFFFF"))
        }
        else{
            btnGuestApply.isEnabled = false
            btnGuestApply.setTextColor(Color.parseColor("#BDBDBD"))
        }
    }

    private fun setAdultCount(from : Int){
        if (from == 1){
            if (adultCount > 0){
                adultCount--

                if (adultCount != 0){
                    imgAdultMinus.setImageResource(R.drawable.ic_circle_minus_black)
                    txtAdultCount.text = "$adultCount"
                    txtAdultCount.setTextColor(Color.parseColor("#35405A"))
                }
                else{
                    imgAdultMinus.setImageResource(R.drawable.ic_circle_minus_gray)
                    txtAdultCount.text = "0"
                    txtAdultCount.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }
        }
        else{
            if (adultCount < 9){
                adultCount++
                imgAdultMinus.setImageResource(R.drawable.ic_circle_minus_black)
                txtAdultCount.text = "$adultCount"
                txtAdultCount.setTextColor(Color.parseColor("#35405A"))
            }
        }

        setBtnGuestApplyEnable()
    }

    private fun setChildrenCount(from : Int){
        if (from == 1){
            if (childrenCount > 0){
                childrenCount--

                if (childrenCount != 0){
                    imgChildrenMinus.setImageResource(R.drawable.ic_circle_minus_black)
                    txtChildrenCount.text = "$childrenCount"
                    txtChildrenCount.setTextColor(Color.parseColor("#35405A"))
                }
                else{
                    imgChildrenMinus.setImageResource(R.drawable.ic_circle_minus_gray)
                    txtChildrenCount.text = "0"
                    txtChildrenCount.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }
        }
        else{
            if (childrenCount < 9){
                childrenCount++
                imgChildrenMinus.setImageResource(R.drawable.ic_circle_minus_black)
                txtChildrenCount.text = "$childrenCount"
                txtChildrenCount.setTextColor(Color.parseColor("#35405A"))
            }
        }

        setBtnGuestApplyEnable()
    }

    private fun setInfantCount(from : Int){
        if (from == 1){
            if (infantCount > 0){
                infantCount--

                if (infantCount != 0){
                    imgInfantMinus.setImageResource(R.drawable.ic_circle_minus_black)
                    txtInfantCount.text = "$infantCount"
                    txtInfantCount.setTextColor(Color.parseColor("#35405A"))
                }
                else{
                    imgInfantMinus.setImageResource(R.drawable.ic_circle_minus_gray)
                    txtInfantCount.text = "0"
                    txtInfantCount.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }
        }
        else{
            if (infantCount < 9){
                infantCount++
                imgInfantMinus.setImageResource(R.drawable.ic_circle_minus_black)
                txtInfantCount.text = "$infantCount"
                txtInfantCount.setTextColor(Color.parseColor("#35405A"))
            }
        }

        setBtnGuestApplyEnable()
    }

    private fun openGoogleMap(destination : LatLng)
    {
        try {
            val strUri =
                "http://maps.google.com/maps?q=loc:${destination.latitude},${destination.longitude}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            startActivity(intent)
        }
        catch (e : Exception){
            Toast.makeText(this,"Please install Google Map!", Toast.LENGTH_LONG).show()
        }
    }

    private fun openWhatsAppAndSendMessage() {

        try {
            intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val text = "Checkout ${experienceDetailModel.exp_title ?:""} on cGO " +
                    "\n https://cgo.co.id/product/${experienceDetailModel.experience_id ?: ""}"

            intent.`package` = whatsAppAppId
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, "Finish the action with:"))

        } catch (e: PackageManager.NameNotFoundException) {
            ViewUtil.showBlackToast(this,"Whatsapp not installed.",0).show()
        }
    }

    private fun openGmailAndSendMessage(){
        val i = Intent(Intent.ACTION_SEND)
        val text = "Checkout ${experienceDetailModel.exp_title ?:""} on cGO " +
                "\n https://cgo.co.id/product/${experienceDetailModel.experience_id ?: ""}"
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_SUBJECT, "cGO Indonesia")
        i.putExtra(Intent.EXTRA_TEXT, text)
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            ViewUtil.showBlackToast(this,"There are no email clients installed.",0).show()
        }
    }

    private fun openLineAndShareMessage(){
        try {
            val appName = "jp.naver.line.android"
            val text = "Checkout ${experienceDetailModel.exp_title ?:""} on cGO " +
                    "\n https://cgo.co.id/product/${experienceDetailModel.experience_id ?: ""}"
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("line://msg/text/$text")
            startActivity(intent)
        }
        catch (e: java.lang.Exception) {
            Log.e("ERROR LINE", e.toString())
            ViewUtil.showBlackToast(this,"Line not installed in your device",0).show()
        }
    }

    private fun shareTwitter(){
        val text = "Checkout ${experienceDetailModel.exp_title ?:""} on cGO " +
                "\n https://cgo.co.id/product/${experienceDetailModel.experience_id ?: ""}"
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.setClassName("com.twitter.android",
                "com.twitter.android.PostActivity")
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(intent)
        }
        catch (e: java.lang.Exception) {
            val tweet = Intent(Intent.ACTION_VIEW)
            tweet.data = Uri.parse(
                "http://twitter.com/?status=" + Uri.encode(text)
            )
            startActivity(tweet)
        }
    }

    private fun setTabListener(position : Int){
        if (position == 1){
            txtOverview.setTextColor(Color.parseColor("#000000"))
            txtPackages.setTextColor(Color.parseColor("#808080"))
            txtExperience.setTextColor(Color.parseColor("#808080"))
            txtFacility.setTextColor(Color.parseColor("#808080"))
            txtAvaibility.setTextColor(Color.parseColor("#808080"))
            txtReviews.setTextColor(Color.parseColor("#808080"))

            dividerOverview.visibility = View.VISIBLE
            dividerPackages.visibility = View.INVISIBLE
            dividerExperience.visibility = View.INVISIBLE
            dividerFacility.visibility = View.INVISIBLE
            dividerAvaibility.visibility = View.INVISIBLE
            dividerReview.visibility = View.INVISIBLE

            myNestedScroll.smoothScrollTo(null,true)
        }
        else if (position == 2){
            txtOverview.setTextColor(Color.parseColor("#808080"))
            txtPackages.setTextColor(Color.parseColor("#000000"))
            txtExperience.setTextColor(Color.parseColor("#808080"))
            txtFacility.setTextColor(Color.parseColor("#808080"))
            txtAvaibility.setTextColor(Color.parseColor("#808080"))
            txtReviews.setTextColor(Color.parseColor("#808080"))

            dividerOverview.visibility = View.INVISIBLE
            dividerPackages.visibility = View.VISIBLE
            dividerExperience.visibility = View.INVISIBLE
            dividerFacility.visibility = View.INVISIBLE
            dividerAvaibility.visibility = View.INVISIBLE
            dividerReview.visibility = View.INVISIBLE

            myNestedScroll.smoothScrollTo(txtPackageList,false)
        }
        else if (position == 3){
            txtOverview.setTextColor(Color.parseColor("#808080"))
            txtPackages.setTextColor(Color.parseColor("#808080"))
            txtExperience.setTextColor(Color.parseColor("#000000"))
            txtFacility.setTextColor(Color.parseColor("#808080"))
            txtAvaibility.setTextColor(Color.parseColor("#808080"))
            txtReviews.setTextColor(Color.parseColor("#808080"))

            dividerOverview.visibility = View.INVISIBLE
            dividerPackages.visibility = View.INVISIBLE
            dividerExperience.visibility = View.VISIBLE
            dividerFacility.visibility = View.INVISIBLE
            dividerAvaibility.visibility = View.INVISIBLE
            dividerReview.visibility = View.INVISIBLE

            myNestedScroll.smoothScrollTo(txtExperienceTitle,false)
        }
        else if (position == 4){
            txtOverview.setTextColor(Color.parseColor("#808080"))
            txtPackages.setTextColor(Color.parseColor("#808080"))
            txtExperience.setTextColor(Color.parseColor("#808080"))
            txtFacility.setTextColor(Color.parseColor("#000000"))
            txtAvaibility.setTextColor(Color.parseColor("#808080"))
            txtReviews.setTextColor(Color.parseColor("#808080"))

            dividerOverview.visibility = View.INVISIBLE
            dividerPackages.visibility = View.INVISIBLE
            dividerExperience.visibility = View.INVISIBLE
            dividerFacility.visibility = View.VISIBLE
            dividerAvaibility.visibility = View.INVISIBLE
            dividerReview.visibility = View.INVISIBLE

            myNestedScroll.smoothScrollTo(txtFacilitiesTitle,false)
        }
        else if (position == 5){
            txtOverview.setTextColor(Color.parseColor("#808080"))
            txtPackages.setTextColor(Color.parseColor("#808080"))
            txtExperience.setTextColor(Color.parseColor("#808080"))
            txtFacility.setTextColor(Color.parseColor("#808080"))
            txtAvaibility.setTextColor(Color.parseColor("#000000"))
            txtReviews.setTextColor(Color.parseColor("#808080"))

            dividerOverview.visibility = View.INVISIBLE
            dividerPackages.visibility = View.INVISIBLE
            dividerExperience.visibility = View.INVISIBLE
            dividerFacility.visibility = View.INVISIBLE
            dividerAvaibility.visibility = View.VISIBLE
            dividerReview.visibility = View.INVISIBLE

            myNestedScroll.smoothScrollTo(txtAvaibilityTitle,false)
        }
        else{
            txtOverview.setTextColor(Color.parseColor("#808080"))
            txtPackages.setTextColor(Color.parseColor("#808080"))
            txtExperience.setTextColor(Color.parseColor("#808080"))
            txtFacility.setTextColor(Color.parseColor("#808080"))
            txtAvaibility.setTextColor(Color.parseColor("#808080"))
            txtReviews.setTextColor(Color.parseColor("#000000"))

            dividerOverview.visibility = View.INVISIBLE
            dividerPackages.visibility = View.INVISIBLE
            dividerExperience.visibility = View.INVISIBLE
            dividerFacility.visibility = View.INVISIBLE
            dividerAvaibility.visibility = View.INVISIBLE
            dividerReview.visibility = View.VISIBLE

            myNestedScroll.smoothScrollTo(txtReviewTitle,false)
        }
    }

    fun NestedScrollView.smoothScrollTo(view: View?, isOverview : Boolean) {
        if (!isOverview){
            var distance = view!!.top
            var viewParent = view.parent

            for (i in 0..9) {
                if ((viewParent as View) === this) break
                distance += (viewParent as View).top
                viewParent = viewParent.getParent()
            }

            smoothScrollTo(0, distance - 50)
        }
        else{
            smoothScrollTo(0, 0)
        }
    }

    private fun setImagePager(){
        imagePager.adapter = ImagePagerAdapter(this,photos,"")
        imagePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {
                }

                @SuppressLint("SetTextI18n")
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                    txtPage.text =  ""+(imagePager.currentItem+1)+" / "+photos.size
                }

                override fun onPageSelected(p0: Int) {
                }
            })
    }

    private fun setsShareFacebook(){
        val experienceLink = "http://cgo.co.id/product/${experienceDetailModel.experience_id ?: ""}"
        Log.d("fb_link",experienceLink)
        facebookShareDialog = ShareDialog(this)
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                val linkContent =  ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(experienceLink))
                    .setShareHashtag(ShareHashtag.Builder()
                        .setHashtag("#cGOExplore")
                        .build()
                    )
                    .build()

                facebookShareDialog.show(linkContent)
            }
        }

    private fun setStar(count : Double, star1 : ImageView, star2 : ImageView, star3 : ImageView,
                        star4 : ImageView, star5 : ImageView,txtCount : TextView
    ){

        if (count == 0.0){
            txtCount.text = "0 /"
            star1.setImageResource(R.drawable.ic_star_empty)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 0.5){
            txtCount.text = "0.5 /"
            star1.setImageResource(R.drawable.ic_star_half)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.0){
            txtCount.text = "1 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.5){
            txtCount.text = "1.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_half)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.0){
            txtCount.text = "2 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.5){
            txtCount.text = "2.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_half)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.0){
            txtCount.text = "3 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.5){
            txtCount.text = "3.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_half)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.0){
            txtCount.text = "4 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.5){
            txtCount.text = "4.5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_half)
        }
        else if (count == 5.0){
            txtCount.text = "5 /"
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }
    }

    private fun setPackage(packageMap : HashMap<String,Any>, packageFrom : Int){
        val packageAvaibility = packageMap["packageAvaibility"] as ArrayList<LocalDate>
        val price = CurrencyUtil.decimal(packageMap["packagePrice"] as Long).replace(",",".")
        val paymentType = (packageMap["packageTypePayment"] as String).split(" ")[1]
        val packageCurrency = packageMap["packageCurrency"] as String
        val strPrice = "$packageCurrency $price/$paymentType"

        txtPrice.text = strPrice
        txtDialogPrice.text = strPrice

        avaibilityDate = packageAvaibility

        calendarViewDialog.notifyCalendarChanged()
        calendarView.notifyCalendarChanged()


        if (packageFrom == 1){
            selectedPackageMap = packageMap
            intentCheckout(packageMap)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun intentCheckout(packageMap : HashMap<String,Any>?){
          val intentMap = HashMap<String,Any>()
          intentMap["packageMap"] = selectedPackageMap
          intentMap["packageExpPayment"] = selectedPackageMap["packageExpPayment"] as ArrayList<HashMap<String,Any>>
          intentMap["experienceDetailModel"] = experienceDetailModel
          intentMap["guideList"] = experienceDetailModel.exp_guides ?: ArrayList<HashMap<String,Any>>()
          intentMap["packageList"] = experienceDetailModel.exp_packages ?: ArrayList<HashMap<String,Any>>()
          intentMap["paymentType"] = paymentType
          intentMap["price"] = price
          intentMap["isDownPaymentAvailable"] = isDownPaymentAvailable
          intentMap["avaibility"] = avaibilityDate
          intentMap["defaultCurrency"] = defaultCurrency
          intentMap["selected_date"] = strSelectedDate
          intentMap["date"] = date
          intentMap["adult_count"] = adultCount
          intentMap["children_count"] = childrenCount
          intentMap["infant_count"] = infantCount
          intentMap["method_list"] = paymentModelList

          if (packageMap != null){
              val i = Intent(this, PackageDetailActivity::class.java)
              i.putExtra("intentMap", intentMap as Serializable)
              startActivity(i)
          }
        else{
              if (selectedDates.size > 0){
                  val sdfCalendar = SimpleDateFormat("yyyy-MM-dd")
                  val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                  val strDate = selectedDates[0].toString()

                  val dateCalendar = sdfCalendar.parse(strDate) ?: Date()
                  date = sdfDate.format(dateCalendar)
              }

              val maxGuest = experienceDetailModel.exp_max_guest
              guest = adultCount + childrenCount + infantCount

              if (date.isEmpty()){
                  ViewUtil.showBlackToast(this,"Please select date",0).show()
                  return
              }

              if (guest == 0){
                  ViewUtil.showBlackToast(this,"Please select guest",0).show()
                  return
              }

              if (guest > maxGuest){
                  ViewUtil.showBlackToast(this,"Maximum guest is $maxGuest",0).show()
                  return
              }

              experienceDetailModel.exp_guides?.let { guideList ->
                  if (guideList.size > 1){
                      val i = Intent(this,ActivitySelectGuide::class.java)
                      i.putExtra("intentMap", intentMap as Serializable)
                      startActivity(i)
                  }
                  else{
                      if (guideList.size == 1){
                          intentMap["selectedGuideMap"] = guideList[0]
                      }
                      val i = Intent(this,ActivityAddOn::class.java)
                      i.putExtra("intentMap", intentMap as Serializable)
                      startActivity(i)
                  }
              }
          }
    }

    override fun onExperienceDetailPrepare() {
        linearContent.visibility = View.GONE
        shimerLayout.visibility = View.VISIBLE
        shimerLayout.startShimmerAnimation()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onExperienceDetailLoaded(expDetailModel: ExperienceDetailModel) {

        experienceDetailModel = expDetailModel
        val countRating = experienceDetailModel.count_rating

        val isFlexibleTicket = experienceDetailModel.is_flexible_ticket
        val flexibleTicketValid = experienceDetailModel.exp_validity_amount
        val flexibleTicketType = experienceDetailModel.exp_validity_type
        val pickupPlace = experienceDetailModel.exp_pickup_place ?: ""

        setTextDescription()

        txtTitle.text = experienceDetailModel.exp_title ?: ""
        txtLocation.text = experienceDetailModel.harbors_name+", "+experienceDetailModel.province
        txtGuest.text = "Max. "+experienceDetailModel.exp_max_guest+" Person"
        txtExpType.text = experienceDetailModel.exp_trip_type
        txtReviewCount.text = ""+countRating

        val address = experienceDetailModel.exp_address ?: ""
        val pickupTime = experienceDetailModel.exp_pickup_time ?: "00:00:00"
        val startTradeHour = experienceDetailModel.trading_hour_start ?: "00:00:00"
        val endTradeHour = experienceDetailModel.trading_hour_end ?: "00:00:00"

        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("HH:mm")
        val isGuideMaleExist = experienceDetailModel.isGuideMaleExist ?: false
        val isGuideFemaleExist = experienceDetailModel.isGuideFemaleExist ?: false

        if (pickupPlace.isNotEmpty()){
            linearMeetingPlace.visibility = View.VISIBLE
            txtPlace.text = pickupPlace
        }
        else{
            linearMeetingPlace.visibility = View.GONE
        }

        if (isGuideMaleExist || isGuideFemaleExist){
            linearTourGuide.visibility = View.VISIBLE

            if (isGuideMaleExist && isGuideFemaleExist){
                txtGuideGender.text = "Female & Male Guide Options"
            }
            else if (isGuideFemaleExist){
                txtGuideGender.text = "Female Guide Options"
            }
            else{
                txtGuideGender.text = "Male Guide Options"
            }
        }
        else{
            linearTourGuide.visibility = View.GONE
        }

        if (isFlexibleTicket != 0){
            linearFlexibleTicket.visibility = View.VISIBLE
            txtFlexibleTicket.text = "Ticket is still valid $flexibleTicketValid $flexibleTicketType" +
                    " after chosen trip date"
        }

        if (address.isNotEmpty() && address != "null"){
            linearAddressExperience.visibility = View.VISIBLE
            txtAddress.text = address
        }
        else{
            linearAddressExperience.visibility = View.GONE
        }

        if (pickupTime != "null" && pickupTime != "00:00:00"){
            linearPickupTime.visibility = View.VISIBLE
            val pickupDateTime = sdf.parse(pickupTime) ?:  Date()
            txtTime.text = sdfs.format(pickupDateTime)
        }
        else{
            linearPickupTime.visibility = View.GONE
        }

        if (startTradeHour != "null" && endTradeHour != "null"){
            linearTradingHour.visibility = View.VISIBLE
            val tradeHourStart = sdf.parse(startTradeHour) ?:  Date()
            val endHourStart = sdf.parse(endTradeHour) ?:  Date()
            txtTradingHour.text = "${sdfs.format(tradeHourStart)} - ${sdfs.format(endHourStart)}"
        }
        else{
            linearTradingHour.visibility = View.GONE
        }

        if (experienceDetailModel.start_point != null){
            if (experienceDetailModel.start_point!!.isNotEmpty()){
                linearStartEndPoint.visibility = View.VISIBLE
                txtStartPoint.text = experienceDetailModel.start_point ?: ""
                txtEndPoint.text = experienceDetailModel.end_point ?: ""
            }
            else{
                linearStartEndPoint.visibility = View.GONE
            }
        }

        if (experienceDetailModel.how_to_get_location != null){
            if (experienceDetailModel.how_to_get_location!! != "null" && experienceDetailModel.how_to_get_location!!.isNotEmpty()){
                linearToGetThere.visibility = View.VISIBLE
                txtToGetThere.text = experienceDetailModel.how_to_get_location ?: ""
            }
            else{
                linearToGetThere.visibility = View.GONE
            }
        }

        if (countRating > 1){
            txtReview.text = "Reviews"
        }
        else {
            txtReview.text = "Review"
        }

        if (experienceDetailModel.is_certified_guide == 1){
            linearCertifiedGuide.visibility = View.VISIBLE
        }
        else{
            linearCertifiedGuide.visibility = View.GONE
        }

        experienceDetailModel.exp_itenerary?.let {
            iteneraries = it
            val itenaryList = ArrayList<ItemItenaryModel>()

            if (it.size > 0){
                linearItinerary.visibility = View.VISIBLE
                if (it.size > 1){
                    for (i in 0 until 1){
                        itenaryList.add(it[i])
                    }
                    dayAdapter.updateItenary(itenaryList)
                    txtItenarySeeMore.visibility = View.VISIBLE
                }
                else{
                    dayAdapter.updateItenary(iteneraries)
                    txtItenarySeeMore.visibility = View.GONE
                }

                if (experienceDetailModel.is_customised_intinerary == 1){
                    txtItineraryCustomize.visibility = View.VISIBLE
                }
                else{
                    txtItineraryCustomize.visibility = View.GONE
                }
            }
            else{
                linearItinerary.visibility = View.GONE
            }
        }

        experienceDetailModel.exp_inclusion?.let {
            rvInclude.adapter = IncludeAdapter(this,it)
        }

        experienceDetailModel.exp_exclusion?.let {
            rvExclude.adapter = ExcludeAdapter(this,it)
        }

        experienceDetailModel.exp_type?.let {
            it.add(0,experienceDetailModel.exp_booking_type ?: "")
            rvServiceType.adapter = ServiceTypeAdapter(1,this,it)
        }

        experienceDetailModel.exp_payment?.let {
            paymentModelList = it
            if (it.size > 1){
                isDownPaymentAvailable = true
                linearDownPayment.visibility = View.VISIBLE
            }
            else{
                isDownPaymentAvailable = false
                linearDownPayment.visibility = View.GONE
            }
        }

        experienceDetailModel.exp_photos?.let {

            photos = ArrayList()

            for(i in 0 until it.size){

                val hashMapPhoto = it[i]
                val photoList = hashMapPhoto["exp_photo_image"] as ArrayList<PhotoModel>
                val photoSize = photoList.size

                for (j in 0 until photoSize){
                    val model = photoList[j]
                    photos.add(model)
                }
            }

           txtPage.text = "1 / "+photos.size
           txtMorePhoto.text = ""+photos.size+ " More Photos"

           setImagePager()

        }

        experienceDetailModel.exp_facility?.let {
            rvFacility.layoutManager = GridLayoutManager(this,2)
            facilityList = it
            val facilityLists = ArrayList<FacilityModel>()

            if (it.size > 6){
                for (i in 0 until 6){
                    facilityLists.add(facilityList[i])
                }
                facilityAdapter.updateFacility(facilityLists)
                txtAllFacility.visibility = View.VISIBLE
            }
            else{
                facilityAdapter.updateFacility(facilityList)
                txtAllFacility.visibility = View.GONE
            }
        }

        experienceDetailModel.exp_rules?.let {
            rvRules.layoutManager = GridLayoutManager(this,2)
            rvRules.adapter = BoatRulesAdapter(this,it)
        }

        experienceDetailModel.exp_guides?.let {
            if (it.size > 0){
                linearYourGuide.visibility = View.VISIBLE
                rvGuide.adapter = GuideAdapter(this,0, it, 0,null)
            }
            else{
                linearYourGuide.visibility = View.GONE
            }
        }

        experienceDetailModel.exp_languages?.let {
           if (it.size > 0){
               var strName = ""

               if (it.size > 1){
                   for (i in 0 until it.size){
                       val language = it[i]
                       val name =  language["languageName"] as? String ?: ""

                       if (i != it.size - 1){
                           strName += "$name, "
                       }
                       else{
                           strName += name
                       }
                   }
               }
               else{
                   strName = it[0]["languageName"] as? String ?: ""
               }

               txtLanguage.text = strName
           }
        }

        experienceDetailModel.exp_accomodations?.let {
          if (it.size > 0){
              var strName = ""

              if (it.size > 1){
                  for (i in 0 until it.size){
                      val accomodation = it[i]
                      val name =  accomodation["accomodationsName"] as? String ?: ""

                      if (i != it.size - 1){
                          strName += "$name, "
                      }
                      else{
                          strName += name
                      }
                  }
              }
              else{
                  strName = it[0]["accomodationsName"] as? String ?: ""
              }

              txtAccomodation.text = strName
          }
        }

        experienceDetailModel.exp_packages?.let {
            val packageMap = it[0]
            val packageId = packageMap["packageId"] as Int

            if (packageId != 0){
                linearPackages.visibility = View.VISIBLE
                linearPackage.visibility = View.VISIBLE
                rvPackage.adapter = PackageAdapter(this,0, it, 0,this)
            }
            else{
                linearPackages.visibility = View.GONE
                linearPackage.visibility = View.GONE
            }

            setPackage(packageMap,0)
            selectedPackageMap = packageMap
        }

        val marker_option =  MarkerOptions()
            .icon( MapUtil.vectorToBitmap(R.drawable.ic_location_red, Color.parseColor("#FFFFFF"),this))
            .position(LatLng( experienceDetailModel.exp_pickup_place_latitude, experienceDetailModel.exp_pickup_place_longitude))

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(experienceDetailModel.exp_pickup_place_latitude
            ,experienceDetailModel.exp_pickup_place_longitude),15f))
        gMap.addMarker(marker_option)
        gMap.uiSettings.setAllGesturesEnabled(false)

        if(experienceDetailModel.exp_pickup_place_latitude == 0.0 &&
            experienceDetailModel.exp_pickup_place_longitude == 0.0){
            linearMap.visibility = View.GONE
            txtDirection.visibility = View.GONE
        }

        setStar(expDetailModel.rating,imgStar1,imgStar2,imgStar3,imgStar4,imgStar5,txtStar)
        experienceController.loadExperienceReview(experience_id,this)

        linearContent.visibility = View.VISIBLE
        shimerLayout.visibility = View.GONE
        shimerLayout.stopShimmerAnimation()
    }

    private fun setTextDescription(){
        if (Build.VERSION.SDK_INT >= 24)
        {
            txtDescription.text = Html.fromHtml(experienceDetailModel.exp_desc, Html.FROM_HTML_MODE_LEGACY)
        }
        else
        {
            @SuppressWarnings("deprecation")
            txtDescription.text = Html.fromHtml(experienceDetailModel.exp_desc)
        }

        val textLine = txtDescription.lineCount

        if (textLine > 3){
            txtDescription.maxLines = 3
            txtDescription.ellipsize = TextUtils.TruncateAt.END
            txtSeeMore.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setItenaryMore(){
        if (!isItenarySeeMore){
            dayAdapter.updateItenary(iteneraries)
            isItenarySeeMore = true
            txtItenarySeeMore.text = "See Less"
        }
        else{
            val itenaryList = ArrayList<ItemItenaryModel>()
            for (i in 0 until 1){
                itenaryList.add(iteneraries[i])
            }
            dayAdapter.updateItenary(itenaryList)
            isItenarySeeMore = false
            txtItenarySeeMore.text = "See More"
            setTabListener(2)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setFacilityMore(){
        if (!isFacilitySeeMore){
            facilityAdapter.updateFacility(facilityList)
            txtAllFacility.text = "See Less"
            isFacilitySeeMore = true
        }
        else{
            val facilityLists = ArrayList<FacilityModel>()
            for (i in 0 until 6){
                    facilityLists.add(facilityList[i])
                }
            facilityAdapter.updateFacility(facilityLists)
            txtAllFacility.text = "See More"
            isFacilitySeeMore = false
            setTabListener(3)
        }
    }

    override fun onExperienceDetailError() {

    }

    override fun onExperienceReviewPrepare() {

    }

    override fun onExperienceReviewLoaded(reviewList: ArrayList<ReviewModel>) {
        rvReview.adapter = ReviewAdapter(this,reviewList)
    }

    override fun onExperienceReviewError() {

    }

    override fun onTripInspirationPrepare() {

    }

    override fun onTripInspirationLoaded(inspirationList: ArrayList<TripInspirationModel>) {
         rvInspiration.adapter = InspirationAdapter(this,inspirationList)
    }

    override fun onTripInspirationError() {

    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let {
            gMap = it
        }
    }

    override fun onCreateWishlistPrepare() {
        loadingDialog.show()
    }

    override fun onCreateWishlistSuccess() {
        loadingDialog.dismiss()
        if (!isWishList){
            isWishList = true
            imgWishlist.setImageResource(R.drawable.ic_wishlist_added)
            ViewUtil.showBlackToast(this,"Success add to wishlist",0).show()
        }
        else{
            isWishList = false
            imgWishlist.setImageResource(R.drawable.ic_draft)
            ViewUtil.showBlackToast(this,"Success to unwishlist",0).show()
        }
    }

    override fun onCreateWishlistError(errorCode: Int) {
        loadingDialog.dismiss()
        if (errorCode == 401){
            ViewUtil.showBlackToast(this,"Session expired, please login again",0).show()
        }
        else if (errorCode == 400){
            ViewUtil.showBlackToast(this,"This experience have added to wishlist",0).show()
        }
        else{
            ViewUtil.showBlackToast(this,"Failed to add wishlist, please try again",0).show()
        }
    }

    override fun onCheckWishlistPrepare() {

    }

    override fun onCheckWishlistSuccess(isExist : Boolean) {
        if (isExist){
            isWishList = true
            imgWishlist.setImageResource(R.drawable.ic_wishlist_added)
        }
        else{
            isWishList = false
            imgWishlist.setImageResource(R.drawable.ic_draft)
        }
    }

    override fun onCheckWishlistError() {

    }

    override fun onPackageClicked(packageMap: java.util.HashMap<String, Any>) {
        setPackage(packageMap,1)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.btnMorePhotos -> {
                val i = Intent(this,ActivityPhoto::class.java)
                i.putExtra("photos", experienceDetailModel.exp_photos as Serializable)
                startActivity(i)
            }

            R.id.btnBook -> {
                checkoutDialog.show()
            }

            R.id.txtDirection -> {
                openGoogleMap(LatLng(experienceDetailModel.exp_pickup_place_latitude,
                    experienceDetailModel.exp_pickup_place_longitude))
            }

            R.id.linearOverview -> {
                setTabListener(1)
            }

            R.id.linearPackages -> {
                setTabListener(2)
            }

            R.id.linearExperience -> {
                setTabListener(3)
            }

            R.id.linearFacility -> {
                setTabListener(4)
            }

            R.id.linearAvaibility -> {
                setTabListener(5)
            }

            R.id.linearReview -> {
                setTabListener(6)
            }

            R.id.imgPrevious -> {
                val currentYearMonth = currentCalendarMonth.yearMonth.minusMonths(
                    1)
                calendarView.scrollToMonth(currentYearMonth)
            }

            R.id.imgNext -> {
                val currentYearMonth = currentCalendarMonth.yearMonth.plusMonths(
                    1)
                calendarView.scrollToMonth(currentYearMonth)
            }

            R.id.imgWishlist -> {
                createWishList()
            }

            R.id.imgSearch -> {
                val i = Intent(this,ActivityExperienceSearch::class.java)
                i.putExtra("from",1)
                startActivity(i)
            }

            R.id.txtSeeMore -> {
                if (!isSeeMore){
                    txtDescription.maxLines = Integer.MAX_VALUE
                    txtDescription.ellipsize = null
                    txtSeeMore.text = "See Less"
                    isSeeMore = true
                }
                else{
                    txtDescription.maxLines = 3
                    txtDescription.ellipsize = TextUtils.TruncateAt.END
                    txtSeeMore.text = "See More"
                    isSeeMore = false
                    setTabListener(1)
                }
            }

            R.id.imgShareCLose -> {
                shareDialog.dismiss()
            }

            R.id.linearFacebook -> {
                setsShareFacebook()
            }

            R.id.linearTwitter -> {
                shareTwitter()
            }

            R.id.linearWhatsapp -> {
                openWhatsAppAndSendMessage()
            }

            R.id.linearGmail -> {
                openGmailAndSendMessage()
            }

            R.id.linearLine -> {
                openLineAndShareMessage()
            }

            R.id.txtItenarySeeMore -> {
                setItenaryMore()
            }

            R.id.txtAllFacility -> {
                setFacilityMore()
            }

            R.id.imgShare -> {
                shareDialog.show()
            }

            R.id.linearDate -> {
                dateBottomSheetDialog.show()
            }

            R.id.linearGuest -> {
                guestBottomSheetDialog.show()
            }

            R.id.icCLose -> {
                guestBottomSheetDialog.dismiss()
            }

            R.id.icDateCLose -> {
                dateBottomSheetDialog.dismiss()
            }

            R.id.imgAdultMinus -> {
                setAdultCount(1)
            }

            R.id.imgAdultPlus -> {
                setAdultCount(2)
            }

            R.id.imgChildrenMinus -> {
                setChildrenCount(1)
            }

            R.id.imgChildrenPlus -> {
                setChildrenCount(2)
            }

            R.id.imgInfantMinus -> {
                setInfantCount(1)
            }

            R.id.imgInfantPlus -> {
                setInfantCount(2)
            }

            R.id.btnGuestApply -> {
                guestBottomSheetDialog.dismiss()
                setGuest()
            }

            R.id.btnDateApply -> {
                dateBottomSheetDialog.dismiss()
                applyDate()
            }

            R.id.btnDialogBook -> {
                intentCheckout(null)
            }
        }
    }
}


