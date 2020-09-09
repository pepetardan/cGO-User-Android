package id.dtech.cgo.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.Adapter.FacilityAdapter
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.FacilityModel
import id.dtech.cgo.Model.TransportationModel

import id.dtech.cgo.R

class FragmentFacilities : Fragment() {

    private lateinit var facilityList : ArrayList<FacilityModel>
    private lateinit var transportationModel: TransportationModel
    private lateinit var transportBoatDetails : HashMap<String,Any>

    private lateinit var rvFacility : RecyclerView
    private lateinit var imgMerchant : ImageView
    private lateinit var txtMerchant : MyTextView
    private lateinit var txtClass : MyTextView

    private lateinit var txtPassenger : MyTextView
    private lateinit var txtLength : MyTextView
    private lateinit var txtWidth : MyTextView
    private lateinit var txtMachine : MyTextView

    companion object {
        @JvmStatic
        fun newInstance(facilityList : ArrayList<FacilityModel>,transportationModel: TransportationModel
                        ,transportBoatDetails : HashMap<String,Any>)
                = FragmentFacilities().apply {
            val b = Bundle()
            b.putParcelableArrayList("facilities",facilityList)
            b.putParcelable("transportation_model",transportationModel)
            b.putSerializable("transport_boat",transportBoatDetails)
            arguments = b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = arguments
        b?.let {
            facilityList = it.getParcelableArrayList("facilities") ?: ArrayList()
            transportationModel = it.getParcelable("transportation_model") ?: TransportationModel()
            transportBoatDetails = it.getSerializable("transport_boat") as HashMap<String,Any>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_facilities, container, false)
        setView(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setView(view : View){
        rvFacility = view.findViewById(R.id.rvFacility)
        imgMerchant  = view.findViewById(R.id.imgMerchant)
        txtMerchant = view.findViewById(R.id.txtMerchant)
        txtClass = view.findViewById(R.id.txtClass)

        txtPassenger = view.findViewById(R.id.txtPassenger)
        txtLength = view.findViewById(R.id.txtLength)
        txtWidth = view.findViewById(R.id.txtWidth)
        txtMachine = view.findViewById(R.id.txtMachine)

        activity?.let {
            rvFacility.layoutManager = GridLayoutManager(it,2)
            rvFacility.adapter = FacilityAdapter(it,facilityList)
        }

        val strImgMerchant = transportationModel.merchant_picture ?: ""

        if (strImgMerchant.isNotEmpty()){
            Picasso.get().load(strImgMerchant).into(imgMerchant)
        }

        txtMerchant.text = transportationModel.merchant_name ?: ""
        txtClass.text = (transportationModel.transportClass ?: "")+" Class"

        val cabin = transportBoatDetails["cabin"] as Int
        val length = transportBoatDetails["length"] as Double
        val width  = transportBoatDetails["width"] as Double
        val machine  = transportBoatDetails["machine"] as String

        txtPassenger.text = "$cabin Passengers"
        txtLength.text = "$length Meters"
        txtWidth.text = "$width Meters"
        txtMachine.text = machine

    }
}
