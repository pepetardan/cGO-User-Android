package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import id.dtech.cgo.Adapter.TransResultPagerAdapter
import id.dtech.cgo.Model.FacilityModel
import id.dtech.cgo.Model.TransportationModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import kotlinx.android.synthetic.main.activity_transportation_result_detail.*

class ActivityTransportationResultDetail : AppCompatActivity(), View.OnClickListener{

    private var departure_date : String? = null

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

    private var adultCount = 1
    private var childrenCount = 0
    private var infantCount = 0
    private var isReturn = 0

    private var totalAdultPrice = 0L
    private var totalChildrenPrice = 0L

    private var totalPricePayment = 0L

    private var fromDetail = 0
    private var fromView = 0
    private var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transportation_result_detail)
        setView()
    }

    private fun setView(){
        intent.extras?.let { bundle ->
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

            transportBoatDetails = intent.getSerializableExtra("transportBoatDetails") as HashMap<String, Any>

            intent.getSerializableExtra("transportReturnBoatDetails")?.let {
                transportReturnBoatDetails = it as HashMap<String, Any>
            }

            transportFacilities = bundle.getParcelableArrayList("transportFacilities")
            transportReturnFacilities = bundle.getParcelableArrayList("transportReturnFacilities")

            date = bundle.getString("date") ?: ""
            departure_date = bundle.getString("departure_date")

            isReturn = bundle.getInt("isReturnSelected")
            fromView = bundle.getInt("fromView")
            fromDetail = bundle.getInt("fromDetail")
            adultCount = bundle.getInt("adultCount")
            childrenCount = bundle.getInt("childrenCount")
            infantCount = bundle.getInt("infantCount")

            setTotalPrice()
            setContentView()
        }

        ivBack.setOnClickListener(this)
        linearOverview.setOnClickListener(this)
        linearFacility.setOnClickListener(this)
        linearImage.setOnClickListener(this)
        btnChoose.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setContentView(){
        if (fromDetail == 0){
            txtTitle.text = "Departure"
            txtDeparture.text = transportationModel?.harbor_source_name ?: ""
            txtArrival.text = transportationModel?.harbor_destination_name ?: ""
            txtDuration.text = transportationModel?.trip_duration ?: ""

            transportationModel?.let {
                vpTransportation.adapter = TransResultPagerAdapter(it,adultCount,childrenCount,
                    transportPrice ?: HashMap(),transportFacilities ?: ArrayList(),
                    transportBoatDetails ?: HashMap(),transportImages!!,supportFragmentManager)
            }
        }
        else{
            txtTitle.text = "Return"
            txtDeparture.text = transportationModelReturn?.harbor_source_name ?: ""
            txtArrival.text = transportationModelReturn?.harbor_destination_name ?: ""
            txtDuration.text = transportationModelReturn?.trip_duration ?: ""

            transportationModelReturn?.let {
                vpTransportation.adapter = TransResultPagerAdapter(it,adultCount,childrenCount,
                    transportReturnPrice ?: HashMap(),transportFacilities ?: ArrayList()
                    , transportReturnBoatDetails ?: HashMap(),transportReturnImages!!,supportFragmentManager)
            }
        }

        vpTransportation.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                setTabState(position)
            }
        })
    }

    private fun setTotalPrice(){
        if (isReturn == 0){
            transportPrice?.let {
                val currency_label = it["currency_label"] as String
                val adultPrice = it["adult_price"] as Long
                val childrenPrice = it["adult_price"] as Long

                val totalAdultPrice = adultPrice * adultCount
                val totalChildrenPrice = childrenPrice * childrenCount

                val totalPrice = totalAdultPrice + totalChildrenPrice

                this.totalAdultPrice = totalAdultPrice
                this.totalChildrenPrice = totalChildrenPrice

                totalPricePayment = totalPrice

                val strTotalPrice = "$currency_label " +CurrencyUtil.decimal(totalPrice).replace(
                    ",",".")
                txtTotalPrice.text = strTotalPrice
            }
        }
        else{
            if (transportPrice != null && transportReturnPrice != null){
                val currency_label = transportPrice!!["currency_label"] as String
                val adultPrice = transportPrice!!["adult_price"] as Long
                val childrenPrice = transportPrice!!["adult_price"] as Long

                val totalAdultPrice = adultPrice * adultCount
                val totalChildrenPrice = childrenPrice * childrenCount

                val totalPrice = totalAdultPrice + totalChildrenPrice

                val return_currency_label = transportReturnPrice!!["currency_label"] as String
                val return_adultPrice = transportReturnPrice!!["adult_price"] as Long
                val return_childrenPrice = transportReturnPrice!!["adult_price"] as Long

                val return_totalAdultPrice = return_adultPrice * adultCount
                val return_totalChildrenPrice = return_childrenPrice * childrenCount

                val return_totalPrice = return_totalAdultPrice + return_totalChildrenPrice

                this.totalAdultPrice = totalAdultPrice + return_totalAdultPrice
                this.totalChildrenPrice = totalChildrenPrice + return_totalChildrenPrice

                val totalPrices = totalPrice + return_totalPrice

                totalPricePayment = totalPrices

                val strTotalPrice = "$currency_label " +CurrencyUtil.decimal(totalPrices).replace(
                    ",",".")
                txtTotalPrice.text = strTotalPrice
            }
        }
    }

    private fun setTabState(from : Int){
        if (from == 0){
            txtOverView.setTextColor(Color.parseColor("#616161"))
            viewOverview.visibility = View.VISIBLE

            txtFacility.setTextColor(Color.parseColor("#BBBBBB"))
            viewFacility.visibility = View.GONE

            txtImage.setTextColor(Color.parseColor("#BBBBBB"))
            viewImage.visibility = View.GONE

            vpTransportation.currentItem = from
        }
        else if (from == 1){
            txtOverView.setTextColor(Color.parseColor("#BBBBBB"))
            viewOverview.visibility = View.GONE

            txtFacility.setTextColor(Color.parseColor("#616161"))
            viewFacility.visibility = View.VISIBLE

            txtImage.setTextColor(Color.parseColor("#BBBBBB"))
            viewImage.visibility = View.GONE

            vpTransportation.currentItem = from
        }
        else{
            txtOverView.setTextColor(Color.parseColor("#BBBBBB"))
            viewOverview.visibility = View.GONE

            txtFacility.setTextColor(Color.parseColor("#BBBBBB"))
            viewFacility.visibility = View.GONE

            txtImage.setTextColor(Color.parseColor("#616161"))
            viewImage.visibility = View.VISIBLE

            vpTransportation.currentItem = from
        }
    }

    private fun intentCheckout(){
        val i = Intent(this,ActivityCheckout::class.java)
        i.putExtra("is_return",isReturn)
        i.putExtra("adult_count",adultCount)
        i.putExtra("children_count",childrenCount)
        i.putExtra("departure_date",departure_date)
        i.putExtra("infant_count",infantCount)
        i.putExtra("totalPricePayment",totalPricePayment)
        i.putExtra("transportationModel",transportationModel)
        i.putExtra("transportationModelReturn",transportationModelReturn)
        i.putExtra("transportImages",transportImages)
        i.putExtra("transportReturnImages",transportReturnImages)
        i.putExtra("transportPrice",transportPrice)
        i.putExtra("totalAdultPrice",totalAdultPrice)
        i.putExtra("transportReturnPrice",transportReturnPrice)
        i.putExtra("transportBoatDetails",transportBoatDetails)
        i.putExtra("totalChildrenPrice",totalChildrenPrice)
        i.putExtra("transportReturnPrice",transportReturnPrice)
        i.putExtra("date",date)
        i.putParcelableArrayListExtra("transportFacilities",transportFacilities)
        i.putParcelableArrayListExtra("transportReturnFacilities",transportReturnFacilities)

        startActivity(i)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBack -> {
                finish()
            }

            R.id.linearOverview -> {
                setTabState(0)
            }

            R.id.linearFacility -> {
                setTabState(1)
            }

            R.id.linearImage -> {
                setTabState(2)
            }

            R.id.btnChoose -> {
                if (fromView != 0){
                    finish()
                }
                else{
                    intentCheckout()
                }
            }
        }
    }
}
