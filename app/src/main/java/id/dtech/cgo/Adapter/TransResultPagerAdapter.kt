package id.dtech.cgo.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.dtech.cgo.Model.FacilityModel
import id.dtech.cgo.Model.TransportationModel
import id.dtech.cgo.View.FragmentFacilities
import id.dtech.cgo.View.FragmentOverview
import id.dtech.cgo.View.FragmentShipImages

class TransResultPagerAdapter(transportationModel : TransportationModel,adultCount : Int,
                              childrenCount : Int,transportPrice : HashMap<String,Any>,
                              facilityList : ArrayList<FacilityModel>, transportBoatDetails :
                              HashMap<String,Any>,transportImages : ArrayList<HashMap<String,Any>>,
                              fm : FragmentManager) : FragmentPagerAdapter(fm,

    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var transportBoatDetail = transportBoatDetails
    private val transportModel = transportationModel
    private val transportPrices  = transportPrice
    private var adultCounts = adultCount
    private var childrenCounts = childrenCount
    private var facilities = facilityList
    private var transportImages = transportImages

    override fun getItem(position: Int): Fragment {
        if (position == 0){
            return FragmentOverview.newInstance(transportModel,adultCounts,childrenCounts,
                transportPrices)
        }
        else if (position == 1){
            return FragmentFacilities.newInstance(facilities,transportModel,transportBoatDetail)
        }
        else{
            return FragmentShipImages.newInstance(transportImages)
        }
    }

    override fun getCount(): Int {
       return 3
    }
}