package id.dtech.cgo.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.dtech.cgo.View.FragmentHistory
import id.dtech.cgo.View.FragmentPending
import id.dtech.cgo.View.FragmentUpcoming
import id.dtech.cgo.View.FragmentWaiting


class TripPagerAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {
    override fun getItem(position: Int): Fragment {
        if (position == 0){
            return FragmentUpcoming.newInstance()
        }
        else if (position == 1){
            return FragmentWaiting.newInstance()
        }
        else if (position == 2){
            return FragmentPending.newInstance()
        }
        else{
            return FragmentHistory.newInstance()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}