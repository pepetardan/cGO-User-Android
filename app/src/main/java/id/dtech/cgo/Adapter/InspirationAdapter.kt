package id.dtech.cgo.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.TripInspirationModel

import id.dtech.cgo.R
import id.dtech.cgo.View.ActivityDetailExperience
import id.dtech.cgo.View.ActivityDetaipTripInspiration
import id.dtech.cgo.View.dp

class InspirationAdapter (contex : Context, itemList : ArrayList<TripInspirationModel>) :
    RecyclerView.Adapter<InspirationAdapter.InspirationViewHolder>() {

    private val contexts = contex
    private val inspirationList = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_inspiration,parent,false)
        return InspirationViewHolder(view)

    }

    override fun getItemCount(): Int {
        return inspirationList.size
    }

    override fun onBindViewHolder(holder: InspirationViewHolder, position: Int) {
        val inspirationModel = inspirationList[position]
        val lastIndex = inspirationList.size - 1
        val layoutParam = FrameLayout.LayoutParams(300.dp,255.dp)
        val id = inspirationModel.exp_inspiration_id
        val trip_url = "https://blog.cgo.co.id/#/blog-detail/$id"

        if (position == 0){
            layoutParam.setMargins(24.dp, 8.dp, 0, 8.dp)
        }
        else if (position != lastIndex && position != 0){
            layoutParam.setMargins(16.dp, 8.dp, 0, 8.dp)
        }
        else{
            layoutParam.setMargins(16.dp, 8.dp, 24.dp, 8.dp)
        }

        Picasso.get().load(inspirationModel.exp_cover_photo ?: "").into(holder.imgImage)

        holder.txtTitle.text = inspirationModel.exp_desc ?: ""
        holder.cardParent.layoutParams = layoutParam

        holder.txtReadMore.setOnClickListener {
            val intent = Intent(contexts, ActivityDetaipTripInspiration::class.java)
            intent.putExtra("trip_url", trip_url)
            contexts.startActivity(intent)
        }
    }

    class InspirationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val imgImage = itemView.findViewById<ImageView>(R.id.imgImage)
        val txtTitle = itemView.findViewById<MyTextView>(R.id.txtTitle)
        val txtReadMore = itemView.findViewById<MyTextView>(R.id.txtReadMore)
    }
}

