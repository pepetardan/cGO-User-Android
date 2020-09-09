package id.dtech.cgo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dtech.cgo.Model.ItemItenaryModel
import id.dtech.cgo.R

class DayAdapter(context : Context, iteneraries : ArrayList<ItemItenaryModel>) :
    RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    private val contexs = context
    private var items = iteneraries

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(contexs).inflate( R.layout.item_day,parent, false)
        return DayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val model = items[position]
        val activities = model.itenary

        holder.txtName.text = "Day "+model.day

        activities?.let {
            holder.rvActivity.layoutManager = LinearLayoutManager(contexs)
            holder.rvActivity.adapter = ActivityAdapter(contexs,it)
        }
    }

    fun updateItenary(iteneraries : ArrayList<ItemItenaryModel>){
        this.items = iteneraries
        notifyDataSetChanged()
    }

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val rvActivity = itemView.findViewById<RecyclerView>(R.id.rvActivity)
    }
}