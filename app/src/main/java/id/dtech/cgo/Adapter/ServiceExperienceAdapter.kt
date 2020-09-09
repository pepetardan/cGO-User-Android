package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.rd.PageIndicatorView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Listener.ApplicationListener
import id.dtech.cgo.Model.ExperienceModel
import id.dtech.cgo.Model.PhotoModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.View.ActivityDetailExperience
import id.dtech.cgo.View.dp
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ServiceExperienceAdapter (context : Context, itemList : ArrayList<ExperienceModel>,
                                serviceExperienceListener : ApplicationListener.Companion.ServiceExperienceListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val contexs = context
    private val listener = serviceExperienceListener
    private var items = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0){
            val view = LayoutInflater.from(contexs).inflate(R.layout.item_service_experience,parent,
                false)
            return ServiceExperienceViewHolder(view)
        }
        else if (viewType == 1){
            val view = LayoutInflater.from(contexs).inflate(R.layout.item_loading,parent,
                false)
            return LoadingViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(contexs).inflate(R.layout.item_error,parent,
                false)
            return ErrorViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ServiceExperienceViewHolder){
            val experienceModel = items[position]
            val type = experienceModel.exp_type ?: ArrayList()
            val lastIndex = items.size - 1
            val price =  CurrencyUtil.decimal(experienceModel.price).replace(",",".")

            val layoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            if (position != lastIndex){
                layoutParam.setMargins(24.dp, 32.dp, 24.dp, 0.dp)
            }
            else{
                layoutParam.setMargins(24.dp, 32.dp, 24.dp, 32.dp)
            }

            holder.linearParent.layoutParams = layoutParam

            if (experienceModel.count_rating > 1){
                holder.txtReview.text = ""+experienceModel.count_rating+" Reviews"
            }
            else{
                holder.txtReview.text = ""+experienceModel.count_rating+" Review"
            }

            experienceModel.payment_type?.let { strType ->
                val paymentType = strType.split(" ")

                if (paymentType.size > 1){
                    val type = paymentType[1].toLowerCase(Locale.ROOT)
                    val currency = experienceModel.currency ?: ""
                    val strPrice = "$currency $price/$type"
                    holder.txtPrice.text = strPrice
                }
            }

            holder.txtTitle.text = experienceModel.exp_title ?: ""

            holder.rvService.layoutManager = LinearLayoutManager(contexs
                , LinearLayoutManager.HORIZONTAL,false)
            holder.rvService.adapter = ServiceTypeAdapter(contexs,type)

            holder.linearParent.setOnClickListener {
                val i = Intent(contexs,ActivityDetailExperience::class.java)
                i.putExtra("experience_id",experienceModel.id)
                contexs.startActivity(i)
            }

            setStar(experienceModel.rating,holder.imgStar1,holder.imgStar2,holder.imgStar3,
                holder.imgStar4,holder.imgStar5)

            experienceModel.list_photo?.let { photos ->
                if (photos.size > 0){
                        val photoList = photos[0]["exp_photo_image"] as ArrayList<PhotoModel>
                        experienceModel.cover_photo?.let { coverPhoto ->
                            photoList.add(0,coverPhoto)
                            val adapter = ImagePagerAdapter(contexs,photoList,experienceModel.id ?: "")
                            holder.viewPager.adapter = adapter
                            holder.indicatorView.setViewPager(holder.viewPager)
                        }
                }
            }
        }
        else if (holder is LoadingViewHolder){

        }
        else{
            (holder as ErrorViewHolder).txtTryAgain.setOnClickListener {
                listener.onServiceErrorClicked()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val experienceModel = items[position]
        return experienceModel.viewType
    }

    private fun setStar(count : Double, star1 : ImageView, star2 : ImageView, star3 : ImageView,
                        star4 : ImageView, star5 : ImageView ) {

        if (count == 0.0) {
            star1.setImageResource(R.drawable.ic_star_empty)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 0.5) {
            star1.setImageResource(R.drawable.ic_star_half)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 1.0) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 1.5) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_half)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 2.0) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 2.5) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_half)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 3.0) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 3.5) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_half)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 4.0) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_empty)
        } else if (count == 4.5) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_half)
        } else if (count == 5.0) {
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }
    }

    fun updateExperienceList(itemList : ArrayList<ExperienceModel>){
        this.items = itemList
        notifyDataSetChanged()
    }

    class ServiceExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvService = itemView.findViewById<RecyclerView>(R.id.rvServiceType)
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)

        val viewPager = itemView.findViewById<ViewPager>(R.id.viewPager)
        val indicatorView = itemView.findViewById<PageIndicatorView>(R.id.indicatorView)

        val txtTitle = itemView.findViewById<MyTextView>(R.id.txtTitle)
        val txtReview = itemView.findViewById<MyTextView>(R.id.txtReview)
        val txtPrice = itemView.findViewById<MyTextView>(R.id.txtPrice)

        val imgStar1 = itemView.findViewById<ImageView>(R.id.imgStar1)
        val imgStar2 = itemView.findViewById<ImageView>(R.id.imgStar2)
        val imgStar3 = itemView.findViewById<ImageView>(R.id.imgStar3)
        val imgStar4 = itemView.findViewById<ImageView>(R.id.imgStar4)
        val imgStar5 = itemView.findViewById<ImageView>(R.id.imgStar5)
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtTryAgain = itemView.findViewById<MyTextView>(R.id.txtTryAgain)
    }
}