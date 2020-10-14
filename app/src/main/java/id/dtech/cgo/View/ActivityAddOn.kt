package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import id.dtech.cgo.Adapter.AddOnAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.BookingController

import id.dtech.cgo.Controller.ExperienceController
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.AddOnModel
import id.dtech.cgo.Model.ExperienceDetailModel
import id.dtech.cgo.Model.PaymentModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CalendarUtil
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.Util.ViewUtil

import kotlinx.android.synthetic.main.activity_add_on.*
import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.layout_calendar_header.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityAddOn : AppCompatActivity(), View.OnClickListener, MyCallback.Companion.AddOnCallback,
    ApplicationListener.Companion.AddOnListener, ApplicationListener.Companion.AddOnCheckoutListener,
    MyCallback.Companion.CalculatePriceCallback
{

    private val SELECT_PACKAGE_REQUEST = 517
    private val SELECT_GUIDE_REQUEST = 515

    private lateinit var selectedAddOnList : ArrayList<AddOnModel>
    private lateinit var bookingController: BookingController

    private lateinit var intentMap : HashMap<String,Any>
    private lateinit var packageMap : HashMap<String,Any>
    private lateinit var selectedGuideMap : HashMap<String,Any>
    private lateinit var guideList : ArrayList<HashMap<String,Any>>
    private lateinit var packageList : ArrayList<HashMap<String,Any>>
    private lateinit var packageExpPayment : ArrayList<HashMap<String,Any>>

    private lateinit var addOnAdapter : AddOnAdapter

    private lateinit var paymentMethodList : ArrayList<PaymentModel>
    private lateinit var avaibilityDate : ArrayList<LocalDate>
    private lateinit var addOnModelList: ArrayList<AddOnModel>

    private lateinit var experienceController: ExperienceController
    private lateinit var experienceDetailModel : ExperienceDetailModel

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

    private lateinit var calendarView : CalendarView
    private val today = LocalDate.now()

    private val selectedDates = ArrayList<LocalDate>()

    private var paymentType = ""
    private var price = 0L

    private var strSelectedDate = ""

    private var date = ""
    private var guest = 0

    private var adultCount = 0
    private var childrenCount = 0
    private var infantCount = 0

    private var isDownPaymentAvailable = false
    private var defaultCurrency = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_on)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        experienceController = ExperienceController()

        val b = intent.extras

        b?.let {
            intentMap = it.getSerializable("intentMap") as HashMap<String, Any>
            bookingController = BookingController()

            initiateMainObject(0)
            setGuest()
            initiatePackage()
            initiateGuide()
            showSelectDateDialog()
            setInputGuestDialog()
        }

        ivBack.setOnClickListener(this)
        btnCheckout.setOnClickListener(this)
        linearDate.setOnClickListener(this)
        linearGuest.setOnClickListener(this)
        linearPackage.setOnClickListener(this)
        linearSelectGuide.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun initiateMainObject(fromMain : Int){
        experienceDetailModel = intentMap["experienceDetailModel"] as ExperienceDetailModel
        packageMap = intentMap["packageMap"] as HashMap<String, Any>

        guideList = intentMap["guideList"] as ArrayList<HashMap<String, Any>>
        packageList = intentMap["packageList"] as ArrayList<HashMap<String, Any>>
        packageExpPayment = intentMap["packageExpPayment"] as ArrayList<HashMap<String, Any>>

        adultCount = intentMap["adult_count"] as Int? ?: 0
        childrenCount = intentMap["children_count"] as Int? ?: 0
        infantCount = intentMap["infant_count"] as Int? ?: 0
        strSelectedDate = intentMap["selected_date"] as String
        date = intentMap["date"] as String? ?: ""

        isDownPaymentAvailable = intentMap["isDownPaymentAvailable"] as Boolean? ?: false
        avaibilityDate = intentMap["avaibility"] as ArrayList<LocalDate>? ?: ArrayList()
        paymentMethodList = intentMap["method_list"] as ArrayList<PaymentModel>? ?: ArrayList()

        paymentType = packageMap["packageTypePayment"] as String? ?: ""
        defaultCurrency = packageMap["packageCurrency"] as String? ?: ""

        val totalGuest = adultCount + childrenCount

        if (totalGuest != 0) {
            price = (packageMap["packagePrice"] as Long? ?: 0) * totalGuest
        }
        else{
            price = packageMap["packagePrice"] as Long? ?: 0
        }

        txtLocation.text = experienceDetailModel.harbors_name+", "+experienceDetailModel.province
        txtTitle.text = experienceDetailModel.exp_title ?: ""

        val paymentTypeArray = paymentType.split(" ")

        if (paymentTypeArray.size > 1){
            paymentType = paymentTypeArray[1]
        }

        if (strSelectedDate.isNotEmpty()){
            txtDate.text = strSelectedDate
        }
        else{
            txtDate.text = "Select"
        }

        if (fromMain == 1){
            calendarView.notifyCalendarChanged()
            txtDate.text = "Select"
            date = ""
        }

        setTotalPrice(0)
        initiateAddOnList()
        initiateAddOnObject()
    }

    private fun loadPrice(){
        val totalGuest = adultCount + childrenCount
        if (date.isNotEmpty()){
            val dateMap = HashMap<String,Any>()
            dateMap["date"] = date
            dateMap["total_guest"] = totalGuest
            dateMap["package_id"] = packageMap["packageId"] as Int
            dateMap["currency"] = packageMap["packageCurrency"] as String
            dateMap["exp_id"] = experienceDetailModel.experience_id ?: ""

            bookingController.getCalculationPrice(dateMap,this)
        }
        else{
            linearPrice.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalPrice(from : Int){
        val total_price = CurrencyUtil.decimal(price).replace(",",".")

        if (from == 0){
            txtPrice.text = "$defaultCurrency $total_price"
        }
        else{
            var totalPrice = 0L
            for (i in 0 until selectedAddOnList.size){
                totalPrice += selectedAddOnList[i].amount
            }
            price = totalPrice
            val strPrice = CurrencyUtil.decimal(price).replace(",",".")
            txtPrice.text = "$defaultCurrency $strPrice/$paymentType"
        }
    }

    private fun initiateAddOnList(){
        selectedAddOnList = ArrayList()

        val addOnTicketOnly = AddOnModel()
        addOnTicketOnly.id = ""
        addOnTicketOnly.name = getString(R.string.ticket_only)
        addOnTicketOnly.desc = getString(R.string.purchase)
        addOnTicketOnly.currency = defaultCurrency
        addOnTicketOnly.amount = price
        addOnTicketOnly.total_price = price
        addOnTicketOnly.exp_id = experienceDetailModel.experience_id ?: ""
        addOnTicketOnly.selected = true

        selectedAddOnList.add(addOnTicketOnly)
    }

    private fun initiateAddOnObject(){
        addOnModelList = ArrayList()
        addOnAdapter = AddOnAdapter(this,addOnModelList,paymentType,this,this)

        rvAddOn.layoutManager = LinearLayoutManager(this)
        rvAddOn.adapter = addOnAdapter

        experienceController.getAddOn(experienceDetailModel.experience_id ?: "",
            price,this)
    }

    private fun initiatePackage(){
        if (packageList.size > 0){
            linearPackage.visibility = View.VISIBLE
            packageSpace.visibility = View.VISIBLE

            val packageId = packageMap["packageId"] as Int
            val packageName = packageMap["packageName"] as String

            if (packageId == 0){
                linearPackage.visibility = View.GONE
                packageSpace.visibility = View.GONE
            }
            else{
                linearPackage.visibility = View.VISIBLE
                txtPackageName.text = packageName

                if (packageList.size > 1){
                    txtPackageTitle.setTextColor(Color.parseColor("#000000"))
                    txtPackageName.setTextColor(Color.parseColor("#000000"))
                }
                else{
                    txtPackageTitle.setTextColor(Color.parseColor("#7A7A7A"))
                    txtPackageName.setTextColor(Color.parseColor("#7A7A7A"))
                    imgPackageDD.visibility = View.GONE
                }
            }
        }
        else{
            linearPackage.visibility = View.GONE
            packageSpace.visibility = View.GONE
        }
    }

    private fun initiateGuide(){
        if (guideList.size > 0){
            linearSelectGuide.visibility = View.VISIBLE

            if (intentMap["selectedGuideMap"] != null){
                selectedGuideMap = intentMap["selectedGuideMap"] as HashMap<String, Any>
                val guideName = selectedGuideMap["guide_name"] as String
                txtGuide.text = guideName
            }

            if (guideList.size > 1){
                txtGuideTitle.setTextColor(Color.parseColor("#000000"))
                txtGuide.setTextColor(Color.parseColor("#000000"))
            }
            else{
                txtGuideTitle.setTextColor(Color.parseColor("#7A7A7A"))
                txtGuide.setTextColor(Color.parseColor("#7A7A7A"))
                imgGuideDD.visibility = View.GONE
            }
        }
        else{
            linearSelectGuide.visibility = View.GONE
        }
    }

    private fun intentSelectGuide(){
        if (guideList.size > 1){
            val i = Intent(this, ActivitySelectGuide::class.java)
            i.putExtra("intentMap", intentMap as Serializable)
            i.putExtra("intent_from",1)
            startActivityForResult(i,SELECT_GUIDE_REQUEST)
        }
    }

    private fun intentSelectPackage(){
        if (packageList.size > 1){
            val i = Intent(this, PackageListActivity::class.java)
                i.putExtra("intentMap", intentMap as Serializable)
                startActivityForResult(i,SELECT_PACKAGE_REQUEST)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun intentCheckout(){

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

        val i = Intent(this,ActivityCheckout::class.java)
        i.putExtra("intentMap", intentMap)
        i.putExtra("experience_model", experienceDetailModel)
        i.putExtra("from", 1)
        i.putExtra("payment_type", paymentType)
        i.putExtra("selected_date",strSelectedDate)
        i.putExtra("date", date)
        i.putExtra("adult_count", adultCount)
        i.putExtra("children_count", childrenCount)
        i.putExtra("infant_count", infantCount)
        i.putExtra("isDownPaymentAvailable", isDownPaymentAvailable)
        i.putParcelableArrayListExtra("addOnList", selectedAddOnList)
        i.putParcelableArrayListExtra("method_list",paymentMethodList)

        startActivity(i)
    }

    private fun showSelectDateDialog() {
        dateBottomSheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.fragment_select_date, null)
        dateBottomSheetDialog.setContentView(sheetView)
        dateBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        icDateCLose = sheetView.findViewById(R.id.icDateCLose)
        calendarView = sheetView.findViewById(R.id.calendarView)
        btnDateApply = sheetView.findViewById(R.id.btnDateApply)

        icDateCLose.setOnClickListener(this)
        btnDateApply.setOnClickListener(this)

        setCalendar()
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
                    if (day.owner == DayOwner.THIS_MONTH && avaibilityDate.contains(day.date) &&
                        day.date.dayOfYear >= today.dayOfYear) {
                        if (selectedDates.size > 0){
                            selectedDates.removeAt(0)
                            selectedDates.add(day.date)
                        }
                        else{
                            selectedDates.add(day.date)
                        }

                        btnDateApply.isEnabled = true
                        calendarView.notifyCalendarChanged()
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.txtMonthYear
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {

            override fun create(view: View) = MonthViewContainer(view)

            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
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

    @SuppressLint("SimpleDateFormat")
    private fun applyDate(){
        if (selectedDates.size > 0){
            val sdfCalendar = SimpleDateFormat("yyyy-MM-dd")
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val strDate = selectedDates[0].toString()
            val dateCalendar = sdfCalendar.parse(strDate) ?: Date()
            date = sdfDate.format(dateCalendar)

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val sdfDayDate = SimpleDateFormat("dd MMM yyyy")
            val sdfDay = SimpleDateFormat("dd")
            val sdfDayMonth = SimpleDateFormat("dd MMMM yyyy")

            val date = sdf.parse(strDate)
            val duration = experienceDetailModel.exp_duration

            if (duration > 1){
                val calendar = Calendar.getInstance()
                calendar.time = date ?: Date()
                calendar.add(Calendar.DATE,duration - 1)

                val strDay = sdfDay.format(date ?: Date())
                val strDayDate = sdfDayDate.format(calendar.time)

                val strDayDateYear = "$strDay - $strDayDate"
                strSelectedDate = strDayDateYear
            }
            else{
                val strDay = sdfDayMonth.format(date ?: Date())
                strSelectedDate = strDay
            }

            txtDate.text = strSelectedDate

            loadPrice()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setGuest(){
        val totalGuest = adultCount + childrenCount + infantCount

        if (totalGuest > 1){
            txtGuest.text = "$totalGuest Guest(s)"
        }
        else{
            txtGuest.text = "$totalGuest Guest"
        }
    }

    override fun onAddOnPrepare() {
        linearAddOn.visibility = View.GONE
        shimerLayout.visibility = View.VISIBLE
        shimerLayout.startShimmerAnimation()
    }

    override fun onAddOnSuccess(data: ArrayList<AddOnModel>) {

        linearAddOn.visibility = View.VISIBLE
        shimerLayout.visibility = View.GONE
        shimerLayout.stopShimmerAnimation()

        val addOnTicketOnly = AddOnModel()
        addOnTicketOnly.id = ""
        addOnTicketOnly.name = getString(R.string.ticket_only)
        addOnTicketOnly.desc = getString(R.string.purchase)
        addOnTicketOnly.currency = defaultCurrency
        addOnTicketOnly.amount = price
        addOnTicketOnly.total_price = price
        addOnTicketOnly.exp_id = experienceDetailModel.experience_id ?: ""
        addOnTicketOnly.selected = true

        data.add(0, addOnTicketOnly)

        addOnModelList = data

        addOnAdapter.updateList(addOnModelList)

        loadPrice()
    }

    override fun onAddOnError() {
        ViewUtil.showBlackToast(this,"Load data error",0).show()
    }

    override fun onAddOnClicked(model: AddOnModel, selected: Boolean, position : Int) {
        if (!selected){
            selectedAddOnList.add(model)
        }
        else{
            selectedAddOnList.remove(model)
        }
        setTotalPrice(1)
        addOnModelList[position].selected = !selected
        addOnAdapter.updateList(addOnModelList)
    }

    override fun onAddOnCheckoutClicked() {
        intentCheckout()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_GUIDE_REQUEST){
            if (resultCode == ActivitySelectGuide.SELECT_GUIDE_RESULT){
                data?.let {
                    val b = it.extras
                    b?.let { bundle ->
                        intentMap = bundle.getSerializable("intentMap") as HashMap<String, Any>
                        initiateGuide()
                    }
                }
            }
        }
        else if (requestCode == SELECT_PACKAGE_REQUEST){
            if (resultCode == PackageListActivity.PACKAGE_RESULT_CODE){
                data?.let {
                    val b = it.extras
                    b?.let { bundle ->
                        intentMap = bundle.getSerializable("intentMap") as HashMap<String, Any>
                        initiateMainObject(1)
                        initiatePackage()
                    }
                }
            }
        }
        else{

        }
    }

    override fun onCalculatePricePrepare() {
        shimerLayout.visibility = View.VISIBLE
        linearPrice.visibility = View.GONE
        shimerLayout.startShimmerAnimation()
    }

    override fun onCalculatePriceSuccess(data: HashMap<String, Any>) {
        shimerLayout.stopShimmerAnimation()
        shimerLayout.visibility = View.GONE
        linearPrice.visibility = View.VISIBLE

        val price = data["price"] as Long

        if (price != 0L){
            selectedAddOnList[0].amount = price
            addOnModelList[0].amount = price
        }
        else{
            val totalGuest = adultCount + childrenCount
            val paymentType = packageMap["packageTypePayment"] as String

            if (paymentType == "Per Pax"){
                if (totalGuest != 0){
                    selectedAddOnList[0].amount = packageMap["packagePrice"] as Long  * totalGuest
                    addOnModelList[0].amount = packageMap["packagePrice"] as Long  * totalGuest
                }
                else{
                    selectedAddOnList[0].amount = packageMap["packagePrice"] as Long
                    addOnModelList[0].amount = packageMap["packagePrice"] as Long
                }
            }
            else{
                selectedAddOnList[0].amount = packageMap["packagePrice"] as Long
                addOnModelList[0].amount = packageMap["packagePrice"] as Long
            }
        }

        addOnAdapter.updateList(addOnModelList)

        setTotalPrice(1)
    }

    override fun onCalculatePriceError() {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.linearGuest -> {
                guestBottomSheetDialog.show()
            }

            R.id.linearDate -> {
                dateBottomSheetDialog.show()
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

            R.id.icCLose -> {
                guestBottomSheetDialog.dismiss()
            }

            R.id.icDateCLose -> {
                dateBottomSheetDialog.dismiss()
            }

            R.id.btnGuestApply -> {
                guestBottomSheetDialog.dismiss()
                setGuest()
                loadPrice()
            }

            R.id.linearPackage -> {
                intentSelectPackage()
            }

            R.id.linearSelectGuide -> {
                intentSelectGuide()
            }

            R.id.btnDateApply -> {
                dateBottomSheetDialog.dismiss()
                applyDate()
            }

            R.id.btnCheckout -> {
                intentCheckout()
            }
        }
    }
}

