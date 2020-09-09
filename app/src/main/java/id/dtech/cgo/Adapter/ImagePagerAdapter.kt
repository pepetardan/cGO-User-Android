package id.dtech.cgo.Adapter

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import id.dtech.cgo.Model.PhotoModel
import id.dtech.cgo.R
import id.dtech.cgo.View.ActivityDetailExperience
import java.lang.Exception

class ImagePagerAdapter(context : Context, photos : ArrayList<PhotoModel>, exp_id : String) : PagerAdapter() {

    private val contexts = context
    private val photoList = photos
    private val expID = exp_id

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_image_pager, container, false)
        val imgImage = view.findViewById<ImageView>(R.id.imgImage)
        val imageModel = photoList[position]

        val strImage = imageModel.original ?: ""

        if (strImage.isNotEmpty()){
            try {
                Picasso.get().load(strImage).into(imgImage)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        imgImage.setOnClickListener {
           if (expID.isNotEmpty()){
               val i = Intent(contexts, ActivityDetailExperience::class.java)
               i.putExtra("experience_id",expID)
               contexts.startActivity(i)
           }
        }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1

    override fun getCount() : Int = photoList.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}