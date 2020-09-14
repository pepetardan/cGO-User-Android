package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.R
import java.util.ArrayList

class LanguageUsedAdapter(context : Context, languageList : ArrayList<String>) :
    RecyclerView.Adapter<LanguageUsedAdapter.LanguageUsedViewHolder>() {

    private val contexts = context
    private val lngList = languageList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageUsedViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_filter_type,parent,false)
        return LanguageUsedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lngList.size
    }

    override fun onBindViewHolder(holder: LanguageUsedViewHolder, position: Int) {
        val name = lngList[position]
        holder.txtName.text = name
    }

    class LanguageUsedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<MyTextView>(R.id.txtName)
    }
}