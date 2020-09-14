package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import java.util.ArrayList

class TourGuideAdapter (context : Context, guideList : ArrayList<String>) :
    RecyclerView.Adapter<TourGuideAdapter.TourGuideViewHolder>() {

    private val contexts = context
    private val guideLists = guideList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourGuideViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_filter_type,parent,false)
        return TourGuideViewHolder(view)
    }

    override fun getItemCount(): Int {
        return guideLists.size
    }

    override fun onBindViewHolder(holder: TourGuideViewHolder, position: Int) {
        val name = guideLists[position]
        holder.txtName.text = name
    }

    class TourGuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }
}