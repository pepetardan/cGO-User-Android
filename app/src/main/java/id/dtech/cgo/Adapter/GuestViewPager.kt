package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.item_boarding.view.*

class GuestViewPager (context : Context) : PagerAdapter() {

    private val contexts = context

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=  LayoutInflater.from(container.context).inflate(R.layout.item_input_guest,
            container, false)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1

    override fun getCount() : Int = 3

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}