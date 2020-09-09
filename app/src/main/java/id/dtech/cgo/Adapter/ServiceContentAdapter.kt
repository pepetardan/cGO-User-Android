package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.Model.ExperienceModel
import id.dtech.cgo.Model.PhotoModel
import id.dtech.cgo.R
import id.dtech.cgo.Util.CurrencyUtil
import id.dtech.cgo.View.ActivityDetailExperience
import id.dtech.cgo.View.dp
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ServiceContentAdapter (context : Context, experienceList : ArrayList<ExperienceModel>) :
    RecyclerView.Adapter<ServiceContentAdapter.ServiceContentViewHolder>() {

    private val contexs = context
    private val experiences = experienceList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceContentViewHolder {
        val view = LayoutInflater.from(contexs).inflate(
            R.layout.item_content_service,parent,
            false)

        return ServiceContentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return experiences.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ServiceContentViewHolder, position: Int) {

        val experienceModel = experiences[position]
        val types = experienceModel.exp_type ?: ArrayList()
        val lastIndex = experiences.size - 1
        val photos = experienceModel.cover_photo ?: PhotoModel()
        val price =  CurrencyUtil.decimal(experienceModel.price).replace(",",".")
        val paymentType = (experienceModel.payment_type ?: "").split(" ")

        if (paymentType.size > 1){
            val currency = experienceModel.currency ?: ""
            val priceType = paymentType[1].toLowerCase(Locale.ROOT)
            holder.txtPrice.text = "$currency $price/$priceType"
        }

        if (experiences.size > 1){
            val layoutParam = FrameLayout.LayoutParams(300.dp,315.dp)

            if (position == 0){
                layoutParam.setMargins(24.dp, 8.dp, 0, 8.dp)
            }
            else if (position != lastIndex && position != 0){
                layoutParam.setMargins(16.dp, 8.dp, 0, 8.dp)
            }
            else{
                layoutParam.setMargins(16.dp, 8.dp, 24.dp, 8.dp)
            }

            holder.cardParent.layoutParams = layoutParam
        }
        else{
            val layoutParam = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                295.dp)
            layoutParam.setMargins(24.dp, 8.dp, 24.dp, 8.dp)
            holder.cardParent.layoutParams = layoutParam
        }

        photos.original?.let { photo ->
            if (photo.isNotEmpty()){
                try {
                    Picasso.get().load(photo).into(holder.imgImage)
                }
                catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }

        holder.txtTitle.text = experienceModel.exp_title ?: ""

        if (experienceModel.count_rating > 1){
            holder.txtReview.text = ""+experienceModel.count_rating+" Reviews"
        }
        else{
            holder.txtReview.text = ""+experienceModel.count_rating+" Review"
        }

        holder.rvServiceType.layoutManager = LinearLayoutManager(contexs
                ,LinearLayoutManager.HORIZONTAL,false)
        holder.rvServiceType.adapter = ServiceTypeAdapter(contexs,types)

        holder.linearParent.setOnClickListener {
            val i = Intent(contexs,ActivityDetailExperience::class.java)
            i.putExtra("experience_id",experienceModel.id)
            contexs.startActivity(i)
        }

        setStar(experienceModel.rating,holder.imgStar1,holder.imgStar2,holder.imgStar3,
            holder.imgStar4,holder.imgStar5)
    }

    private fun setStar(count : Double, star1 : ImageView, star2 : ImageView, star3 : ImageView,
                        star4 : ImageView, star5 : ImageView ){

        if (count == 0.0){
            star1.setImageResource(R.drawable.ic_star_empty)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 0.5){
            star1.setImageResource(R.drawable.ic_star_half)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_empty)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 1.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star_half)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_empty)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 2.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star_half)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_empty)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 3.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star_half)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_empty)
        }
        else if (count == 4.5){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star_half)
        }
        else if (count == 5.0){
            star1.setImageResource(R.drawable.ic_star)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }
    }

    class ServiceContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)

        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitle)
        val txtReview = itemView.findViewById<TextView>(R.id.txtReview)
        val txtPrice = itemView.findViewById<TextView>(R.id.txtPrice)

        val imgImage = itemView.findViewById<ImageView>(R.id.imgImage)
        val imgStar1 = itemView.findViewById<ImageView>(R.id.imgStar1)
        val imgStar2 = itemView.findViewById<ImageView>(R.id.imgStar2)
        val imgStar3 = itemView.findViewById<ImageView>(R.id.imgStar3)
        val imgStar4 = itemView.findViewById<ImageView>(R.id.imgStar4)
        val imgStar5 = itemView.findViewById<ImageView>(R.id.imgStar5)

        val rvServiceType = itemView.findViewById<RecyclerView>(R.id.rvServiceType)
    }
}
