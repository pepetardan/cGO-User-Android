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
import id.dtech.cgo.Model.RulesModel
import id.dtech.cgo.R

class BoatRulesAdapter (context : Context, facilityList : ArrayList<RulesModel>) :
    RecyclerView.Adapter<BoatRulesAdapter.BoatRulesViewHolder>() {

    private val contexts = context
    private val facilities = facilityList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoatRulesViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_facility,parent,false)
        return BoatRulesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return facilities.size
    }

    override fun onBindViewHolder(holder: BoatRulesViewHolder, position: Int) {
        val facility = facilities[position]
        holder.txtName.text = facility.name ?: ""
    }

    class BoatRulesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgFacility = itemView.findViewById<ImageView>(R.id.imgFacility)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
    }
}