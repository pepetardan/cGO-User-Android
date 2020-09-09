package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.Model.PromoModel
import id.dtech.cgo.R
import id.dtech.cgo.View.dp

class PromoAdapter(contex : Context, promoList : ArrayList<PromoModel>) : RecyclerView.Adapter<PromoAdapter.PromoViewHolder>() {

    private val contexts = contex
    private val promos = promoList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_promo,parent,false)
        return PromoViewHolder(view)
    }

    override fun getItemCount(): Int {
       return promos.size
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val promoModel = promos[position]
        val lastIndex = promos.size - 1
        val layoutParam = FrameLayout.LayoutParams(246.dp,131.dp)

        if (position == 0){
            layoutParam.setMargins(24.dp, 0, 0, 0)
        }
        else if (position != lastIndex && position != 0){
            layoutParam.setMargins(12.dp, 0, 0, 0)
        }
        else{
            layoutParam.setMargins(12.dp, 0, 24.dp, 0)
        }

        holder.cardParent.layoutParams = layoutParam

        val promoImage = promoModel.promo_image ?: ""

        if (promoImage.isNotEmpty()){
            Picasso.get().load(promoImage).into(holder.imgImage)
        }
    }

    class PromoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val imgImage = itemView.findViewById<ImageView>(R.id.imgImage)
    }
}