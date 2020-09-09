package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import id.dtech.cgo.Adapter.CancelationPolicyAdapter
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.TransportationModel

import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FragmentOverview : Fragment(), View.OnClickListener{

    private lateinit var cancelBottomSheetDialog : RoundedBottomSheetDialog

    private lateinit var transportModel : TransportationModel
    private lateinit var transportPrice : HashMap<String,Any>

    private lateinit var txtOriginTime : MyTextView
    private lateinit var txtOriginDate : MyTextView
    private lateinit var txtDuration : MyTextView
    private lateinit var txtDestinationTime : MyTextView
    private lateinit var txtDestinationDate : MyTextView
    private lateinit var txtProvinceOrigin : MyTextView
    private lateinit var  txtCityOrigin : MyTextView
    private lateinit var txtOriginHarbour : MyTextView
    private lateinit var txtProvinceDestination : MyTextView
    private lateinit var txtCityDestination : MyTextView
    private lateinit var txtDestinationHarbour : MyTextView
    private lateinit var txtAdult : MyTextView
    private lateinit var txtAdultPrice : MyTextView
    private lateinit var txtChildren : MyTextView
    private lateinit var txtChildrenPrice : MyTextView
    private lateinit var txtTotalPrice : MyTextView

    private lateinit var linearAdult : LinearLayout
    private lateinit var linearChildren : LinearLayout
    private lateinit var linearInsurance : LinearLayout

    private lateinit var cardCancelation : CardView

    //CANCELATION
    private lateinit var imgClose : ImageView
    private lateinit var imgMerchant : ImageView
    private lateinit var txtMerchantName : MyTextView
    private lateinit var txtDeparture : MyTextView
    private lateinit var txtArrival : MyTextView

    private var adultCount = 0
    private var childrenCount = 0

    companion object {
        @JvmStatic
        fun newInstance(transportModel : TransportationModel, adultCount : Int, childrenCount : Int,
                        transportPrice : HashMap<String,Any>) = FragmentOverview().apply {
            val b = Bundle()
            b.putParcelable("transport_model",transportModel)
            b.putInt("adultCount",adultCount)
            b.putInt("childrenCount",childrenCount)
            b.putSerializable("transportPrice",transportPrice)
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         arguments?.let {
             transportModel = it.getParcelable("transport_model") ?: TransportationModel()
             adultCount = it.getInt("adultCount")
             childrenCount = it.getInt("childrenCount")
             transportPrice = it.getSerializable("transportPrice") as HashMap<String,Any>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        setView(view)
        return view
    }

    private fun setView(view : View){
        txtOriginTime = view.findViewById(R.id.txtOriginTime)
        txtOriginDate = view.findViewById(R.id.txtOriginDate)
        txtDuration = view.findViewById(R.id.txtDuration)
        txtDestinationTime = view.findViewById(R.id.txtDestinationTime)
        txtDestinationDate = view.findViewById(R.id.txtDestinationDate)
        txtProvinceOrigin = view.findViewById(R.id.txtProvinceOrigin)
        txtCityOrigin = view.findViewById(R.id.txtCityOrigin)
        txtOriginHarbour = view.findViewById(R.id.txtOriginHarbour)
        txtProvinceDestination = view.findViewById(R.id.txtProvinceDestination)
        txtCityDestination = view.findViewById(R.id.txtCityDestination)
        txtDestinationHarbour = view.findViewById(R.id.txtDestinationHarbour)
        txtAdult = view.findViewById(R.id.txtAdult)
        txtAdultPrice = view.findViewById(R.id.txtAdultPrice)
        txtChildren = view.findViewById(R.id.txtChildren)
        txtChildrenPrice = view.findViewById(R.id.txtChildrenPrice)
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice)

        linearAdult = view.findViewById(R.id.linearAdult)
        linearChildren = view.findViewById(R.id.linearChildren)
        linearInsurance = view.findViewById(R.id.linearInsurance)

        cardCancelation = view.findViewById(R.id.cardCancelation)

        cardCancelation.setOnClickListener(this)

        setContentView()
        setUpCancelation()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setContentView(){

        val adultPriceNumber = transportPrice["adult_price"] as Long * adultCount
        val childrenPriceNumber = transportPrice["children_price"] as Long * childrenCount
        val totalPriceNumber = adultPriceNumber + childrenPriceNumber
        val strTotalPrice = CurrencyUtil.decimal(totalPriceNumber).replace(",",
            ".")

        val adultPrice = CurrencyUtil.decimal(transportPrice["adult_price"] as Long * adultCount)
            .replace(",",".")
        val childrenPrice = CurrencyUtil.decimal(transportPrice["children_price"] as Long * childrenCount)
            .replace(",",".")
        val currency = transportPrice["currency_label"] as String

        val sdfDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        val sdfDate = SimpleDateFormat("dd MMM")
        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("HH:mm")

        val dateTimeDate =  sdfDateTime.parse(transportModel.departure_date ?: "")
        val startDateTime = sdf.parse(transportModel.departure_time ?: "")
        val endDateTime = sdf.parse(transportModel.arrival_time ?: "")

        txtOriginTime.text = sdfs.format(startDateTime ?: Date())
        txtDestinationTime.text = sdfs.format(endDateTime ?: Date())

        txtOriginDate.text = sdfDate.format(dateTimeDate ?: Date())
        txtDestinationDate.text = sdfDate.format(dateTimeDate ?: Date())

        txtDuration.text = transportModel.trip_duration ?: ""

        txtProvinceOrigin.text = transportModel.harbor_source_name ?: ""
        txtProvinceDestination.text = transportModel.harbor_destination_name ?: ""

        txtCityOrigin.text = ", "+transportModel.city_source_name ?: ""
        txtCityDestination.text = ", "+transportModel.city_dest_name ?: ""

        txtOriginHarbour.text = transportModel.harbor_source_name ?: ""
        txtDestinationHarbour.text = transportModel.harbor_destination_name ?: ""

        if (adultCount > 0){
            linearAdult.visibility = View.VISIBLE
            txtAdult.text = "Adult(x$adultCount)"
            txtAdultPrice.text = "$currency $adultPrice"
        }

        if (childrenCount > 0){
            linearChildren.visibility = View.VISIBLE
            txtChildren.text = "Children(x$childrenCount)"
            txtChildrenPrice.text = "$currency $childrenPrice"
        }

        txtTotalPrice.text = "$currency $strTotalPrice"

    }

    private fun setUpCancelation(){
        activity?.let {
            cancelBottomSheetDialog = RoundedBottomSheetDialog(it)
            val sheetView = layoutInflater.inflate(R.layout.layout_transport_cancellation, null)
            cancelBottomSheetDialog.setContentView(sheetView)
            cancelBottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

            val rvCancelation = sheetView.findViewById<RecyclerView>(R.id.rvCancelation)
            imgClose = sheetView.findViewById(R.id.imgClose)
            imgMerchant = sheetView.findViewById(R.id.imgMerchant)
            txtMerchantName = sheetView.findViewById(R.id.txtMerchantName)
            txtDeparture = sheetView.findViewById(R.id.txtDeparture)
            txtArrival = sheetView.findViewById(R.id.txtArrival)

            txtMerchantName.text = transportModel.merchant_name ?: ""
            txtDeparture.text = transportModel.harbor_source_name ?: ""
            txtArrival.text = transportModel.harbor_destination_name ?: ""

            val strImgMerchant = transportModel.merchant_picture ?: ""

            if (strImgMerchant.isNotEmpty()){
                Picasso.get().load(strImgMerchant).into(imgMerchant)
            }

            rvCancelation.layoutManager = LinearLayoutManager(it)
            rvCancelation.adapter = CancelationPolicyAdapter(it)
        }

        imgClose.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.cardCancelation -> {
                cancelBottomSheetDialog.show()
            }
            R.id.imgClose -> {
                cancelBottomSheetDialog.dismiss()
            }
        }
    }
}
