package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import id.dtech.cgo.R
import kotlinx.android.synthetic.main.item_boarding.view.*

class BoardingViewPager(context : Context) : PagerAdapter() {

    private val contexts = context

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view= LayoutInflater.from(container.context).inflate(R.layout.item_boarding, container, false)

        when(position){
            0 -> {
                view.imgBoarding.setImageResource(R.drawable.onboarding_image_1)
                view.txtTitle.text = contexts.getString(R.string.boarding_word_1)
                view.txtContent.text = contexts.getString(R.string.boarding_content_1)
            }
            1 -> {
                view.imgBoarding.setImageResource(R.drawable.onboarding_image_2)
                view.txtTitle.text = contexts.getString(R.string.boarding_word_2)
                view.txtContent.text = contexts.getString(R.string.boarding_content_2)
            }
            else -> {
                view.imgBoarding.setImageResource(R.drawable.onboarding_image_3)
                view.txtTitle.text = contexts.getString(R.string.boarding_word_3)
                view.txtContent.text = contexts.getString(R.string.boarding_content_3)
            }
        }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1

    override fun getCount() : Int = 3

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}