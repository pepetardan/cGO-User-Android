package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.R

class GuestAdapter (context : Context) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    private val contexts = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_guest,parent,false)
        return GuestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {

        val total = position + 1
        holder.txtGuest.text = "$total Guest"

        if (position == 0){
            holder.dividerView.visibility = View.VISIBLE
            holder.txtGuest.setTextColor(Color.parseColor("#4B4B4B"))
        }
        else{
            holder.dividerView.visibility = View.INVISIBLE
            holder.txtGuest.setTextColor(Color.parseColor("#8B8B8B"))
        }
    }

    class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtGuest = itemView.findViewById<TextView>(R.id.txtGuest)
        val dividerView = itemView.findViewById<View>(R.id.dividerView)
    }
}