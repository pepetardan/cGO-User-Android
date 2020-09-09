package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.dtech.cgo.Model.FacilityModel
import id.dtech.cgo.R

class FacilityAdapter(context : Context, facilityList : ArrayList<FacilityModel>) :
    RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder>() {

    private val contexts = context
    private var facilities = facilityList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_facility,parent,false)
        return FacilityViewHolder(view)
    }

    override fun getItemCount(): Int {
      return facilities.size
    }

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val facility = facilities[position]
        holder.txtName.text = facility.name ?: ""

        facility.icon?.let {
            if (it.isNotEmpty()){
                Picasso.get().load(it).into(holder.imgFacility)
            }
        }
    }

    fun updateFacility(facilityList : ArrayList<FacilityModel>){
        this.facilities = facilityList
        notifyDataSetChanged()
    }

    class FacilityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgFacility = itemView.findViewById<ImageView>(R.id.imgFacility)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
    }
}