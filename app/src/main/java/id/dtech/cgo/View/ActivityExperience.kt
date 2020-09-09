package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.innovattic.rangeseekbar.RangeSeekBar
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import id.dtech.cgo.Adapter.ActivityTypeAdapter
import id.dtech.cgo.Adapter.ServiceExperienceAdapter
import id.dtech.cgo.Adapter.SortByAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.ExperienceController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ActivityTypeModel
import id.dtech.cgo.Model.ExpDestinationModel
import id.dtech.cgo.Model.ExperienceModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CalendarUtil
import id.dtech.cgo.Util.CurrencyUtil
import kotlinx.android.synthetic.main.activity_experience.*
import kotlinx.android.synthetic.main.calendar_day.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActivityExperience : AppCompatActivity(), View.OnClickListener, MyCallback.Companion.ExperienceSearchCallback,
    ApplicationListener.Companion.ActivityTypeListener, ApplicationListener.Companion.SortByListener,
    RangeSeekBar.SeekBarChangeListener, ApplicationListener.Companion.ServiceExperienceListener {

    private lateinit var experienceController: ExperienceController

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private lateinit var sortBySheetDialog : RoundedBottomSheetDialog
    private lateinit var filterBySheetDialog : RoundedBottomSheetDialog
    private lateinit var currentCalendarMonth : CalendarMonth

    private lateinit var typeBooleanList : ArrayList<Boolean>
    private lateinit var sortDataList : ArrayList<HashMap<String,Any>>

    private lateinit var rvType : RecyclerView
    private lateinit var icCLose : ImageView

    private lateinit var calendarView : CalendarView
    private lateinit var txtMonthYear : MyTextView
    private lateinit var calendarBtnApply : Button
    private lateinit var imgNext : ImageView
    private lateinit var imgPrev : ImageView

    private lateinit var icGuestClose : ImageView
    private lateinit var btnGuestApply : Button

    private lateinit var imgAdultMinus : ImageView
    private lateinit var imgAdultPlus : ImageView
    private lateinit var txtAdultCount : TextView

    private lateinit var imgChildrenMinus : ImageView
    private lateinit var imgChildrenPlus : ImageView
    private lateinit var txtChildrenCount : MyTextView

    private lateinit var imgInfantMinus : ImageView
    private lateinit var imgInfantPlus : ImageView
    private lateinit var txtInfantCount : TextView

    private lateinit var txtMinPrice : MyTextView
    private lateinit var txtMaxPrice : MyTextView

    private lateinit var dialogType : Dialog
    private lateinit var dialogCalendar : Dialog
    private lateinit var dialogGuest : Dialog

    private lateinit var icFilterByClose : ImageView
    private lateinit var icSortByClose : ImageView

    private lateinit var linearCard : LinearLayout
    private lateinit var imgCard : ImageView

    // SORT BY FILTER
    private lateinit var btnFilter : Button
    private lateinit var txtClear : MyTextView
    private lateinit var rvSortBy : RecyclerView

    // FILTER BY FILTER
    private lateinit var btnFilters : Button
    private lateinit var txtFilterClearAll : MyTextView
    private lateinit var txtPrivate : MyTextView
    private lateinit var txtSharing : MyTextView
    private lateinit var mySeekBar : RangeSeekBar

    private val typelist = ArrayList<Int>()

    private var start_date : String? = null
    private var end_date : String? = null
    private var sortby : String? = null

    private var guest : Int? = null
    private var trip : Int? = null

    private var harbor_id : String? = null
    private var city_id : Int? = null
    private var province_id : Int? = null

    private var bottomprice : Long = 0
    private var upperprice : Long = 500000000

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private var adultCount = 0
    private var childrenCount = 0
    private var infantCount = 0

    // VIEW SEARCH OBJECT
    private var viewTrip : Int? = null
    private var strSortBy : String? = null
    private var viewBottomprice : Long = 0
    private var viewUpperprice : Long = 500000000
    private var isSortByClearClicked = false
    private var isFilterByClearClicked = false

    // OBJECT EXPERIENCE
    private lateinit var experienceList : ArrayList<ExperienceModel>
    private lateinit var serviceExperienceAdapter : ServiceExperienceAdapter

    private var from = 0
    private var loadFrom = 1
    private var currentPage = 1

    private var isLoading = false
    private var isErrorOccured = false
    private var totalRecords = 0

    private var isLinearCardClicked = false

    private var lastIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience)
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView(){
        rvService.layoutManager = LinearLayoutManager(this)

        experienceList = ArrayList()
        serviceExperienceAdapter = ServiceExperienceAdapter(this,experienceList,this)
        rvService.adapter = serviceExperienceAdapter

        experienceController = ExperienceController()
        typeBooleanList = setBooleanList()

        val b = intent.extras

        b?.let { bundle ->

            from = bundle.getInt("from")

            if (from == 1){
                nestedScrollView.visibility = View.VISIBLE
                nestedContent.visibility = View.GONE
            }
            else{
                nestedScrollView.visibility = View.GONE
                nestedContent.visibility = View.VISIBLE

                if (from == 2){
                    val typePosition = bundle.getInt("activity_position")
                    val booleanPosition = typePosition - 1

                    typelist.add(typePosition)
                    typeBooleanList[booleanPosition] = true

                    linearActivity.setBackgroundResource(R.drawable.background_more_stroke)
                    txtActivities.text = "Activities: 1"
                }
                else{
                    val cityName = bundle.getString("name")
                    val from_location = bundle.getInt("from_location")

                    if (from_location == 1){
                        harbor_id = bundle.getString("location_id") ?: ""
                    }
                    else if (from_location == 2){
                        city_id = bundle.getInt("location_id")
                    }
                    else{
                        province_id = bundle.getInt("location_id")
                    }

                    txtSearch.text = cityName ?: ""
                    txtSearch.setTextColor(Color.parseColor("#000000"))
                    txtSearch.setTypeface(txtSearch.typeface,Typeface.BOLD)

                    icSearchClose.visibility = View.VISIBLE
                }

                experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString().replace(" ", ""),start_date,
                    end_date,guest,trip,bottomprice,upperprice,sortby,1,10,1,this)
            }
        }

        setLoadMore()
        setActivityTypeAlertDialog()
        setCalenderAlertDialog()
        setGuestDialog()
        setTransportationSortBy()
        setTransportationFilterBy()

        ivBack.setOnClickListener(this)
        icSearchClose.setOnClickListener(this)
        linearShortBy.setOnClickListener(this)
        linearFilterBy.setOnClickListener(this)
        linearSearch.setOnClickListener(this)
        linearActivity.setOnClickListener(this)
        linearGuest.setOnClickListener(this)
    }

    private fun setLoadMore(){
        nestedContent.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int,
                                                  _: Int, oldScrollY: Int ->
            v?.let {
                if(v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY) {
                        val lastVisiblePosition = (rvService.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val totalList = experienceList.size - 1

                        if (lastVisiblePosition == totalList){
                            if (experienceList.size != totalRecords && !isLoading){
                                loadFrom = 2
                                val page = currentPage + 1
                                experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString().replace(" ", ""),start_date,
                                    end_date,guest,trip,bottomprice,upperprice,sortby,page,10,2,this@ActivityExperience)

                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setActivityTypeAlertDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_activity_dialog,null)
        dialogType = Dialog(this,R.style.FullWidth_Dialog)
        dialogType.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogType.setContentView(view)
        dialogType.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        rvType = view.findViewById(R.id.rvType)
        icCLose = view.findViewById(R.id.icCLose)

        rvType.layoutManager = LinearLayoutManager(this)
        rvType.adapter = ActivityTypeAdapter(this,typeBooleanList,setActivityTypeData(),this)

        dialogType.setOnDismissListener {
            val size = typelist.size

            if (size > 0){
                txtActivities.text = "Activities: $size"
                linearActivity.setBackgroundResource(R.drawable.background_more_stroke)
            }
            else{
                txtActivities.text = "Activities"
                linearActivity.setBackgroundResource(R.drawable.background_more)
            }

            loadFrom = 1
            currentPage = 1
            experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString().replace(" ", ""),start_date,
                end_date,guest,trip,bottomprice,upperprice,sortby,1,10,1,this)
        }

        icCLose.setOnClickListener(this)
        linearDate.setOnClickListener(this)

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

    @SuppressLint("SimpleDateFormat")
    private fun setCalendar() {

        val daysOfWeek = CalendarUtil.daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)

        calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)


        class DayViewContainer(view: View) : ViewContainer(view) {

            lateinit var day: CalendarDay

            val textView = view.exOneDayText
            val bgView = view.exFourRoundBgView

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {

                        val date = day.date

                        if (startDate != null){
                            if (date < startDate || endDate != null) {
                                startDate = date
                                endDate = null
                            }
                            else if (date != startDate) {
                                endDate = date
                            }
                            else{
                                startDate = null
                                endDate = null
                            }
                        }
                        else{
                            startDate = date
                        }

                        if (startDate != null){
                            start_date = startDate!!.toString()

                            if (endDate != null){
                                end_date = endDate!!.toString()
                            }
                            else{
                                end_date = null
                            }

                            calendarBtnApply.isEnabled = true
                            calendarBtnApply.setTextColor(Color.parseColor("#FFFFFF"))
                        }
                        else{
                            start_date = null
                            end_date = null
                            calendarBtnApply.isEnabled = false
                            calendarBtnApply.setTextColor(Color.parseColor("#BDBDBD"))
                        }

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
                val bgView = container.bgView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                        when {
                            startDate == day.date && endDate == null -> {
                                bgView.visibility = View.GONE
                                textView.setTextColor(Color.parseColor("#FFFFFF"))
                                textView.setBackgroundResource(R.drawable.background_calender_selected)
                            }
                            day.date == startDate -> {
                                textView.setTextColor(Color.parseColor("#FFFFFF"))
                                textView.setBackgroundResource(R.drawable.background_calender_selected)

                                bgView.visibility = View.VISIBLE
                                bgView.setBackgroundResource(R.drawable.background_calender_middle_left)

                                val layoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams
                                    .MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
                                layoutParam.setMargins(4.dp,4.dp,0.dp,4.dp)
                                bgView.layoutParams = layoutParam
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColor(Color.parseColor("#35405A"))
                                textView.setBackgroundResource(0)

                                bgView.visibility = View.VISIBLE
                                bgView.setBackgroundResource(R.drawable.background_calender_middle)

                                val layoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams
                                    .MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
                                layoutParam.setMargins(0.dp,4.dp,0.dp,4.dp)
                                bgView.layoutParams = layoutParam
                            }
                            day.date == endDate -> {
                                textView.setTextColor(Color.parseColor("#FFFFFF"))
                                textView.setBackgroundResource(R.drawable.background_calender_selected)

                                bgView.visibility = View.VISIBLE
                                bgView.setBackgroundResource(R.drawable.background_calender_middle_right)

                                val layoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams
                                    .MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
                                layoutParam.setMargins(0.dp,4.dp,4.dp,4.dp)
                                bgView.layoutParams = layoutParam
                            }
                            else -> {
                                textView.setTextColor(Color.parseColor("#000000"))
                                textView.setBackgroundColor(0)
                                bgView.visibility = View.GONE
                            }
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

    private fun setGuestDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_guest_dialog,null)
        dialogGuest = Dialog(this,R.style.FullWidth_Dialog)
        dialogGuest.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialogGuest.setContentView(view)
        dialogGuest.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        icGuestClose = view.findViewById(R.id.icDialogCLose)

        imgAdultMinus  = view.findViewById(R.id.imgAdultMinus)
        imgAdultPlus = view.findViewById(R.id.imgAdultPlus)
        txtAdultCount = view.findViewById(R.id.txtAdultCount)

        imgChildrenMinus = view.findViewById(R.id.imgChildrenMinus)
        imgChildrenPlus = view.findViewById(R.id.imgChildrenPlus)
        txtChildrenCount = view.findViewById(R.id.txtChildrenCount)

        imgInfantMinus = view.findViewById(R.id.imgInfantMinus)
        imgInfantPlus = view.findViewById(R.id.imgInfantPlus)
        txtInfantCount = view.findViewById(R.id.txtInfantCount)

        btnGuestApply = view.findViewById(R.id.btnGuestApply)

        icGuestClose.setOnClickListener(this)

        imgAdultMinus.setOnClickListener(this)
        imgAdultPlus.setOnClickListener(this)

        imgChildrenMinus.setOnClickListener(this)
        imgChildrenPlus.setOnClickListener(this)

        imgInfantMinus.setOnClickListener(this)
        imgInfantPlus.setOnClickListener(this)

        btnGuestApply.setOnClickListener(this)
    }

    private fun setActivityTypeData() : ArrayList<ActivityTypeModel> {
        val activityTypeList = ArrayList<ActivityTypeModel>()

        val activityTypeModel1 = ActivityTypeModel()
        activityTypeModel1.id = 1
        activityTypeModel1.name = "Diving"

        val activityTypeModel2 = ActivityTypeModel()
        activityTypeModel2.id = 2
        activityTypeModel2.name = "Fishing"

        val activityTypeModel3 = ActivityTypeModel()
        activityTypeModel3.id = 3
        activityTypeModel3.name = "Snorkeling"

        val activityTypeModel4 = ActivityTypeModel()
        activityTypeModel4.id = 4
        activityTypeModel4.name = "Sailing"

        val activityTypeModel5 = ActivityTypeModel()
        activityTypeModel5.id = 5
        activityTypeModel5.name = "Tour"

        activityTypeList.add(activityTypeModel1)
        activityTypeList.add(activityTypeModel2)
        activityTypeList.add(activityTypeModel3)
        activityTypeList.add(activityTypeModel4)
        activityTypeList.add(activityTypeModel5)

        return activityTypeList
    }

    private fun setTransportationFilterBy(){
        filterBySheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.layout_experience_filterby, null)
        sheetView.minimumHeight = getScreenHeight() - 235
        filterBySheetDialog.setContentView(sheetView)
        filterBySheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        mySeekBar = sheetView.findViewById<RangeSeekBar>(R.id.mySeekBar)
        btnFilters = sheetView.findViewById(R.id.btnFilters)
        txtFilterClearAll = sheetView.findViewById(R.id.txtFilterClearAll)
        txtPrivate = sheetView.findViewById(R.id.txtPrivate)
        txtSharing = sheetView.findViewById(R.id.txtSharing)
        txtMinPrice = sheetView.findViewById(R.id.txtMinPrice)
        txtMaxPrice = sheetView.findViewById(R.id.txtMaxPrice)
        icFilterByClose = sheetView.findViewById(R.id.icFilterByClose)
        linearCard = sheetView.findViewById(R.id.linearCard)
        imgCard = sheetView.findViewById(R.id.imgCard)

        mySeekBar.seekBarChangeListener = this
        icFilterByClose.setOnClickListener(this)
        txtSharing.setOnClickListener(this)
        txtPrivate.setOnClickListener(this)
        btnFilters.setOnClickListener(this)
        txtFilterClearAll.setOnClickListener(this)
        linearCard.setOnClickListener(this)
    }

    private fun setTransportationSortBy(){
        sortBySheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.layout_experience_sortby, null)
        sheetView.minimumHeight = getScreenHeight() - 235
        sortBySheetDialog.setContentView(sheetView)
        sortBySheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        rvSortBy = sheetView.findViewById<RecyclerView>(R.id.rvSortBy)
        txtClear = sheetView.findViewById(R.id.txtClear)
        btnFilter = sheetView.findViewById(R.id.btnFilter)

        icSortByClose = sheetView.findViewById(R.id.icSortByClose)

        val highestMap = HashMap<String,Any>()
        val lowestMap = HashMap<String,Any>()
        val highestRatingMap = HashMap<String,Any>()
        val lowestRatingMap = HashMap<String,Any>()

        highestMap["name"] = "Highest price to lowest price"
        highestMap["code"] = "priceup"

        lowestMap["name"] = "Lowest price to highest price"
        lowestMap["code"] = "pricedown"

        highestRatingMap["name"] = "Highest rating to lowest rating"
        highestRatingMap["code"] = "ratingup"

        lowestRatingMap["name"] = "Lowest rating to highest rating"
        lowestRatingMap["code"] = "ratingdown"


        sortDataList = ArrayList()
        sortDataList.add(highestMap)
        sortDataList.add(lowestMap)
        sortDataList.add(highestRatingMap)
        sortDataList.add(lowestRatingMap)

        rvSortBy.layoutManager = LinearLayoutManager(this)
        rvSortBy.adapter = SortByAdapter(this,sortDataList,this)

        txtLearnMore.setOnClickListener(this)
        icSortByClose.setOnClickListener(this)
        btnFilter.setOnClickListener(this)
        txtClear.setOnClickListener(this)
    }

    private fun setPrivateSharing(from : Int){
        if (from == 0){
            txtPrivate.setBackgroundResource(R.drawable.backgrounf_timeops)
            txtSharing.setBackgroundResource(R.drawable.background_typeoftrip)
        }
        else{
            txtPrivate.setBackgroundResource(R.drawable.background_typeoftrip)
            txtSharing.setBackgroundResource(R.drawable.backgrounf_timeops)
        }
        viewTrip = from
    }

    private fun getScreenHeight() : Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.y
    }

    private fun setBooleanList() : ArrayList<Boolean>{
        val booleanList = ArrayList<Boolean>()
        booleanList.add(false)
        booleanList.add(false)
        booleanList.add(false)
        booleanList.add(false)
        booleanList.add(false)
        return booleanList
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun calendarApply(){
        dialogCalendar.dismiss()

        if (start_date!= null && end_date != null){
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(startDate!!.toString())
            val enDate = sdf.parse(endDate!!.toString())
            val sdf2 = SimpleDateFormat("yyyy-MMM-dd")

            val strStartDate = sdf2.format(date).split("-")[2]
            val strEndDate = sdf2.format(enDate).split("-")[2]+" "+sdf2.format(enDate).
            split("-")[1]

            val strDate = "$strStartDate - $strEndDate"

            txtDate.text = strDate
            linearDate.setBackgroundResource(R.drawable.background_more_stroke)
        }
        else if (start_date != null && end_date == null){
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(startDate!!.toString())
            val sdf2 = SimpleDateFormat("yyyy-MMM-dd")

            val strStartDate = sdf2.format(date).split("-")[2]+" "+sdf2.format(date).
            split("-")[1]
            txtDate.text = strStartDate
            linearDate.setBackgroundResource(R.drawable.background_more_stroke)
        }
        else{
            txtDate.text = "Date"
            linearDate.setBackgroundResource(R.drawable.background_more)
        }

        loadFrom = 1
        currentPage = 1
        experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString().
        replace(" ", ""),start_date, end_date,guest,trip,bottomprice,
            upperprice,sortby,1,10,1,this)
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

    private fun btnGuestApplyAction(){
        guest = adultCount + childrenCount + infantCount
        loadFrom = 1
        currentPage = 1
        experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString().replace(" ", ""),start_date,
            end_date,guest,trip,bottomprice,upperprice,sortby,1,10,1,this)
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

    @SuppressLint("SetTextI18n")
    private fun setGuestState(){
        linearGuest.setBackgroundResource(R.drawable.background_more_stroke)

        guest?.let { guest_ ->
            if (guest_ > 1){
                txtTotalGuest.text = "$guest_ Guest(s)"
            }
            else{
                txtTotalGuest.text = "$guest_ Guest"
            }
        }

        dialogGuest.dismiss()
    }

    private fun searchCloseAction(){
        txtSearch.text = getString(R.string.search_content)
        txtSearch.setTextColor(Color.parseColor("#BDBDBD"))
        icSearchClose.visibility = View.GONE
        province_id = null
        harbor_id = null

        loadFrom = 1
        currentPage = 1
        experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString()
            .replace(" ", ""),start_date, end_date,guest,trip,bottomprice,upperprice
            ,sortby,1,10,1,this)
    }

    override fun onActivityTypeClicked(model: ActivityTypeModel, isSelected: Boolean) {
        if (isSelected){
            typelist.add(model.id)
        }
        else{
            typelist.remove(model.id)
        }
    }

    override fun onSortByClicked(sortMap: HashMap<String, Any>) {
        strSortBy = sortMap["code"] as String
    }

    override fun onStartedSeeking() {

    }

    override fun onStoppedSeeking() {

    }

    override fun onValueChanged(minThumbValue: Int, maxThumbValue: Int) {
        viewBottomprice = minThumbValue.toLong()
        viewUpperprice = maxThumbValue.toLong()

       val strMinPrice = "Min. IDR "+ CurrencyUtil.decimal(viewBottomprice).replace(
           ",",".")
       val strMaxPrice = "Max. IDR "+ CurrencyUtil.decimal(viewUpperprice).replace(
           ",",".")

      txtMinPrice.text = strMinPrice
      txtMaxPrice.text = strMaxPrice
    }

    override fun onExperienceSearchPrepare() {

        cardFilter.visibility = View.VISIBLE

        if (from == 1 && loadFrom == 1){
            nestedScrollView.visibility = View.GONE
            nestedContent.visibility = View.VISIBLE
        }

        if (loadFrom == 1){
            txtTripFound.visibility = View.GONE
            shimerLayout.visibility = View.VISIBLE
            rvService.visibility = View.GONE
            linearEmpty.visibility = View.GONE
        }
        else{
            isLoading = true

            if (isErrorOccured){
                experienceList.removeAt(lastIndex)
            }

            val experienceModel = ExperienceModel()
            experienceModel.viewType = 1
            experienceList.add(experienceModel)

            serviceExperienceAdapter.updateExperienceList(experienceList)

            lastIndex = experienceList.size - 1
            isErrorOccured = false
        }

        shimerLayout.startShimmerAnimation()
    }

    @SuppressLint("SetTextI18n")
    override fun onExperienceSearchLoaded(experienceList: ArrayList<ExperienceModel>, totalRecords : Int) {

        this.totalRecords = totalRecords

        if (loadFrom == 1){
            shimerLayout.visibility = View.GONE
            shimerLayout.stopShimmerAnimation()

            if (experienceList.size > 0){
                this.experienceList = experienceList
                linearEmpty.visibility = View.GONE
                rvService.visibility = View.VISIBLE
                serviceExperienceAdapter.updateExperienceList(experienceList)

                txtTripFound.visibility = View.VISIBLE
            }
            else{
                rvService.visibility = View.GONE
                txtTripFound.visibility = View.GONE
                linearEmpty.visibility = View.VISIBLE
                txtError1.visibility = View.GONE

                imgEmpty.setImageResource(R.drawable.person_empty_state)
                txtError.text = "Opss, what you're looking for \n isn't available right now :("
            }
        }
        else{
          experienceList.removeAt(lastIndex)
          currentPage += 1
          this.experienceList = experienceList
          serviceExperienceAdapter.updateExperienceList(experienceList)
          isLoading = false
        }

        val size = this.experienceList.size
        txtTripFound.text = ""+size+" Trip Found"
    }

    @SuppressLint("SetTextI18n")
    override fun onExperienceSearchError() {
        if (loadFrom == 1){
            shimerLayout.stopShimmerAnimation()
            shimerLayout.visibility = View.GONE
            rvService.visibility = View.GONE
            txtTripFound.visibility = View.GONE
            linearEmpty.visibility = View.VISIBLE

            txtError1.visibility = View.VISIBLE

            imgEmpty.setImageResource(R.drawable.example_empty1)
            txtError.text = "There was an error, please try again"
        }
        else{
            experienceList.removeAt(lastIndex)

            val experienceModel = ExperienceModel()
            experienceModel.viewType = 2
            experienceList.add(experienceModel)
            serviceExperienceAdapter.updateExperienceList(experienceList)

            isLoading = false
            isErrorOccured = true
        }
    }

    override fun onServiceErrorClicked() {
        loadFrom = 2
        val page = currentPage + 1
        experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString().replace(" ", ""),start_date,
            end_date,guest,trip,bottomprice,upperprice,sortby,page,10,2,this@ActivityExperience)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 515){
            if (resultCode == ActivityExperienceSearch.searchResult){

                val destinationModel = data?.getParcelableExtra<ExpDestinationModel>("destination_model")
                val type = destinationModel?.type

                if (type == 1){
                    province_id = destinationModel.province_id
                    harbor_id = null
                    txtSearch.text = destinationModel.province ?: ""
                }
                else{
                    province_id = null
                    harbor_id = destinationModel?.id
                    txtSearch.text = destinationModel?.harbors_name ?: ""
                }

                icSearchClose.visibility = View.VISIBLE
                txtSearch.setTextColor(Color.parseColor("#000000"))
                txtSearch.setTypeface(txtSearch.typeface,Typeface.BOLD)

                loadFrom = 1
                currentPage = 1
                experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString()
                    .replace(" ", ""),start_date, end_date,guest,trip,bottomprice,
                    upperprice,sortby,1,10,1,this)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.linearSearch -> {
                val i = Intent(this,ActivityExperienceSearch::class.java)
                startActivityForResult(i,515)
            }

            R.id.linearActivity -> {
                dialogType.show()
            }

            R.id.linearDate -> {
                dialogCalendar.show()
            }

            R.id.linearGuest -> {
                dialogGuest.show()
            }

            R.id.calendarBtnApply -> {
                calendarApply()
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
                btnGuestApplyAction()
                setGuestState()
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

            R.id.linearShortBy -> {
                sortBySheetDialog.show()
            }

            R.id.linearFilterBy -> {
                filterBySheetDialog.show()
            }

            R.id.icDialogCLose -> {
                dialogGuest.dismiss()
            }

            R.id.icSearchClose -> {
                searchCloseAction()
            }

            R.id.icFilterByClose -> {
                filterBySheetDialog.dismiss()
            }

            R.id.icSortByClose -> {
                sortBySheetDialog.dismiss()
            }

            R.id.icCLose -> {
                dialogType.dismiss()
            }

            R.id.btnFilter -> {
                if (!isSortByClearClicked){
                    sortby = strSortBy
                }
                else{
                    sortby = null
                    strSortBy = null
                }
                loadFrom = 1
                currentPage = 1
                experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString()
                    .replace(" ", ""),start_date, end_date,guest,trip,bottomprice,
                    upperprice,sortby,1,10,1,this)
                sortBySheetDialog.dismiss()
                isSortByClearClicked = false
            }

            R.id.txtClear -> {
                isSortByClearClicked = true
                rvSortBy.adapter = SortByAdapter(this,sortDataList,this)
            }

            R.id.txtPrivate -> {
                setPrivateSharing(0)
            }

            R.id.txtSharing -> {
                setPrivateSharing(1)
            }

            R.id.txtLearnMore -> {
                startActivity(Intent(this,ActivityShareReferal::class.java))
            }

            R.id.btnFilters -> {
                if (!isFilterByClearClicked){
                    trip = viewTrip
                    bottomprice = viewBottomprice
                    upperprice = viewUpperprice
                }
                else{
                    trip = null
                    bottomprice = 0
                    upperprice = 500000000
                    viewTrip = null
                    viewBottomprice = 0
                    viewBottomprice = 500000000
                }
                loadFrom = 1
                currentPage = 1
                experienceController.getExperienceSearch(harbor_id,city_id,province_id,typelist.toString()
                    .replace(" ", ""),start_date, end_date,guest,trip,bottomprice,
                    upperprice,sortby,1,10,1,this)
                filterBySheetDialog.dismiss()
                isFilterByClearClicked = false
            }

            R.id.txtFilterClearAll -> {
                isFilterByClearClicked = true
                mySeekBar.setMinThumbValue(0)
                mySeekBar.setMaxThumbValue(500000000)
                txtPrivate.setBackgroundResource(R.drawable.background_typeoftrip)
                txtSharing.setBackgroundResource(R.drawable.background_typeoftrip)
                val strMinPrice = "Min. IDR "+ CurrencyUtil.decimal(0).replace(
                    ",",".")
                val strMaxPrice = "Max. IDR "+ CurrencyUtil.decimal(500000000).replace(
                    ",",".")
            }

            R.id.linearCard -> {
                if(!isLinearCardClicked){
                    imgCard.setImageResource(R.drawable.ic_checktype_blue)
                    isLinearCardClicked = true
                } else {
                    imgCard.setImageResource(R.drawable.ic_check_gray)
                    isLinearCardClicked = false
                }
            }
        }
    }
}
