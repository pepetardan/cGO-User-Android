package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import id.dtech.cgo.Adapter.FerryAdapter
import id.dtech.cgo.Adapter.SortByAdapter
import id.dtech.cgo.Adapter.TimeOptionAdapter
import id.dtech.cgo.Adapter.TransportationResultAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.TransportationController
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.*
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import kotlinx.android.synthetic.main.activity_experience.*
import kotlinx.android.synthetic.main.activity_transportation_result.*
import kotlinx.android.synthetic.main.activity_transportation_result.imgEmpty
import kotlinx.android.synthetic.main.activity_transportation_result.ivBack
import kotlinx.android.synthetic.main.activity_transportation_result.linearEmpty
import kotlinx.android.synthetic.main.activity_transportation_result.linearFilterBy
import kotlinx.android.synthetic.main.activity_transportation_result.shimerLayout
import kotlinx.android.synthetic.main.activity_transportation_result.txtError
import kotlinx.android.synthetic.main.activity_transportation_result.txtError1

class ActivityTransportationResult : AppCompatActivity(), View.OnClickListener,
    MyCallback.Companion.TransportationSearchCallback, ApplicationListener.Companion.SortByListener,
    ApplicationListener.Companion.TransportationResultListener, MyCallback.Companion.TimeOptionCallback,
    SwipeRefreshLayout.OnRefreshListener, ApplicationListener.Companion.TimeOptionListener {

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

    private lateinit var timeOptionList : ArrayList<TimeOptionModel>

    private lateinit var icFilterClose : ImageView
    private lateinit var icDateCLose : ImageView
    private lateinit var txtClear : MyTextView

    private lateinit var btnFilterNow : Button

    private lateinit var rvDeparture : RecyclerView
    private lateinit var rvArrival : RecyclerView
    private lateinit var rvFerry : RecyclerView

    private lateinit var sortBySheetDialog : RoundedBottomSheetDialog
    private lateinit var filterBySheetDialog : RoundedBottomSheetDialog

    private lateinit var transportationController: TransportationController
    private lateinit var transportAdapter : TransportationResultAdapter

    private var adultCount = 1
    private var childrenCount = 0
    private var infantCount = 0

    private var harbor_source_id : String? = null
    private var harbor_dest_id : String? = null
    private var guest : Int? = null
    private var departure_date : String? = null
    private var transportClass : String? = null
    private var isReturn : Int? = null
    private var dep_timeoption_id : Int? = null
    private var arr_timeoption_id : Int? = null
    private var sortBy : String? = null
    private var return_date : String? = null

    private var originName = ""
    private var destinationName = ""
    private var strDate = ""
    private var return_trans_id = ""

    private var depTimeOp : Int? = null
    private var arrTimeOp : Int? = null

    private var depReturnTimeOp : Int? = null
    private var arrReturnTimeOp : Int? = null

    private var dep_return_timeoption_id : Int? = null
    private var arr_return_timeoption_id : Int? = null

    private var isReturnSelected = 0
    private var fromDetail = 0
    private var date = ""

    private var currentSearchType = 0

    private lateinit var transportationList : ArrayList<TransportationModel>
    private lateinit var transportationReturnList : ArrayList<TransportationModel>

    private var size = 10

    //DEPARTURE
    private var loadFrom = 1
    private var currentPage = 1

    private var isLoading = false
    private var isErrorOccured = false
    private var totalRecords = 0

    private var lastIndex = 0

    //RETURN
    private var returnLoadFrom = 1
    private var returnCurrentPage = 1

    private var returnIsLoading = false
    private var returnIsErrorOccured = false
    private var returnTotalRecords = 0

    private var returnLastIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transportation_result)
        setView()
    }

    private fun setView(){

        intent.extras?.let { bundle ->
            adultCount = bundle.getInt("adultCount")
            childrenCount = bundle.getInt("childrenCount")
            infantCount = bundle.getInt("infantCount")
            harbor_source_id = bundle.getString("harbor_source_id")
            harbor_dest_id = bundle.getString("harbor_dest_id")
            guest = bundle.getInt("guest")
            departure_date = bundle.getString("departure_date")
            transportClass = bundle.getString("transportClass")
            isReturn = bundle.getInt("isReturn")
            Log.d("logaldi",""+isReturn)
            sortBy = bundle.getString("sortBy")
            return_date = bundle.getString("return_date")
            date = bundle.getString("date") ?: ""

            originName = bundle.getString("origin_name") ?: ""
            destinationName = bundle.getString("destination_name") ?: ""
            strDate = bundle.getString("strDate") ?: ""

            txtOrigin.text = originName
            txtDestination.text = destinationName
            txtClass.text = transportClass ?: ""
            txtStrDate.text = strDate

            if (isReturn == 0){
                imgReturn.setImageResource(R.drawable.ic_right_gray)
            }
            else{
                imgReturn.setImageResource(R.drawable.ic_return)
            }

            setGuestText()
            setTransportationSortBy()
        }

        timeOptionList = ArrayList()
        transportationList = ArrayList()
        transportationReturnList = ArrayList()

        transportAdapter = TransportationResultAdapter(this,transportationList,
            this)
        transportationController = TransportationController()

        rvResult.layoutManager = LinearLayoutManager(this)
        rvResult.adapter = transportAdapter

        transportationController.getTimeOption(this)

        searchTransportation(1)
        setTransportationFilterBy()
        setLoadMore()

        swipeRefresh.setOnRefreshListener(this)
        ivBack.setOnClickListener(this)
        btnChange.setOnClickListener(this)

        linearSortBy.setOnClickListener(this)
        linearFilterBy.setOnClickListener(this)
    }

    private fun searchTransportation(listPage : Int){
        if (currentSearchType == 0){
            val dataMap = HashMap<String,Any?>()
            dataMap["page"]  = listPage
            dataMap["size"] = size
            dataMap["harbor_source_id"]  = harbor_source_id
            dataMap["harbor_dest_id"]  = harbor_dest_id
            dataMap["guest"]  = guest
            dataMap["departure_date"]  = departure_date
            dataMap["transportClass"] = transportClass
            dataMap["dep_timeoption_id"]  = dep_timeoption_id
            dataMap["arr_timeoption_id"]  = arr_timeoption_id
            dataMap["sortBy"]  = sortBy

            if (isReturn == 0){
                dataMap["not_return"]  = true
            }
            else{
                dataMap["isReturn"]  = 1
            }

            transportationController.getTransportationSearch(dataMap,loadFrom,currentSearchType,this)
        }
        else{
            val dataMap = HashMap<String,Any?>()
            dataMap["page"]  = listPage
            dataMap["size"] = size
            dataMap["harbor_source_id"]  = harbor_dest_id
            dataMap["harbor_dest_id"]  = harbor_source_id
            dataMap["guest"]  = guest
            dataMap["departure_date"]  = return_date
            dataMap["transportClass"] = null
            dataMap["dep_timeoption_id"]  = dep_return_timeoption_id
            dataMap["arr_timeoption_id"]  = arr_return_timeoption_id
            dataMap["sortBy"]  = sortBy
            dataMap["return_trans_id"]  = return_trans_id

            transportationController.getTransportationSearch(dataMap,returnLoadFrom,currentSearchType,this)
        }
    }

    private fun setSearchBeginning(){
        if (currentSearchType == 0){
            loadFrom = 1
            currentPage = 1
        }
        else{
            returnLoadFrom = 1
            returnCurrentPage = 1
        }
        searchTransportation(1)
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

    private fun setTransportationSortBy(){
        sortBySheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.layout_transportation_sortby, null)
        sortBySheetDialog.setContentView(sheetView)
        sortBySheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val rvSortBy = sheetView.findViewById<RecyclerView>(R.id.rvSortBy)
        icDateCLose = sheetView.findViewById(R.id.icDateCLose)

        val highestMap = HashMap<String,Any>()
        val lowestMap = HashMap<String,Any>()

        highestMap["name"] = "Highest Price"
        highestMap["code"] = "priceup"

        lowestMap["name"] = "Lowest Price"
        lowestMap["code"] = "pricedown"

        val sortDataList = ArrayList<HashMap<String,Any>>()
        sortDataList.add(highestMap)
        sortDataList.add(lowestMap)

        rvSortBy.layoutManager = LinearLayoutManager(this)
        rvSortBy.adapter = SortByAdapter(this,sortDataList,this)

        icDateCLose.setOnClickListener(this)
    }

    private fun setTransportationFilterBy(){
        filterBySheetDialog = RoundedBottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.layout_timeoption_filter, null)
        filterBySheetDialog.setContentView(sheetView)
        sheetView.minimumHeight = getScreenHeight() - 235
        filterBySheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        icFilterClose = sheetView.findViewById(R.id.icFilterClose)
        btnFilterNow = sheetView.findViewById(R.id.btnFilterNow)

        rvDeparture = sheetView.findViewById(R.id.rvDeparture)
        rvArrival = sheetView.findViewById(R.id.rvArrival)
        rvFerry = sheetView.findViewById(R.id.rvFerry)

        txtClear = sheetView.findViewById(R.id.txtClear)

        rvDeparture.layoutManager = GridLayoutManager(this,2)
        rvArrival.layoutManager = GridLayoutManager(this,2)

        setFerry()

        icFilterClose.setOnClickListener(this)
        btnFilterNow.setOnClickListener(this)
        txtClear.setOnClickListener(this)
    }

    private fun clearAllFilter(){
        rvDeparture.adapter = TimeOptionAdapter(this,timeOptionList,1,this)
        rvArrival.adapter = TimeOptionAdapter(this,timeOptionList,2,this)

        if (currentSearchType == 0){
            depTimeOp = null
            arrTimeOp = null
        }
        else{
            depReturnTimeOp = null
            arrReturnTimeOp = null
        }
    }

    private fun setFerry(){
        val ferryList = ArrayList<FerryModel>()
        val ferry1 = FerryModel()
        ferry1.name = "Golden Queen"
        ferry1.pic = R.drawable.golden_queen

        val ferry2 = FerryModel()
        ferry2.name = "ASDP"
        ferry2.pic = R.drawable.example_asdp

        ferryList.add(ferry1)
        ferryList.add(ferry2)

        rvFerry.layoutManager = LinearLayoutManager(this)
        rvFerry.adapter = FerryAdapter(this,ferryList)
    }

    private fun getScreenHeight() : Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.y
    }

    private fun intentResultDetail(){

        transportImages = transportationModel?.transportation_images
        transportReturnImages = transportationModelReturn?.transportation_images

        transportPrice = transportationModel?.price
        transportReturnPrice = transportationModelReturn?.price

        val i = Intent(this,ActivityTransportationResultDetail::class.java)
        i.putExtra("is_return",isReturn)
        i.putExtra("adultCount",adultCount)
        i.putExtra("childrenCount",childrenCount)
        i.putExtra("infantCount",infantCount)
        i.putExtra("isReturnSelected",isReturnSelected)
        i.putExtra("fromDetail",fromDetail)
        i.putExtra("transportationModel",transportationModel)
        i.putExtra("transportationModelReturn",transportationModelReturn)
        i.putExtra("transportImages",transportImages)
        i.putExtra("transportReturnImages",transportReturnImages)
        i.putExtra("transportPrice",transportPrice)
        i.putExtra("transportReturnPrice",transportReturnPrice)
        i.putExtra("transportBoatDetails",transportBoatDetails)
        i.putExtra("transportReturnBoatDetails",transportReturnBoatDetails)
        i.putExtra("date",date)

        i.putParcelableArrayListExtra("transportFacilities",transportFacilities)
        i.putParcelableArrayListExtra("transportReturnFacilities",transportReturnFacilities)

        startActivity(i)
    }

    @SuppressLint("SetTextI18n")
    private fun setLinearDepartureContent(transportModel : TransportationModel){
        val merchantImage = transportModel.merchant_picture ?: ""
        val trsPrice = transportModel.price ?: HashMap()
        val adultPrice = trsPrice["adult_price"] as Long
        val childrenPrice = trsPrice["children_price"] as Long
        val currencyLabel =  trsPrice["currency_label"] as String
        val priceType = trsPrice["price_type"] as String

        val strAdultPrice = "$currencyLabel "+CurrencyUtil.decimal(adultPrice).replace(
            ",",".")

        val strChildrenPrice = "$currencyLabel "+CurrencyUtil.decimal(childrenPrice).replace(
            ",",".")

        txtAdultPrice.text = strAdultPrice
        txtChildrenPrice.text = strChildrenPrice

        txtAdultPax.text = " / $priceType"
        txtChildrenPax.text = " / $priceType"

        if (merchantImage.isNotEmpty()){
            Picasso.get().load(merchantImage).into(imgMerchant)
        }

        txtMerchantName.text = transportModel.merchant_name ?: ""
    }

    private fun setLoadMore(){
        transportNestedScroll.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int,
                                                  _: Int, oldScrollY: Int ->
            v?.let {
                if(v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY) {
                        val lastVisiblePosition = (rvResult.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                        if (currentSearchType == 0){
                            val totalList = transportationList.size - 1

                            if (lastVisiblePosition == totalList){
                                if (transportationList.size != totalRecords && !isLoading){
                                    loadFrom = 2

                                    val page = currentPage + 1
                                    searchTransportation(page)
                                }
                            }
                        }
                        else{
                            val totalList = transportationReturnList.size - 1

                            if (lastVisiblePosition == totalList){
                                if (transportationReturnList.size != returnTotalRecords && !returnIsLoading){
                                    returnLoadFrom = 2

                                    val page = returnCurrentPage + 1
                                    searchTransportation(page)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setPrepareState(){
        swipeRefresh.isRefreshing = false
        shimerLayout.startShimmerAnimation()
        shimerLayout.visibility = View.VISIBLE
        linearContent.visibility = View.GONE
        linearEmpty.visibility = View.GONE
    }

    private fun setSuccessState(){
        shimerLayout.stopShimmerAnimation()
        shimerLayout.visibility = View.GONE

        linearContent.visibility = View.VISIBLE
        linearEmpty.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setErrorState(){
        shimerLayout.stopShimmerAnimation()
        shimerLayout.visibility = View.GONE
        linearContent.visibility = View.GONE
        linearEmpty.visibility = View.VISIBLE

        imgEmpty.setImageResource(R.drawable.example_empty1)
        txtError1.visibility = View.VISIBLE
        txtError.text = "There was an error, please try again"
    }

    override fun onTransportationSearchPrepare() {
          if(currentSearchType == 0){
              if (loadFrom == 1){
                  setPrepareState()
              }
              else{
                  isLoading = true

                  if (isErrorOccured){
                      transportationList.removeAt(lastIndex)
                  }

                  val transportModel = TransportationModel()
                  transportModel.viewType = 1
                  transportationList.add(transportModel)

                  transportAdapter.updateTransportList(transportationList)

                  lastIndex = transportationList.size - 1
                  isErrorOccured = false
              }
          }
          else{
              if (returnLoadFrom == 1){
                  setPrepareState()
              }
              else{
                  returnIsLoading = true

                  if (returnIsErrorOccured){
                      transportationReturnList.removeAt(returnLastIndex)
                  }

                  val transportModel = TransportationModel()
                  transportModel.viewType = 1

                  transportationReturnList.add(transportModel)

                  transportAdapter.updateTransportList(transportationReturnList)

                  returnLastIndex = transportationReturnList.size - 1
                  returnIsErrorOccured = false
              }
          }
    }

    @SuppressLint("SetTextI18n")
    override fun onTransportationSearchSuccess(transportationList: ArrayList<TransportationModel>,
                                               metaData: HashMap<String, Any>) {
        if (currentSearchType == 0) {
            totalRecords = metaData["total_records"] as Int

            if (loadFrom == 1) {
                setSuccessState()

                if (transportationList.size > 0) {
                    this.transportationList = transportationList
                    transportAdapter.updateTransportList(transportationList)
                }
                else {
                    linearContent.visibility = View.GONE
                    linearEmpty.visibility = View.VISIBLE

                    imgEmpty.setImageResource(R.drawable.person_empty_state)
                    txtError1.visibility = View.GONE
                    txtError.text = "Opss, what you're looking for \n isn't available right now :("
                }
            }
            else {
                transportationList.removeAt(lastIndex)
                currentPage += 1
                this.transportationList = transportationList
                transportAdapter.updateTransportList(transportationList)
                isLoading = false
            }

            txtFound.text = "Found ${this.transportationList.size} departure transportation"
        }
        else {
            returnTotalRecords = metaData["total_records"] as Int

            if (returnLoadFrom == 1){
                setSuccessState()

                if (transportationList.size > 0){
                    this.transportationReturnList = transportationList
                    transportAdapter.updateTransportList(transportationList)
                }
                else{
                    linearContent.visibility = View.GONE
                    linearEmpty.visibility = View.VISIBLE

                    imgEmpty.setImageResource(R.drawable.person_empty_state)
                    txtError1.visibility = View.GONE
                    txtError.text = "Opss, what you're looking for \n isn't available right now :("
                }
            }
            else{
                transportationList.removeAt(returnLastIndex)
                returnCurrentPage += 1
                this.transportationReturnList = transportationList
                transportAdapter.updateTransportList(transportationReturnList)
                returnIsLoading = false
            }

            txtFound.text = "Found ${this.transportationReturnList.size} return transportation"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onTransportationSearchError(message: String) {
       if (currentSearchType == 0){
           if (loadFrom == 1){
               setErrorState()
           }
           else{
               transportationList.removeAt(lastIndex)

               val transportModel = TransportationModel()
               transportModel.viewType = 2

               transportationList.add(transportModel)
               transportAdapter.updateTransportList(transportationList)

               isLoading = false
               isErrorOccured = true
           }
       }
        else{
           if (returnLoadFrom == 1){
               setErrorState()
           }
           else{
               transportationReturnList.removeAt(returnLastIndex)

               val transportModel = TransportationModel()
               transportModel.viewType = 2

               transportationReturnList.add(transportModel)
               transportAdapter.updateTransportList(transportationReturnList)

               returnIsLoading = false
               returnIsErrorOccured = true
           }
       }
    }

    @SuppressLint("SetTextI18n")
    override fun onTransportationClassClicked(transportationModel: TransportationModel, from : Int) {
        if (currentSearchType == 0){

            this.transportationModel = transportationModel
            this.transportFacilities = transportationModel.facilities
            this.transportBoatDetails = transportationModel.boat_details

            if (isReturn == 0){
                    isReturnSelected = 0
                    fromDetail = 0
                    intentResultDetail()
            }
            else{
             //   if (from == 0){
                    return_trans_id = transportationModel.return_trans_id ?: ""
                    setLinearDepartureContent(transportationModel)
                    linearReturn.visibility = View.VISIBLE
                    currentSearchType = 1
                    txtTitle.text = "Select Return"
                    setSearchBeginning()
              //  }
              /*  else{
                    fromDetail = 0
                    isReturnSelected = 0
                    intentResultDetail()
                }*/
            }
        }
        else{
                this.transportReturnFacilities = transportationModel.facilities
                this.transportationModelReturn = transportationModel
                this.transportReturnBoatDetails = transportationModel.boat_details

                isReturnSelected = 1
                fromDetail = 1
                linearReturn.visibility = View.VISIBLE
                intentResultDetail()
        }
    }

    override fun onSortByClicked(sortMap: java.util.HashMap<String, Any>) {
         sortBy = sortMap["code"] as String
         sortBySheetDialog.dismiss()
         setSearchBeginning()
    }

    override fun onTimeOptionPrepare() {

    }

    override fun onTimeOptionSuccess(timeOptionList: ArrayList<TimeOptionModel>) {
            this.timeOptionList = timeOptionList
            rvDeparture.adapter = TimeOptionAdapter(this,timeOptionList,1,this)
            rvArrival.adapter = TimeOptionAdapter(this,timeOptionList,2,this)
    }

    override fun onTimeOptionError(message: String) {

    }

    override fun onRefresh() {
       setSearchBeginning()
    }

    override fun onTimeOptionClicked(timeOptionModel: TimeOptionModel, from: Int) {
        if (currentSearchType == 0){
            if (from == 1){
                depTimeOp = timeOptionModel.id
            }
            else{
                arrTimeOp = timeOptionModel.id
            }
        }
        else{
            if (from == 1){
                depReturnTimeOp = timeOptionModel.id
            }
            else{
                arrReturnTimeOp = timeOptionModel.id
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.linearSortBy -> {
                sortBySheetDialog.show()
            }

            R.id.linearFilterBy -> {
                filterBySheetDialog.show()
            }

            R.id.icFilterClose -> {
                filterBySheetDialog.dismiss()
            }

            R.id.icDateCLose -> {
                sortBySheetDialog.dismiss()
            }

            R.id.txtClear -> {
                clearAllFilter()
            }

            R.id.btnFilterNow -> {
                if (currentSearchType == 0){
                    dep_timeoption_id = depTimeOp
                    arr_timeoption_id = arrTimeOp
                }
                else{
                    dep_return_timeoption_id = depReturnTimeOp
                    arr_return_timeoption_id = arrReturnTimeOp
                }

                filterBySheetDialog.dismiss()
                setSearchBeginning()
            }

            R.id.btnChange -> {
                currentSearchType = 0
                linearReturn.visibility = View.GONE
                txtFound.text = "Found ${transportationList.size} departure transportation"
                txtTitle.text = "Select Departure"
                linearContent.visibility = View.VISIBLE
                linearEmpty.visibility = View.GONE
                transportAdapter.updateTransportList(transportationList)
            }
        }
    }
}
