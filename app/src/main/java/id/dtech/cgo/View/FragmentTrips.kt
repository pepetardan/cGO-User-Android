package id.dtech.cgo.View

import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import id.dtech.cgo.Adapter.TripPagerAdapter
import id.dtech.cgo.Adapter.TripsAdapter

import id.dtech.cgo.R

class FragmentTrips : Fragment(), View.OnClickListener{

    companion object {
        @JvmStatic
        fun newInstance(from : Int) = FragmentTrips().apply {
            val b = Bundle()
            b.putInt("from",from)
            arguments = b
        }
    }

    private lateinit var viewPager : ViewPager
    private lateinit var myHorizontalScroll : HorizontalScrollView

    private lateinit var linearUpcoming : LinearLayout
    private lateinit var linearWaiting : LinearLayout
    private lateinit var linearPending : LinearLayout
    private lateinit var linearHistory : LinearLayout

    private lateinit var txtUpcoming : TextView
    private lateinit var txtWaiting : TextView
    private lateinit var txtPending : TextView
    private lateinit var txtHistory : TextView

    private lateinit var viewUpcoming : View
    private lateinit var viewWaiting : View
    private lateinit var viewPending : View
    private lateinit var viewHistory : View

    private var from = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            from = it.getInt("from")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_trips, container, false)
        setView(view)
        return view
    }

    private fun setView(view: View){

        viewPager = view.findViewById(R.id.viewPager)
        myHorizontalScroll = view.findViewById(R.id.myHorizontalScroll)

        linearUpcoming = view.findViewById(R.id.linearUpcoming)
        linearWaiting = view.findViewById(R.id.linearWaiting)
        linearPending = view.findViewById(R.id.linearPending)
        linearHistory = view.findViewById(R.id.linearHistory)

        txtUpcoming = view.findViewById(R.id.txtUpcoming)
        txtWaiting = view.findViewById(R.id.txtWaiting)
        txtPending = view.findViewById(R.id.txtPending)
        txtHistory = view.findViewById(R.id.txtHistory)

        viewUpcoming = view.findViewById(R.id.viewUpcoming)
        viewWaiting = view.findViewById(R.id.viewWaiting)
        viewPending = view.findViewById(R.id.viewPending)
        viewHistory = view.findViewById(R.id.viewHistory)

        activity?.let {
            viewPager.adapter = TripPagerAdapter(childFragmentManager)

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float,
                                            positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    setTabBar(position)
                }
            })
        }

        if (from == 1){
            setTabBar(3)
            myHorizontalScroll.smoothScrollTo(getScreenWidth(),0)
        }

        linearUpcoming.setOnClickListener(this)
        linearWaiting.setOnClickListener(this)
        linearPending.setOnClickListener(this)
        linearHistory.setOnClickListener(this)
    }

    private fun getScreenWidth() : Int {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            return size.x
        }

        return 0
    }

    private fun setTabBar(position : Int){
        if (position == 0){
            txtUpcoming.setTextColor(Color.parseColor("#000000"))
            txtWaiting.setTextColor(Color.parseColor("#808080"))
            txtPending.setTextColor(Color.parseColor("#808080"))
            txtHistory.setTextColor(Color.parseColor("#808080"))

            viewUpcoming.visibility = View.VISIBLE
            viewWaiting.visibility = View.INVISIBLE
            viewPending.visibility = View.INVISIBLE
            viewHistory.visibility = View.INVISIBLE

            viewPager.currentItem = 0
        }
        else if (position == 1){
            txtUpcoming.setTextColor(Color.parseColor("#808080"))
            txtWaiting.setTextColor(Color.parseColor("#000000"))
            txtPending.setTextColor(Color.parseColor("#808080"))
            txtHistory.setTextColor(Color.parseColor("#808080"))

            viewUpcoming.visibility = View.INVISIBLE
            viewWaiting.visibility = View.VISIBLE
            viewPending.visibility = View.INVISIBLE
            viewHistory.visibility = View.INVISIBLE

            viewPager.currentItem = 1
        }
        else if (position == 2){
            txtUpcoming.setTextColor(Color.parseColor("#808080"))
            txtWaiting.setTextColor(Color.parseColor("#808080"))
            txtPending.setTextColor(Color.parseColor("#000000"))
            txtHistory.setTextColor(Color.parseColor("#808080"))

            viewUpcoming.visibility = View.INVISIBLE
            viewWaiting.visibility = View.INVISIBLE
            viewPending.visibility = View.VISIBLE
            viewHistory.visibility = View.INVISIBLE

            viewPager.currentItem = 2
        }
        else{
            txtUpcoming.setTextColor(Color.parseColor("#808080"))
            txtWaiting.setTextColor(Color.parseColor("#808080"))
            txtPending.setTextColor(Color.parseColor("#808080"))
            txtHistory.setTextColor(Color.parseColor("#000000"))

            viewUpcoming.visibility = View.INVISIBLE
            viewWaiting.visibility = View.INVISIBLE
            viewPending.visibility = View.INVISIBLE
            viewHistory.visibility = View.VISIBLE

            viewPager.currentItem = 3
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.linearUpcoming -> {
                setTabBar(0)
            }

            R.id.linearWaiting -> {
                setTabBar(1)
            }

            R.id.linearPending -> {
                setTabBar(2)
            }

            R.id.linearHistory -> {
                setTabBar(3)
            }
        }
    }
}
