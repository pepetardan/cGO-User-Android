package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.DiscoverPreferanceModel
import id.dtech.cgo.R
import id.dtech.cgo.View.ActivityExperience

class ServiceAdapter(context : Context, discoverList : ArrayList<DiscoverPreferanceModel>) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    private val contexs = context
    private val discoverLists = discoverList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(contexs).inflate(R.layout.item_service,parent,
            false)
        return ServiceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return discoverLists.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val discoverModel = discoverLists[position]
        val experienceList = discoverModel.item ?: ArrayList()
        val i = Intent(contexs, ActivityExperience::class.java)

        val provinceName = discoverModel.province_name ?: ""
        val provinceID = discoverModel.province_id

        val cityName = discoverModel.city ?: ""
        val cityID = discoverModel.city_id

        val harborName = discoverModel.harbors_name ?: ""
        val harborID = discoverModel.harbors_id

       if (harborName.isNotEmpty()){
           holder.txtLocation.text = harborName
           i.putExtra("name",harborName)
           i.putExtra("location_id",harborID)
           i.putExtra("from_location",1)
       }
        else{
           if (cityName.isNotEmpty()){
               holder.txtLocation.text = cityName
               i.putExtra("name",cityName)
               i.putExtra("location_id",cityID)
               i.putExtra("from_location",2)
           }
           else{
               if (provinceName.isNotEmpty()){
                   holder.txtLocation.text = provinceName
                   i.putExtra("name",provinceName)
                   i.putExtra("location_id",provinceID)
                   i.putExtra("from_location",3)
               }
           }
       }
        holder.txtContent.text = discoverModel.city_desc ?: ""

        holder.rvService.layoutManager = LinearLayoutManager(contexs,LinearLayoutManager.HORIZONTAL,
            false)
        holder.rvService.adapter = ServiceContentAdapter(contexs,experienceList)

        holder.txtMore.setOnClickListener {
            i.putExtra("from",3)
            contexs.startActivity(i)
        }
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtLocation = itemView.findViewById<MyTextView>(R.id.txtLocation)
        val txtContent = itemView.findViewById<MyTextView>(R.id.txtContent)
        val txtMore = itemView.findViewById<MyTextView>(R.id.txtMore)
        val rvService = itemView.findViewById<RecyclerView>(R.id.rvService)
    }
}