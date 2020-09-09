package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.R
import id.dtech.cgo.View.dp

class GuideAdapter(context : Context, from : Int) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private val contexts = context
    private val froms = from

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
       val view = LayoutInflater.from(contexts).inflate(R.layout.item_guide,parent,false)
        return GuideViewHolder(view)
    }

    override fun getItemCount(): Int {
       return 2
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val layoutParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        if (position != 1){
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 0.dp)
        }
        else{
            layoutParam.setMargins(24.dp, 24.dp, 24.dp, 24.dp)
        }

        if (froms == 1){
            holder.imgCheckTypeBlue.visibility = View.VISIBLE
            holder.btnNext.visibility = View.VISIBLE
        }

        holder.cardParent.layoutParams = layoutParam
    }

    class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardParent = itemView.findViewById<CardView>(R.id.cardParent)
        val imgCheckTypeBlue = itemView.findViewById<ImageView>(R.id.imgCheckTypeBlue)
        val btnNext = itemView.findViewById<Button>(R.id.btnNext)
    }
}