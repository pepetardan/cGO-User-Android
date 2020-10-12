package id.dtech.cgo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.CustomView.MyTextView
import id.dtech.cgo.Model.ItenaryModel
import id.dtech.cgo.R

class ActivityAdapter (context : Context, activities : ArrayList<ItenaryModel>) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    private val contexs = context
    private val listActivity = activities

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(contexs).inflate( R.layout.item_activity,parent, false)
        return ActivityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listActivity.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val iteneraryModel = listActivity[position]
        holder.txtTime.text = iteneraryModel.time
        holder.txtActivity.text = iteneraryModel.activity
    }

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTime = itemView.findViewById<MyTextView>(R.id.txtTime)
        val txtActivity = itemView.findViewById<MyTextView>(R.id.txtActivity)
    }
}

