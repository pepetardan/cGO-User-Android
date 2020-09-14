package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import java.util.ArrayList

class TripAccomodationAdapter(context : Context, accomodationList : ArrayList<String>) :
    RecyclerView.Adapter<TripAccomodationAdapter.TripAccomodationViewHolder>() {

    private val contexts = context
    private val accList = accomodationList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripAccomodationViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_filter_type,parent,false)
        return TripAccomodationViewHolder(view)
    }

    override fun getItemCount(): Int {
       return accList.size
    }

    override fun onBindViewHolder(holder: TripAccomodationViewHolder, position: Int) {
        val name = accList[position]
        holder.txtName.text = name
    }

    class TripAccomodationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }
}