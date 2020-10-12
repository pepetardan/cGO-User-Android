package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.AddOnModel
import id.dtech.cgo.R

class AddonNameAdapter(context : Context, addOnList : ArrayList<AddOnModel>) :
    RecyclerView.Adapter<AddonNameAdapter.AddonViewHolder>() {

    private val contexts = context
    private val addonLists = addOnList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddonViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_addon_name, parent,
            false)
        return AddonViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddonViewHolder, position: Int) {
       val addOnModel = addonLists[position]
       holder.txtName.text = addOnModel.name ?: ""
    }

    override fun getItemCount(): Int {
       return addonLists.size
    }

    class AddonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearParent = itemView.findViewById<LinearLayout>(R.id.linearParent)
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }
}