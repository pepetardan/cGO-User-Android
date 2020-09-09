package id.dtech.cgo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Adapter.InspirationAdapter
import id.dtech.cgo.Adapter.PromoAdapter
import id.dtech.cgo.Adapter.ServiceAdapter
import id.dtech.cgo.Callback.MyCallback
import id.dtech.cgo.Controller.DestinationController
import id.dtech.cgo.Controller.ExperienceController
import id.dtech.cgo.Controller.PromoController
import id.dtech.cgo.Controller.TripInspirationController
import id.dtech.cgo.Model.DiscoverPreferanceModel
import id.dtech.cgo.Model.PromoModel
import id.dtech.cgo.Model.TripInspirationModel
import id.dtech.cgo.Preferance.UserSession

import id.dtech.cgo.R
import io.supercharge.shimmerlayout.ShimmerLayout

class FragmentExplore : Fragment(), View.OnClickListener, MyCallback.Companion.TripInspirationCallback,
    MyCallback.Companion.PromoCallback, MyCallback.Companion.DiscoverPreferanceCallback{

    private lateinit var userSession: UserSession
    private lateinit var experienceController: ExperienceController

    private lateinit var myNestedScroll : NestedScrollView

    private lateinit var shimmerInspiration : ShimmerLayout
    private lateinit var shimmerPromo : ShimmerLayout
    private lateinit var shimmerTripReference : ShimmerLayout

    private lateinit var linearDiving : LinearLayout
    private lateinit var linearFishing : LinearLayout
    private lateinit var linearSnorkeling : LinearLayout
    private lateinit var linearSailing : LinearLayout
    private lateinit var linearTour : LinearLayout

    private lateinit var linearExperience : LinearLayout
    private lateinit var lineartransportation : LinearLayout

    private lateinit var linearExperience2 : LinearLayout
    private lateinit var lineartransportation2 : LinearLayout

    private lateinit var linearLogin : LinearLayout

    private lateinit var linearHeader1 : LinearLayout
    private lateinit var linearHeader2 : LinearLayout

    private lateinit var txtChoose : TextView

    private lateinit var rvService : RecyclerView
    private lateinit var rvPromo : RecyclerView
    private lateinit var rvInspiration : RecyclerView

    private var activityPosition = 0
    private var isHeader2Showed = false

    companion object {
        @JvmStatic
        fun newInstance() = FragmentExplore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        setView(view)
        return view
    }

    private fun setView(view : View){

        experienceController = ExperienceController()

        myNestedScroll = view.findViewById(R.id.myNestedScroll)

        shimmerInspiration = view.findViewById(R.id.shimmerInspiration)
        shimmerPromo = view.findViewById(R.id.shimmerPromo)
        shimmerTripReference = view.findViewById(R.id.shimmerTripReference)

        linearDiving = view.findViewById(R.id.linearDiving)
        linearFishing = view.findViewById(R.id.linearFishing)
        linearSnorkeling = view.findViewById(R.id.linearSnorkeling)
        linearSailing = view.findViewById(R.id.linearSailing)
        linearTour = view.findViewById(R.id.linearTour)

        linearHeader1 = view.findViewById(R.id.linearHeader1)
        linearHeader2 = view.findViewById(R.id.linearHeader2)

        linearExperience = view.findViewById(R.id.linearExperience)
        lineartransportation = view.findViewById(R.id.linearTransportation)

        linearExperience2 = view.findViewById(R.id.linearExperience2)
        lineartransportation2 = view.findViewById(R.id.linearTransportation2)

        linearLogin = view.findViewById(R.id.linearLogin)

        txtChoose = view.findViewById(R.id.txtChoose)

        rvService = view.findViewById(R.id.rvService)
        rvPromo = view.findViewById(R.id.rvPromo)
        rvInspiration = view.findViewById(R.id.rvInspiration)

        activity?.let {

            userSession = UserSession(it.getSharedPreferences("user_session",Context.MODE_PRIVATE))

            rvService.layoutManager = LinearLayoutManager(it)

            rvPromo.layoutManager = LinearLayoutManager(it,LinearLayoutManager.HORIZONTAL,
                false)

            rvInspiration.layoutManager = LinearLayoutManager(it,LinearLayoutManager.HORIZONTAL,
                false)

            checkLogin()

        }

        experienceController.getDiscoverPreference(this)
        PromoController.getPromo(this)
        TripInspirationController.getTripInspiration(this)

        setNestedScroll()

        linearDiving.setOnClickListener(this)
        linearFishing.setOnClickListener(this)
        linearSnorkeling.setOnClickListener(this)
        linearSailing.setOnClickListener(this)
        linearTour.setOnClickListener(this)
        linearExperience.setOnClickListener(this)
        lineartransportation.setOnClickListener(this)
        linearExperience2.setOnClickListener(this)
        lineartransportation2.setOnClickListener(this)
        linearLogin.setOnClickListener(this)
    }

    private fun checkLogin(){
        if (userSession.access_token != null){
            txtChoose.visibility = View.VISIBLE
            linearLogin.visibility = View.GONE
        }
        else{
            txtChoose.visibility = View.GONE
            linearLogin.visibility = View.VISIBLE
        }

        Log.d("aldieys",userSession.access_token ?: "")
    }

    private fun intentExperience(from : Int){
        activity?.let {
            val i = Intent(it,ActivityExperience::class.java)
            i.putExtra("from",from)
            i.putExtra("activity_position",activityPosition)
            it.startActivity(i)
        }
    }

    private fun setNestedScroll(){
        myNestedScroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int,
                                                   oldScrollX: Int, oldScrollY: Int ->

            if (scrollY >= 729 && oldScrollY >= 730){
                if (!isHeader2Showed){
                    linearHeader1.visibility = View.GONE
                    linearHeader2.visibility = View.VISIBLE

                    isHeader2Showed = true
                }
            }
            else if (scrollY == 0){
                linearHeader1.visibility = View.VISIBLE
                linearHeader2.visibility = View.GONE
                isHeader2Showed = false
            }

            Log.d("aldieyScroll",""+scrollY+"__"+oldScrollY)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.linearExperience -> {
               intentExperience(1)
            }

            R.id.linearTransportation -> {
                activity?.let {
                    it.startActivity(Intent(it,ActivitySearchTransportation::class.java))
                }
            }

            R.id.linearExperience2 -> {
                intentExperience(1)
            }

            R.id.linearTransportation2 -> {
                activity?.let {
                    it.startActivity(Intent(it,ActivitySearchTransportation::class.java))
                }
            }

            R.id.linearDiving -> {
                activityPosition = 1
                intentExperience(2)
            }

            R.id.linearFishing -> {
                activityPosition = 2
                intentExperience(2)
            }

            R.id.linearSnorkeling -> {
                activityPosition = 3
                intentExperience(2)
            }

            R.id.linearSailing -> {
                activityPosition = 4
                intentExperience(2)
            }

            R.id.linearTour -> {
                activityPosition = 5
                intentExperience(2)
            }

            R.id.linearLogin -> {
                activity?.let {
                    it.startActivity(Intent(it,ActivityLogin::class.java))
                }
            }
        }
    }

    override fun onTripInspirationPrepare() {
        shimmerInspiration.visibility = View.VISIBLE
        rvInspiration.visibility = View.GONE
        shimmerInspiration.startShimmerAnimation()
    }

    override fun onTripInspirationLoaded(inspirationList: ArrayList<TripInspirationModel>) {
       activity?.let {
           shimmerInspiration.stopShimmerAnimation()
           shimmerInspiration.visibility = View.GONE
           rvInspiration.visibility = View.VISIBLE
           rvInspiration.adapter = InspirationAdapter(it,inspirationList)
       }
    }

    override fun onTripInspirationError() {

    }

    override fun onPromoPrepare() {
        shimmerPromo.visibility = View.VISIBLE
        rvPromo.visibility = View.GONE
        shimmerPromo.startShimmerAnimation()
    }

    override fun onPromoLoaded(promoList: ArrayList<PromoModel>) {
      activity?.let {
          shimmerPromo.stopShimmerAnimation()
          shimmerPromo.visibility = View.GONE
          rvPromo.visibility = View.VISIBLE
          rvPromo.adapter = PromoAdapter(it,promoList)
      }
    }

    override fun onPromoError() {

    }

    override fun onDiscoverPreferancePrepare() {
        shimmerTripReference.visibility = View.VISIBLE
        rvService.visibility = View.GONE
        shimmerTripReference.startShimmerAnimation()
    }

    override fun onDiscoverPreferanceLoaded(discoverList: ArrayList<DiscoverPreferanceModel>) {
        activity?.let {
            shimmerTripReference.stopShimmerAnimation()
            shimmerTripReference.visibility = View.GONE
            rvService.visibility = View.VISIBLE
            rvService.adapter = ServiceAdapter(it,discoverList)
        }
    }

    override fun onDiscoverPreferanceError() {

    }
}
