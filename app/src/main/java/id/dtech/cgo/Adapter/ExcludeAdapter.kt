package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Model.InclusionModel
import id.dtech.cgo.R

class ExcludeAdapter (context : Context, items : ArrayList<InclusionModel>) : RecyclerView.Adapter<ExcludeAdapter.ExcludeViewHolder>() {

    private val contexts = context
    private val itemList = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcludeViewHolder {
        val view = LayoutInflater.from(contexts).inflate(R.layout.item_exclude,parent,false)
        return ExcludeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ExcludeViewHolder, position: Int) {

        val model = itemList[position]

        if (position != 0){
            holder.txtInclude.visibility = View.INVISIBLE
        }
        else{
            holder.txtInclude.visibility = View.VISIBLE
        }

        holder.txtName.text = model.name

    }

    class ExcludeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtInclude = itemView.findViewById<TextView>(R.id.txtInclude)
    }
}