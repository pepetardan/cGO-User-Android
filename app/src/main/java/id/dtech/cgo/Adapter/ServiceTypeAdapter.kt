package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R

class ServiceTypeAdapter (from : Int,context : Context, typeList : ArrayList<String>) :
    RecyclerView.Adapter<ServiceTypeAdapter.ServiceTypeViewHolder>() {

    private val contexs = context
    private val types  = typeList
    private val froms = from

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceTypeViewHolder {
        val view = LayoutInflater.from(contexs).inflate(R.layout.item_service_type,parent,
            false)
        return ServiceTypeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: ServiceTypeViewHolder, position: Int) {
        val typeName = types[position]
        holder.txtName.text = typeName

        if (froms == 1){
            if (position == 0){
                holder.linearParent.setBackgroundResource(R.drawable.background_service_type_brown)
            }
            else{
                holder.linearParent.setBackgroundResource(R.drawable.background_service_type)
            }
        }
    }

    class ServiceTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
    }
}