package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import id.dtech.cgo.Adapter.ClassAdapter
import id.dtech.cgo.Adapter.DestinationAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.DestinationController
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExpDestinationModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CalendarUtil
import id.dtech.cgo.Util.ViewUtil
import kotlinx.android.synthetic.main.activity_search_transportation.*
import kotlinx.android.synthetic.main.activity_search_transportation.imgReturn
import kotlinx.android.synthetic.main.activity_search_transportation.ivBack
import kotlinx.android.synthetic.main.activity_search_transportation.linearReturn
import kotlinx.android.synthetic.main.activity_search_transportation.txtClass
import kotlinx.android.synthetic.main.activity_search_transportation.txtDestination
import kotlinx.android.synthetic.main.activity_search_transportation.txtGuest
import kotlinx.android.synthetic.main.activity_search_transportation.txtOrigin
import kotlinx.android.synthetic.main.activity_transportation_result.*
import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.layout_calendar_header.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivitySearchTransportation : AppCompatActivity(), View.OnClickListener, MyCallback.Companion.
    ExperienceDestinationCallback, ApplicationListener.Companion.ExperienceDestinationListener,
    ApplicationListener.Companion.TransportationClassListener {

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private lateinit var expDestinationList: ArrayList<ExpDestinationModel>
    private lateinit var currentCalendarMonth : CalendarMonth
    private lateinit var dialogCalendar : Dialog
    private lateinit var calendarView : CalendarView
    private lateinit var txtMonthYear : TextView
    private lateinit var calendarBtnApply : Button
    private lateinit var imgNext : ImageView
    private lateinit var imgPrev : ImageView
    private lateinit var rvLocation : RecyclerView

    private lateinit var classPopUp : PopupWindow
    private lateinit var locationPopUp : PopupWindow

    private var isReturnClicked = false
    private var isGuestClicked = false

    private val selectedDates = ArrayList<LocalDate>()

    private var adultCount = 1
    private var childrenCount = 0
    private var infantCount = 0

    private var page = 1
    private var size = 10
    private var harbor_source_id : String? = null
    private var harbor_dest_id : String? = null
    private var guest : Int? = null
    private var departure_date : String? = null
    private var transportClass : String? = null
    private var isReturn : Int? = null
    private var sortBy : String? = null
    private var return_date : String? = null

    private var strDate = ""
    private var originName = ""
    private var destinationName = ""

    private var fromDestination = 0
    private var fromDate = 0
    private var isSwitched = false

    private var date = ""
    private var isLocationClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_transportation)
        setView()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setView(){
        transportClass = "Economy"
        guest = 1

        DestinationController.loadExperienceDestination(
            this@ActivitySearchTransportation, "")
        initiateFirstDate()

        setCalenderAlertDialog()
        setTransportClassPopUp()

        txtOrigin.setOnClickListener(this)
        txtDestination.setOnClickListener(this)

        ivBack.setOnClickListener(this)
        btnSearch.setOnClickListener(this)

        imgSwitch.setOnClickListener(this)

        imgAdultMinus.setOnClickListener(this)
        imgAdultPlus.setOnClickListener(this)

        imgChildrenMinus.setOnClickListener(this)
        imgChildrenPlus.setOnClickListener(this)

        imgInfantMinus.setOnClickListener(this)
        imgInfantPlus.setOnClickListener(this)

        linearDateReturn.setOnClickListener(this)
        linearFrom.setOnClickListener(this)
        linearTo.setOnClickListener(this)
        linearDeparture.setOnClickListener(this)
        linearReturn.setOnClickListener(this)
        linearGuest.setOnClickListener(this)
        linearClass.setOnClickListener(this)
    }

    private fun showPopUpWindow(anchor : View){
        val view = LayoutInflater.from(this).inflate(R.layout.layout_location_popup,
            null)
        rvLocation = view.findViewById<RecyclerView>(R.id.rvLocation)
        locationPopUp = PopupWindow(this)
        locationPopUp.contentView = view
        locationPopUp.width = LinearLayout.LayoutParams.MATCH_PARENT
        locationPopUp.height = 250.dp
        locationPopUp.contentView.layout(24.dp,0.dp,24.dp,0.dp)
        locationPopUp.isFocusable = true

        locationPopUp.setBackgroundDrawable(resources.getDrawable(R.drawable.bakcground_white,null))
        locationPopUp.showAsDropDown(anchor,-2,0)

        rvLocation.layoutManager = LinearLayoutManager(this)
        rvLocation.adapter = DestinationAdapter(this,expDestinationList,this,1)
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

        calendarBtnApply.setTextColor(Color.parseColor("#FFFFFF"))

        setCalendar()
    }

    private fun setTransportClassPopUp(){

        val classNameList = ArrayList<String>()
        classNameList.add("Economy")
        classNameList.add("Executive")

        val view = LayoutInflater.from(this).inflate(R.layout.layout_location_popup,
            null)
         rvLocation = view.findViewById<RecyclerView>(R.id.rvLocation)

        classPopUp = PopupWindow(this)
        classPopUp.contentView = view
        classPopUp.width = LinearLayout.LayoutParams.MATCH_PARENT
        classPopUp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        classPopUp.contentView.layout(24.dp,0.dp,24.dp,0.dp)
        classPopUp.isFocusable = true

        classPopUp.setBackgroundDrawable(resources.getDrawable(R.drawable.bakcground_white,null))

        rvLocation.layoutManager = LinearLayoutManager(this)
        rvLocation.adapter = ClassAdapter(this,classNameList,this)
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
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.size > 0){
                            selectedDates.removeAt(0)
                            selectedDates.add(day.date)
                        }
                        else{
                            selectedDates.add(day.date)
                        }

                        calendarBtnApply.isEnabled = true
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

    @SuppressLint("SetTextI18n")
    private fun setReturnState(){
        if (!isReturnClicked){
            linearDateReturn.visibility = View.VISIBLE
            imgReturn.setImageResource(R.drawable.ic_checkblueyoung)
            txtReturnTitle.text = "Return"

            isReturn = 1
            isReturnClicked = true
        }
        else{
            linearDateReturn.visibility = View.GONE
            imgReturn.setImageResource(R.drawable.ic_check_gray)
            txtReturnTitle.text = "Return ?"

            isReturn = 0
            isReturnClicked = false
        }
    }

    private fun setGuestState(){
        if (!isGuestClicked){
            linearAddGuest.visibility = View.VISIBLE
            imgGuestArrow.setImageResource(R.drawable.ic_uparrow)

            isGuestClicked = true
        }
        else{
            linearAddGuest.visibility = View.GONE
            imgGuestArrow.setImageResource(R.drawable.ic_black_dropdown)

            isGuestClicked = false
        }
    }

    private fun setAdultCount(from : Int){
        if (from == 1){
            if (adultCount > 1){
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

    }

    private fun setSwitch(){

        if (harbor_source_id != null) {
            if (harbor_source_id!!.isEmpty()){
                ViewUtil.showBlackToast(this,"Please select from " +
                        "destination",0).show()
                return
            }
        }
        else{
            ViewUtil.showBlackToast(this,"Please select from " +
                    "destination",0).show()
            return
        }

        if (harbor_dest_id != null) {
            if (harbor_dest_id!!.isEmpty()){
                ViewUtil.showBlackToast(this,"Please select to " +
                        "destination",0).show()
                return
            }
        }
        else{
            ViewUtil.showBlackToast(this,"Please select to " +
                    "destination",0).show()
            return
        }

        val originID = harbor_dest_id
        val destinationID = harbor_source_id

        val originName_ = destinationName
        val destinationName_ = destinationName

        originName = destinationName_
        destinationName = originName_

        harbor_source_id = destinationID
        harbor_dest_id = originID

        txtOrigin.setText(destinationName)
        txtDestination.setText(originName)

    }

    @SuppressLint("SimpleDateFormat")
    private fun initiateFirstDate(){
        val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sdf = SimpleDateFormat("EEE, dd MMM yy")
        val sdfSelected = SimpleDateFormat("yyyy-MM-dd")
        val monthSdf = SimpleDateFormat("dd MMM")

        val currentCalendar = Calendar.getInstance()
        val tomorrowCalendar = Calendar.getInstance()
        tomorrowCalendar.add(Calendar.DAY_OF_YEAR,1)

        date = sdfDate.format(currentCalendar.time)
        strDate = monthSdf.format(currentCalendar.time)
        departure_date = sdfSelected.format(currentCalendar.time)
        return_date = sdfSelected.format(tomorrowCalendar.time)

        txtDeparture.text = sdf.format(currentCalendar.time)
        txtReturnDate.text = sdf.format(tomorrowCalendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCalendarButtonApply(){
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val selectedDate = selectedDates[0]
        val sdfSelected = SimpleDateFormat("yyyy-MM-dd")
        val monthSdf = SimpleDateFormat("dd MMM")
        val selectedDateTime = sdfSelected.parse(selectedDate.toString()) ?: Date()
        val sdfText = SimpleDateFormat("EEE, dd MMM yy")
        val strText = sdfText.format(selectedDateTime)

        if (fromDate == 0){
            strDate = monthSdf.format(selectedDateTime)
            departure_date = selectedDate.toString()
            date = sdf.format(selectedDateTime)
            txtDeparture.text = strText
        }
        else{
            return_date = selectedDate.toString()
            txtReturnDate.text = strText
        }

        dialogCalendar.dismiss()
    }

    @SuppressLint("SetTextI18n")
    private fun setGuestText(){
        if (adultCount != 0 && childrenCount == 0 && infantCount == 0 ){
            if (adultCount > 1){
                txtGuest.text = "$adultCount Adults"
            }
            else{
                txtGuest.text = "$adultCount Adult"
            }
        }
        else if (adultCount != 0 && childrenCount != 0 && infantCount == 0 ){
            if (adultCount > 1){
                if (childrenCount > 1){
                    txtGuest.text = "$adultCount Adults, $childrenCount Children"
                }
                else{
                    txtGuest.text = "$adultCount Adults, $childrenCount Children"
                }
            }
            else{
                if (childrenCount > 1){
                    txtGuest.text = "$adultCount Adult, $childrenCount Children"
                }
                else{
                    txtGuest.text = "$adultCount Adult, $childrenCount Children"
                }
            }
        }
        else{
            if (adultCount > 1){
                if (childrenCount > 1){
                    if (infantCount > 1){
                        txtGuest.text = "$adultCount Adults, $childrenCount Children, $infantCount Infants"
                    }
                    else{
                        txtGuest.text = "$adultCount Adults, $childrenCount Children, $infantCount Infant"
                    }
                }
                else{
                    if (infantCount > 1){
                        txtGuest.text = "$adultCount Adults, $childrenCount Children, $infantCount Infants"
                    }
                    else{
                        txtGuest.text = "$adultCount Adults, $childrenCount Children, $infantCount Infant"
                    }
                }
            }
            else{
                if (childrenCount > 1){
                    if (infantCount > 1){
                        txtGuest.text = "$adultCount Adult, $childrenCount Children, $infantCount Infants"
                    }
                    else{
                        txtGuest.text = "$adultCount Adult, $childrenCount Children, $infantCount Infant"
                    }
                }
                else{
                    if (infantCount > 1){
                        txtGuest.text = "$adultCount Adult, $childrenCount Children, $infantCount Infants"
                    }
                    else{
                        txtGuest.text = "$adultCount Adult, $childrenCount Children, $infantCount Infant"
                    }
                }
            }
        }
    }

    private fun intentTransportationResult(){

        if (harbor_source_id != null) {
            if (harbor_source_id!!.isEmpty()){
                ViewUtil.showBlackToast(this,"Please select from " +
                        "destination",0).show()
                return
            }
        }
        else{
            ViewUtil.showBlackToast(this,"Please select from " +
                    "destination",0).show()
            return
        }

        if (harbor_dest_id != null) {
            if (harbor_dest_id!!.isEmpty()){
                ViewUtil.showBlackToast(this,"Please select to " +
                        "destination",0).show()
                return
            }
        }
        else{
            ViewUtil.showBlackToast(this,"Please select to " +
                    "destination",0).show()
            return
        }

        originName = txtOrigin.text.toString().trim()
        destinationName = txtDestination.text.toString().trim()

        val i = Intent(this,ActivityTransportationResult::class.java)
        i.putExtra("page",page)
        i.putExtra("size",size)
        i.putExtra("harbor_source_id",harbor_source_id)
        i.putExtra("harbor_dest_id",harbor_dest_id)
        i.putExtra("guest",guest)
        i.putExtra("departure_date",departure_date)
        i.putExtra("transportClass",transportClass)
        i.putExtra("isReturn",isReturn)
        i.putExtra("sortBy",sortBy)
        i.putExtra("return_date",return_date)
        i.putExtra("adultCount",adultCount)
        i.putExtra("childrenCount",childrenCount)
        i.putExtra("infantCount",infantCount)
        i.putExtra("origin_name",originName)
        i.putExtra("destination_name",destinationName)
        i.putExtra("strDate",strDate)
        i.putExtra("date",date)
        startActivity(i)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.txtOrigin -> {
                fromDestination = 0
                showPopUpWindow(linearFrom)
                isLocationClicked = true
            }

            R.id.txtDestination -> {
                fromDestination = 1
                showPopUpWindow(linearTo)
                isLocationClicked = true
            }

            R.id.linearDeparture -> {
                fromDate = 0
                dialogCalendar.show()
            }

            R.id.linearDateReturn -> {
                fromDate = 1
                dialogCalendar.show()
            }

            R.id.linearReturn -> {
                setReturnState()
            }

            R.id.linearGuest -> {
                setGuestState()
            }

            R.id.imgAdultMinus -> {
                setAdultCount(1)
                setGuestText()
            }

            R.id.imgAdultPlus -> {
                setAdultCount(2)
                setGuestText()
            }

            R.id.imgChildrenMinus -> {
                setChildrenCount(1)
                setGuestText()
            }

            R.id.imgChildrenPlus -> {
                setChildrenCount(2)
                setGuestText()
            }

            R.id.imgInfantMinus -> {
                setInfantCount(1)
                setGuestText()
            }

            R.id.imgInfantPlus -> {
                setInfantCount(2)
                setGuestText()
            }

            R.id.linearClass -> {
                classPopUp.showAsDropDown(linearClass,-2,0)
            }

            R.id.calendarBtnApply -> {
                setCalendarButtonApply()
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

            R.id.imgSwitch -> {
                setSwitch()
            }

            R.id.btnSearch -> {
                intentTransportationResult()
            }
        }
    }

    override fun onExperienceDestinationPrepare() {

    }

    override fun onExperienceDestinationLoaded(expDestination: ArrayList<ExpDestinationModel>) {
        expDestinationList = expDestination
        if (isLocationClicked){
            rvLocation.adapter = DestinationAdapter(this,expDestinationList,this,1)
        }
    }

    override fun onExperienceDestinationError() {

    }

    override fun onExperienceDestinationClicked(model: ExpDestinationModel) {
        if (fromDestination == 0){
            harbor_source_id = model.id ?: ""
            originName = model.harbors_name ?: ""
            txtOrigin.setText(model.harbors_name ?: "")
        }
        else{
            harbor_dest_id = model.id ?: ""
            destinationName = model.harbors_name ?: ""
            txtDestination.setText(model.harbors_name ?: "")
        }

        locationPopUp.dismiss()
    }

    override fun onTransportationClassClicked(strClass: String) {
        txtClass.text = strClass
        transportClass = strClass
        classPopUp.dismiss()
    }
}
