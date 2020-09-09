package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Model.FerryModel
import id.dtech.cgo.R

class FerryAdapter(context : Context ,ferryList : ArrayList<FerryModel>) :
    RecyclerView.Adapter<FerryAdapter.FerryViewHolder>() {

    private val contexts = context
    private val ferries = ferryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FerryViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_ferry,parent,
            false)
        return FerryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ferries.size
    }

    override fun onBindViewHolder(holder: FerryViewHolder, position: Int) {
        val ferry = ferries[position]
        holder.txtFerryName.text = ferry.name ?: ""
        holder.imgFerry.setImageResource(ferry.pic)
    }

    class FerryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtFerryName = itemView.findViewById<TextView>(R.id.txtFerryName)
        val imgFerry = itemView.findViewById<ImageView>(R.id.imgFerry)
        val imgCheck = itemView.findViewById<ImageView>(R.id.imgCheck)
    }
}